package me.davidml16.acubelets.conversation.crafting;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.CommonPrompts;
import me.davidml16.acubelets.menus.admin.crafting.EditCraftingCraftsMenu;
import me.davidml16.acubelets.objects.CraftParent;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EditCraftParentConversation implements ConversationAbandonedListener, CommonPrompts {

    private Main main;
    public EditCraftParentConversation(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, CraftParent craftParent) {
        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RewardMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("cubeletType", craftParent.getCubeletType());
        conversation.getContext().setSessionData("slot", craftParent.getSlot());
        conversation.getContext().setSessionData("craftParent", craftParent);

        main.getConversationHandler().addConversation(paramPlayer);

        return conversation;
    }

    public Conversation getConversation(Player paramPlayer) { return getConversation(paramPlayer, null); }

    public void conversationAbandoned(ConversationAbandonedEvent paramConversationAbandonedEvent) {}

    public class RewardMenuOptions extends FixedSetPrompt {
        RewardMenuOptions() { super("1", "2", "3", "4"); }

        protected Prompt acceptValidatedInput(ConversationContext param1ConversationContext, String param1String) {
            CraftParent craftParent = (CraftParent) param1ConversationContext.getSessionData("craftParent");
            switch (param1String) {
                case "1":
                    List<String> cubelets = new ArrayList<>(main.getCubeletTypesHandler().getTypes().keySet());

                    return new CommonStringPrompt(main,this, false, ChatColor.YELLOW + "  Edit crafted cubelet, \"cancel\" to return.\n  Available cubelets: " + cubelets + "\n\n ", "cubeletType");
                case "2":
                    return new NumericIntegerRangePrompt(main, this, ChatColor.YELLOW + "  Edit craft icon slot, \"cancel\" to return.\n  Range: 0 to " + (main.getCubeletCraftingHandler().getInventorySizeRows() - 1) * 9 + "\n\n ", "slot", 0, (main.getCubeletCraftingHandler().getInventorySizeRows() - 1) * 9);
                case "3":
                    if(param1ConversationContext.getSessionData("cubeletType") != null
                            && param1ConversationContext.getSessionData("slot") != null) {
                        String cubeletType = (String) param1ConversationContext.getSessionData("cubeletType");
                        int slot = (int) param1ConversationContext.getSessionData("slot");

                        if(main.getCubeletTypesHandler().getTypeBydId(cubeletType) == null) {
                            return new ErrorPrompt(main, this, "\n" + ChatColor.RED + "  The crafted cubelet type not exists!\n  Write anything to continue\n ");
                        }

                        craftParent.setCubeletType(cubeletType);
                        craftParent.setSlot(slot);
                        main.getCubeletCraftingHandler().saveCrafting();

                        param1ConversationContext.getForWhom().sendRawMessage("\n" + Utils.translate(main.getLanguageHandler().getPrefix()
                                + " &aYou edited craft for the cubelet &e" + craftParent.getCubeletType()));

                        Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                                ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);


                        main.getMenuHandler().reloadAllMenus(EditCraftingCraftsMenu.class);

                        Player player = (Player) param1ConversationContext.getSessionData("player");

                        new EditCraftingCraftsMenu(main, player).open();

                        main.getConversationHandler().removeConversation(player);

                        return Prompt.END_OF_CONVERSATION;

                    } else {
                        return new ErrorPrompt(main, this, "\n" + ChatColor.RED + "  You need to setup cubeletType and Slot to save craft!\n  Write anything to continue\n ");
                    }
                case "4":
                    return new ConfirmExitPrompt(main, this);
            }
            return null;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {
            String cadena = "";
            cadena += ChatColor.GOLD + "" + ChatColor.BOLD + "\n  CUBELET CRAFT EDITOR MENU\n";
            cadena += ChatColor.GREEN + " \n";

            if (param1ConversationContext.getSessionData("cubeletType") == null) {
                cadena += ChatColor.RED + "    1 " + ChatColor.GRAY + "- Edit crafted cubelet (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    1 " + ChatColor.GRAY + "- Edit crafted cubelet (" + ChatColor.YELLOW + param1ConversationContext.getSessionData("cubeletType") + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("slot") == null) {
                cadena += ChatColor.RED + "    2 " + ChatColor.GRAY + "- Edit craft icon slot (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    2 " + ChatColor.GRAY + "- Edit craft icon slot (" + ChatColor.YELLOW + param1ConversationContext.getSessionData("slot") +  ChatColor.GRAY + ")\n";
            }

            cadena += ChatColor.GREEN + "    3 " + ChatColor.GRAY + "- Save\n";
            cadena += ChatColor.GREEN + "    4 " + ChatColor.GRAY + "- Exit and discard\n";
            cadena += ChatColor.GREEN + " \n";
            cadena += ChatColor.GOLD + "" + ChatColor.YELLOW + "  Choose the option: \n";
            cadena += ChatColor.GREEN + " \n";
            return cadena;
        }
    }

}