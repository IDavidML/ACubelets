package me.davidml16.acubelets.animations.normal.animation16;

import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Animation16_Charge extends BukkitRunnable {

    private final Location musicLoc;
    private float notePitch;

    public Animation16_Charge(Location musicLoc) {
        this.musicLoc = musicLoc;
        this.notePitch = 0.85F;
    }

    public void run() {
        Sounds.playSound(musicLoc, Sounds.MySound.FIREWORK_TWINKLE2, 0.5F, this.notePitch);

        this.notePitch += 0.05F;

        if (this.notePitch >= 1.100000023841858D)
            cancel();

    }
}
