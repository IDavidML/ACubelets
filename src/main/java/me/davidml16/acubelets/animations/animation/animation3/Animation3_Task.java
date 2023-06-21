package me.davidml16.acubelets.animations.animation.animation3;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.Objects;

public class Animation3_Task extends Animation {

	public Animation3_Task(Main main, AnimationSettings animationSettings) {
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
			armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.26, 0));

		}

		if(time == 60) {

			cancelRunnable("music");
			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.FIREWORK_LAUNCH, 0.5f, 0);

		}

		if(time > 60 && time < 130) {

			Objects.requireNonNull(armorStand).setHeadPose(armorStand.getHeadPose().add(0, 0.26, 0));
			UtilParticles.display(Particles.FLAME, 0.10F, 0.25F, 0.15F, armorStandLocation, 5);

		}

		if(time > 60 && time < 80)
			Objects.requireNonNull(armorStandLocation).add(0, 0.45, 0);
		else if(time > 80 && time < 90)
			Objects.requireNonNull(armorStandLocation).add(0, 0.35, 0);
		else if(time > 90 && time < 100)
			Objects.requireNonNull(armorStandLocation).add(0, 0.25, 0);
		else if(time > 100 && time < 110)
			Objects.requireNonNull(armorStandLocation).add(0, 0.15, 0);
		else if(time > 110 && time < 120)
			Objects.requireNonNull(armorStandLocation).add(0, 0.05, 0);

		if(time > 60 && time < 120)
			armorStand.teleport(armorStandLocation);

		if(time == 130) {

			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.IRONGOLEM_DEATH, 0.5f, 0);

		} else if(time > 130 && time < 150) {

			Objects.requireNonNull(armorStandLocation).add(0, -0.85, 0);
			armorStand.teleport(armorStandLocation);
			armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.26, 0));
			UtilParticles.display(Particles.SMOKE_LARGE, 0.15F, 0, 0.15F, armorStandLocation.clone().add(0, 0.75, 0), 4);

		}

		if(time == 148) {

			doPreRewardReveal();

		}

	}

	@Override
	public void onStart() {

		armorStand = ASSpawner.spawn(getMain(), getCubeletBox(), getCubeletType(), true);
		armorStandLocation = armorStand.getLocation();
		getMain().getAnimationHandler().getEntities().add(armorStand);

		addRunnable("music", new Animation3_Music(getCubeletBox().getLocation()));
		startRunnable("music", 5L, 1L);

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
	public void onPreRewardReveal() { }

	@Override
	public void onRewardReveal() {

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 2, 0.5),
				FireworkEffect.Type.STAR,
				getColors().get(0),
				getColors().get(1)
		);

		Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.EXPLODE, 0.5f, 0);

		armorStand.remove();
		armorStand = null;

	}

}
