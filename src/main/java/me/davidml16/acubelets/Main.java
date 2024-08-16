package me.davidml16.acubelets;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationHandler;
import me.davidml16.acubelets.api.CubeletsAPI;
import me.davidml16.acubelets.api.PointsAPI;
import me.davidml16.acubelets.database.DatabaseHandler;
import me.davidml16.acubelets.events.*;
import me.davidml16.acubelets.handlers.*;
import me.davidml16.acubelets.holograms.HologramHandler;
import me.davidml16.acubelets.holograms.HologramImplementation;
import me.davidml16.acubelets.tasks.*;
import me.davidml16.acubelets.utils.ConfigUpdater;
import me.davidml16.acubelets.utils.FireworkUtil;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class Main extends JavaPlugin {

    private static Main main;
    public static ConsoleCommandSender log;
    private MetricsLite metrics;

    private CubeletsAPI cubeletsAPI;
    private PointsAPI pointsAPI;

    private ProtocolManager protocolManager;

    private HologramTask hologramTask;
    private DataSaveTask dataSaveTask;
    private LiveGuiTask liveGuiTask;
    private DataCacheTask dataCacheTask;
    private MachineEffectsTask machineEffectsTask;

    private LanguageHandler languageHandler;
    private DatabaseHandler databaseHandler;
    private PlayerDataHandler playerDataHandler;
    private CubeletTypesHandler cubeletTypesHandler;
    private CubeletRarityHandler cubeletRarityHandler;
    private CubeletRewardHandler cubeletRewardHandler;
    private CubeletMachineHandler cubeletMachineHandler;
    private HologramHandler hologramHandler;
    private CubeletOpenHandler cubeletOpenHandler;
    private AnimationHandler animationHandler;
    private CubeletCraftingHandler cubeletCraftingHandler;
    private EconomyHandler economyHandler;
    private LayoutHandler layoutHandler;
    private ConversationHandler conversationHandler;
    private TransactionHandler transactionHandler;

    private MenuHandler menuHandler;

    private FireworkUtil fireworkUtil;

    private PluginHandler pluginHandler;

    private int playerCount;

    private Map<String, Object> settings;

    private CommandMap commandMap;

    private List<String> templates = Arrays.asList("example");

    @Override
    public void onEnable() {
        main = this;
        log = Bukkit.getConsoleSender();
        metrics = new MetricsLite(this, 7349);

        settings = new HashMap<>();

        saveDefaultConfig();
        try {
            ConfigUpdater.update(this, "config.yml", new File(main.getDataFolder(), "config.yml"), Collections.emptyList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadConfig();

        if (!XMaterial.supports(13)) {
            getLogger().severe("***   Plugin only supports 1.13+ versions.");
            getLogger().severe("***   Use the latest plugin version 2.1.8 where supports 1.8+.");
            setEnabled(false);
            return;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().severe("*** ProtocolLib is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            setEnabled(false);
            return;
        }

        protocolManager = ProtocolLibrary.getProtocolManager();

        registerSettings();

        pluginHandler = new PluginHandler(this);

        transactionHandler = new TransactionHandler(this);

        languageHandler = new LanguageHandler(this, getConfig().getString("Language").toLowerCase());
        languageHandler.pushMessages();

        databaseHandler = new DatabaseHandler(this);
        databaseHandler.openConnection();
        databaseHandler.loadTables();

        animationHandler = new AnimationHandler(this);
        animationHandler.loadAnimations();

        cubeletMachineHandler = new CubeletMachineHandler(this);
        cubeletMachineHandler.loadMachines();
        cubeletMachineHandler.setClickType(getConfig().getString("CubeletMachine.ClickType"));

        cubeletTypesHandler = new CubeletTypesHandler(this);
        cubeletTypesHandler.loadTypes();

        cubeletRarityHandler = new CubeletRarityHandler(this);
        cubeletRarityHandler.loadRarities();

        cubeletRewardHandler = new CubeletRewardHandler(this);
        cubeletRewardHandler.loadRewards();

        cubeletTypesHandler.printLog();

        economyHandler = new EconomyHandler();
        economyHandler.load();

        cubeletCraftingHandler = new CubeletCraftingHandler(this);
        cubeletCraftingHandler.loadCrafting();

        playerDataHandler = new PlayerDataHandler(this);

        hologramHandler = new HologramHandler(this);

        if(hologramHandler.getImplementation() == null) {
            getLogger().severe("*** HolographicDisplays or Decent Holograms is not installed or not enabled. ***");
            getLogger().severe("*** Now the plugin will be disabled. ***");
            setEnabled(false);
            return;
        }

        int distance = getConfig().getInt("Holograms.VisibilityDistance");
        hologramHandler.setVisibilityDistance(distance * distance);

        hologramHandler.getColorAnimation().setColors(getConfig().getStringList("Holograms.ColorAnimation"));
        hologramHandler.getImplementation().loadHolograms();

        playerDataHandler.loadAllPlayerData();

        hologramTask = new HologramTask(this);
        hologramTask.start();

        dataSaveTask = new DataSaveTask(this);
        dataSaveTask.start();

        dataCacheTask = new DataCacheTask(this);
        dataCacheTask.start();

        machineEffectsTask = new MachineEffectsTask(this);
        machineEffectsTask.start();

        cubeletOpenHandler = new CubeletOpenHandler(this);

        liveGuiTask = new LiveGuiTask(this);
        if(isSetting("LiveGuiUpdates"))
            liveGuiTask.start();

        layoutHandler = new LayoutHandler(this);

        menuHandler = new MenuHandler(this);

        menuHandler.setClickType(getConfig().getString("Rewards.Preview.ClickType"));

        conversationHandler = new ConversationHandler(this);

        fireworkUtil = new FireworkUtil(this);

        cubeletsAPI = new CubeletsAPI(this);
        pointsAPI = new PointsAPI(this);

        registerCommands();
        registerEvents();

        playerCount = getServer().getOnlinePlayers().size();

        PluginDescriptionFile pdf = getDescription();
        log.sendMessage(Utils.translate("  &eACubelets Enabled!"));
        log.sendMessage(Utils.translate("    &aVersion: &b" + pdf.getVersion()));
        log.sendMessage(Utils.translate("    &aAuthor: &b" + pdf.getAuthors().get(0)));
        log.sendMessage("");

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderHook(this).register();
            settings.put("placeholderapi", true);
        } else {
            settings.put("placeholderapi", false);
        }

    }

    @Override
    public void onDisable() {

        PluginDescriptionFile pdf = getDescription();
        log.sendMessage("");
        log.sendMessage(Utils.translate("  &eACubelets Disabled!"));
        log.sendMessage(Utils.translate("    &aVersion: &b" + pdf.getVersion()));
        log.sendMessage(Utils.translate("    &aAuthor: &b" + pdf.getAuthors().get(0)));
        log.sendMessage("");

        if(hologramHandler != null && hologramHandler.getImplementation() != null) hologramHandler.getImplementation().removeHolograms();

        main.getPlayerDataHandler().saveAllPlayerDataSync();

        for(Animation task : new ArrayList<>(main.getAnimationHandler().getTasks())) {
            task.stop();
        }
        main.getAnimationHandler().getTasks().clear();

        for(Entity entity : main.getAnimationHandler().getEntities()) {
            entity.remove();
        }
        main.getAnimationHandler().getEntities().clear();

        if(hologramTask != null) hologramTask.stop();
        if(dataSaveTask != null) dataSaveTask.stop();
        if(machineEffectsTask != null) machineEffectsTask.stop();
        if(databaseHandler != null) databaseHandler.getDatabaseConnection().stop();

    }

    public void registerSettings() {
        settings.put("Crafting", getConfig().getBoolean("Crafting"));

        settings.put("Rewards.Broadcast", getConfig().getBoolean("Rewards.Broadcast"));

        settings.put("LoginReminder", getConfig().getBoolean("LoginReminder"));

        settings.put("CubeletsCommand", getConfig().getBoolean("NoCubelets.ExecuteCommand"));
        settings.put("NoCubelets.ExecuteCommand", getConfig().getBoolean("NoCubelets.ExecuteCommand"));
        settings.put("NoCubelets.Command", getConfig().getString("NoCubelets.Command"));
        settings.put("NoCubelets.Executor", getConfig().getString("NoCubelets.Executor"));

        settings.put("Rewards.Duplication.Enabled", getConfig().getBoolean("Rewards.Duplication.Enabled"));
        settings.put("Rewards.Duplication.PointsCommand", getConfig().getBoolean("Rewards.Duplication.PointsCommand"));
        settings.put("Rewards.PermissionCommand", getConfig().getString("Rewards.PermissionCommand"));

        settings.put("NoGuiMode", getConfig().getBoolean("NoGuiMode"));

        settings.put("AnimationsByPlayer", getConfig().getBoolean("AnimationsByPlayer"));

        settings.put("SerializeBase64", getConfig().getBoolean("SerializeBase64"));
        settings.put("Rewards.AutoSorting", getConfig().getBoolean("Rewards.AutoSorting"));
        settings.put("UseKeys", getConfig().getBoolean("UseKeys"));

        settings.put("HDVisibleToAllPlayers", getConfig().getBoolean("Holograms.Duplication.VisibleToAllPlayers"));

        settings.put("LiveGuiUpdates", getConfig().getBoolean("LiveGuiUpdates"));

        settings.put("Rewards.Preview.Enabled", getConfig().getBoolean("Rewards.Preview.Enabled"));

        settings.put("GiftCubeletsCommand", getConfig().getBoolean("GiftCubeletsCommand"));
    }

    public static Main get() { return main; }

    public boolean isSetting(String key) {
        return settings.containsKey(key) ? (boolean) settings.get(key) : false;
    }

    public String getSetting(String key) {
        return settings.containsKey(key) ? (String) settings.get(key) : "";
    }

    public ProtocolManager getProtocolManager() { return protocolManager; }

    public MetricsLite getMetrics() {
        return metrics;
    }

    public LanguageHandler getLanguageHandler() {
        return languageHandler;
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public DatabaseHandler getDatabase() { return databaseHandler; }

    public TransactionHandler getTransactionHandler() { return transactionHandler; }

    public PlayerDataHandler getPlayerDataHandler() { return playerDataHandler; }

    public CubeletTypesHandler getCubeletTypesHandler() { return cubeletTypesHandler; }

    public CubeletRarityHandler getCubeletRarityHandler() { return cubeletRarityHandler; }

    public CubeletRewardHandler getCubeletRewardHandler() { return cubeletRewardHandler; }

    public CubeletMachineHandler getCubeletBoxHandler() { return cubeletMachineHandler; }

    public HologramHandler getHologramHandler() { return hologramHandler; }

    public HologramImplementation getHologramImplementation() { return hologramHandler.getImplementation(); }

    public CubeletOpenHandler getCubeletOpenHandler() { return cubeletOpenHandler; }

    public AnimationHandler getAnimationHandler() { return animationHandler; }

    public CubeletCraftingHandler getCubeletCraftingHandler() { return cubeletCraftingHandler; }

    public EconomyHandler getEconomyHandler() { return economyHandler; }

    public LayoutHandler getLayoutHandler() { return layoutHandler; }

    public MenuHandler getMenuHandler() {
        return menuHandler;
    }

    public ConversationHandler getConversationHandler() { return conversationHandler; }

    public PluginHandler getPluginHandler() { return pluginHandler; }

    public HologramTask getHologramTask() { return hologramTask; }

    public LiveGuiTask getLiveGuiTask() { return liveGuiTask; }

    public MachineEffectsTask getMachineEffectsTask() { return machineEffectsTask; }

    public FireworkUtil getFireworkUtil() { return fireworkUtil; }

    public int getPlayerCount() { return playerCount; }

    public void setPlayerCount(int playerCount) { this.playerCount = playerCount; }

    public List<String> getTemplates() { return templates; }

    public boolean playerHasPermission(Player p, String permission) {
        return p.hasPermission(permission) || p.isOp();
    }

    private void registerCommands() {
        Field bukkitCommandMap;
        try {
            bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register("acubelets", new me.davidml16.acubelets.commands.cubelets.CoreCommand(getConfig().getString("Commands.Cubelets").toLowerCase()));
            commandMap.register("acubelets", new me.davidml16.acubelets.commands.points.CoreCommand(getConfig().getString("Commands.Points").toLowerCase()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Event_Interact(this), this);
        Bukkit.getPluginManager().registerEvents(new Event_JoinQuit(this), this);
        Bukkit.getPluginManager().registerEvents(new Event_Damage(), this);
        Bukkit.getPluginManager().registerEvents(new Event_Menus(this), this);
        Bukkit.getPluginManager().registerEvents(new Event_Block(this), this);
    }

}
