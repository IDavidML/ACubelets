package me.davidml16.acubelets.animations;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.normal.animation1.Animation1_Task;
import me.davidml16.acubelets.animations.normal.animation2.Animation2_Task;
import me.davidml16.acubelets.animations.normal.animation3.Animation3_Task;
import me.davidml16.acubelets.animations.normal.animation4.Animation4_Task;
import me.davidml16.acubelets.animations.seasonal.easter.AnimationEaster_Task;
import me.davidml16.acubelets.animations.seasonal.halloween.AnimationHalloween_Task;
import me.davidml16.acubelets.animations.seasonal.summer.AnimationSummer_Task;
import me.davidml16.acubelets.animations.seasonal.xmas.AnimationXmas_Task;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationHandler {

    private final Main main;

    private final Map<String, AnimationInfo> animations;

    private List<Animation> tasks;
    private List<Entity> entities;

    public AnimationHandler(Main main) {
        this.main = main;
        this.animations = new HashMap<>();
        this.tasks = new ArrayList<>();
        this.entities = new ArrayList<>();
    }

    public void loadAnimations() {
        this.animations.put("animation1", new AnimationInfo("animation1", "ANIMATION #1", "https://www.youtube.com/watch?v=m0kxg7MEEKw"));
        this.animations.put("animation2", new AnimationInfo("animation2", "ANIMATION #2", "https://www.youtube.com/watch?v=BkepLkZidVs"));
        this.animations.put("animation3", new AnimationInfo("animation3", "ANIMATION #3", "https://www.youtube.com/watch?v=aNU7vyLEcpk"));
        this.animations.put("animation4", new AnimationInfo("animation4", "ANIMATION #4", "https://www.youtube.com/watch?v=oRraT_K6jkY"));
        this.animations.put("summer", new AnimationInfo("summer", "SUMMER ANIMATION", "https://www.youtube.com/watch?v=Cc0MqN6DtNM"));
        this.animations.put("easter", new AnimationInfo("easter", "EASTER ANIMATION", "https://www.youtube.com/watch?v=l41emwvJuHc"));
        this.animations.put("halloween", new AnimationInfo("halloween", "HALLOWEEN ANIMATION", "https://www.youtube.com/watch?v=CF2gwwGl0E0"));
        this.animations.put("christmas", new AnimationInfo("christmas", "CHRISTMAS ANIMATION", "https://www.youtube.com/watch?v=H079Z3aLdAM"));
    }

    public AnimationInfo getAnimationInfo(String id) {
        return animations.get(id);
    }

    public Animation getAnimation(String animation) {
        if(!this.animations.containsKey(animation))
            return new Animation1_Task(main);

        if(animation.equalsIgnoreCase("animation1"))
            return new Animation1_Task(main);
        else if(animation.equalsIgnoreCase("animation2"))
            return new Animation2_Task(main);
        else if(animation.equalsIgnoreCase("animation3"))
            return new Animation3_Task(main);
        else if(animation.equalsIgnoreCase("animation4"))
            return new Animation4_Task(main);
        else if(animation.equalsIgnoreCase("summer"))
            return new AnimationSummer_Task(main);
        else if(animation.equalsIgnoreCase("easter"))
            return new AnimationEaster_Task(main);
        else if(animation.equalsIgnoreCase("halloween"))
            return new AnimationHalloween_Task(main);
        else if(animation.equalsIgnoreCase("christmas"))
            return new AnimationXmas_Task(main);

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

}
