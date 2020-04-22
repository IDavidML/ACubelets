package me.davidml16.acubelets.conversation;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.data.CubeletType;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TypeIconMenu implements ConversationAbandonedListener, CommonPrompts {

    private Main main;
    public TypeIconMenu(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, CubeletType type) {
        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RenameMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("type", type);
        conversation.getContext().setSessionData("texture", main.getCubeletTypesHandler().getConfig(type.getId()).get("type.icon.texture"));
        return conversation;
    }

    public Conversation getConversation(Player paramPlayer) { return getConversation(paramPlayer, null); }

    public void conversationAbandoned(ConversationAbandonedEvent paramConversationAbandonedEvent) {}

    public class RenameMenuOptions extends FixedSetPrompt {
        RenameMenuOptions() { super("1", "2", "3", "4", "5"); }

        protected Prompt acceptValidatedInput(ConversationContext param1ConversationContext, String param1String) {
            CubeletType cubeletType = (CubeletType) param1ConversationContext.getSessionData("type");
            Player player = (Player) param1ConversationContext.getSessionData("player");
            switch (param1String) {
                case "1":
                    param1ConversationContext.setSessionData("method", "base64");
                    return new SkullStringPrompt(main, this, false, ChatColor.YELLOW + "  Enter base64 texture string, \"cancel\" to return.\n\n ", "texture");
                case "2":
                    param1ConversationContext.setSessionData("method", "uuid");
                    return new SkullStringPrompt(main, this, false, ChatColor.YELLOW + "  Enter player uuid, \"cancel\" to return.\n\n ", "texture");
                case "3":
                    param1ConversationContext.setSessionData("method", "name");
                    return new SkullStringPrompt(main, this, false, ChatColor.YELLOW + "  Enter player name, \"cancel\" to return.\n\n ", "texture");
                case "4":
                    String method = (String) param1ConversationContext.getSessionData("method");
                    String texture = (String) param1ConversationContext.getSessionData("texture");

                    main.getCubeletTypesHandler().getConfig(cubeletType.getId()).set("type.icon.texture", method + ":" + param1ConversationContext.getSessionData("texture"));

                    switch(method) {
                        case "base64":
                            cubeletType.setIcon(SkullCreator.itemFromBase64(texture));
                            break;
                        case "uuid":
                            cubeletType.setIcon(SkullCreator.itemFromUuid(UUID.fromString(texture)));
                            break;
                        case "name":
                            cubeletType.setIcon(SkullCreator.itemFromName(texture));
                            break;
                    }

                    main.getCubeletTypesHandler().saveConfig(cubeletType.getId());
                    param1ConversationContext.getForWhom().sendRawMessage("\n" + ColorUtil.translate(main.getLanguageHandler().getPrefix()
                            + " &aSaved skull texture of cubelet type &e" + cubeletType.getId() + " &awithout errors!"));

                    Sounds.playSound(player, player.getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);
                    main.getTypeConfigGUI().reloadGUI(cubeletType.getId());
                    main.getTypeConfigGUI().open(player, cubeletType.getId());
                    return Prompt.END_OF_CONVERSATION;
                case "5":
                    return new CommonPrompts.ConfirmExitPrompt(main, this);
            }
            return null;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {
            String cadena = "";
            cadena += ChatColor.GOLD + "" + ChatColor.BOLD + "\n  CUBELET TYPE ICON MENU\n ";
            cadena += ChatColor.GREEN + " \n ";
            cadena += ChatColor.GREEN + "    1 " + ChatColor.GRAY + "- Base64 String\n ";
            cadena += ChatColor.GREEN + "    2 " + ChatColor.GRAY + "- Player UUID\n ";
            cadena += ChatColor.GREEN + "    3 " + ChatColor.GRAY + "- Player Name\n ";
            cadena += ChatColor.GREEN + " \n ";
            cadena += ChatColor.GOLD + "    4 " + ChatColor.GRAY + "- Save and exit\n ";
            cadena += ChatColor.GOLD + "    5 " + ChatColor.GRAY + "- Exit and discard\n ";
            cadena += ChatColor.GREEN + "\n ";
            cadena += ChatColor.GOLD + "" + ChatColor.YELLOW + "  Choose the option: \n ";
            return cadena;
        }
    }

}