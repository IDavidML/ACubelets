package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PluginHandler {

    private final Main main;
    public PluginHandler(Main main) {
        this.main = main;
    }

    public void reloadAll() {

        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(main.getMenuHandler().hasOpenedMenu(player))
                player.closeInventory();
        }

        main.getPlayerDataHandler().saveAllPlayerDataAsync();

        for(Animation task : new ArrayList<>(main.getAnimationHandler().getTasks())) {
            task.stop();
        }
        main.getAnimationHandler().getTasks().clear();

        for(Entity entity : main.getAnimationHandler().getEntities()) {
            if(entity == null || entity.isDead()) continue;
            entity.remove();
        }
        main.getAnimationHandler().getEntities().clear();

        main.reloadConfig();

        main.registerSettings();

        main.getMenuHandler().setClickType(main.getConfig().getString("Rewards.Preview.ClickType"));
        main.getCubeletBoxHandler().setClickType(main.getConfig().getString("CubeletMachine.ClickType"));

        main.getLanguageHandler().loadLanguage("en");
        main.getLanguageHandler().loadLanguage("es");
        main.getLanguageHandler().setLanguage(main.getConfig().getString("Language").toLowerCase());

        main.getHologramTask().stop();
        main.getLiveGuiTask().stop();
        main.getMachineEffectsTask().stop();

        main.getHologramImplementation().removeHolograms();
        main.getLanguageHandler().pushMessages();
        main.getCubeletBoxHandler().loadMachines();
        main.getCubeletTypesHandler().loadTypes();
        main.getCubeletRarityHandler().loadRarities();
        main.getCubeletRewardHandler().loadRewards();
        main.getCubeletTypesHandler().printLog();
        main.getCubeletCraftingHandler().loadCrafting();
        main.getLayoutHandler().loadLayouts();
        main.getAnimationHandler().loadAnimations();
        main.getHologramHandler().getColorAnimation().setColors(main.getConfig().getStringList("Holograms.ColorAnimation"));

        int distance = main.getConfig().getInt("Holograms.VisibilityDistance");
        main.getHologramHandler().setVisibilityDistance(distance * distance);

        main.getHologramImplementation().loadHolograms();

        main.getHologramTask().start();
        main.getMachineEffectsTask().start();

        if(main.isSetting("LiveGuiUpdates"))
            main.getLiveGuiTask().start();
    }

}
