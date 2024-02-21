package me.davidml16.acubelets.animations.animation.animation9;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Animation9_Task extends Animation {

	public Animation9_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;

	private double boxLocIncrease = -1.25;
	private double circleSize = 0.0D;
	private int circleStep = 0;

	private float rotation;

	@Override
	public void onTick(int time) {

		if (time <= 25) {

			Location newBoxLoc = getBoxLocation().clone();
			this.boxLocIncrease += 0.050D;
			newBoxLoc.add(0.0D, this.boxLocIncrease, 0.0D);
			newBoxLoc.setYaw(rotation);

			UtilParticles.display(Particles.FIREWORKS_SPARK, armorStand.getLocation().clone().add(0, 1.25, 0), 1);

			armorStand.teleport(newBoxLoc);

		} else if (time <= 60) {

			this.boxLocIncrease += 0.040D;

			Location newBoxLoc = getBoxLocation().clone();
			newBoxLoc.add(0.0D, this.boxLocIncrease, 0.0D);

			if (time <= 40)
				this.circleSize += 0.07D;
			else
				this.circleSize -= 0.075D;

			if (this.circleSize < 0.0D)
				this.circleSize = 0.0D;

			List<Location> teleportLocs = LocationUtils.getCircle(newBoxLoc, this.circleSize, 50);
			Location teleportLoc = teleportLocs.get(this.circleStep).clone();

			teleportLoc.setYaw(rotation);

			Sounds.playSound(armorStand.getLocation(), Sounds.MySound.NOTE_PLING, 0.5F, 3F);

			UtilParticles.display(Particles.FIREWORKS_SPARK, armorStand.getLocation().clone().add(0, 1.25, 0), 1);
			armorStand.teleport(teleportLoc);

			this.circleStep++;

			if (this.circleStep == teleportLocs.size())
				this.circleStep = 0;

		}

		if(time == 65)
			chargeParticles();

		if(time == 100)
			cancelRunnable("music");

		if(time == 105) {

			doPreRewardReveal();

		}

	}

	@Override
	public void onStart() {

		rotation = getRotation(false).value;

		armorStand = ASSpawner.spawn(getMain(), getCubeletBox(), getCubeletType(), false, false, getBoxLocation().clone().add(0, -1.25, 0));
		getMain().getAnimationHandler().getEntities().add(armorStand);

		addRunnable("music", new Animation9_Music(getCubeletBox().getLocation()));
		startRunnable("music", 5L, 3L);

		addRunnable("charge", new Animation9_Charge(getCubeletBox().getLocation()));
		startRunnable("charge", 65L, 3L);

		setColors(Arrays.asList(Color.BLACK, Color.ORANGE));

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
				armorStand.getLocation().clone().add(0, 1.75, 0),
				FireworkEffect.Type.BALL,
				getColors().get(0),
				getColors().get(1)
		);

		Sounds.playSound(armorStand.getLocation(), Sounds.MySound.EXPLODE, 0.5F, 1F);

	}

	@Override
	public void onRewardReveal() {

		Sounds.playSound(armorStand.getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

	}

	@Override
	public void onRewardDuplication() {}

	private void chargeParticles() {

		Random random = new Random();
		Location loc = armorStand.getLocation().clone().add(0, 3, 0);

		for (int i = 0; i < 500; i++) {

			Location randomLoc = loc.clone();
			randomLoc.add((random.nextDouble() - 0.5D) / 2.0D, (new Random().nextDouble() - 0.5D) / 2.0D, (random.nextDouble() - 0.5D) / 2.0D);

			Vector vector = randomLoc.toVector().subtract(loc.toVector()).normalize();
			Vector direction = vector.multiply(1.5D + new Random().nextDouble() * 5.0D);

			for(int j = 0; j < 3; j++)
				UtilParticles.display(Particles.ENCHANTMENT_TABLE, direction, loc, 5);

		}

	}
	
}
