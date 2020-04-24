package me.davidml16.acubelets.conversation;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.CustomIcon;
import me.davidml16.acubelets.objects.Rarity;
import me.davidml16.acubelets.objects.Reward;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RewardMenu implements ConversationAbandonedListener, CommonPrompts {

    private Main main;
    public RewardMenu(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, CubeletType cubeletType) {
        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RewardMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("cubeletType", cubeletType);
        return conversation;
    }

    public Conversation getConversation(Player paramPlayer) { return getConversation(paramPlayer, null); }

    public void conversationAbandoned(ConversationAbandonedEvent paramConversationAbandonedEvent) {}

    public class RewardMenuOptions extends FixedSetPrompt {
        RewardMenuOptions() { super("1", "2", "3", "4", "5", "6", "7"); }

        protected Prompt acceptValidatedInput(ConversationContext param1ConversationContext, String param1String) {
            CubeletType cubeletType = (CubeletType) param1ConversationContext.getSessionData("cubeletType");
            switch (param1String) {
                case "1":
                    return new CommonPrompts.CommonStringPrompt(main, this, false, ChatColor.YELLOW + "  Enter reward identificator, \"cancel\" to return.\n\n ", "rewardID");
                case "2":
                    return new CommonPrompts.CommonStringPrompt(main, this, true, ChatColor.YELLOW + "  Enter reward name (You can use color codes), \"cancel\" to return.\n\n ", "rewardName");
                case "3":
                    List<Rarity> rts = new ArrayList<>(cubeletType.getRarities().values());
                    rts.sort(Collections.reverseOrder());

                    List<String> rarities = new ArrayList<>();
                    for(Rarity rarity : rts) {
                        rarities.add(rarity.getId());
                    }

                    return new CommonPrompts.CommonStringPrompt(main,this, false, ChatColor.YELLOW + "  Enter reward rarity, \"cancel\" to return.\n  Available rarities: " + rarities + "\n\n ", "rewardRarity");
                case "4":
                    return new CommonPrompts.CommonStringPrompt(main,this, true,ChatColor.YELLOW + "  Enter reward command, \"cancel\" to return.\n  Available variables: %player%\n\n ", "rewardCommand");
                case "5":
                    return new CommonPrompts.CommonStringPrompt(main, this, false, ChatColor.YELLOW + "  Enter reward icon, \"cancel\" to return.\n\n  For an item icon write 'materialName' or 'materialName:materialData', for example 'WOOL:14'\n\n  For an skull icon write 'base64:texture', 'uuid:playerUUID' or 'name:playerName' \n\n ", "rewardIcon");
                case "6":
                    if(param1ConversationContext.getSessionData("rewardID") != null
                            && param1ConversationContext.getSessionData("rewardName") != null
                            && param1ConversationContext.getSessionData("rewardCommand") != null
                            && param1ConversationContext.getSessionData("rewardRarity") != null
                            && param1ConversationContext.getSessionData("rewardIcon") != null) {
                        if(cubeletType.getRarities().containsKey((String) param1ConversationContext.getSessionData("rewardRarity"))) {
                            if (!rewardsIdExist(cubeletType, (String) param1ConversationContext.getSessionData("rewardID"))) {
                                String rewardID = (String) param1ConversationContext.getSessionData("rewardID");
                                String rewardName = (String) param1ConversationContext.getSessionData("rewardName");
                                String rewardRarity = (String) param1ConversationContext.getSessionData("rewardRarity");
                                String rewardCommand = (String) param1ConversationContext.getSessionData("rewardCommand");
                                String rewardIcon = (String) param1ConversationContext.getSessionData("rewardIcon");

                                CustomIcon customIcon = null;
                                if(rewardIcon.startsWith("base64:") ||rewardIcon.startsWith("uuid:") || rewardIcon.startsWith("name:")) {
                                    String[] icon = rewardIcon.split(":");
                                    switch(icon[0].toLowerCase()) {
                                        case "base64":
                                        case "uuid":
                                        case "name":
                                            customIcon = new CustomIcon(icon[0], icon[1]);
                                            break;
                                    }
                                } else {
                                    String[] parts = rewardIcon.split(":");
                                    if (parts.length == 1) {
                                        customIcon = new CustomIcon(Material.matchMaterial(parts[0]), (byte) 0);
                                    } else if (parts.length == 2) {
                                        customIcon = new CustomIcon(Material.matchMaterial(parts[0]), Byte.parseByte(parts[1]));
                                    }
                                }

                                Reward reward = new Reward(rewardID, rewardName, cubeletType.getRarities().get(rewardRarity), rewardCommand, customIcon);
                                cubeletType.addReward(rewardRarity, reward);
                                cubeletType.saveType();

                                param1ConversationContext.getForWhom().sendRawMessage("\n" + ColorUtil.translate(main.getLanguageHandler().getPrefix()
                                        + " &aYou added reward &e" + reward.getId() + " &ato rewards of cubelet type &e" + cubeletType.getId()));

                                Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                                        ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);
                                main.getRewardsGUI().reloadGUI(cubeletType.getId());
                                main.getRewardsGUI().open((Player) param1ConversationContext.getSessionData("player"), cubeletType.getId());
                                return Prompt.END_OF_CONVERSATION;
                            } else {
                                return new CommonPrompts.ErrorPrompt(main, this, "\n" + ChatColor.RED + "  There is already a reward with that ID, please change it and try again\n  Write anything to continue\n ");
                            }
                        } else {
                            return new CommonPrompts.ErrorPrompt(main, this, "\n" + ChatColor.RED + "  This reward rarity not exist, please change it and try again\n  Write anything to continue\n ");
                        }
                    } else {
                        return new CommonPrompts.ErrorPrompt(main, this, "\n" + ChatColor.RED + "  You need to setup ID, NAME, RARITY, COMMAND and ICON to save reward!\n  Write anything to continue\n ");
                    }
                case "7":
                    return new CommonPrompts.ConfirmExitPrompt(main, this);
            }
            return null;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {
            String cadena = "";
            cadena += ChatColor.GOLD + "" + ChatColor.BOLD + "\n  CUBEET REWARD CREATION MENU\n";
            cadena += ChatColor.GREEN + " \n";
            if (param1ConversationContext.getSessionData("rewardID") == null) {
                cadena += ChatColor.RED + "    1 " + ChatColor.GRAY + "- Set reward ID (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    1 " + ChatColor.GRAY + "- Set reward ID (" + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', (String)param1ConversationContext.getSessionData("rewardID")) + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("rewardName") == null) {
                cadena += ChatColor.RED + "    2 " + ChatColor.GRAY + "- Set reward name (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    2 " + ChatColor.GRAY + "- Set reward name (" + ChatColor.YELLOW + param1ConversationContext.getSessionData("rewardName") + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("rewardRarity") == null) {
                cadena += ChatColor.RED + "    3 " + ChatColor.GRAY + "- Set reward rarity (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    3 " + ChatColor.GRAY + "- Set reward rarity (" + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', (String)param1ConversationContext.getSessionData("rewardRarity")) + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("rewardCommand") == null) {
                cadena += ChatColor.RED + "    4 " + ChatColor.GRAY + "- Set reward command (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    4 " + ChatColor.GRAY + "- Set reward command (" + ChatColor.YELLOW + param1ConversationContext.getSessionData("rewardCommand") + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("rewardIcon") == null) {
                cadena += ChatColor.RED + "    5 " + ChatColor.GRAY + "- Set reward icon (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                String icon = (String) param1ConversationContext.getSessionData("rewardIcon");
                if(icon.startsWith("base64:") || icon.startsWith("uuid:") || icon.startsWith("name:")) {
                    cadena += ChatColor.GREEN + "    5 " + ChatColor.GRAY + "- Set reward icon (" + ChatColor.YELLOW + "CustomSkull" + ChatColor.GRAY + ")\n";
                } else {
                    cadena += ChatColor.GREEN + "    5 " + ChatColor.GRAY + "- Set reward icon (" + ChatColor.YELLOW + icon + ChatColor.GRAY + ")\n";
                }
            }

            cadena += ChatColor.GREEN + "    6 " + ChatColor.GRAY + "- Save\n";
            cadena += ChatColor.GREEN + "    7 " + ChatColor.GRAY + "- Exit and discard\n";
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