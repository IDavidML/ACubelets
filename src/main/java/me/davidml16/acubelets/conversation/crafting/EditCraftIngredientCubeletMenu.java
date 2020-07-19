package me.davidml16.acubelets.conversation.crafting;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.CommonPrompts;
import me.davidml16.acubelets.enums.CraftType;
import me.davidml16.acubelets.objects.CraftIngredient;
import me.davidml16.acubelets.objects.CraftParent;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EditCraftIngredientCubeletMenu implements ConversationAbandonedListener, CommonPrompts {

    private Main main;
    public EditCraftIngredientCubeletMenu(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, CraftParent craftParent, CraftIngredient craftIngredient) {
        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RewardMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("craftParent", craftParent);
        conversation.getContext().setSessionData("craftIngredient", craftIngredient);
        conversation.getContext().setSessionData("cubeletType", craftIngredient.getName());
        conversation.getContext().setSessionData("amount", craftIngredient.getAmount());

        main.getGuiHandler().addConversation(paramPlayer);

        return conversation;
    }

    public void conversationAbandoned(ConversationAbandonedEvent paramConversationAbandonedEvent) {}

    public class RewardMenuOptions extends FixedSetPrompt {
        RewardMenuOptions() { super("1", "2", "3", "4"); }

        protected Prompt acceptValidatedInput(ConversationContext param1ConversationContext, String param1String) {
            switch (param1String) {
                case "1":
                    List<String> cubelets = new ArrayList<>(main.getCubeletTypesHandler().getTypes().keySet());

                    return new CommonStringPrompt(main,this, false, ChatColor.YELLOW + "  Enter required cubelet, \"cancel\" to return.\n  Available cubelets: " + cubelets + "\n\n ", "cubeletType");
                case "2":
                    return new IntegerPrompt(main,this, ChatColor.YELLOW + "  Enter required amount, \"cancel\" to return.\n\n ", "amount");
                case "3":
                    if(param1ConversationContext.getSessionData("cubeletType") != null
                            && param1ConversationContext.getSessionData("amount") != null) {
                        String cubeletType = (String) param1ConversationContext.getSessionData("cubeletType");
                        int amount = (int) param1ConversationContext.getSessionData("amount");
                        CraftIngredient craftIngredient = (CraftIngredient) param1ConversationContext.getSessionData("craftIngredient");
                        CraftParent craftParent = (CraftParent) param1ConversationContext.getSessionData("craftParent");

                        craftIngredient.setName(cubeletType);
                        craftIngredient.setAmount(amount);
                        main.getCubeletCraftingHandler().saveCrafting();

                        param1ConversationContext.getForWhom().sendRawMessage("\n" + ColorUtil.translate(main.getLanguageHandler().getPrefix()
                                + " &aYou edited ingredient for cubelet &e" + craftIngredient.getParentType()));

                        Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                                ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);
                        main.getEditCraftingIngredientsGUI().reloadGUI(craftParent);
                        main.getEditCraftingIngredientsGUI().open((Player) param1ConversationContext.getSessionData("player"), craftParent);
                        main.getGuiHandler().removeConversation((Player) param1ConversationContext.getSessionData("player"));
                        return Prompt.END_OF_CONVERSATION;
                    } else {
                        return new ErrorPrompt(main, this, "\n" + ChatColor.RED + "  You need to setup cubeletType and Amount to save craft!\n  Write anything to continue\n ");
                    }
                case "4":
                    return new ConfirmExitPrompt(main, this);
            }
            return null;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {
            String cadena = "";
            cadena += ChatColor.GOLD + "" + ChatColor.BOLD + "\n  CUBELET INGREDIENT (CUBELET) EDITOR MENU\n";
            cadena += ChatColor.GREEN + " \n";

            if (param1ConversationContext.getSessionData("cubeletType") == null) {
                cadena += ChatColor.RED + "    1 " + ChatColor.GRAY + "- Edit required cubelet (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    1 " + ChatColor.GRAY + "- Edit required cubelet (" + ChatColor.YELLOW + param1ConversationContext.getSessionData("cubeletType") + ChatColor.GRAY + ")\n";
            }

            if (param1ConversationContext.getSessionData("amount") == null) {
                cadena += ChatColor.RED + "    2 " + ChatColor.GRAY + "- Edit required amount (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    2 " + ChatColor.GRAY + "- Edit required amount (" + ChatColor.YELLOW + param1ConversationContext.getSessionData("amount") +  ChatColor.GRAY + ")\n";
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