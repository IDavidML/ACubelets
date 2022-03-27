package me.davidml16.acubelets.animations.animation.animation10;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;

import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Objects;

public class Animation10_Task extends Animation {

	public Animation10_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	@Override
	public void onTick(int time) {

		if(time > 0 && time < 83)
			Objects.requireNonNull(armorStand).setHeadPose(armorStand.getHeadPose().add(0, 0.26, 0));

		if(time > 0 && time < 20)
			Objects.requireNonNull(armorStandLocation).add(0, 0.35, 0);
		else if(time > 20 && time < 30)
			Objects.requireNonNull(armorStandLocation).add(0, 0.25, 0);
		else if(time > 30 && time < 40)
			Objects.requireNonNull(armorStandLocation).add(0, 0.15, 0);
		else if(time > 40 && time < 50)
			Objects.requireNonNull(armorStandLocation).add(0, 0.05, 0);

		if(time > 0 && time < 70)
			armorStand.teleport(armorStandLocation);

		if(time == 50) {

			Location eye = armorStand.getEyeLocation().add(0.0D, 0.4D, 0.0D);

			for (Location location2 : LocationUtils.getCircle(armorStand.getLocation().clone().add(0, 0.75,0), 0.25D, 50)) {

				Vector direction = location2.toVector().subtract(armorStand.getLocation().clone().add(0, 0.75,0).toVector()).normalize();
				UtilParticles.display(Particles.CLOUD, direction, eye, 0.3F);

			}

			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.FIREWORK_BLAST, 0.5f, 0);

		} else if(time == 70) {

			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.IRONGOLEM_DEATH, 0.5f, 0);
			cancelRunnable("spiral");

		} else if(time > 70 && time < 83) {

			Objects.requireNonNull(armorStandLocation).add(0, -0.85, 0);
			armorStand.teleport(armorStandLocation);

			getMain().getFireworkUtil().spawn(
					armorStandLocation.clone().add(0, 1.75, 0),
					FireworkEffect.Type.BURST,
					getColors().get(0),
					getColors().get(1)
			);

		} else if(time == 83) {

			doPreRewardReveal();

		}

	}

	@Override
	public void onStart() {

		armorStand = ASSpawner.spawn(getMain(), getCubeletBox(), getCubeletType(), false, false, getBoxLocation().clone().add(0, -1.25, 0));
		getMain().getAnimationHandler().getEntities().add(armorStand);

		armorStandLocation = armorStand.getLocation();
		Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.FIREWORK_LAUNCH, 0.5f, 0);

		addRunnable("spiral", new Animation10_Spiral(armorStand));
		startRunnable("spiral", 0L, 1L);

		setColors(Arrays.asList(Color.ORANGE, Color.WHITE));

	}

	@Override
	public void onStop() {

		cancelRunnables();

		if(getMain().getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			getMain().getAnimationHandler().getEntities().remove(armorStand);
		}
		
	}

	@Override
	public void onPreRewardReveal() {

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 2, 0.5),
				FireworkEffect.Type.STAR,
				getColors().get(0),
				getColors().get(1)
		);

		Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.EXPLODE, 0.5f, 0);

	}

	@Override
	public void onRewardReveal() {

		armorStand.remove();
		armorStand = null;

	}

}
