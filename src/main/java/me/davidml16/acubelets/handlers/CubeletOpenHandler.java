package me.davidml16.acubelets.handlers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.CubeletOpener;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CubeletOpenHandler {

    private Main main;
    public CubeletOpenHandler(Main main) {
        this.main = main;
    }

    public void openAnimation(Player p, CubeletBox box, CubeletType type) {
        if(box.isWaiting()) {

            CubeletOpener cubeletOpener = new CubeletOpener(p.getUniqueId(), p.getName());

            box.setPlayerOpening(cubeletOpener);

            for (Hologram hologram : box.getHolograms().values()) {
                hologram.clearLines();
            }

            Animation animation;

            if(!main.isAnimationByPlayer()) {
                animation = main.getAnimationHandler().getAnimation(type.getAnimation());
            } else {

                Profile profile = main.getPlayerDataHandler().getData(p);
                if(!profile.getAnimation().equalsIgnoreCase("animation2")) {
                    AnimationSettings animationSettings = main.getAnimationHandler().getAnimationSetting(profile.getAnimation());
                    if(!p.hasPermission("acubelets.animations.animation" + animationSettings.getAnimationNumber()))
                        profile.setAnimation("animation2");
                }

                animation = main.getAnimationHandler().getAnimation(profile.getAnimation());
            }
                
            animation.start(box, type);
            Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletOpener, type));

        } else {
            if(box.getPlayerOpening().getUuid() == p.getUniqueId()) {
                p.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Me"));
            } else {
                p.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Other")
                        .replaceAll("%player%", box.getPlayerOpening().getName()));
            }
        }
    }

}
