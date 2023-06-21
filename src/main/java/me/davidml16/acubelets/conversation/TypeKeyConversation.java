package me.davidml16.acubelets.conversation;

import com.cryptomorin.xseries.XItemStack;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.admin.type.TypeSettingsMenu;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.ItemStack64;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TypeKeyConversation implements ConversationAbandonedListener, CommonPrompts {

    private Main main;
    public TypeKeyConversation(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, CubeletType type) {
        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RenameMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("type", type);
        conversation.getContext().setSessionData("typeKey", type.getKey());

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
                    Player p = (Player) param1ConversationContext.getSessionData("player");
                    ItemStack itemHand = p.getInventory().getItemInHand();

                    if(itemHand == null || itemHand.getType() == Material.AIR) {
                        param1ConversationContext.getForWhom().sendRawMessage(ChatColor.RED + "  AIR icon not allowed!\n ");
                        Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                                ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.NOTE_PLING, 10, 0);
                        return this;
                    }

                    param1ConversationContext.setSessionData("typeKey", itemHand);
                    param1ConversationContext.getForWhom().sendRawMessage(
                            ChatColor.GREEN + "  Succesfully setup type key.");
                    Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                            ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.CLICK, 10, 2);

                    return this;
                case "2":
                    ItemStack key = (ItemStack) param1ConversationContext.getSessionData("typeKey");

                    type.setKey(key);

                    FileConfiguration config = main.getCubeletTypesHandler().getConfig(type.getId());

                    config.set("type.key", null);

                    if(!main.isSerializeBase64())
                        XItemStack.serialize(key, Utils.getConfigurationSection(config, "type.key"));
                    else
                        config.set("type.key", ItemStack64.itemStackToBase64(key));

                    main.getCubeletTypesHandler().saveConfig(type.getId());

                    param1ConversationContext.getForWhom().sendRawMessage("\n" + Utils.translate(main.getLanguageHandler().getPrefix()
                            + " &aSaved key of cubelet type &e" + type.getId() + " &awithout errors!"));
                    Sounds.playSound(player, player.getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);

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
            cadena += ChatColor.GOLD + "" + ChatColor.BOLD + "\n  CUBELET TYPE KEY MENU\n";
            cadena += ChatColor.GREEN + " \n";

            if (param1ConversationContext.getSessionData("typeKey") == null) {
                cadena += ChatColor.RED + "    1 " + ChatColor.GRAY + "- Edit reward icon 'Item in Hand' (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                ItemStack key = (ItemStack) param1ConversationContext.getSessionData("typeKey");
                cadena += ChatColor.GREEN + "    1 " + ChatColor.GRAY + "- Edit type key 'Item in Hand' (" + ChatColor.YELLOW + key.getType().name() + ChatColor.GRAY + ")\n";
            }

            cadena += ChatColor.GREEN + "    2 " + ChatColor.GRAY + "- Save and exit\n";
            cadena += ChatColor.GREEN + " \n";
            cadena += ChatColor.GOLD + "" + ChatColor.YELLOW + "  Choose the option: \n";
            cadena += ChatColor.GREEN + " \n";
            return cadena;
        }
    }

}