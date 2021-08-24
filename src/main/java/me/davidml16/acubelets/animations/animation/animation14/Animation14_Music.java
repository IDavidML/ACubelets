package me.davidml16.acubelets.animations.animation.animation14;

import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Animation14_Music extends BukkitRunnable {

    private final Location musicLoc;
    private int noteValue;
    private float notePitch;

    public Animation14_Music(Location musicLoc) {
        this.musicLoc = musicLoc;
    }

    public void run() {

        this.noteValue++;
        if (this.noteValue == 1) {
            Sounds.playSound(this.musicLoc, Sounds.MySound.NOTE_PLING, 0.5F, 1.0F + this.notePitch);
        } else if (this.noteValue == 2) {
            Sounds.playSound(this.musicLoc, Sounds.MySound.NOTE_PLING, 0.5F, 1.0F + this.notePitch);
        } else if (this.noteValue == 3) {
            Sounds.playSound(this.musicLoc, Sounds.MySound.NOTE_PLING, 0.5F, 1.25F + this.notePitch);
        } else if (this.noteValue == 4) {
            Sounds.playSound(this.musicLoc, Sounds.MySound.NOTE_PLING, 0.5F, 1.25F + this.notePitch);
            this.noteValue = 0;
            this.notePitch = (float)(this.notePitch + 0.125D);
        }

    }
}
