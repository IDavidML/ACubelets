package me.davidml16.acubelets.animations.animation4;

import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Animation4_ArchSounds extends BukkitRunnable {

    private final Location musicLoc;

    public Animation4_ArchSounds(Location musicLoc) {
        this.musicLoc = musicLoc;
    }

    public void run() {
        Sounds.playSound(musicLoc, Sounds.MySound.FIREWORK_BLAST, 0.25F, 0);
    }
}
