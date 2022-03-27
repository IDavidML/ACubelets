package me.davidml16.acubelets.conversation.crafting;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.CommonPrompts;
import me.davidml16.acubelets.enums.CraftType;
import me.davidml16.acubelets.menus.admin.crafting.EditCraftingIngredientsMenu;
import me.davidml16.acubelets.objects.CraftIngredient;
import me.davidml16.acubelets.objects.CraftParent;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class CraftIngredientPointsConversation implements ConversationAbandonedListener, CommonPrompts {

    private Main main;
    public CraftIngredientPointsConversation(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, CraftParent craftParent, CraftType craftType) {
        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RewardMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("craftParent", craftParent);
        conversation.getContext().setSessionData("craftType", craftType);

        main.getConversationHandler().addConversation(paramPlayer);

        return conversation;
    }

    public void conversationAbandoned(ConversationAbandonedEvent paramConversationAbandonedEvent) {}

    public class RewardMenuOptions extends FixedSetPrompt {
        RewardMenuOptions() { super("1", "2", "3"); }

        protected Prompt acceptValidatedInput(ConversationContext param1ConversationContext, String param1String) {
            switch (param1String) {
                case "1":
                    return new IntegerPrompt(main,this, ChatColor.YELLOW + "  Enter required amount, \"cancel\" to return.\n\n ", "amount");
                case "2":
                    if(param1ConversationContext.getSessionData("amount") != null) {
                        int amount = (int) param1ConversationContext.getSessionData("amount");
                        CraftParent craftParent = (CraftParent) param1ConversationContext.getSessionData("craftParent");
                        CraftType craftType = (CraftType) param1ConversationContext.getSessionData("craftType");

                        CraftIngredient craftIngredient = new CraftIngredient(craftParent.getCubeletType(), craftType, amount);
                        craftParent.getIngrediens().add(craftIngredient);
                        main.getCubeletCraftingHandler().saveCrafting();

                        param1ConversationContext.getForWhom().sendRawMessage("\n" + Utils.translate(main.getLanguageHandler().getPrefix()
                                + " &aYou added a new ingredient for cubelet &e" + craftParent.getCubeletType()));

                        Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                                ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);

                        main.getMenuHandler().reloadAllMenus(EditCraftingIngredientsMenu.class);

                        Player player = (Player) param1ConversationContext.getSessionData("player");

                        EditCraftingIngredientsMenu editCraftingIngredientsMenu = new EditCraftingIngredientsMenu(main, player);
                        editCraftingIngredientsMenu.setAttribute(Menu.AttrType.CRAFT_PARENT_ATTR, craftParent);
                        editCraftingIngredientsMenu.open();

                        main.getConversationHandler().removeConversation(player);

                        return Prompt.END_OF_CONVERSATION;

                    } else {
                        return new ErrorPrompt(main, this, "\n" + ChatColor.RED + "  You need to setup amount to save craft!\n  Write anything to continue\n ");
                    }
                case "3":
                    return new ConfirmExitPrompt(main, this);
            }
            return null;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {
            String cadena = "";
            cadena += ChatColor.GOLD + "" + ChatColor.BOLD + "\n  CUBELET INGREDIENT (LOOT POINTS) CREATION MENU\n";
            cadena += ChatColor.GREEN + " \n";

            if (param1ConversationContext.getSessionData("amount") == null) {
                cadena += ChatColor.RED + "    1 " + ChatColor.GRAY + "- Set required amount (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    1 " + ChatColor.GRAY + "- Set required amount (" + ChatColor.YELLOW + param1ConversationContext.getSessionData("amount") +  ChatColor.GRAY + ")\n";
            }

            cadena += ChatColor.GREEN + "    2 " + ChatColor.GRAY + "- Save\n";
            cadena += ChatColor.GREEN + "    3 " + ChatColor.GRAY + "- Exit and discard\n";
            cadena += ChatColor.GREEN + " \n";
            cadena += ChatColor.GOLD + "" + ChatColor.YELLOW + "  Choose the option: \n";
            cadena += ChatColor.GREEN + " \n";
            return cadena;
        }
    }

}