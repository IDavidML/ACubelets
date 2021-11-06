package me.davidml16.acubelets.animations.animation.animation2;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;

public class Animation2_Task extends Animation {

	public Animation2_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private Animation2_Music music;

	@Override
	public void onTick(int time) {

		if(armorStand != null) {

			if (time <= 50)
				armorStandLocation.add(0, 0.02, 0);

			armorStand.teleport(armorStandLocation);
			armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.159, 0));

		}

		if(time == 98) {

			doPreRewardReveal();

		} else if(time == 100) {

			doRewardReveal();

		} else if(time == 140) {

			doRewardDuplication();

		} else if (time > 100 && time < 240) {

			doShowBoxParticles();

		} else if(time >= 240) {

			stop();

		}

	}

	@Override
	public void onStart() {

		armorStand = ASSpawner.spawn(getMain(), getCubeletBox(), getCubeletType(), true);
		armorStandLocation = armorStand.getLocation();
		getMain().getAnimationHandler().getEntities().add(armorStand);

		music = new Animation2_Music(getCubeletBox().getLocation());
		music.runTaskTimer(getMain(), 0L, 4L);

	}

	@Override
	public void onStop() {

		music.cancel();

		if(getMain().getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			getMain().getAnimationHandler().getEntities().remove(armorStand);
		}

	}

	@Override
	public void onPreRewardHologram() {

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 1.50, 0.5),
				FireworkEffect.Type.STAR,
				getColors().get(0),
				getColors().get(1)
		);

	}

	@Override
	public void onRewardReveal() {

		music.cancel();

		armorStand.remove();
		armorStand = null;

	}

}
