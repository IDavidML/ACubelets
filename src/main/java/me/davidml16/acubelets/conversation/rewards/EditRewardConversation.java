package me.davidml16.acubelets.conversation.rewards;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.CommonPrompts;
import me.davidml16.acubelets.menus.admin.rewards.RewardsMenu;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditRewardConversation implements ConversationAbandonedListener, CommonPrompts {

    private Main main;
    public EditRewardConversation(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, CubeletType cubeletType, Reward reward) {
        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RewardMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("cubeletType", cubeletType);

        conversation.getContext().setSessionData("rewardID", reward.getId());
        conversation.getContext().setSessionData("rewardName", reward.getName());
        conversation.getContext().setSessionData("rewardRarity", reward.getRarity().getId());
        conversation.getContext().setSessionData("rewardIcon", reward.getIcon());
        conversation.getContext().setSessionData("bypassDuplication", reward.isBypassDuplicationSystem());

        main.getConversationHandler().addConversation(paramPlayer);

        return conversation;
    }

    public Conversation getConversation(Player paramPlayer) { return getConversation(paramPlayer, null, null); }

    public void conversationAbandoned(ConversationAbandonedEvent paramConversationAbandonedEvent) {}

    public class RewardMenuOptions extends FixedSetPrompt {
        RewardMenuOptions() { super("1", "2", "3", "4", "5", "6"); }

        protected Prompt acceptValidatedInput(ConversationContext param1ConversationContext, String param1String) {
            CubeletType cubeletType = (CubeletType) param1ConversationContext.getSessionData("cubeletType");
            switch (param1String) {
                case "1":
                    return new UncoloredStringPrompt(main, this, true, ChatColor.YELLOW + "  Edit reward name, \"cancel\" to return.\n\n ", "rewardName");
                case "2":
                    return new CommonStringPrompt(main,this, false, ChatColor.YELLOW + "  Edit reward rarity, \"cancel\" to return.\n  Available rarities: " + cubeletType.getRaritiesIDs() + "\n\n ", "rewardRarity");
                case "3":
                     Player p = (Player) param1ConversationContext.getSessionData("player");
                     ItemStack itemHand = p.getInventory().getItemInHand();

                    if(itemHand == null || itemHand.getType() == Material.AIR) {
                        param1ConversationContext.getForWhom().sendRawMessage(ChatColor.RED + "  AIR icon not allowed!\n ");
                        Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                                ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.NOTE_PLING, 10, 0);
                        return this;
                    }

                    param1ConversationContext.setSessionData("rewardIcon", itemHand);
                    param1ConversationContext.getForWhom().sendRawMessage(
                            ChatColor.GREEN + "  Succesfully setup reward icon.");
                    Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                            ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.CLICK, 10, 2);

                    return this;
                case "4":
                    return new BooleanPrompt(main,this, ChatColor.YELLOW + "  Toggle bypass duplication system, \"cancel\" to return.\n  Available parameters: true or false" + "\n\n ", "bypassDuplication");
                case "5":
                    if(param1ConversationContext.getSessionData("rewardName") != null
                            && param1ConversationContext.getSessionData("rewardRarity") != null
                            && param1ConversationContext.getSessionData("rewardIcon") != null) {
                        if(cubeletType.getRarities().containsKey((String) param1ConversationContext.getSessionData("rewardRarity"))) {
                            if (rewardsIdExist(cubeletType, (String) param1ConversationContext.getSessionData("rewardID"))) {
                                String rewardID = (String) param1ConversationContext.getSessionData("rewardID");
                                String rewardName = (String) param1ConversationContext.getSessionData("rewardName");
                                String rewardRarity = (String) param1ConversationContext.getSessionData("rewardRarity");
                                boolean bypassDuplication = (boolean) param1ConversationContext.getSessionData("bypassDuplication");
                                ItemStack rewardIcon = (ItemStack) param1ConversationContext.getSessionData("rewardIcon");

                                Reward permissionReward = cubeletType.getReward(rewardID);
                                permissionReward.setName(rewardName);
                                permissionReward.setRarity(cubeletType.getRarities().get(rewardRarity));
                                permissionReward.setIcon(rewardIcon.clone());
                                permissionReward.setBypassDuplicationSystem(bypassDuplication);

                                cubeletType.saveType();

                                main.getCubeletRewardHandler().loadReward(cubeletType);

                                param1ConversationContext.getForWhom().sendRawMessage("\n" + Utils.translate(main.getLanguageHandler().getPrefix()
                                        + " &aYou edited reward &e" + permissionReward.getId() + " &afrom rewards of cubelet type &e" + cubeletType.getId()));

                                Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                                        ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);

                                main.getMenuHandler().reloadAllMenus(RewardsMenu.class);

                                Player player = (Player) param1ConversationContext.getSessionData("player");

                                RewardsMenu rewardsMenu = new RewardsMenu(main, player);
                                rewardsMenu.setAttribute(Menu.AttrType.CUSTOM_ID_ATTR, cubeletType.getId());
                                rewardsMenu.open();

                                main.getConversationHandler().removeConversation(player);

                                return Prompt.END_OF_CONVERSATION;

                            } else {
                                main.getConversationHandler().removeConversation((Player) param1ConversationContext.getSessionData("player"));
                                return Prompt.END_OF_CONVERSATION;
                            }
                        } else {
                            return new ErrorPrompt(main, this, "\n" + ChatColor.RED + "  This reward rarity not exist, please change it and try again\n  Write anything to continue\n ");
                        }
                    } else {
                        return new ErrorPrompt(main, this, "\n" + ChatColor.RED + "  You need to setup ID, NAME, RARITY, PERMISSION and ICON to save reward!\n  Write anything to continue\n ");
                    }
                case "6":
                    return new ConfirmExitPrompt(main, this);
            }
            return null;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {
            String cadena = "";
            cadena += ChatColor.GOLD + "" + ChatColor.BOLD + "\n  CUBELET REWARD EDITOR MENU\n";
            cadena += ChatColor.GREEN + " \n";
            if (param1ConversationContext.getSessionData("rewardName") == null) {
                cadena += ChatColor.RED + "    1 " + ChatColor.GRAY + "- Edit reward name (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    1 " + ChatColor.GRAY + "- Edit reward name (" + ChatColor.YELLOW + param1ConversationContext.getSessionData("rewardName") + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("rewardRarity") == null) {
                cadena += ChatColor.RED + "    2 " + ChatColor.GRAY + "- Edit reward rarity (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    2 " + ChatColor.GRAY + "- Edit reward rarity (" + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', (String)param1ConversationContext.getSessionData("rewardRarity")) + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("rewardIcon") == null) {
                cadena += ChatColor.RED + "    3 " + ChatColor.GRAY + "- Edit reward icon 'Item in Hand' (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                ItemStack icon = (ItemStack) param1ConversationContext.getSessionData("rewardIcon");
                cadena += ChatColor.GREEN + "    3 " + ChatColor.GRAY + "- Edit reward icon 'Item in Hand' (" + ChatColor.YELLOW + icon.getType().name() + ChatColor.GRAY + ")\n";
            }

            cadena += ChatColor.GREEN + "    4 " + ChatColor.GRAY + "- Bypass duplication system (" + ChatColor.YELLOW + (boolean) param1ConversationContext.getSessionData("bypassDuplication") + ChatColor.GRAY + ")\n";

            cadena += ChatColor.GREEN + "    5 " + ChatColor.GRAY + "- Save\n";
            cadena += ChatColor.GREEN + "    6 " + ChatColor.GRAY + "- Exit and discard\n";
            cadena += ChatColor.GREEN + " \n";
            cadena += ChatColor.GOLD + "" + ChatColor.YELLOW + "  Choose the option: \n";
            cadena += ChatColor.GREEN + " \n";
            return cadena;
        }
    }

    private boolean rewardsIdExist(CubeletType cubeletType, String rewardID) {
        for(Reward reward : cubeletType.getAllRewards()) {
            if(reward.getId().equalsIgnoreCase(rewardID)) return true;
        }
        return false;
    }
}