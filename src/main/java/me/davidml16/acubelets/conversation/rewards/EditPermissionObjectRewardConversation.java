package me.davidml16.acubelets.conversation.rewards;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.CommonPrompts;
import me.davidml16.acubelets.menus.rewards.EditRewardCommandsMenu;
import me.davidml16.acubelets.menus.rewards.EditRewardPermissionsMenu;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.objects.rewards.PermissionObject;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class EditPermissionObjectRewardConversation implements ConversationAbandonedListener, CommonPrompts {

    private Main main;
    public EditPermissionObjectRewardConversation(Main main) {
        this.main = main;
    }

    public Conversation getConversation(Player paramPlayer, CubeletType cubeletType, Reward reward, PermissionObject permissionObject) {
        Conversation conversation = (new ConversationFactory(main)).withModality(true).withLocalEcho(false).withFirstPrompt(new RewardMenuOptions()).withTimeout(3600).thatExcludesNonPlayersWithMessage("").addConversationAbandonedListener(this).buildConversation(paramPlayer);
        conversation.getContext().setSessionData("player", paramPlayer);
        conversation.getContext().setSessionData("permissionObject", permissionObject);
        conversation.getContext().setSessionData("reward", reward);
        conversation.getContext().setSessionData("permission", permissionObject.getPermission());
        conversation.getContext().setSessionData("cubeletType", cubeletType);

        main.getConversationHandler().addConversation(paramPlayer);

        return conversation;
    }

    public Conversation getConversation(Player paramPlayer) { return getConversation(paramPlayer, null, null,null); }

    public void conversationAbandoned(ConversationAbandonedEvent paramConversationAbandonedEvent) {}

    public class RewardMenuOptions extends FixedSetPrompt {
        RewardMenuOptions() { super("1", "2", "3"); }

        protected Prompt acceptValidatedInput(ConversationContext param1ConversationContext, String param1String) {
            CubeletType cubeletType = (CubeletType) param1ConversationContext.getSessionData("cubeletType");
            switch (param1String) {
                case "1":
                    return new CommonStringPrompt(main,this, true,ChatColor.YELLOW + "  Edit reward permission, \"cancel\" to return.\n\n ", "permission");
                case "2":
                    if(param1ConversationContext.getSessionData("permissionObject") != null) {
                        String rewardPermission = (String) param1ConversationContext.getSessionData("permission");

                        PermissionObject permissionObject = (PermissionObject) param1ConversationContext.getSessionData("permissionObject");
                        permissionObject.setPermission(rewardPermission);

                        cubeletType.saveType();

                        param1ConversationContext.getForWhom().sendRawMessage("\n" + Utils.translate(main.getLanguageHandler().getPrefix()
                                + " &aYou edited &e" + permissionObject.getId() + " &afrom permissions of cubelet type &e" + cubeletType.getId()));

                        Sounds.playSound((Player) param1ConversationContext.getSessionData("player"),
                                ((Player) param1ConversationContext.getSessionData("player")).getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);

                        Reward reward = (Reward) param1ConversationContext.getSessionData("reward");

                        main.getMenuHandler().reloadAllMenus(EditRewardPermissionsMenu.class);

                        Player player = (Player) param1ConversationContext.getSessionData("player");

                        EditRewardPermissionsMenu editRewardPermissionsMenu = new EditRewardPermissionsMenu(main, player);
                        editRewardPermissionsMenu.setAttribute(Menu.AttrType.REWARD_ATTR, reward);
                        editRewardPermissionsMenu.open();

                        main.getConversationHandler().removeConversation(player);

                        return Prompt.END_OF_CONVERSATION;

                    } else {
                        return new ErrorPrompt(main, this, "\n" + ChatColor.RED + "  You need to setup PERMISSION to save command reward!\n  Write anything to continue\n ");
                    }
                case "3":
                    return new ConfirmExitPrompt(main, this);
            }
            return null;
        }


        public String getPromptText(ConversationContext param1ConversationContext) {
            String cadena = "";
            cadena += ChatColor.GOLD + "" + ChatColor.BOLD + "\n  CUBELET PERMISSION REWARD EDITOR MENU\n";
            cadena += ChatColor.GREEN + " \n";

            if (param1ConversationContext.getSessionData("permissionObject") == null) {
                cadena += ChatColor.RED + "    1 " + ChatColor.GRAY + "- Edit reward permission (" + ChatColor.RED + "none" + ChatColor.GRAY + ")\n";
            } else {
                cadena += ChatColor.GREEN + "    1 " + ChatColor.GRAY + "- Edit reward permission (" + ChatColor.YELLOW + param1ConversationContext.getSessionData("permission") + ChatColor.GRAY + ")\n";
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