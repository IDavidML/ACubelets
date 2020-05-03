package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PluginHandler {

    private final Main main;
    public PluginHandler(Main main) {
        this.main = main;
    }

    public void reloadAll() {

        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            if(main.getCubeletsGUI().getOpened().containsKey(p.getUniqueId())) p.closeInventory();
            if(main.getTypeConfigGUI().getOpened().containsKey(p.getUniqueId())) p.closeInventory();
            if(main.getRewardsGUI().getOpened().containsKey(p.getUniqueId())) p.closeInventory();
            if(main.getRaritiesGUI().getOpened().containsKey(p.getUniqueId())) p.closeInventory();
            if(main.getAnimationsGUI().getOpened().containsKey(p.getUniqueId())) p.closeInventory();
        }

        for(Animation task : new ArrayList<>(main.getAnimationHandler().getTasks())) {
            task.stop();
        }
        main.getAnimationHandler().getTasks().clear();

        for(ArmorStand armorStand : main.getAnimationHandler().getArmorStands()) {
            armorStand.remove();
        }
        main.getAnimationHandler().getArmorStands().clear();

        main.reloadConfig();

        main.getLanguageHandler().setLanguage(main.getConfig().getString("Language").toLowerCase());
        main.getHologramTask().stop();
        main.getHologramHandler().removeHolograms();
        main.getLanguageHandler().pushMessages();
        main.getDatabaseHandler().loadTables();
        main.getPlayerDataHandler().loadAllPlayerData();
        main.getCubeletBoxHandler().loadBoxes();
        main.getCubeletTypesHandler().loadTypes();
        main.getCubeletRarityHandler().loadRarities();
        main.getCubeletRewardHandler().loadRewards();
        main.getHologramHandler().loadHolograms();
        main.getHologramTask().start();
    }

}
