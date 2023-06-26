package me.davidml16.acubelets.animations.animation.animation21;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.animations.animation.animation14.Animation14_Music;
import me.davidml16.acubelets.animations.animation.animation17.Animation17_Snowball;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Animation21_Task extends Animation {

	public Animation21_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private double rotSpeed = 0.1;

	private Set<Animation21_RotatingParticle> rotatingParticles = new HashSet<>();

	@Override
	public void onTick(int time) {

		if(time == 24) {

			Animation21_RotatingParticle rotatingParticle1 = new Animation21_RotatingParticle(getMain(), getCubeletBox().getLocation().clone().add(0.5, -1.5, 0.5), 0.1, 100, true, 0, false);
			rotatingParticle1.runTaskTimer(getMain(), 0L, 1L);
			rotatingParticles.add(rotatingParticle1);

			Animation21_RotatingParticle rotatingParticle2 = new Animation21_RotatingParticle(getMain(), getCubeletBox().getLocation().clone().add(0.5, -1.5, 0.5), 0.1, 100, true, 50, true);
			rotatingParticle2.runTaskTimer(getMain(), 0L, 1L);
			rotatingParticles.add(rotatingParticle2);

		}

		if(time == 45) {
			armorStand = ASSpawner.spawn(getMain(), getCubeletBox(), SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzRhNWRiOGVhNzk3N2M3Zjk2YTY4ZGZlMWZhNDliNjZlYWZiZWNkMzgxMWE2YjZkM2QxNzdkZGE1Zjk0MjMxMyJ9fX0="), false, false, getCubeletBox().getLocation().clone().add(0.5, -1.25, 0.5));
			armorStandLocation = armorStand.getLocation();
			getMain().getAnimationHandler().getEntities().add(armorStand);

			getCubeletBox().getLocation().getWorld().strikeLightningEffect(getCubeletBox().getLocation().clone().add(0.5, 1, 0.5));
		}

		if(time > 45 && time < 121) {

			if(armorStand != null) {

				if (time <= 100)
					armorStandLocation.add(0, 0.02, 0);

				armorStand.teleport(armorStandLocation);
				armorStand.setHeadPose(armorStand.getHeadPose().add(0, rotSpeed, 0));
				rotSpeed += 0.0030;

			}

		}

		if(time == 120) {

			doPreRewardReveal();

		}

		if(time == 140)
			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

	}

	@Override
	public void onStart() {

		setAnimationBlocks(new Animation21_Blocks(getCubeletBox().getLocation()));
		startAnimationBlocks(6L);

		addRunnable("music", new Animation21_Music(getCubeletBox().getLocation()));
		startRunnable("music", 45L, 2L);

		setColors(Arrays.asList(Color.BLACK, Color.WHITE));

	}

	@Override
	public void onStop() {

		cancelRunnables();

		stopAnimationBlocks();

		try {
			for(Animation21_RotatingParticle rotatingParticle : rotatingParticles) {
				rotatingParticle.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(rotatingParticle.getArmorStand())) {
					ArmorStand armorStand = rotatingParticle.getArmorStand();
					if(armorStand != null) armorStand.remove();
					getMain().getAnimationHandler().getEntities().remove(armorStand);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(getMain().getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			getMain().getAnimationHandler().getEntities().remove(armorStand);
		}

	}

	@Override
	public void onPreRewardReveal() {

		cancelRunnable("music");

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 0.35, 0.5),
				FireworkEffect.Type.BALL_LARGE,
				getColors().get(0),
				getColors().get(1)
		);

	}

	@Override
	public void onRewardReveal() {

		armorStand.remove();
		armorStand = null;

	}

	@Override
	public void doRewardDuplication() {

		for(Animation21_RotatingParticle rotatingParticle : rotatingParticles) {
			rotatingParticle.cancel();
			getMain().getFireworkUtil().spawn(rotatingParticle.getArmorStand().getLocation().add(0, 1, 0), FireworkEffect.Type.BURST, getColors().toArray(new Color[getColors().size()]));
			if(getMain().getAnimationHandler().getEntities().contains(rotatingParticle.getArmorStand())) {
				ArmorStand armorStand = rotatingParticle.getArmorStand();
				if(armorStand != null) armorStand.remove();
				getMain().getAnimationHandler().getEntities().remove(armorStand);
			}
		}
		rotatingParticles.clear();

	}
}
