package me.davidml16.acubelets.animations.animation.animation13;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;

import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.*;

public class Animation13_Task extends Animation {

	public Animation13_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private Set<Animation13_Gwen> guardians = new HashSet<>();

	private double rotSpeed = 0.001;

	@Override
	public void onTick(int time) {

		if(time == 45) {

			armorStand = ASSpawner.spawn(
					getMain(),
					getCubeletBox().getLocation().clone().add(0.5, -0.3, 0.5),
					SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTExM2VlNjEwODQxZGVkMjE1YWNkMmI0Y2FhZWVmODdkZmQ2ZTNkNDc2OGU3YWI0ZTE5ZWI3NmIzZDgxMjFjZiJ9fX0="),
					false,
					false,
					true
			);

			armorStandLocation = armorStand.getLocation();

			getMain().getAnimationHandler().getEntities().add(armorStand);

		}

		if(time == 47) {

			Animation13_Gwen gwen1 = new Animation13_Gwen(getMain(), armorStand, armorStand.getLocation().clone().add(0, 5, 0), 3, 180, 44);
			guardians.add(gwen1);

		}

		if(time == 49) {

			Animation13_Gwen gwen2 = new Animation13_Gwen(getMain(), armorStand, armorStand.getLocation().clone().add(0, 5, 0), 3, 180, 89);
			guardians.add(gwen2);

		}

		if(time == 51) {

			Animation13_Gwen gwen3 = new Animation13_Gwen(getMain(), armorStand, armorStand.getLocation().clone().add(0, 5, 0), 3, 180, 134);
			guardians.add(gwen3);

		}

		if(time == 53) {

			Animation13_Gwen gwen4 = new Animation13_Gwen(getMain(), armorStand, armorStand.getLocation().clone().add(0, 5, 0), 3, 180, 179);
			guardians.add(gwen4);

		}

		if(time == 55) {

			try {
				for(Animation13_Gwen guardian : guardians) {
					guardian.runTaskTimer(getMain(), 0L, 1L);
				}
			} catch(IllegalStateException | NullPointerException ignored) {}

		}

		if(time > 45 && time < 165) {

			if (armorStand != null) {

				if(time < 94)
					armorStandLocation.add(0, 0.021, 0);

				if(time > 85)
					armorStand.setHeadPose(armorStand.getHeadPose().add(0, rotSpeed, 0));

				armorStand.teleport(armorStandLocation);

			}

			rotSpeed += 0.0030;

		}

		if(time == 163) {

			doPreRewardReveal();

		}

		if(time == 185)
			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

	}

	@Override
	public void onStart() {

		setAnimationBlocks(new Animation13_Blocks(getCubeletBox().getLocation()));
		startAnimationBlocks(0L);

		setColors(Arrays.asList(Color.BLUE, Color.AQUA));

	}

	@Override
	public void onStop() {

		stopAnimationBlocks();

		if(getMain().getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			getMain().getAnimationHandler().getEntities().remove(armorStand);
		}

		try {
			for(Animation13_Gwen guardian : guardians) {
				guardian.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(guardian.getEntity())) {
					Entity entity = guardian.getEntity();
					if(entity != null) entity.remove();
					getMain().getAnimationHandler().getEntities().remove(entity);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

	}

	@Override
	public void onPreRewardReveal() {

		Sounds.playSound(armorStand.getLocation(), Sounds.MySound.EXPLODE, 0.5F, 1F);

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 1.50, 0.5),
				FireworkEffect.Type.BALL_LARGE,
				getColors().get(0),
				getColors().get(1)
		);

	}

	@Override
	public void onRewardReveal() {

		try {
			for(Animation13_Gwen guardian : guardians) {
				guardian.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(guardian.getEntity())) {
					Entity entity = guardian.getEntity();
					if(entity != null) entity.remove();
					getMain().getAnimationHandler().getEntities().remove(entity);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		armorStand.remove();
		armorStand = null;

	}

}
