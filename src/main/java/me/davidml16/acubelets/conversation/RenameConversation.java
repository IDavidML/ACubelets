package me.davidml16.acubelets.conversation;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.admin.type.TypeConfigMenu;
import me.davidml16.acubelets.menus.admin.type.TypeSettingsMenu;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class RenameConversation implements ConversationAbandonedListener, CommonPrompts {

    private Main main;
    public RenameConversation(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, CubeletType type) {
        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RenameMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("type", type);
        conversation.getContext().setSessionData("typeName", type.getName());

        main.getConversationHandler().addConversation(paramPlayer);

        return conversation;
    }

    public Conversation getConversation(Player paramPlayer) { return getConversation(paramPlayer, null); }

    public void conversationAbandoned(ConversationAbandonedEvent paramConversationAbandonedEvent) {}

    public class RenameMenuOptions extends FixedSetPrompt {
        RenameMenuOptions() { super("1", "2"); }

        protected Prompt acceptValidatedInput(ConversationContext param1ConversationContext, String param1String) {
            CubeletType type = (CubeletType) param1ConversationContext.getSessionData("type");
            Player player = (Player) param1ConversationContext.getSessionData("player");
            switch (param1String) {
                case "1":
                    return new CommonStringPrompt(main, this, true, ChatColor.YELLOW + "  Enter cubelet type name, \"cancel\" to return.\n\n ", "typeName");
                case "2":
                    String name = (String) param1ConversationContext.getSessionData("typeName");
                    type.setName(name);
                    main.getCubeletTypesHandler().getConfig(type.getId()).set("type.name", name);
                    main.getCubeletTypesHandler().saveConfig(type.getId());
                    param1ConversationContext.getForWhom().sendRawMessage("\n" + Utils.translate(main.getLanguageHandler().getPrefix()
                            + " &aSaved data of cubelet type &e" + type.getId() + " &awithout errors!"));
                    Sounds.playSound(player, player.getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);

                    main.getMenuHandler().reloadAllMenus(TypeConfigMenu.class);
                    main.getMenuHandler().reloadAllMenus(TypeSettingsMenu.class);

                    TypeSettingsMenu typeSettingsMenu = new TypeSettingsMenu(main, player);
                    typeSettingsMenu.setAttribute(Menu.AttrType.CUSTOM_ID_ATTR, type.getId());
                    typeSettingsMenu.open();

                    main.getConversationHandler().removeConversation(player);

                    return Prompt.END_OF_CONVERSATION;
            }
            return null;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {
            String cadena = "";
            cadena += ChatColor.GOLD + "" + ChatColor.BOLD + "\n  CUBELET TYPE RENAME MENU\n";
            cadena += ChatColor.GREEN + " \n";
            cadena += ChatColor.GREEN + "    1 " + ChatColor.GRAY + "- Rename cubelet type (" + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', (String)param1ConversationContext.getSessionData("typeName")) + ChatColor.GRAY + ")\n";
            cadena += ChatColor.GREEN + "    2 " + ChatColor.GRAY + "- Save and exit\n";
            cadena += ChatColor.GREEN + " \n";
            cadena += ChatColor.GOLD + "" + ChatColor.YELLOW + "  Choose the option: \n";
            cadena += ChatColor.GREEN + " \n";
            return cadena;
        }
    }

}