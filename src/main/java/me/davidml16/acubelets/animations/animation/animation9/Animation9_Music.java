package me.davidml16.acubelets.animations.animation.animation9;

import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Animation9_Music extends BukkitRunnable {

    private final Location musicLoc;
    private int noteValue;
    private float notePitch;

    public Animation9_Music(Location musicLoc) {
        this.musicLoc = musicLoc;
    }

    public void run() {

        this.noteValue++;
        if (this.noteValue == 1) {
            Sounds.playSound(this.musicLoc, Sounds.MySound.NOTE_BASS, 0.5F, 0.5F + this.notePitch);
        } else if (this.noteValue == 2) {
            Sounds.playSound(this.musicLoc, Sounds.MySound.NOTE_BASS, 0.5F, 0.5F + this.notePitch);
        } else if (this.noteValue == 3) {
            Sounds.playSound(this.musicLoc, Sounds.MySound.NOTE_BASS, 0.5F, 0.75F + this.notePitch);
        } else if (this.noteValue == 4) {
            Sounds.playSound(this.musicLoc, Sounds.MySound.NOTE_BASS, 0.5F, 0.75F + this.notePitch);
            this.noteValue = 0;
            this.notePitch = (float)(this.notePitch + 0.125D);
        }

    }
}
