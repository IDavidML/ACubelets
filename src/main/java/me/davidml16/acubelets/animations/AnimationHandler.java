package me.davidml16.acubelets.animations;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.normal.animation0.Animation0_Task;
import me.davidml16.acubelets.animations.normal.animation1.Animation1_Task;
import me.davidml16.acubelets.animations.normal.animation10.Animation10_Task;
import me.davidml16.acubelets.animations.normal.animation12.Animation12_Task;
import me.davidml16.acubelets.animations.normal.animation13.Animation13_Task;
import me.davidml16.acubelets.animations.normal.animation14.Animation14_Task;
import me.davidml16.acubelets.animations.normal.animation15.Animation15_Task;
import me.davidml16.acubelets.animations.normal.animation16.Animation16_Task;
import me.davidml16.acubelets.animations.normal.animation17.Animation17_Task;
import me.davidml16.acubelets.animations.normal.animation18.Animation18_Task;
import me.davidml16.acubelets.animations.normal.animation2.Animation2_Task;
import me.davidml16.acubelets.animations.normal.animation3.Animation3_Task;
import me.davidml16.acubelets.animations.normal.animation4.Animation4_Task;
import me.davidml16.acubelets.animations.normal.animation9.Animation9_Task;
import me.davidml16.acubelets.animations.seasonal.easter.AnimationEaster_Task;
import me.davidml16.acubelets.animations.seasonal.galaxy.AnimationGalaxy_Task;
import me.davidml16.acubelets.animations.seasonal.halloween.AnimationHalloween_Task;
import me.davidml16.acubelets.animations.seasonal.summer.AnimationSummer_Task;
import me.davidml16.acubelets.animations.seasonal.xmas.AnimationXmas_Task;
import me.davidml16.acubelets.utils.ConfigUpdater;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AnimationHandler {

    private final Main main;

    private final Map<String, AnimationSettings> animations;

    private List<Animation> tasks;
    private List<Entity> entities;

    private File file;
    private YamlConfiguration config;

    public static final String DEFAULT_ANIMATION = "animation2";

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

        AnimationSettings animation0 = new AnimationSettings("animation0");
        animation0.setDisplayName(getDisplayName("animation_0"));
        animation0.setDisplayItem(XMaterial.BARRIER.parseItem());
        animation0.setAnimationNumber(0);
        animation0.setPreviewURL("https://www.youtube.com/watch?v=mOMt3lVctKE");
        animation0.setOutlineParticles(getOutlineParticles("animation_0"));
        animation0.setFloorParticles(getFloorParticles("animation_0"));
        animation0.setNeedPermission(false);
        this.animations.put("animation0", animation0);

        AnimationSettings animation1 = new AnimationSettings("animation1");
        animation1.setDisplayName(getDisplayName("animation_1"));
        animation1.setDisplayItem(XMaterial.FIREWORK_STAR.parseItem());
        animation1.setAnimationNumber(1);
        animation1.setPreviewURL("https://www.youtube.com/watch?v=m0kxg7MEEKw");
        animation1.setAroundParticles(getAroundParticles("animation_1"));
        animation1.setNeedPermission(true);
        this.animations.put("animation1", animation1);

        AnimationSettings animation2 = new AnimationSettings("animation2");
        animation2.setDisplayName(getDisplayName("animation_2"));
        animation2.setDisplayItem(XMaterial.CRAFTING_TABLE.parseItem());
        animation2.setAnimationNumber(2);
        animation2.setPreviewURL("https://www.youtube.com/watch?v=BkepLkZidVs");
        animation2.setOutlineParticles(getOutlineParticles("animation_2"));
        animation2.setFloorParticles(getFloorParticles("animation_2"));
        animation2.setNeedPermission(false);
        this.animations.put("animation2", animation2);

        AnimationSettings animation3 = new AnimationSettings("animation3");
        animation3.setDisplayName(getDisplayName("animation_3"));
        animation3.setDisplayItem(XMaterial.FIREWORK_ROCKET.parseItem());
        animation3.setAnimationNumber(3);
        animation3.setPreviewURL("https://www.youtube.com/watch?v=aNU7vyLEcpk");
        animation3.setOutlineParticles(getOutlineParticles("animation_3"));
        animation3.setFloorParticles(getFloorParticles("animation_3"));
        animation3.setNeedPermission(true);
        this.animations.put("animation3", animation3);

        AnimationSettings animation4 = new AnimationSettings("animation4");
        animation4.setDisplayName(getDisplayName("animation_4"));
        animation4.setDisplayItem(XMaterial.NETHERRACK.parseItem());
        animation4.setAnimationNumber(4);
        animation4.setPreviewURL("https://www.youtube.com/watch?v=oRraT_K6jkY");
        animation4.setOutlineParticles(getOutlineParticles("animation_4"));
        animation4.setFloorParticles(getFloorParticles("animation_4"));
        animation4.setNeedPermission(true);
        this.animations.put("animation4", animation4);

        AnimationSettings summer = new AnimationSettings("summer");
        summer.setDisplayName(getDisplayName("animation_5_summer"));
        summer.setDisplayItem(XMaterial.SANDSTONE.parseItem());
        summer.setAnimationNumber(5);
        summer.setPreviewURL("https://www.youtube.com/watch?v=Cc0MqN6DtNM");
        summer.setOutlineParticles(getOutlineParticles("animation_5_summer"));
        summer.setFloorParticles(getFloorParticles("animation_5_summer"));
        summer.setNeedPermission(true);
        this.animations.put("summer", summer);

        AnimationSettings easter = new AnimationSettings("easter");
        easter.setDisplayName(getDisplayName("animation_6_easter"));
        easter.setDisplayItem(XMaterial.LIME_WOOL.parseItem());
        easter.setAnimationNumber(6);
        easter.setPreviewURL("https://www.youtube.com/watch?v=l41emwvJuHc");
        easter.setOutlineParticles(getOutlineParticles("animation_6_easter"));
        easter.setFloorParticles(getFloorParticles("animation_6_easter"));
        easter.setNeedPermission(true);
        this.animations.put("easter", easter);

        AnimationSettings halloween = new AnimationSettings("halloween");
        halloween.setDisplayName(getDisplayName("animation_7_halloween"));
        halloween.setDisplayItem(XMaterial.PUMPKIN.parseItem());
        halloween.setAnimationNumber(7);
        halloween.setPreviewURL("https://www.youtube.com/watch?v=CF2gwwGl0E0");
        halloween.setOutlineParticles(getOutlineParticles("animation_7_halloween"));
        halloween.setFloorParticles(getFloorParticles("animation_7_halloween"));
        halloween.setNeedPermission(true);
        this.animations.put("halloween", halloween);

        AnimationSettings christmas = new AnimationSettings("christmas");
        christmas.setDisplayName(getDisplayName("animation_8_christmas"));
        christmas.setDisplayItem(XMaterial.RED_WOOL.parseItem());
        christmas.setAnimationNumber(8);
        christmas.setPreviewURL("https://www.youtube.com/watch?v=H079Z3aLdAM");
        christmas.setOutlineParticles(getOutlineParticles("animation_8_christmas"));
        christmas.setFloorParticles(getFloorParticles("animation_8_christmas"));
        christmas.setNeedPermission(true);
        this.animations.put("christmas", christmas);

        AnimationSettings animation9 = new AnimationSettings("animation9");
        animation9.setDisplayName(getDisplayName("animation_9"));
        animation9.setDisplayItem(XMaterial.LADDER.parseItem());
        animation9.setAnimationNumber(9);
        animation9.setPreviewURL("https://www.youtube.com/watch?v=pTRbqnQNqz0");
        animation9.setOutlineParticles(getOutlineParticles("animation_9"));
        animation9.setFloorParticles(getFloorParticles("animation_9"));
        animation9.setNeedPermission(true);
        this.animations.put("animation9", animation9);

        AnimationSettings animation10 = new AnimationSettings("animation10");
        animation10.setDisplayName(getDisplayName("animation_10"));
        animation10.setDisplayItem(XMaterial.FIRE_CHARGE.parseItem());
        animation10.setAnimationNumber(10);
        animation10.setPreviewURL("https://www.youtube.com/watch?v=uBwLCUTiisw");
        animation10.setOutlineParticles(getOutlineParticles("animation_10"));
        animation10.setFloorParticles(getFloorParticles("animation_10"));
        animation10.setNeedPermission(true);
        this.animations.put("animation10", animation10);

        AnimationSettings galaxy = new AnimationSettings("galaxy");
        galaxy.setDisplayName(getDisplayName("animation_11_galaxy"));
        galaxy.setDisplayItem(XMaterial.ENDER_EYE.parseItem());
        galaxy.setAnimationNumber(11);
        galaxy.setPreviewURL("https://www.youtube.com/watch?v=Xv0ReDWH3y0");
        galaxy.setOutlineParticles(getOutlineParticles("animation_11_galaxy"));
        galaxy.setFloorParticles(getFloorParticles("animation_11_galaxy"));
        galaxy.setNeedPermission(true);
        this.animations.put("galaxy", galaxy);

        AnimationSettings animation12 = new AnimationSettings("animation12");
        animation12.setDisplayName(getDisplayName("animation_12"));
        animation12.setDisplayItem(XMaterial.CAULDRON.parseItem());
        animation12.setAnimationNumber(12);
        animation12.setPreviewURL("https://www.youtube.com/watch?v=_2_mlpcll84");
        animation12.setOutlineParticles(getOutlineParticles("animation_12"));
        animation12.setFloorParticles(getFloorParticles("animation_12"));
        animation12.setNeedPermission(true);
        this.animations.put("animation12", animation12);

        AnimationSettings animation13 = new AnimationSettings("animation13");
        animation13.setDisplayName(getDisplayName("animation_13"));
        animation13.setDisplayItem(XMaterial.PRISMARINE_SHARD.parseItem());
        animation13.setAnimationNumber(13);
        animation13.setPreviewURL("https://www.youtube.com/watch?v=t6KGQ-Pet6E");
        animation13.setOutlineParticles(getOutlineParticles("animation_13"));
        animation13.setFloorParticles(getFloorParticles("animation_13"));
        animation13.setNeedPermission(true);
        this.animations.put("animation13", animation13);

        AnimationSettings animation14 = new AnimationSettings("animation14");
        animation14.setDisplayName(getDisplayName("animation_14"));
        animation14.setDisplayItem(XMaterial.POPPY.parseItem());
        animation14.setAnimationNumber(14);
        animation14.setPreviewURL("https://www.youtube.com/watch?v=V56LIrVMULY");
        animation14.setOutlineParticles(getOutlineParticles("animation_14"));
        animation14.setFloorParticles(getFloorParticles("animation_14"));
        animation14.setNeedPermission(true);
        this.animations.put("animation14", animation14);

        AnimationSettings animation15 = new AnimationSettings("animation15");
        animation15.setDisplayName(getDisplayName("animation_15"));
        animation15.setDisplayItem(XMaterial.BLAZE_POWDER.parseItem());
        animation15.setAnimationNumber(15);
        animation15.setPreviewURL("https://www.youtube.com/watch?v=lKq_Q-owv-Q");
        animation15.setOutlineParticles(getOutlineParticles("animation_15"));
        animation15.setFloorParticles(getFloorParticles("animation_15"));
        animation15.setNeedPermission(true);
        this.animations.put("animation15", animation15);

        AnimationSettings animation16 = new AnimationSettings("animation16");
        animation16.setDisplayName(getDisplayName("animation_16"));
        animation16.setDisplayItem(XMaterial.PINK_WOOL.parseItem());
        animation16.setAnimationNumber(16);
        animation16.setPreviewURL("https://www.youtube.com/watch?v=iaGyS69gHwY");
        animation16.setOutlineParticles(getOutlineParticles("animation_16"));
        animation16.setFloorParticles(getFloorParticles("animation_16"));
        animation16.setNeedPermission(true);
        this.animations.put("animation16", animation16);

        AnimationSettings animation17 = new AnimationSettings("animation17");
        animation17.setDisplayName(getDisplayName("animation_17"));
        animation17.setDisplayItem(XMaterial.SNOWBALL.parseItem());
        animation17.setAnimationNumber(17);
        animation17.setPreviewURL("https://www.youtube.com/watch?v=KRirzc3U_YM");
        animation17.setOutlineParticles(getOutlineParticles("animation_17"));
        animation17.setFloorParticles(getFloorParticles("animation_17"));
        animation17.setNeedPermission(true);
        this.animations.put("animation17", animation17);

        AnimationSettings animation18 = new AnimationSettings("animation18");
        animation18.setDisplayName(getDisplayName("animation_18"));
        animation18.setDisplayItem(XMaterial.DIAMOND.parseItem());
        animation18.setAnimationNumber(18);
        animation18.setPreviewURL("https://www.youtube.com/watch?v=0sHTDuihqFY");
        animation18.setOutlineParticles(getOutlineParticles("animation_18"));
        animation18.setFloorParticles(getFloorParticles("animation_18"));
        animation18.setNeedPermission(true);
        this.animations.put("animation18", animation18);

    }

    public AnimationSettings getAnimationSetting(String id) {
        return animations.get(id);
    }

    public Animation getAnimation(String animation) {
        switch (animation) {
            case "animation0":
                return new Animation0_Task(main, getAnimationSetting(animation));
            case "animation1":
                return new Animation1_Task(main, getAnimationSetting(animation));
            case "animation3":
                return new Animation3_Task(main, getAnimationSetting(animation));
            case "animation4":
                return new Animation4_Task(main, getAnimationSetting(animation));
            case "summer":
                return new AnimationSummer_Task(main, getAnimationSetting(animation));
            case "easter":
                return new AnimationEaster_Task(main, getAnimationSetting(animation));
            case "halloween":
                return new AnimationHalloween_Task(main, getAnimationSetting(animation));
            case "christmas":
                return new AnimationXmas_Task(main, getAnimationSetting(animation));
            case "animation9":
                return new Animation9_Task(main, getAnimationSetting(animation));
            case "animation10":
                return new Animation10_Task(main, getAnimationSetting(animation));
            case "galaxy":
                return new AnimationGalaxy_Task(main, getAnimationSetting(animation));
            case "animation12":
                return new Animation12_Task(main, getAnimationSetting(animation));
            case "animation13":
                return new Animation13_Task(main, getAnimationSetting(animation));
            case "animation14":
                return new Animation14_Task(main, getAnimationSetting(animation));
            case "animation15":
                return new Animation15_Task(main, getAnimationSetting(animation));
            case "animation16":
                return new Animation16_Task(main, getAnimationSetting(animation));
            case "animation17":
                return new Animation17_Task(main, getAnimationSetting(animation));
            case "animation18":
                return new Animation18_Task(main, getAnimationSetting(animation));
            default:
                return new Animation2_Task(main, getAnimationSetting(animation));
        }
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
        return (player.hasPermission("acubelets.animations.*") || player.hasPermission("acubelets.animations.animation" + animationSettings.getAnimationNumber()));
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
