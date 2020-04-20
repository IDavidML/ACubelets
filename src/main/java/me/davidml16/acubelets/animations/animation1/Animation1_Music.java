package me.davidml16.acubelets.animations.animation1;

import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Animation1_Music extends BukkitRunnable {

    private final Location musicLoc;
    private int noteValue;
    private float notePitch;

    public Animation1_Music(Location musicLoc) {
        this.musicLoc = musicLoc;
    }

    public void run() {
        this.noteValue++;
        if (this.noteValue >= 1 && this.noteValue <= 3) {
            Sounds.playSound(musicLoc, Sounds.MySound.NOTE_PLING, 0.5F, 0.05F);
        } else {
            Sounds.playSound(musicLoc, Sounds.MySound.NOTE_PLING, 0.5F, 0.05F + this.notePitch);
                this.notePitch = (float) (this.notePitch + 0.044D);
        }
    }
}
