package me.davidml16.acubelets.animations;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.normal.animation1.Animation1_Task;
import me.davidml16.acubelets.animations.normal.animation2.Animation2_Task;
import me.davidml16.acubelets.animations.normal.animation3.Animation3_Task;
import me.davidml16.acubelets.animations.normal.animation4.Animation4_Task;
import me.davidml16.acubelets.animations.normal.animation9.Animation9_Task;
import me.davidml16.acubelets.animations.seasonal.easter.AnimationEaster_Task;
import me.davidml16.acubelets.animations.seasonal.halloween.AnimationHalloween_Task;
import me.davidml16.acubelets.animations.seasonal.summer.AnimationSummer_Task;
import me.davidml16.acubelets.animations.seasonal.xmas.AnimationXmas_Task;
import me.davidml16.acubelets.utils.ConfigUpdater;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AnimationHandler {

    private final Main main;

    private final Map<String, AnimationSettings> animations;

    private List<Animation> tasks;
    private List<Entity> entities;

    private File file;
    private YamlConfiguration config;

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

        this.animations.put("animation1", new AnimationSettings(
                "animation1",
                getDisplayName("animation_1"),
                XMaterial.FIREWORK_STAR.parseItem(),
                1,
                "https://www.youtube.com/watch?v=m0kxg7MEEKw",
                getAroundParticles("animation_1")
        ));
        this.animations.put("animation2", new AnimationSettings(
                "animation2",
                getDisplayName("animation_2"),
                XMaterial.CRAFTING_TABLE.parseItem(),
                2,
                "https://www.youtube.com/watch?v=BkepLkZidVs",
                getOutlineParticles("animation_2"),
                getFloorParticles("animation_2")
        ));
        this.animations.put("animation3", new AnimationSettings(
                "animation3",
                getDisplayName("animation_3"),
                XMaterial.FIREWORK_ROCKET.parseItem(),
                3,
                "https://www.youtube.com/watch?v=aNU7vyLEcpk",
                getOutlineParticles("animation_3"),
                getFloorParticles("animation_3")
        ));
        this.animations.put("animation4", new AnimationSettings(
                "animation4",
                getDisplayName("animation_4"),
                XMaterial.NETHERRACK.parseItem(),
                4,
                "https://www.youtube.com/watch?v=oRraT_K6jkY",
                getOutlineParticles("animation_4"),
                getFloorParticles("animation_4")
        ));
        this.animations.put("summer", new AnimationSettings(
                "summer",
                getDisplayName("animation_5_summer"),
                XMaterial.SANDSTONE.parseItem(),
                5,
                "https://www.youtube.com/watch?v=Cc0MqN6DtNM",
                getOutlineParticles("animation_5_summer"),
                getFloorParticles("animation_5_summer")
        ));
        this.animations.put("easter", new AnimationSettings(
                "easter",
                getDisplayName("animation_6_easter"),
                XMaterial.LIME_WOOL.parseItem(),
                6,
                "https://www.youtube.com/watch?v=l41emwvJuHc",
                getOutlineParticles("animation_6_easter"),
                getFloorParticles("animation_6_easter")
        ));
        this.animations.put("halloween", new AnimationSettings(
                "halloween",
                getDisplayName("animation_7_halloween"),
                XMaterial.CARVED_PUMPKIN.parseItem(),
                7,
                "https://www.youtube.com/watch?v=CF2gwwGl0E0",
                getOutlineParticles("animation_7_halloween"),
                getFloorParticles("animation_7_halloween")
        ));
        this.animations.put("christmas", new AnimationSettings(
                "christmas",
                getDisplayName("animation_8_christmas"),
                XMaterial.RED_WOOL.parseItem(),
                8,
                "https://www.youtube.com/watch?v=H079Z3aLdAM",
                getOutlineParticles("animation_8_christmas"),
                getFloorParticles("animation_8_christmas")
        ));
        this.animations.put("animation9", new AnimationSettings(
                "animation9",
                getDisplayName("animation_9"),
                XMaterial.LADDER.parseItem(),
                9,
                "https://www.youtube.com/watch?v=pTRbqnQNqz0",
                getOutlineParticles("animation_9"),
                getFloorParticles("animation_9")
        ));
    }

    public AnimationSettings getAnimationSetting(String id) {
        return animations.get(id);
    }

    public Animation getAnimation(String animation) {
        if(!this.animations.containsKey(animation))
            return new Animation1_Task(main, getAnimationSetting("animation1"));

        if(animation.equalsIgnoreCase("animation1"))
            return new Animation1_Task(main, getAnimationSetting(animation));
        else if(animation.equalsIgnoreCase("animation2"))
            return new Animation2_Task(main, getAnimationSetting(animation));
        else if(animation.equalsIgnoreCase("animation3"))
            return new Animation3_Task(main, getAnimationSetting(animation));
        else if(animation.equalsIgnoreCase("animation4"))
            return new Animation4_Task(main, getAnimationSetting(animation));
        else if(animation.equalsIgnoreCase("summer"))
            return new AnimationSummer_Task(main, getAnimationSetting(animation));
        else if(animation.equalsIgnoreCase("easter"))
            return new AnimationEaster_Task(main, getAnimationSetting(animation));
        else if(animation.equalsIgnoreCase("halloween"))
            return new AnimationHalloween_Task(main, getAnimationSetting(animation));
        else if(animation.equalsIgnoreCase("christmas"))
            return new AnimationXmas_Task(main, getAnimationSetting(animation));
        else if(animation.equalsIgnoreCase("animation9"))
            return new Animation9_Task(main, getAnimationSetting(animation));

        return null;
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
