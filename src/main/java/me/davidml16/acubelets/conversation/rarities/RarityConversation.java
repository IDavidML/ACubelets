package me.davidml16.acubelets.conversation.rarities;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.CommonPrompts;
import me.davidml16.acubelets.menus.admin.rewards.RaritiesMenu;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.objects.Rarity;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class RarityConversation implements ConversationAbandonedListener, CommonPrompts {

    private Main main;
    public RarityConversation(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, CubeletType cubeletType) {
        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RewardMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("cubeletType", cubeletType);

        main.getConversationHandler().addConversation(paramPlayer);

        return conversation;
    }

    public Conversation getConversation(Player paramPlayer) { return getConversation(paramPlayer, null); }

    public void conversationAbandoned(ConversationAbandonedEvent paramConversationAbandonedEvent) {}

    public class RewardMenuOptions extends FixedSetPrompt {
        RewardMenuOptions() { super("1", "2", "3", "4", "5", "6"); }

        protected Prompt acceptValidatedInput(ConversationContext param1ConversationContext, String param1String) {
            CubeletType cubeletType = (CubeletType) param1ConversationContext.getSessionData("cubeletType");
            switch (param1String) {
                case "1":
                    return new CommonStringPrompt(main, this, false, ChatColor.YELLOW + "  Enter rarity identificator, \"cancel\" to return.\n\n ", "rarityID");
                case "2":
                    return new CommonStringPrompt(main, this, true, ChatColor.YELLOW + "  Enter rarity name (You can use color codes), \"cancel\" to return.\n\n ", "rarityName");
                case "3":
                    return new NumericRangePrompt(main, this, ChatColor.YELLOW + "  Enter rarity chance, \"cancel\" to return.\n  Range: 0 to 100\n\n ", "rarityChance", Double.valueOf(0), Double.valueOf(100));
                case "4":
                    return new DuplicateRangeStringPrompt(main, this, false, ChatColor.YELLOW + "  Enter rarity duplicate points range, \"cancel\" to return.\n  Format: min-max. Example: 50-450\n\n ", "rarityDuplicate");
                case "5":
                    if(param1ConversationContext.getSessionData("rarityID") != null
                            && param1ConversationContext.getSessionData("rarityName") != null
                            && param1ConversationContext.getSessionData("rarityChance") != null
                            && param1ConversationContext.getSessionData("rarityDuplicate") != null) {
                        if (!rarityIdExist(cubeletType, (String) param1ConversationContext.getSessionData("rarityID"))) {
                            String rarityID = (String) param1ConversationContext.getSessionData("rarityID");
                            String rarityName = (String) param1ConversationContext.getSessionData("rarityName");
                            double rarityChance = (Double) param1ConversationContext.getSessionData("rarityChance");
                            String rarityDuplicate = (String) param1ConversationContext.getSessionData("rarityDuplicate");

                            Rarity rarity = new Rarity(rarityID, rarityName, rarityChance, rarityDuplicate);
                            cubeletType.getRarities().put(rarityID, rarity);
                            cubeletType.saveType();

                            param1ConversationContext.getForWhom().sendRawMessage("\n" + Utils.translate(main.getLanguageHandler().getPrefix()
                                    + " &aYou added rarity &e" + rarity.getId() + " &ato rarities of cubelet type &e" + cubeletType.getId()));

                            Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                                    ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);

                            main.getMenuHandler().reloadAllMenus(RaritiesMenu.class);

                            Player player = (Player) param1ConversationContext.getSessionData("player");

                            RaritiesMenu raritiesMenu = new RaritiesMenu(main, player);
                            raritiesMenu.setAttribute(Menu.AttrType.CUSTOM_ID_ATTR, cubeletType.getId());
                            raritiesMenu.open();

                            main.getConversationHandler().removeConversation(player);

                            return Prompt.END_OF_CONVERSATION;

                        } else {
                            return new ErrorPrompt(main, this, "\n" + ChatColor.RED + "  There is already a rarity with that ID, please change it and try again\n  Write anything to continue\n ");
                        }
                    } else {
                        return new ErrorPrompt(main, this, "\n" + ChatColor.RED + "  You need to setup ID, NAME and CHANCE to save rarity!\n  Write anything to continue\n ");
                    }
                case "6":
                    return new ConfirmExitPrompt(main, this);
            }
            return null;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {
            String cadena = "";
            cadena += ChatColor.GOLD + "" + ChatColor.BOLD + "\n  CUBELET RARITY CREATION MENU\n";
            cadena += ChatColor.GREEN + " \n";
            if (param1ConversationContext.getSessionData("rarityID") == null) {
                cadena += ChatColor.RED + "    1 " + ChatColor.GRAY + "- Set rarity ID (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    1 " + ChatColor.GRAY + "- Set rarity ID (" + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', (String)param1ConversationContext.getSessionData("rarityID")) + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("rarityName") == null) {
                cadena += ChatColor.RED + "    2 " + ChatColor.GRAY + "- Set rarity name (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    2 " + ChatColor.GRAY + "- Set rarity name (" + ChatColor.YELLOW + param1ConversationContext.getSessionData("rarityName") + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("rarityChance") == null) {
                cadena += ChatColor.RED + "    3 " + ChatColor.GRAY + "- Set rarity chance (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    3 " + ChatColor.GRAY + "- Set rarity chance (" + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', String.valueOf((Double) param1ConversationContext.getSessionData("rarityChance"))) + "%" + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("rarityDuplicate") == null) {
                cadena += ChatColor.RED + "    4 " + ChatColor.GRAY + "- Set rarity duplicate points range (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    4 " + ChatColor.GRAY + "- Set rarity duplicate points range (" + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', String.valueOf(param1ConversationContext.getSessionData("rarityDuplicate"))) + ChatColor.GRAY + ")\n";
            }

            cadena += ChatColor.GREEN + "    5 " + ChatColor.GRAY + "- Save\n";
            cadena += ChatColor.GREEN + "    6 " + ChatColor.GRAY + "- Exit and discard\n";
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