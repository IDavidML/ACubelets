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
        RenameMenuOptions() { super("1", "2"); }

        protected Prompt acceptValidatedInput(ConversationContext param1ConversationContext, String param1String) {
            CubeletType cubeletType = (CubeletType) param1ConversationContext.getSessionData("type");
            Player player = (Player) param1ConversationContext.getSessionData("player");
            switch (param1String) {
                case "1":
                    return new SkullStringPrompt(main, this, false, ChatColor.YELLOW + "  Enter skull texture, \"cancel\" to return.\n\n  Options:\n   - base64:\n   - uuid:\n   - url:\n   - name:\n\n ", "texture");
                case "2":
                    String[] icon = ((String) param1ConversationContext.getSessionData("texture")).split(":");
                    main.getCubeletTypesHandler().getConfig(cubeletType.getId()).set("type.icon.texture", param1ConversationContext.getSessionData("texture"));
                    if (icon[0].equalsIgnoreCase("base64"))
                        cubeletType.setIcon(SkullCreator.itemFromBase64(icon[1]));
                    else if (icon[0].equalsIgnoreCase("url"))
                        cubeletType.setIcon(SkullCreator.itemFromUrl(icon[1]));
                    else if (icon[0].equalsIgnoreCase("uuid"))
                        cubeletType.setIcon(SkullCreator.itemFromUuid(UUID.fromString(icon[1])));
                    else if (icon[0].equalsIgnoreCase("name"))
                        cubeletType.setIcon(SkullCreator.itemFromName(icon[1]));
                    main.getCubeletTypesHandler().saveConfig(cubeletType.getId());
                    param1ConversationContext.getForWhom().sendRawMessage("\n" + ColorUtil.translate(main.getLanguageHandler().getPrefix()
                            + " &aSaved skull texture of cubelet type &e" + cubeletType.getId() + " &awithout errors!"));
                    Sounds.playSound(player, player.getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);
                    main.getTypeConfigGUI().reloadGUI(cubeletType.getId());
                    main.getTypeConfigGUI().open(player, cubeletType.getId());
                    return Prompt.END_OF_CONVERSATION;
            }
            return null;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {
            String cadena = "";
            cadena += ChatColor.GOLD + "" + ChatColor.BOLD + "\n  CUBELET TYPE ICON MENU\n";
            cadena += ChatColor.GREEN + " \n";
            cadena += ChatColor.GREEN + "    1 " + ChatColor.GRAY + "- Change cubelet skull texture (" + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', (String)param1ConversationContext.getSessionData("texture")).split(":")[0] + ChatColor.GRAY + ")\n";
            cadena += ChatColor.GREEN + "    2 " + ChatColor.GRAY + "- Save and exit\n";
            cadena += ChatColor.GREEN + " \n";
            cadena += ChatColor.GOLD + "" + ChatColor.YELLOW + "  Choose the option: \n";
            cadena += ChatColor.GREEN + " \n";
            return cadena;
        }
    }

}