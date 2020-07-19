package me.davidml16.acubelets.conversation.rewards;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.CommonPrompts;
import me.davidml16.acubelets.interfaces.Reward;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.PermissionReward;
import me.davidml16.acubelets.objects.Rarity;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditPermissionRewardMenu implements ConversationAbandonedListener, CommonPrompts {

    private Main main;
    public EditPermissionRewardMenu(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, CubeletType cubeletType, Reward reward) {
        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RewardMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("cubeletType", cubeletType);

        conversation.getContext().setSessionData("rewardID", reward.getId());
        conversation.getContext().setSessionData("rewardName", reward.getName());
        conversation.getContext().setSessionData("rewardPermission", ((PermissionReward) reward).getPermission());
        conversation.getContext().setSessionData("rewardRarity", reward.getRarity().getId());
        conversation.getContext().setSessionData("rewardIcon", reward.getIcon());

        main.getGuiHandler().addConversation(paramPlayer);

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
                    List<Rarity> rts = new ArrayList<>(cubeletType.getRarities().values());
                    rts.sort(Collections.reverseOrder());

                    List<String> rarities = new ArrayList<>();
                    for(Rarity rarity : rts) {
                        rarities.add(rarity.getId());
                    }

                    return new CommonStringPrompt(main,this, false, ChatColor.YELLOW + "  Edit reward rarity, \"cancel\" to return.\n  Available rarities: " + rarities + "\n\n ", "rewardRarity");
                case "3":
                    return new CommonStringPrompt(main,this, false,ChatColor.YELLOW + "  Edit reward permission, \"cancel\" to return.\n\n ", "rewardPermission");
                case "4":
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
                case "5":
                    if(param1ConversationContext.getSessionData("rewardName") != null
                            && param1ConversationContext.getSessionData("rewardPermission") != null
                            && param1ConversationContext.getSessionData("rewardRarity") != null
                            && param1ConversationContext.getSessionData("rewardIcon") != null) {
                        if(cubeletType.getRarities().containsKey((String) param1ConversationContext.getSessionData("rewardRarity"))) {
                            if (rewardsIdExist(cubeletType, (String) param1ConversationContext.getSessionData("rewardID"))) {
                                String rewardID = (String) param1ConversationContext.getSessionData("rewardID");
                                String rewardName = (String) param1ConversationContext.getSessionData("rewardName");
                                String rewardRarity = (String) param1ConversationContext.getSessionData("rewardRarity");
                                String rewardPermission = (String) param1ConversationContext.getSessionData("rewardPermission");
                                ItemStack rewardIcon = (ItemStack) param1ConversationContext.getSessionData("rewardIcon");

                                Reward permissionReward = cubeletType.getReward(rewardID);
                                permissionReward.setName(rewardName);
                                permissionReward.setRarity(cubeletType.getRarities().get(rewardRarity));
                                ((PermissionReward) permissionReward).setPermission(rewardPermission);
                                permissionReward.setIcon(rewardIcon);

                                cubeletType.saveType();

                                main.getCubeletRewardHandler().loadReward(cubeletType, false);

                                param1ConversationContext.getForWhom().sendRawMessage("\n" + ColorUtil.translate(main.getLanguageHandler().getPrefix()
                                        + " &aYou edited reward &e" + permissionReward.getId() + " &afrom rewards of cubelet type &e" + cubeletType.getId()));

                                Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                                        ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);
                                main.getRewardsGUI().reloadGUI(cubeletType.getId());
                                main.getRewardsGUI().open((Player) param1ConversationContext.getSessionData("player"), cubeletType.getId());
                                main.getGuiHandler().removeConversation((Player) param1ConversationContext.getSessionData("player"));
                                return Prompt.END_OF_CONVERSATION;
                            } else {
                                main.getGuiHandler().removeConversation((Player) param1ConversationContext.getSessionData("player"));
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

            if (param1ConversationContext.getSessionData("rewardPermission") == null) {
                cadena += ChatColor.RED + "    3 " + ChatColor.GRAY + "- Edit reward permission (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    3 " + ChatColor.GRAY + "- Edit reward permission (" + ChatColor.YELLOW + param1ConversationContext.getSessionData("rewardPermission") + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("rewardIcon") == null) {
                cadena += ChatColor.RED + "    4 " + ChatColor.GRAY + "- Edit reward icon 'Item in Hand' (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                ItemStack icon = (ItemStack) param1ConversationContext.getSessionData("rewardIcon");
                cadena += ChatColor.GREEN + "    4 " + ChatColor.GRAY + "- Edit reward icon 'Item in Hand' (" + ChatColor.YELLOW + icon.getType().name() + ChatColor.GRAY + ")\n";
            }

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