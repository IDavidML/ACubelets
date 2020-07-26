package me.davidml16.acubelets.animations.seasonal.xmas;

import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimationXmas_PlaceSound extends BukkitRunnable {

    private final Location musicLoc;

    public AnimationXmas_PlaceSound(Location musicLoc) {
        this.musicLoc = musicLoc;
    }

    public void run() {
        Sounds.playSound(musicLoc, Sounds.MySound.LAVA_POP, 1F, 0.25F);
    }
}
