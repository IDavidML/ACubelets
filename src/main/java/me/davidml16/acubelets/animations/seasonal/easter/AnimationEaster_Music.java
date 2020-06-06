package me.davidml16.acubelets.animations.seasonal.easter;

import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimationEaster_Music extends BukkitRunnable {

    private final Location musicLoc;
    private int noteValue;
    private float notePitch;
    private int speed = 4;
    private int tick = 0;

    public AnimationEaster_Music(Location musicLoc) {
        this.musicLoc = musicLoc;
    }

    public void run() {
        if(tick == 0) {
            this.noteValue++;
            if (this.noteValue >= 1 && this.noteValue <= 3) {
                Sounds.playSound(musicLoc, Sounds.MySound.NOTE_STICKS, 1F, 0.05F);
            } else {
                Sounds.playSound(musicLoc, Sounds.MySound.NOTE_STICKS, 1F, 0.05F + this.notePitch);
                this.notePitch = (float) (this.notePitch + 0.044D);
            }

            if(noteValue == 8) speed = 3;
            if(noteValue == 10) speed = 2;
            if(noteValue == 12) speed = 1;
            this.tick = speed;

        } else tick--;
    }
}
