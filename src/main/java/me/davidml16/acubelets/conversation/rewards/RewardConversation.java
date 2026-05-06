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

public class RewardConversation implements ConversationAbandonedListener, CommonPrompts {

    private final Main main;

    public RewardConversation(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, CubeletType cubeletType) {

        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RewardMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("cubeletType", cubeletType);

        conversation.getContext().setSessionData("bypassDuplication", false);

        main.getConversationHandler().addConversation(paramPlayer);

        return conversation;

    }

    public Conversation getConversation(Player paramPlayer) {
        return getConversation(paramPlayer, null);
    }

    public void conversationAbandoned(ConversationAbandonedEvent paramConversationAbandonedEvent) {
    }

    private boolean rewardsIdExist(CubeletType cubeletType, String rewardID) {
        for (Reward reward : cubeletType.getAllRewards()) {
            if (reward.getId().equalsIgnoreCase(rewardID)) return true;
        }
        return false;
    }

    public class RewardMenuOptions extends FixedSetPrompt {
        RewardMenuOptions() {
            super("1", "2", "3", "4", "5", "6");
        }

        protected Prompt acceptValidatedInput(ConversationContext param1ConversationContext, String param1String) {
            CubeletType cubeletType = (CubeletType) param1ConversationContext.getSessionData("cubeletType");
            Player p = (Player) param1ConversationContext.getSessionData("player");

            switch (param1String) {
                case "1":
                    return new UncoloredStringPrompt(main, this, true, ChatColor.YELLOW + "  Enter reward name, \"cancel\" to return.\n\n ", "rewardName");
                case "2":
                    return new CommonStringPrompt(main, this, false, ChatColor.YELLOW + "  Enter reward rarity, \"cancel\" to return.\n  Available rarities: " + cubeletType.getRaritiesIDs() + "\n\n ", "rewardRarity");
                case "3":
                    ItemStack itemHand = p.getInventory().getItemInHand();
                    if (itemHand == null || itemHand.getType() == Material.AIR) {
                        param1ConversationContext.getForWhom().sendRawMessage(ChatColor.RED + "  AIR icon not allowed!\n ");
                        Sounds.playSound(p, p.getLocation(), Sounds.MySound.NOTE_PLING, 10, 0);
                        return this;
                    }
                    param1ConversationContext.setSessionData("rewardIcon", itemHand.clone());
                    param1ConversationContext.getForWhom().sendRawMessage(ChatColor.GREEN + "  Succesfully setup reward icon.");
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                    return this;

                case "4": // Toggle Bypass Duplication
                    boolean currentBypass = (boolean) param1ConversationContext.getSessionData("bypassDuplication");
                    param1ConversationContext.setSessionData("bypassDuplication", !currentBypass);
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                    return this;

                case "5": // Save
                    if (param1ConversationContext.getSessionData("rewardName") != null
                            && param1ConversationContext.getSessionData("rewardRarity") != null
                            && param1ConversationContext.getSessionData("rewardIcon") != null) {

                        if (cubeletType.getRarities().containsKey((String) param1ConversationContext.getSessionData("rewardRarity"))) {
                            String rewardID = "reward_" + cubeletType.getAllRewards().size();
                            String rewardName = (String) param1ConversationContext.getSessionData("rewardName");
                            String rewardRarity = (String) param1ConversationContext.getSessionData("rewardRarity");
                            ItemStack rewardIcon = (ItemStack) param1ConversationContext.getSessionData("rewardIcon");
                            boolean bypass = (boolean) param1ConversationContext.getSessionData("bypassDuplication");

                            Reward reward = new Reward(rewardID, rewardName, cubeletType.getRarities().get(rewardRarity), rewardIcon.clone(), cubeletType);

                            reward.setBypassDuplicationSystem(bypass);

                            cubeletType.addReward(rewardRarity, reward);
                            cubeletType.saveType();

                            param1ConversationContext.getForWhom().sendRawMessage("\n" + Utils.translate(main.getLanguageHandler().getPrefix()
                                    + " &aYou added reward &e" + reward.getId() + " &ato rewards of cubelet type &e" + cubeletType.getId()));

                            Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);
                            main.getMenuHandler().reloadAllMenus(RewardsMenu.class);

                            RewardsMenu rewardsMenu = new RewardsMenu(main, p);
                            rewardsMenu.setAttribute(Menu.AttrType.CUSTOM_ID_ATTR, cubeletType.getId());
                            rewardsMenu.open();
                            main.getConversationHandler().removeConversation(p);

                            return Prompt.END_OF_CONVERSATION;
                        } else {
                            return new ErrorPrompt(main, this, "\n" + ChatColor.RED + "  This reward rarity not exist, please change it and try again\n  Write anything to continue\n ");
                        }
                    } else {
                        return new ErrorPrompt(main, this, "\n" + ChatColor.RED + "  You need to setup NAME, RARITY and ICON to save reward!\n  Write anything to continue\n ");
                    }
                case "6":
                    return new ConfirmExitPrompt(main, this);
            }
            return null;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {
            StringBuilder cadena = new StringBuilder();
            cadena.append(ChatColor.GOLD).append(ChatColor.BOLD).append("\n  CUBELET REWARD CREATION MENU\n");
            cadena.append(ChatColor.GREEN).append(" \n");

            if (param1ConversationContext.getSessionData("rewardName") == null) {
                cadena.append(ChatColor.RED).append("    1 ").append(ChatColor.GRAY).append("- Set reward name (").append(ChatColor.RED).append("none").append(ChatColor.GRAY).append(")\n");
            } else {
                cadena.append(ChatColor.GREEN).append("    1 ").append(ChatColor.GRAY).append("- Set reward name (").append(ChatColor.YELLOW).append(param1ConversationContext.getSessionData("rewardName")).append(ChatColor.GRAY).append(")\n");
            }

            if (param1ConversationContext.getSessionData("rewardRarity") == null) {
                cadena.append(ChatColor.RED).append("    2 ").append(ChatColor.GRAY).append("- Set reward rarity (").append(ChatColor.RED).append("none").append(ChatColor.GRAY).append(")\n");
            } else {
                cadena.append(ChatColor.GREEN).append("    2 ").append(ChatColor.GRAY).append("- Set reward rarity (").append(ChatColor.YELLOW).append(ChatColor.translateAlternateColorCodes('&', (String) param1ConversationContext.getSessionData("rewardRarity"))).append(ChatColor.GRAY).append(")\n");
            }

            if (param1ConversationContext.getSessionData("rewardIcon") == null) {
                cadena.append(ChatColor.RED).append("    3 ").append(ChatColor.GRAY).append("- Set reward icon 'Item in Hand' (").append(ChatColor.RED).append("none").append(ChatColor.GRAY).append(")\n");
            } else {
                ItemStack icon = (ItemStack) param1ConversationContext.getSessionData("rewardIcon");
                cadena.append(ChatColor.GREEN).append("    3 ").append(ChatColor.GRAY).append("- Set reward icon 'Item in Hand' (").append(ChatColor.YELLOW).append(icon.getType().name()).append(ChatColor.GRAY).append(")\n");
            }

            boolean bypass = (boolean) param1ConversationContext.getSessionData("bypassDuplication");
            cadena.append(ChatColor.GREEN).append("    4 ").append(ChatColor.GRAY).append("- Bypass Duplication: ")
                    .append(bypass ? ChatColor.GREEN + "TRUE" : ChatColor.RED + "FALSE").append("\n");

            cadena.append(ChatColor.GREEN).append("    5 ").append(ChatColor.GRAY).append("- Save\n");
            cadena.append(ChatColor.GREEN).append("    6 ").append(ChatColor.GRAY).append("- Exit and discard\n");
            cadena.append(ChatColor.GREEN).append(" \n");
            cadena.append(ChatColor.GOLD).append(ChatColor.YELLOW).append("  Choose the option: \n");

            return cadena.toString();
        }
    }
}