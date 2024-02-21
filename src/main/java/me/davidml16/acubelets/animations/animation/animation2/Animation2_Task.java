package me.davidml16.acubelets.animations.animation.animation2;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class Animation2_Task extends Animation {

	public Animation2_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

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

		}

	}

	@Override
	public void onStart() {

		armorStand = ASSpawner.spawn(getMain(), getCubeletBox(), getCubeletType(), true);
		armorStandLocation = armorStand.getLocation();
		getMain().getAnimationHandler().getEntities().add(armorStand);

		addRunnable("music", new Animation2_Music(getCubeletBox().getLocation()));
		startRunnable("music", 0L, 4L);

	}

	@Override
	public void onStop() {

		cancelRunnable("music");

		if(getMain().getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			getMain().getAnimationHandler().getEntities().remove(armorStand);
		}

	}

	@Override
	public void onPreRewardReveal() {

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 1.50, 0.5),
				FireworkEffect.Type.STAR,
				getColors().get(0),
				getColors().get(1)
		);

	}

	@Override
	public void onRewardReveal() {

		cancelRunnable("music");

		armorStand.remove();
		armorStand = null;

	}

	@Override
	public void onRewardDuplication() {}

}
