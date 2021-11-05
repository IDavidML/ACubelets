package me.davidml16.acubelets.animations;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.animation.animation0.Animation0_Task;
import me.davidml16.acubelets.animations.animation.animation1.Animation1_Task;
import me.davidml16.acubelets.animations.animation.animation10.Animation10_Task;
import me.davidml16.acubelets.animations.animation.animation12.Animation12_Task;
import me.davidml16.acubelets.animations.animation.animation13.Animation13_Task;
import me.davidml16.acubelets.animations.animation.animation14.Animation14_Task;
import me.davidml16.acubelets.animations.animation.animation15.Animation15_Task;
import me.davidml16.acubelets.animations.animation.animation16.Animation16_Task;
import me.davidml16.acubelets.animations.animation.animation17.Animation17_Task;
import me.davidml16.acubelets.animations.animation.animation18.Animation18_Task;
import me.davidml16.acubelets.animations.animation.animation19.Animation19_Task;
import me.davidml16.acubelets.animations.animation.animation2.Animation2_Task;
import me.davidml16.acubelets.animations.animation.animation20.Animation20_Task;
import me.davidml16.acubelets.animations.animation.animation3.Animation3_Task;
import me.davidml16.acubelets.animations.animation.animation4.Animation4_Task;
import me.davidml16.acubelets.animations.animation.animation9.Animation9_Task;
import me.davidml16.acubelets.animations.animation.animation6.Animation6_Task;
import me.davidml16.acubelets.animations.animation.animation11.Animation11_Task;
import me.davidml16.acubelets.animations.animation.animation7.Animation7_Task;
import me.davidml16.acubelets.animations.animation.animation5.Animation5_Task;
import me.davidml16.acubelets.animations.animation.animation8.Animation8_Task;
import me.davidml16.acubelets.utils.ConfigUpdater;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
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
    public static int ANIMATION_COUNT;

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
                XMaterial.EMERALD.parseItem()
        );

        ANIMATION_COUNT = animationItems.size();

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

        for(int index = 0; index < ANIMATION_COUNT; index++) {

            AnimationSettings animation = new AnimationSettings("animation" + index);
            animation.setDisplayName(getDisplayName("animation_" + index));
            animation.setDisplayItem(animationItems.get(index));
            animation.setAnimationNumber(index);
            animation.setOutlineParticles(getOutlineParticles("animation_" + index));
            animation.setFloorParticles(getFloorParticles("animation_" + index));
            animation.setAroundParticles(getAroundParticles("animation_" + index));
            animation.setNeedPermission(getNeedPermission("animation_" + index));
            this.animations.put("animation" + index, animation);

        }

        DEFAULT_ANIMATION = getDefaultAnimation();

    }

    public AnimationSettings getAnimationSetting(String id) {
        return animations.containsKey(id) ? animations.get(id) : null;
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

    private String getDefaultAnimation() {
        return this.config.getString("default_animation");
    }

    private String getDisplayName(String animation) {
        return this.config.getString("animations." + animation + ".DisplayName", "Unknown");
    }

    private boolean getOutlineParticles(String animation) {
        return this.config.getBoolean("animations." + animation + ".OutlineParticles", true);
    }

    private boolean getFloorParticles(String animation) {
        return this.config.getBoolean("animations." + animation + ".FloorParticles", true);
    }

    private boolean getAroundParticles(String animation) {
        return this.config.getBoolean("animations." + animation + ".AroundParticles", true);
    }

    private boolean getNeedPermission(String animation) {
        return this.config.getBoolean("animations." + animation + ".NeedPermission", true);
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