package me.davidml16.acubelets.animations.normal.animation4;

import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Animation4_Music extends BukkitRunnable {

    private final Location musicLoc;
    private int noteValue;
    private float notePitch;

    public Animation4_Music(Location musicLoc) {
        this.musicLoc = musicLoc;
    }

    public void run() {
        this.noteValue++;
        if (this.noteValue == 1) {
            Sounds.playSound(musicLoc, Sounds.MySound.NOTE_PLING, 0.5F, 0F);
        } else {
            Sounds.playSound(musicLoc, Sounds.MySound.NOTE_PLING, 0.5F, 0F + this.notePitch);
            if(this.noteValue == 2) this.notePitch = (float) (this.notePitch + 0.35D);
            else this.notePitch = (float) (this.notePitch + 0.10D);
        }
    }
}
