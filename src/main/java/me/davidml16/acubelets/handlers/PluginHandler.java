package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.objects.CubeletMachine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

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

        main.setCraftingEnabled(main.getConfig().getBoolean("Crafting"));
        main.setPreviewEnabled(main.getConfig().getBoolean("RewardsPreview.Enabled"));
        main.getMenuHandler().setClickType(main.getConfig().getString("RewardsPreview.ClickType"));

        main.setCubeletsCommandEnabled(main.getConfig().getBoolean("NoCubelets.ExecuteCommand"));
        main.setNoCubeletsCommand(main.getConfig().getString("NoCubelets.Command"));
        main.setNoCubeletsCommandExecutor(main.getConfig().getString("NoCubelets.Executor"));

        main.setDuplicationEnabled(main.getConfig().getBoolean("RewardsDuplication.Enabled"));
        main.setDuplicationPermissionCommand(main.getConfig().getString("RewardsDuplication.PermissionCommand"));
        main.setDuplicationPointsCommand(main.getConfig().getString("RewardsDuplication.PointsCommand"));

        main.setDuplicationVisibleAllPlayers(main.getConfig().getBoolean("Holograms.Duplication.VisibleToAllPlayers"));

        main.setNoGuiMode(main.getConfig().getBoolean("NoGuiMode"));

        main.setRewardSorting(main.getConfig().getBoolean("RewardAutoSorting"));

        main.setLiveGuiUpdates(main.getConfig().getBoolean("LiveGuiUpdates"));

        main.setSerializeBase64(main.getConfig().getBoolean("SerializeBase64"));

        main.setGiftCubelets(main.getConfig().getBoolean("GiftCubeletsCommand"));

        main.setAnimationsByPlayer(main.getConfig().getBoolean("AnimationsByPlayer"));

        main.setKeysEnabled(main.getConfig().getBoolean("UseKeys"));

        main.setBroadcastEnabled(main.getConfig().getBoolean("BroadcastReward"));

        main.setLoginReminder(main.getConfig().getBoolean("LoginReminder"));

        main.getLanguageHandler().loadLanguage("en");
        main.getLanguageHandler().loadLanguage("es");
        main.getLanguageHandler().setLanguage(main.getConfig().getString("Language").toLowerCase());

        main.getHologramTask().stop();
        main.getLiveGuiTask().stop();
        main.getMachineEffectsTask().stop();

        main.getHologramImplementation().removeHolograms();
        main.getLanguageHandler().pushMessages();
        main.getCubeletBoxHandler().loadBoxes();
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

        if(main.isLiveGuiUpdates())
            main.getLiveGuiTask().start();
    }

}
