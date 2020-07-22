package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GUIHandler {

    private Set<UUID> conversations;

    private Main main;

    public GUIHandler(Main main) {
        this.main = main;
        this.conversations = new HashSet<>();
    }

    public boolean isOpened(Player player) {
        if(main.getCubeletsGUI().getOpened().containsKey(player.getUniqueId())) return true;
        if(main.getTypeConfigGUI().getOpened().containsKey(player.getUniqueId())) return true;
        if(main.getRewardsGUI().getOpened().containsKey(player.getUniqueId())) return true;
        if(main.getRaritiesGUI().getOpened().containsKey(player.getUniqueId())) return true;
        if(main.getAnimationsGUI().getOpened().containsKey(player.getUniqueId())) return true;
        if(main.getTypeListGUI().getOpened().containsKey(player.getUniqueId())) return true;
        if(main.getCraftingGUI().getOpened().contains(player.getUniqueId())) return true;
        if(main.getCraftingConfirmationGUI().getOpened().containsKey(player.getUniqueId())) return true;
        if(main.getEditBoxGUI().getOpened().containsKey(player.getUniqueId())) return true;
        if(main.getRewardsPreviewGUI().getOpened().containsKey(player.getUniqueId())) return true;
        if(main.getEditCraftingGUI().getOpened().contains(player.getUniqueId())) return true;
        if(main.getEditCraftingCraftsGUI().getOpened().containsKey(player.getUniqueId())) return true;
        if(main.getEditCraftingIngredientsGUI().getOpened().containsKey(player.getUniqueId())) return true;
        
        return false;
    }

    public void closeIfOpened(Player player) {
        if(isOpened(player))
            player.closeInventory();
    }

    public void removeOpened(Player player) {
        main.getCubeletsGUI().getOpened().remove(player.getUniqueId());
        main.getTypeConfigGUI().getOpened().remove(player.getUniqueId());
        main.getRewardsGUI().getOpened().remove(player.getUniqueId());
        main.getRaritiesGUI().getOpened().remove(player.getUniqueId());
        main.getAnimationsGUI().getOpened().remove(player.getUniqueId());
        main.getEditBoxGUI().getOpened().remove(player.getUniqueId());
        main.getCraftingGUI().getOpened().remove(player.getUniqueId());
        main.getCraftingConfirmationGUI().getOpened().remove(player.getUniqueId());
        main.getRewardsPreviewGUI().getOpened().remove(player.getUniqueId());
        main.getTypeListGUI().getOpened().remove(player.getUniqueId());
        main.getEditCraftingGUI().getOpened().remove(player.getUniqueId());
        main.getEditCraftingCraftsGUI().getOpened().remove(player.getUniqueId());
        main.getEditCraftingIngredientsGUI().getOpened().remove(player.getUniqueId());
    }

    public void addConversation(Player player) {
        conversations.add(player.getUniqueId());
    }

    public boolean haveConversation(Player player) {
        return conversations.contains(player.getUniqueId());
    }

    public void removeConversation(Player player) {
        conversations.remove(player.getUniqueId());
    }

}
