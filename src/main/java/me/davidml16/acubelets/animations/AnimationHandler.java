package me.davidml16.acubelets.animations;

import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.animation.animation2.Animation2_Task;
import me.davidml16.acubelets.utils.ConfigUpdater;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AnimationHandler {

    private final Main main;

    private final Map<String, AnimationSettings> animations;

    private List<Animation> tasks;
    private List<Entity> entities;

    private File file;
    private YamlConfiguration config;

    public static String DEFAULT_ANIMATION = "animation2";
    public static int ANIMATION_COUNT = 22;

    private static List<ItemStack> animationItems;
    static {

        animationItems = Arrays.asList(
                XMaterial.BARRIER.parseItem(), XMaterial.FIREWORK_STAR.parseItem(), XMaterial.CRAFTING_TABLE.parseItem(),
                XMaterial.FIREWORK_ROCKET.parseItem(), XMaterial.NETHERRACK.parseItem(), XMaterial.SANDSTONE.parseItem(),
                XMaterial.LIME_WOOL.parseItem(), XMaterial.PUMPKIN.parseItem(), XMaterial.RED_WOOL.parseItem(),
                XMaterial.LADDER.parseItem(), XMaterial.FIRE_CHARGE.parseItem(), XMaterial.ENDER_EYE.parseItem(),
                XMaterial.CAULDRON.parseItem(), XMaterial.PRISMARINE_SHARD.parseItem(), XMaterial.POPPY.parseItem(),
                XMaterial.BLAZE_POWDER.parseItem(), XMaterial.PINK_WOOL.parseItem(), XMaterial.SNOWBALL.parseItem(),
                XMaterial.DIAMOND.parseItem(), XMaterial.ANVIL.parseItem(), XMaterial.ENDER_PEARL.parseItem(),
                XMaterial.HEART_OF_THE_SEA.parseItem(), XMaterial.PURPUR_BLOCK.parseItem()
        );

    }

    public AnimationHandler(Main main) {

        this.main = main;
        this.animations = new HashMap<>();
        this.tasks = new ArrayList<>();
        this.entities = new ArrayList<>();

    }

    public void loadAnimations() {

        File file = new File(main.getDataFolder(), "animations.yml");
        if(!file.exists()) {
            try { file.createNewFile();
            } catch (IOException e) { e.printStackTrace(); }
        }

        try {
            ConfigUpdater.update(main, "animations.yml", new File(main.getDataFolder(), "animations.yml"), Collections.emptyList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        this.file = file;
        this.config = config;

        this.animations.clear();

        for(int index = 0; index <= ANIMATION_COUNT; index++) {

            AnimationSettings animation = new AnimationSettings("animation" + index);

            String animationId = "animation_" + index;

            animation.setAnimationNumber(index);

            animation.setEnabled(config.getBoolean("animations." + animationId + ".Enabled", true));

            animation.setDisplayName(config.getString("animations." + animationId + ".DisplayName", "Unknown"));

            animation.setOutlineParticles(config.getBoolean("animations." + animationId + ".OutlineParticles", true));

            animation.setFloorParticles(config.getBoolean("animations." + animationId + ".FloorParticles", true));

            animation.setAroundParticles(config.getBoolean("animations." + animation + ".AroundParticles", true));

            animation.setNeedPermission(config.getBoolean("animations." + animation + ".NeedPermission", true));

            if(config.contains("animations." + animationId + ".Icon")
                    && config.getConfigurationSection("animations." + animationId + ".Icon") != null) {

                ItemStack item = XItemStack.deserialize(Utils.getConfigurationSection(config, "animations." + animationId + ".Icon"));

                animation.setDisplayItem(item);

            } else {

                animation.setDisplayItem(animationItems.get(index));

            }

            this.animations.put("animation" + index, animation);

        }

        DEFAULT_ANIMATION = config.getString("default_animation", "animation2");

        saveAnimations();

    }

    public void saveAnimations() {

        config.set("default_animation", DEFAULT_ANIMATION);

        for(AnimationSettings animationSettings : animations.values()) {

            String animationId = "animation_" + animationSettings.getAnimationNumber();

            config.set("animations." + animationId, null);

            config.set("animations." + animationId + ".Enabled", animationSettings.isEnabled());

            config.set("animations." + animationId + ".DisplayName", animationSettings.getDisplayName());

            config.set("animations." + animationId + ".OutlineParticles", animationSettings.isOutlineParticles());

            config.set("animations." + animationId + ".FloorParticles", animationSettings.isFloorParticles());

            config.set("animations." + animationId + ".AroundParticles", animationSettings.isAroundParticles());

            config.set("animations." + animationId + ".NeedPermission", animationSettings.isNeedPermission());

            XItemStack.serialize(animationSettings.getDisplayItem(), Utils.getConfigurationSection(config, "animations." + animationId + ".Icon"));
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ConfigUpdater.update(main, "animations.yml", new File(main.getDataFolder(), "animations.yml"), Collections.emptyList());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public AnimationSettings getAnimationSetting(String id) {
        return animations.containsKey(id) ? animations.get(id) : animations.get(DEFAULT_ANIMATION);
    }

    public Animation getAnimation(String animation) {

        AnimationSettings animationSettings = getAnimationSetting(animation);

        Animation animationObject = null;

        if(animationSettings != null) {

            int index = animationSettings.getAnimationNumber();

            try {

                Class<?> clazz = Class.forName("me.davidml16.acubelets.animations.animation.animation" + index + ".Animation" + index + "_Task");
                Constructor<?> constructor = clazz.getConstructor(Main.class, AnimationSettings.class);

                animationObject = (Animation) constructor.newInstance(main, animationSettings);

            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        return animationObject != null ? animationObject : new Animation2_Task(main, getAnimationSetting("animation2"));

    }

    public List<AnimationSettings> getAnimationsAvailable(Player player) {

        List<AnimationSettings> animations = new ArrayList<>();

        for(AnimationSettings animation : this.getAnimations().values()) {
            if(animation.getAnimationNumber() == 0) continue;

            if(animation.isNeedPermission()) if(!haveAnimationPermission(player, animation)) continue;

            animations.add(animation);
        }

        return animations;

    }

    public List<AnimationSettings> getAnimationSettings() {

        List<AnimationSettings> animationSettings = new ArrayList<>();

        for(AnimationSettings animation : animations.values()) {

            if(!animation.getId().equalsIgnoreCase(DEFAULT_ANIMATION) && !animation.isEnabled())
                continue;

            animationSettings.add(animation);

        }

        return animationSettings;

    }

    public AnimationSettings getRandomAnimation(Player player) {
        List<AnimationSettings> animations = getAnimationsAvailable(player);
        return animations.get(ThreadLocalRandom.current().nextInt(animations.size()) % animations.size());
    }

    public AnimationSettings getRandomAnimation() {
        List<AnimationSettings> animations = new ArrayList<>(getAnimations().values());
        return animations.get(ThreadLocalRandom.current().nextInt(animations.size()) % animations.size());
    }

    public boolean haveAnimationPermission(Player player, AnimationSettings animationSettings) {
        return (animationSettings.getId().equalsIgnoreCase(DEFAULT_ANIMATION) ||
                player.hasPermission("acubelets.animations.*") ||
                player.hasPermission("acubelets.animations.animation" + animationSettings.getAnimationNumber()));
    }

    public List<Animation> getTasks() {
        return tasks;
    }

    public void setTasks(List<Animation> tasks) {
        this.tasks = tasks;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public Map<String, AnimationSettings> getAnimations() { return animations; }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public void setConfig(YamlConfiguration config) {
        this.config = config;
    }

}