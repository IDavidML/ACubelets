package me.davidml16.acubelets.animations.seasonal.xmas;

import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimationXmas_Bell extends BukkitRunnable {

    private final Location musicLoc;
    private int note;

    public AnimationXmas_Bell(Location musicLoc) {
        this.musicLoc = musicLoc;
        this.note = 0;
    }

    public void run() {
        note++;
        if (note == 1) {
            Sounds.playSound(musicLoc, Sounds.MySound.ORB_PICKUP, 0.25F, 1.681793F);
        } else if (note == 2) {
            Sounds.playSound(musicLoc, Sounds.MySound.ORB_PICKUP, 0.25F, 1.587401F);
        } else if (note == 3) {
            Sounds.playSound(musicLoc, Sounds.MySound.ORB_PICKUP, 0.25F, 1.498307F);
            note = 0;
        }
    }
}
