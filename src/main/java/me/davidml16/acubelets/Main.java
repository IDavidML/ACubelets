package me.davidml16.acubelets;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.davidml16.acubelets.animations.AnimationHandler;
import me.davidml16.acubelets.api.CubeletsAPI;
import me.davidml16.acubelets.commands.CoreCommand;
import me.davidml16.acubelets.commands.TabCompleter_ACubelets;
import me.davidml16.acubelets.database.DatabaseHandler;
import me.davidml16.acubelets.database.types.Database;
import me.davidml16.acubelets.events.Event_Damage;
import me.davidml16.acubelets.events.Event_Interact;
import me.davidml16.acubelets.events.Event_JoinQuit;
import me.davidml16.acubelets.gui.*;
import me.davidml16.acubelets.handlers.*;
import me.davidml16.acubelets.handlers.PluginHandler;
import me.davidml16.acubelets.tasks.HologramTask;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.FireworkUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main main;
    public static ConsoleCommandSender log;
    private MetricsLite metrics;

    private HologramTask hologramTask;

    private LanguageHandler languageHandler;
    private DatabaseHandler databaseHandler;
    private PlayerDataHandler playerDataHandler;
    private CubeletTypesHandler cubeletTypesHandler;
    private CubeletRarityHandler cubeletRarityHandler;
    private CubeletRewardHandler cubeletRewardHandler;
    private CubeletBoxHandler cubeletBoxHandler;
    private HologramHandler hologramHandler;
    private CubeletOpenHandler cubeletOpenHandler;
    private AnimationHandler animationHandler;

    private FireworkUtil fireworkUtil;

    private PluginHandler pluginHandler;

    private Cubelets_GUI cubeletsGUI;
    private TypeConfig_GUI typeConfigGUI;
    private Rewards_GUI rewardsGUI;
    private Rarities_GUI raritiesGUI;
    private Animations_GUI animationsGUI;

    @Override
    public void onEnable() {
        main = this;
        log = Bukkit.getConsoleSender();
        metrics = new MetricsLite(this, 7349);

        saveDefaultConfig();
        reloadConfig();

        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays") || !Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().severe("*** HolographicDisplays / ProtocolLib is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            setEnabled(false);
            return;
        }

        pluginHandler = new PluginHandler(this);

        languageHandler = new LanguageHandler(this, getConfig().getString("Language").toLowerCase());
        languageHandler.pushMessages();

        databaseHandler = new DatabaseHandler(this);
        databaseHandler.openConnection();
        databaseHandler.getDatabase().loadTables();

        animationHandler = new AnimationHandler(this);
        animationHandler.loadAnimations();

        cubeletBoxHandler = new CubeletBoxHandler(this);
        cubeletBoxHandler.loadBoxes();

        cubeletTypesHandler = new CubeletTypesHandler(this);
        cubeletTypesHandler.loadTypes();

        cubeletRarityHandler = new CubeletRarityHandler(this);
        cubeletRarityHandler.loadRarities();

        cubeletRewardHandler = new CubeletRewardHandler(this);
        cubeletRewardHandler.loadRewards();

        playerDataHandler = new PlayerDataHandler(this);

        hologramHandler = new HologramHandler(this);
        hologramHandler.loadHolograms();

        hologramTask = new HologramTask(this);
        hologramTask.start();

        cubeletOpenHandler = new CubeletOpenHandler(this);

        cubeletsGUI = new Cubelets_GUI(this);
        cubeletsGUI.loadGUI();

        typeConfigGUI = new TypeConfig_GUI(this);
        typeConfigGUI.loadGUI();

        rewardsGUI = new Rewards_GUI(this);
        rewardsGUI.loadGUI();

        raritiesGUI = new Rarities_GUI(this);
        raritiesGUI.loadGUI();

        animationsGUI = new Animations_GUI(this);
        animationsGUI.loadGUI();

        fireworkUtil = new FireworkUtil(this);

        registerCommands();
        registerEvents();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderHook(this).register();
        }

        PluginDescriptionFile pdf = getDescription();
        log.sendMessage(ColorUtil.translate("  &eACubelets Enabled!"));
        log.sendMessage(ColorUtil.translate("    &aVersion: &b" + pdf.getVersion()));
        log.sendMessage(ColorUtil.translate("    &aAuthor: &b" + pdf.getAuthors().get(0)));
        log.sendMessage("");

    }

    @Override
    public void onDisable() {
        PluginDescriptionFile pdf = getDescription();
        log.sendMessage("");
        log.sendMessage(ColorUtil.translate("  &eACubelets Disabled!"));
        log.sendMessage(ColorUtil.translate("    &aVersion: &b" + pdf.getVersion()));
        log.sendMessage(ColorUtil.translate("    &aAuthor: &b" + pdf.getAuthors().get(0)));
        log.sendMessage("");

        hologramHandler.removeHolograms();

        for (Hologram hologram : HologramsAPI.getHolograms(this)) {
            hologram.delete();
        }

        hologramTask.stop();
        databaseHandler.getDatabase().close();
    }

    public static Main get() { return main; }

    public MetricsLite getMetrics() {
        return metrics;
    }

    public LanguageHandler getLanguageHandler() {
        return languageHandler;
    }

    public Database getDatabaseHandler() {
        return databaseHandler.getDatabase();
    }

    public DatabaseHandler getDatabase() { return databaseHandler; }

    public PlayerDataHandler getPlayerDataHandler() { return playerDataHandler; }

    public CubeletTypesHandler getCubeletTypesHandler() { return cubeletTypesHandler; }

    public CubeletRarityHandler getCubeletRarityHandler() { return cubeletRarityHandler; }

    public CubeletRewardHandler getCubeletRewardHandler() { return cubeletRewardHandler; }

    public CubeletBoxHandler getCubeletBoxHandler() { return cubeletBoxHandler; }

    public HologramHandler getHologramHandler() { return hologramHandler; }

    public CubeletOpenHandler getCubeletOpenHandler() { return cubeletOpenHandler; }

    public AnimationHandler getAnimationHandler() { return animationHandler; }

    public Cubelets_GUI getCubeletsGUI() { return cubeletsGUI; }

    public TypeConfig_GUI getTypeConfigGUI() { return typeConfigGUI; }

    public Rewards_GUI getRewardsGUI() { return rewardsGUI; }

    public Rarities_GUI getRaritiesGUI() { return raritiesGUI; }

    public Animations_GUI getAnimationsGUI() { return animationsGUI; }

    public PluginHandler getPluginHandler() { return pluginHandler; }

    public HologramTask getHologramTask() { return hologramTask; }

    public FireworkUtil getFireworkUtil() { return fireworkUtil; }

    public boolean playerHasPermission(Player p, String permission) {
        return p.hasPermission(permission) || p.isOp();
    }

    private void registerCommands() {
        getCommand("cubelets").setExecutor(new CoreCommand(this));
        getCommand("cubelets").setTabCompleter(new TabCompleter_ACubelets(this));
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Event_Interact(this), this);
        Bukkit.getPluginManager().registerEvents(new Event_JoinQuit(this), this);
        Bukkit.getPluginManager().registerEvents(new Event_Damage(), this);
    }

}
