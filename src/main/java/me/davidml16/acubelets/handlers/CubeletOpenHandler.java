package me.davidml16.acubelets.handlers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.data.CubeletBox;
import me.davidml16.acubelets.data.CubeletType;
import org.bukkit.entity.Player;

public class CubeletOpenHandler {

    private Main main;
    public CubeletOpenHandler(Main main) {
        this.main = main;
    }

    public void openAnimation(Player p, CubeletBox box, CubeletType type) {
        if(!box.isUsing()) {
            box.setUsing(true);
            box.setPlayerOpening(p);

            for (Hologram hologram : box.getHolograms().values()) {
                hologram.clearLines();
            }

            Animation animation = main.getAnimationHandler().getAnimation("animation1");
            animation.start(box, type);
        }
    }

}
