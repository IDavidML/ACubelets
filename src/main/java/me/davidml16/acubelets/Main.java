package me.davidml16.acubelets;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationHandler;
import me.davidml16.acubelets.database.DatabaseHandler;
import me.davidml16.acubelets.database.types.Database;
import me.davidml16.acubelets.events.Event_Damage;
import me.davidml16.acubelets.events.Event_Interact;
import me.davidml16.acubelets.events.Event_JoinQuit;
import me.davidml16.acubelets.gui.*;
import me.davidml16.acubelets.handlers.*;
import me.davidml16.acubelets.handlers.PluginHandler;
import me.davidml16.acubelets.tasks.DataSaveTask;
import me.davidml16.acubelets.tasks.HologramTask;
import me.davidml16.acubelets.utils.ColorUtil;
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

    private ProtocolManager protocolManager;

    private HologramTask hologramTask;
    private DataSaveTask dataSaveTask;

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
    private GUIHandler guiHandler;

    private FireworkUtil fireworkUtil;

    private PluginHandler pluginHandler;

    private Cubelets_GUI cubeletsGUI;
    private EditBox_GUI editBoxGUI;
    private TypeConfig_GUI typeConfigGUI;
    private Rewards_GUI rewardsGUI;
    private Rarities_GUI raritiesGUI;
    private Animations_GUI animationsGUI;
    private Crafting_GUI craftingGUI;
    private CraftingConfirmation_GUI craftingConfirmationGUI;
    private RewardsPreview_GUI rewardsPreviewGUI;
    private TypeList_GUI typeListGUI;

    private int playerCount;

    private Map<String, Boolean> settings;

    private String noCubeletsCommand;

    private String duplicationPermissionCommand;
    private String duplicationPointsCommand;

    private CommandMap commandMap;

    private List<String> templates = Arrays.asList("normal", "super", "uber", "old", "summer", "easter");

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

        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays") || !Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().severe("*** HolographicDisplays / ProtocolLib is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            setEnabled(false);
            return;
        }

        protocolManager = ProtocolLibrary.getProtocolManager();

        settings.put("Crafting", getConfig().getBoolean("Crafting"));

        settings.put("CubeletsCommand", getConfig().getBoolean("NoCubelets.ExecuteCommand"));
        noCubeletsCommand = getConfig().getString("NoCubelets.Command");

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

        settings.put("RewardsDuplication", getConfig().getBoolean("RewardsDuplication.Enabled"));
        duplicationPermissionCommand = getConfig().getString("RewardsDuplication.PermissionCommand");
        duplicationPointsCommand = getConfig().getString("RewardsDuplication.PointsCommand");

        economyHandler = new EconomyHandler();
        economyHandler.load();

        cubeletCraftingHandler = new CubeletCraftingHandler(this);
        cubeletCraftingHandler.loadCrafting();

        playerDataHandler = new PlayerDataHandler(this);

        hologramHandler = new HologramHandler(this);
        hologramHandler.loadHolograms();

        playerDataHandler.loadAllPlayerData();

        hologramTask = new HologramTask(this);
        hologramTask.start();

        dataSaveTask = new DataSaveTask(this);
        dataSaveTask.start();

        cubeletOpenHandler = new CubeletOpenHandler(this);

        layoutHandler = new LayoutHandler(this);

        cubeletsGUI = new Cubelets_GUI(this);

        typeConfigGUI = new TypeConfig_GUI(this);
        typeConfigGUI.loadGUI();

        rewardsGUI = new Rewards_GUI(this);
        rewardsGUI.loadGUI();

        raritiesGUI = new Rarities_GUI(this);
        raritiesGUI.loadGUI();

        animationsGUI = new Animations_GUI(this);
        animationsGUI.loadGUI();

        editBoxGUI = new EditBox_GUI(this);

        craftingGUI = new Crafting_GUI(this);
        craftingConfirmationGUI = new CraftingConfirmation_GUI(this);

        settings.put("RewardsPreview", getConfig().getBoolean("RewardsPreview.Enabled"));
        rewardsPreviewGUI = new RewardsPreview_GUI(this);
        cubeletsGUI.setClickType(getConfig().getString("RewardsPreview.ClickType"));

        typeListGUI = new TypeList_GUI(this);

        guiHandler = new GUIHandler(this);

        fireworkUtil = new FireworkUtil(this);

        registerCommands();
        registerEvents();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderHook(this).register();
        }

        playerCount = getServer().getOnlinePlayers().size();

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

        if(hologramHandler != null) hologramHandler.removeHolograms();

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
        if(databaseHandler != null) databaseHandler.getDatabase().close();
    }

    public static Main get() { return main; }

    public ProtocolManager getProtocolManager() { return protocolManager; }

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

    public CubeletCraftingHandler getCubeletCraftingHandler() { return cubeletCraftingHandler; }

    public EconomyHandler getEconomyHandler() { return economyHandler; }

    public LayoutHandler getLayoutHandler() { return layoutHandler; }

    public GUIHandler getGuiHandler() { return guiHandler; }

    public Cubelets_GUI getCubeletsGUI() { return cubeletsGUI; }

    public TypeConfig_GUI getTypeConfigGUI() { return typeConfigGUI; }

    public Rewards_GUI getRewardsGUI() { return rewardsGUI; }

    public Rarities_GUI getRaritiesGUI() { return raritiesGUI; }

    public Animations_GUI getAnimationsGUI() { return animationsGUI; }

    public EditBox_GUI getEditBoxGUI() { return editBoxGUI; }

    public Crafting_GUI getCraftingGUI() { return craftingGUI; }

    public CraftingConfirmation_GUI getCraftingConfirmationGUI() { return craftingConfirmationGUI; }

    public RewardsPreview_GUI getRewardsPreviewGUI() { return rewardsPreviewGUI; }

    public TypeList_GUI getTypeListGUI() { return typeListGUI; }

    public PluginHandler getPluginHandler() { return pluginHandler; }

    public HologramTask getHologramTask() { return hologramTask; }

    public FireworkUtil getFireworkUtil() { return fireworkUtil; }

    public int getPlayerCount() { return playerCount; }

    public void setPlayerCount(int playerCount) { this.playerCount = playerCount; }

    public List<String> getTemplates() { return templates; }

    public boolean playerHasPermission(Player p, String permission) {
        return p.hasPermission(permission) || p.isOp();
    }

    public boolean isCubeletsCommandEnabled() { return settings.get("CubeletsCommand"); }

    public void setCubeletsCommandEnabled(boolean cubeletsCommandEnabled) { settings.put("CubeletsCommand", cubeletsCommandEnabled); }

    public boolean isCraftingEnabled() { return settings.get("Crafting"); }

    public void setCraftingEnabled(boolean craftingEnabled) { settings.put("Crafting", craftingEnabled); }

    public boolean isPreviewEnabled() { return settings.get("RewardsPreview"); }

    public void setPreviewEnabled(boolean rewardsPreview) { settings.put("RewardsPreview", rewardsPreview); }

    public String getNoCubeletsCommand() {
        return noCubeletsCommand;
    }

    public void setNoCubeletsCommand(String noCubeletsCommand) {
        this.noCubeletsCommand = noCubeletsCommand;
    }

    public CommandMap getCommandMap() {
        return commandMap;
    }

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
    }

}
