package me.davidml16.acubelets.animations.animation.animation3;

import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Animation3_Music extends BukkitRunnable {

    private final Location musicLoc;
    private int tick = 0;
    private int speed = 6;

    public Animation3_Music(Location musicLoc) {
        this.musicLoc = musicLoc;
    }

    public void run() {

        if(tick > 40)
            speed = 2;
        else if(tick > 35)
            speed = 3;
        else if(tick > 25)
            speed = 4;
        else
            speed = 6;

        if(tick % speed == 0) Sounds.playSound(musicLoc, Sounds.MySound.CLICK, 0.5F, 0);

        tick++;

    }

}
