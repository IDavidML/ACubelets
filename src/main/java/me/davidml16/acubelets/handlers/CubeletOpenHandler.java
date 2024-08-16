package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationHandler;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.objects.CubeletMachine;
import me.davidml16.acubelets.objects.CubeletOpener;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Profile;
import org.bukkit.entity.Player;

public class CubeletOpenHandler {

    private Main main;
    public CubeletOpenHandler(Main main) {
        this.main = main;
    }

    public void openAnimation(Player p, CubeletMachine box, CubeletType type, boolean openedByKey) {
        if(box.isWaiting()) {

            CubeletOpener cubeletOpener = new CubeletOpener(p.getUniqueId(), p.getName());

            box.setPlayerOpening(cubeletOpener);

            main.getHologramImplementation().clearLines(box);

            Animation animation;

            if(!main.isSetting("AnimationsByPlayer") || openedByKey) {

                if(!type.getAnimation().equalsIgnoreCase("random")) {

                    animation = main.getAnimationHandler().getAnimation(type.getAnimation());

                } else {

                    animation = main.getAnimationHandler().getAnimation(main.getAnimationHandler().getRandomAnimation().getId());

                }

            } else {

                Profile profile = main.getPlayerDataHandler().getData(p);

                if(!profile.getAnimation().equalsIgnoreCase("random")) {

                    AnimationSettings animationSetting = main.getAnimationHandler().getAnimationSetting(profile.getAnimation());
                    if (animationSetting.isNeedPermission()) {
                        if (!main.getAnimationHandler().haveAnimationPermission(p, animationSetting))
                            profile.setAnimation(AnimationHandler.DEFAULT_ANIMATION);
                    }

                    animation = main.getAnimationHandler().getAnimation(profile.getAnimation());

                } else {

                    animation = main.getAnimationHandler().getAnimation(main.getAnimationHandler().getRandomAnimation(p).getId());

                }

            }

            animation.setCubeletBox(box);
            animation.setCubeletType(type);
            animation.start();

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
