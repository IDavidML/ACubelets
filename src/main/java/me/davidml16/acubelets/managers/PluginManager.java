package me.davidml16.acubelets.managers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;

public class PluginManager {

    private final Main main;
    public PluginManager(Main main) {
        this.main = main;
    }

    public void reloadAll() {

        for(Animation task : new ArrayList<>(main.getAnimationHandler().getTasks())) {
            task.stop();
        }

        for(ArmorStand armorStand : main.getAnimationHandler().getArmorStands()) {
            armorStand.remove();
        }

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
