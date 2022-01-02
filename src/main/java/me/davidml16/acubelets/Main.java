package me.davidml16.acubelets;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationHandler;
import me.davidml16.acubelets.api.CubeletsAPI;
import me.davidml16.acubelets.api.PointsAPI;
import me.davidml16.acubelets.database.DatabaseHandler;
import me.davidml16.acubelets.events.*;
import me.davidml16.acubelets.handlers.*;
import me.davidml16.acubelets.handlers.PluginHandler;
import me.davidml16.acubelets.holograms.HologramHandler;
import me.davidml16.acubelets.holograms.HologramImplementation;
import me.davidml16.acubelets.tasks.DataSaveTask;
import me.davidml16.acubelets.tasks.HologramTask;
import me.davidml16.acubelets.tasks.LiveGuiTask;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.ConfigUpdater;
import me.davidml16.acubelets.utils.FireworkUtil;
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
    private CubeletCraftingHandler cubeletCraftingHandler;
    private EconomyHandler economyHandler;
    private LayoutHandler layoutHandler;
    private ConversationHandler conversationHandler;
    private TransactionHandler transactionHandler;

    private MenuHandler menuHandler;

    private FireworkUtil fireworkUtil;

    private PluginHandler pluginHandler;

    private int playerCount;

    private Map<String, Boolean> settings;

    private String noCubeletsCommand;
    private String noCubeletsCommandExecutor;

    private String duplicationPermissionCommand;
    private String duplicationPointsCommand;

    private CommandMap commandMap;

    private List<String> templates = Arrays.asList("normal", "super", "uber", "old", "summer", "easter", "halloween", "christmas");

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

        if (!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().severe("*** ProtocolLib is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            setEnabled(false);
            return;
        }

        protocolManager = ProtocolLibrary.getProtocolManager();

        settings.put("Crafting", getConfig().getBoolean("Crafting"));

        settings.put("BroadcastReward", getConfig().getBoolean("BroadcastReward"));

        settings.put("LoginReminder", getConfig().getBoolean("LoginReminder"));

        settings.put("CubeletsCommand", getConfig().getBoolean("NoCubelets.ExecuteCommand"));
        noCubeletsCommand = getConfig().getString("NoCubelets.Command");
        noCubeletsCommandExecutor = getConfig().getString("NoCubelets.Executor");

        pluginHandler = new PluginHandler(this);

        transactionHandler = new TransactionHandler(this);

        languageHandler = new LanguageHandler(this, getConfig().getString("Language").toLowerCase());
        languageHandler.pushMessages();

        databaseHandler = new DatabaseHandler(this);
        databaseHandler.openConnection();
        databaseHandler.loadTables();

        animationHandler = new AnimationHandler(this);
        animationHandler.loadAnimations();

        settings.put("AnimationsByPlayer", getConfig().getBoolean("AnimationsByPlayer"));

        cubeletBoxHandler = new CubeletBoxHandler(this);
        cubeletBoxHandler.loadBoxes();
        settings.put("NoGuiMode", getConfig().getBoolean("NoGuiMode"));

        settings.put("SerializeBase64", getConfig().getBoolean("SerializeBase64"));
        settings.put("RewardAutoSorting", getConfig().getBoolean("RewardAutoSorting"));
        settings.put("UseKeys", getConfig().getBoolean("UseKeys"));
        cubeletTypesHandler = new CubeletTypesHandler(this);
        cubeletTypesHandler.loadTypes();

        cubeletRarityHandler = new CubeletRarityHandler(this);
        cubeletRarityHandler.loadRarities();

        cubeletRewardHandler = new CubeletRewardHandler(this);
        cubeletRewardHandler.loadRewards();

        cubeletTypesHandler.printLog();

        settings.put("RewardsDuplication", getConfig().getBoolean("RewardsDuplication.Enabled"));
        settings.put("duplicationPermissionCommand", getConfig().getBoolean("RewardsDuplication.Enabled"));
        duplicationPermissionCommand = getConfig().getString("RewardsDuplication.PermissionCommand");
        duplicationPointsCommand = getConfig().getString("RewardsDuplication.PointsCommand");

        economyHandler = new EconomyHandler();
        economyHandler.load();

        cubeletCraftingHandler = new CubeletCraftingHandler(this);
        cubeletCraftingHandler.loadCrafting();

        playerDataHandler = new PlayerDataHandler(this);

        hologramHandler = new HologramHandler(this);

        if(hologramHandler.getImplementation() == null) {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            setEnabled(false);
            return;
        }

        hologramHandler.setVisibilityDistance(getConfig().getInt("Holograms.VisibilityDistance"));

        hologramHandler.getColorAnimation().setColors(getConfig().getStringList("Holograms.ColorAnimation"));
        hologramHandler.getImplementation().loadHolograms();

        settings.put("HDVisibleToAllPlayers", getConfig().getBoolean("Holograms.Duplication.VisibleToAllPlayers"));

        playerDataHandler.loadAllPlayerData();

        hologramTask = new HologramTask(this);
        hologramTask.start();

        dataSaveTask = new DataSaveTask(this);
        dataSaveTask.start();

        cubeletOpenHandler = new CubeletOpenHandler(this);

        settings.put("LiveGuiUpdates", getConfig().getBoolean("LiveGuiUpdates"));
        liveGuiTask = new LiveGuiTask(this);
        if(isLiveGuiUpdates())
            liveGuiTask.start();

        layoutHandler = new LayoutHandler(this);

        menuHandler = new MenuHandler(this);

        settings.put("RewardsPreview", getConfig().getBoolean("RewardsPreview.Enabled"));

        menuHandler.setClickType(getConfig().getString("RewardsPreview.ClickType"));

        settings.put("GiftCubeletsCommand", getConfig().getBoolean("GiftCubeletsCommand"));

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

        if(hologramHandler != null) hologramHandler.getImplementation().removeHolograms();

        for (Hologram hologram : HologramsAPI.getHolograms(this)) {
            hologram.delete();
        }

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
        if(databaseHandler != null) databaseHandler.getDatabaseConnection().stop();

    }

    public static Main get() { return main; }

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

    public CubeletBoxHandler getCubeletBoxHandler() { return cubeletBoxHandler; }

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

    public FireworkUtil getFireworkUtil() { return fireworkUtil; }

    public int getPlayerCount() { return playerCount; }

    public void setPlayerCount(int playerCount) { this.playerCount = playerCount; }

    public List<String> getTemplates() { return templates; }

    public boolean playerHasPermission(Player p, String permission) {
        return p.hasPermission(permission) || p.isOp();
    }

    public boolean isCubeletsCommandEnabled() { return settings.get("CubeletsCommand"); }

    public void setCubeletsCommandEnabled(boolean value) { settings.put("CubeletsCommand", value); }

    public String getNoCubeletsCommandExecutor() { return noCubeletsCommandExecutor; }

    public void setNoCubeletsCommandExecutor(String noCubeletsCommandExecutor) { this.noCubeletsCommandExecutor = noCubeletsCommandExecutor; }

    public boolean isCraftingEnabled() { return settings.get("Crafting"); }

    public void setCraftingEnabled(boolean value) { settings.put("Crafting", value); }

    public boolean isPreviewEnabled() { return settings.get("RewardsPreview"); }

    public void setPreviewEnabled(boolean value) { settings.put("RewardsPreview", value); }

    public boolean isDuplicationVisibleAllPlayers() { return settings.get("HDVisibleToAllPlayers"); }

    public void setDuplicationVisibleAllPlayers(boolean value) { settings.put("HDVisibleToAllPlayers", value); }

    public boolean isNoGuiMode() { return settings.get("NoGuiMode"); }

    public void setNoGuiMode(boolean value) { settings.put("NoGuiMode", value); }

    public boolean isRewardSorting() { return settings.get("RewardAutoSorting"); }

    public void setRewardSorting(boolean value) { settings.put("RewardAutoSorting", value); }

    public boolean isLiveGuiUpdates() { return settings.get("LiveGuiUpdates"); }

    public void setLiveGuiUpdates(boolean value) { settings.put("LiveGuiUpdates", value); }

    public boolean isAnimationByPlayer() { return settings.get("AnimationsByPlayer"); }

    public void setAnimationsByPlayer(boolean value) { settings.put("AnimationsByPlayer", value); }

    public boolean isSerializeBase64() { return settings.get("SerializeBase64"); }

    public void setSerializeBase64(boolean value) { settings.put("SerializeBase64", value); }

    public boolean isGiftCubelets() { return settings.get("GiftCubeletsCommand"); }

    public void setGiftCubelets(boolean value) { settings.put("GiftCubeletsCommand", value); }

    public boolean isKeysEnabled() { return settings.get("UseKeys"); }

    public void setKeysEnabled(boolean value) { settings.put("UseKeys", value); }

    public boolean isBroadcastEnabled() { return settings.get("BroadcastReward"); }

    public void setBroadcastEnabled(boolean value) { settings.put("BroadcastReward", value); }

    public void setLoginReminder(boolean value) { settings.put("LoginReminder", value); }

    public boolean isLoginReminder() { return settings.get("LoginReminder"); }

    public String getNoCubeletsCommand() {
        return noCubeletsCommand;
    }

    public void setNoCubeletsCommand(String noCubeletsCommand) {
        this.noCubeletsCommand = noCubeletsCommand;
    }

    public CommandMap getCommandMap() {
        return commandMap;
    }

    public boolean hasPlaceholderAPI() { return settings.get("placeholderapi"); }

    public String getDuplicationPermissionCommand() { return duplicationPermissionCommand; }
    public void setDuplicationPermissionCommand(String duplicationPermissionCommand) { this.duplicationPermissionCommand = duplicationPermissionCommand; }
    public String getDuplicationPointsCommand() { return duplicationPointsCommand; }
    public void setDuplicationPointsCommand(String duplicationPointsCommand) { this.duplicationPointsCommand = duplicationPointsCommand; }
    public void setDuplicationEnabled(boolean duplicationEnabled) { settings.put("RewardsDuplication", duplicationEnabled); }
    public boolean isDuplicationEnabled() { return settings.get("RewardsDuplication"); }

    private void registerCommands() {
        Field bukkitCommandMap = null;
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
