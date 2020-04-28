package me.davidml16.acubelets.animations;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.animation1.Animation1_Task;
import me.davidml16.acubelets.animations.animation2.Animation2_Task;
import me.davidml16.acubelets.animations.animation3.Animation3_Task;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.List;

public class AnimationHandler {

    private final Main main;

    private final List<String> animations;

    private List<Animation> tasks;
    private List<ArmorStand> armorStands;

    public AnimationHandler(Main main) {
        this.main = main;
        this.animations = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.armorStands = new ArrayList<>();
    }

    public void loadAnimations() {
        this.animations.add("animation1");
        this.animations.add("animation2");
        this.animations.add("animation3");
    }

    public Animation getAnimation(String animation) {
        if(!this.animations.contains(animation))
            return new Animation1_Task(main);

        if(animation.equalsIgnoreCase("animation1"))
            return new Animation1_Task(main);
        else if(animation.equalsIgnoreCase("animation2"))
            return new Animation2_Task(main);
        else if(animation.equalsIgnoreCase("animation3"))
            return new Animation3_Task(main);

        return null;
    }

    public List<Animation> getTasks() {
        return tasks;
    }

    public void setTasks(List<Animation> tasks) {
        this.tasks = tasks;
    }

    public List<ArmorStand> getArmorStands() {
        return armorStands;
    }

    public void setArmorStands(List<ArmorStand> armorStands) {
        this.armorStands = armorStands;
    }

}
