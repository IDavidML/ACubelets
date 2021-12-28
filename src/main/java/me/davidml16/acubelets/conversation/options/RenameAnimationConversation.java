package me.davidml16.acubelets.conversation.options;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.conversation.CommonPrompts;
import me.davidml16.acubelets.menus.options.OptionsAnimationsMenu;
import me.davidml16.acubelets.menus.rewards.EditRewardCommandsMenu;
import me.davidml16.acubelets.objects.Menu;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class RenameAnimationConversation implements ConversationAbandonedListener, CommonPrompts {

    private Main main;
    public RenameAnimationConversation(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, AnimationSettings animationSettings) {
        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RenameMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("animationSettings", animationSettings);
        conversation.getContext().setSessionData("animationName", animationSettings.getDisplayName());

        main.getConversationHandler().addConversation(paramPlayer);

        return conversation;
    }

    public Conversation getConversation(Player paramPlayer) { return getConversation(paramPlayer, null); }

    public void conversationAbandoned(ConversationAbandonedEvent paramConversationAbandonedEvent) {}

    public class RenameMenuOptions extends FixedSetPrompt {
        RenameMenuOptions() { super("1", "2"); }

        protected Prompt acceptValidatedInput(ConversationContext param1ConversationContext, String param1String) {

            AnimationSettings animationSettings = (AnimationSettings) param1ConversationContext.getSessionData("animationSettings");
            Player player = (Player) param1ConversationContext.getSessionData("player");

            switch (param1String) {

                case "1":

                    return new CommonStringPrompt(main, this, true, ChatColor.YELLOW + "  Enter animation display name, \"cancel\" to return.\n\n ", "animationName");

                case "2":

                    String name = (String) param1ConversationContext.getSessionData("animationName");
                    animationSettings.setDisplayName(name);

                    main.getAnimationHandler().saveAnimations();

                    main.getMenuHandler().reloadAllMenus(OptionsAnimationsMenu.class);

                    OptionsAnimationsMenu optionsAnimationsMenu = new OptionsAnimationsMenu(main, player);
                    optionsAnimationsMenu.setAttribute(Menu.AttrType.ANIMATION_SETTINGS_ATTR, animationSettings);
                    optionsAnimationsMenu.open();

                    main.getConversationHandler().removeConversation(player);

                    return Prompt.END_OF_CONVERSATION;

            }
            return null;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {

            String cadena = "";
            cadena += ChatColor.GOLD + "" + ChatColor.BOLD + "\n  ANIMATION RENAME MENU\n";
            cadena += ChatColor.GREEN + " \n";
            cadena += ChatColor.GREEN + "    1 " + ChatColor.GRAY + "- Change animation display name (" + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', (String) param1ConversationContext.getSessionData("animationName")) + ChatColor.GRAY + ")\n";
            cadena += ChatColor.GREEN + "    2 " + ChatColor.GRAY + "- Save and exit\n";
            cadena += ChatColor.GREEN + " \n";
            cadena += ChatColor.GOLD + "" + ChatColor.YELLOW + "  Choose the option: \n";
            cadena += ChatColor.GREEN + " \n";

            return cadena;

        }
    }

}