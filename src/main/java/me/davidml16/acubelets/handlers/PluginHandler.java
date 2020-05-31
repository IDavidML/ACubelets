package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.commands.CoreCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

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

        main.setCraftingEnabled(main.getConfig().getBoolean("Crafting"));
        main.setPreviewEnabled(main.getConfig().getBoolean("RewardsPreview.Enabled"));
        main.getCubeletsGUI().setClickType(main.getConfig().getString("RewardsPreview.ClickType"));

        main.setCubeletsCommandEnabled(main.getConfig().getBoolean("NoCubelets.ExecuteCommand"));
        main.setNoCubeletsCommand(main.getConfig().getString("NoCubelets.Command"));

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
        main.getCubeletCraftingHandler().loadCrafting();
        main.getHologramTask().start();
    }

}
