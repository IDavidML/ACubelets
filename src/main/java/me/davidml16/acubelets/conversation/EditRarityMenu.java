package me.davidml16.acubelets.conversation;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Rarity;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class EditRarityMenu implements ConversationAbandonedListener, CommonPrompts {

    private Main main;
    public EditRarityMenu(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, CubeletType cubeletType, Rarity rarity) {
        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RewardMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("cubeletType", cubeletType);
        conversation.getContext().setSessionData("rarity", rarity);
        conversation.getContext().setSessionData("rarityID", rarity.getId());
        conversation.getContext().setSessionData("rarityName", rarity.getName());
        conversation.getContext().setSessionData("rarityChance", rarity.getChance());
        conversation.getContext().setSessionData("rarityDuplicate", rarity.getDuplicatePointsRange());

        main.getGuiHandler().addConversation(paramPlayer);

        return conversation;
    }

    public Conversation getConversation(Player paramPlayer) { return getConversation(paramPlayer, null, null); }

    public void conversationAbandoned(ConversationAbandonedEvent paramConversationAbandonedEvent) {}

    public class RewardMenuOptions extends FixedSetPrompt {
        RewardMenuOptions() { super("1", "2", "3", "4", "5"); }

        protected Prompt acceptValidatedInput(ConversationContext param1ConversationContext, String param1String) {
            CubeletType cubeletType = (CubeletType) param1ConversationContext.getSessionData("cubeletType");
            switch (param1String) {
                case "1":
                    return new CommonStringPrompt(main, this, true, ChatColor.YELLOW + "  Edit rarity name (You can use color codes), \"cancel\" to return.\n\n ", "rarityName");
                case "2":
                    return new NumericRangePrompt(main, this, ChatColor.YELLOW + "  Edit rarity chance, \"cancel\" to return.\n  Range: 0 to 100\n\n ", "rarityChance", Double.valueOf(0), Double.valueOf(100));
                case "3":
                    return new DuplicateRangeStringPrompt(main, this, false, ChatColor.YELLOW + "  Edit rarity duplicate points range, \"cancel\" to return.\n  Format: min-max. Example: 50-450\n\n ", "rarityDuplicate");
                case "4":
                    if(param1ConversationContext.getSessionData("rarityID") != null
                            && param1ConversationContext.getSessionData("rarityName") != null
                            && param1ConversationContext.getSessionData("rarityChance") != null
                            && param1ConversationContext.getSessionData("rarityDuplicate") != null) {
                        if (rarityIdExist(cubeletType, (String) param1ConversationContext.getSessionData("rarityID"))) {
                            String rarityID = (String) param1ConversationContext.getSessionData("rarityID");
                            String rarityName = (String) param1ConversationContext.getSessionData("rarityName");
                            double rarityChance = (Double) param1ConversationContext.getSessionData("rarityChance");
                            String rarityDuplicate = (String) param1ConversationContext.getSessionData("rarityDuplicate");

                            Rarity rarity = (Rarity) param1ConversationContext.getSessionData("rarity");
                            rarity.setName(rarityName);
                            rarity.setChance(rarityChance);
                            rarity.setDuplicatePointsRange(rarityDuplicate);
                            cubeletType.saveType();

                            param1ConversationContext.getForWhom().sendRawMessage("\n" + ColorUtil.translate(main.getLanguageHandler().getPrefix()
                                    + " &aYou edited rarity &e" + rarity.getId() + " &afrom rarities of cubelet type &e" + cubeletType.getId()));

                            Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                                    ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);
                            main.getRaritiesGUI().reloadGUI(cubeletType.getId());
                            main.getRaritiesGUI().open((Player) param1ConversationContext.getSessionData("player"), cubeletType.getId());
                            main.getGuiHandler().removeConversation((Player) param1ConversationContext.getSessionData("player"));
                            return Prompt.END_OF_CONVERSATION;
                        } else {
                            main.getGuiHandler().removeConversation((Player) param1ConversationContext.getSessionData("player"));
                            return Prompt.END_OF_CONVERSATION;
                        }
                    } else {
                        return new ErrorPrompt(main, this, "\n" + ChatColor.RED + "  You need to setup ID, NAME and CHANCE to save rarity!\n  Write anything to continue\n ");
                    }
                case "5":
                    return new ConfirmExitPrompt(main, this);
            }
            return null;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {
            String cadena = "";
            cadena += ChatColor.GOLD + "" + ChatColor.BOLD + "\n  CUBELET RARITY EDITOR MENU\n";
            cadena += ChatColor.GREEN + " \n";

            if (param1ConversationContext.getSessionData("rarityName") == null) {
                cadena += ChatColor.RED + "    1 " + ChatColor.GRAY + "- Edit rarity name (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    1 " + ChatColor.GRAY + "- Edit rarity name (" + ChatColor.YELLOW + param1ConversationContext.getSessionData("rarityName") + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("rarityChance") == null) {
                cadena += ChatColor.RED + "    2 " + ChatColor.GRAY + "- Edit rarity chance (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    2 " + ChatColor.GRAY + "- Edit rarity chance (" + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', String.valueOf((Double) param1ConversationContext.getSessionData("rarityChance"))) + "%" + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("rarityDuplicate") == null) {
                cadena += ChatColor.RED + "    3 " + ChatColor.GRAY + "- Edit rarity duplicate points range (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    3 " + ChatColor.GRAY + "- Edit rarity duplicate points range (" + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', String.valueOf(param1ConversationContext.getSessionData("rarityDuplicate"))) + ChatColor.GRAY + ")\n";
            }

            cadena += ChatColor.GREEN + "    4 " + ChatColor.GRAY + "- Save\n";
            cadena += ChatColor.GREEN + "    5 " + ChatColor.GRAY + "- Exit and discard\n";
            cadena += ChatColor.GREEN + " \n";
            cadena += ChatColor.GOLD + "" + ChatColor.YELLOW + "  Choose the option: \n";
            cadena += ChatColor.GREEN + " \n";
            return cadena;
        }
    }

    private boolean rarityIdExist(CubeletType cubeletType, String rarityID) {
        return cubeletType.getRarities().containsKey(rarityID);
    }
}