package me.davidml16.acubelets.animations.animation.animation6;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.XSeries.XParticle;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Animation6_Task extends Animation {

	public Animation6_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private Location pc1, pc2;

	private Animation6_Music music;

	private Set<Animation6_Rabbit> rabbits = new HashSet<>();

	private double rotSpeed = 0.1;

	@Override
	public void onTick(int time) {

		if(time == 0) {

			Animation6_Rabbit ball = new Animation6_Rabbit(getMain(), getBoxLocation().clone().add(1, 0, 1), 135);
			ball.runTaskTimer(getMain(), 0L, 1L);
			rabbits.add(ball);

			music.runTaskTimer(getMain(), 0L, 1L);

			armorStand = ASSpawner.spawn(getMain(), getCubeletBox(), getCubeletType(), false);
			armorStandLocation = armorStand.getLocation();
			getMain().getAnimationHandler().getEntities().add(armorStand);

		} else if(time == 10) {

			Animation6_Rabbit rabbit = new Animation6_Rabbit(getMain(), getBoxLocation().clone().add(-1, 0, 1), -135);
			rabbit.runTaskTimer(getMain(), 0L, 1L);
			rabbits.add(rabbit);

		} else if(time == 20) {

			Animation6_Rabbit rabbit = new Animation6_Rabbit(getMain(), getBoxLocation().clone().add(-1, 0, -1), -45);
			rabbit.runTaskTimer(getMain(), 0L, 1L);
			rabbits.add(rabbit);

		} else if(time == 30) {

			Animation6_Rabbit rabbit = new Animation6_Rabbit(getMain(), getBoxLocation().clone().add(1, 0, -1), 45);
			rabbit.runTaskTimer(getMain(), 0L, 1L);
			rabbits.add(rabbit);

		}

		if(time > 0 && time < 100) {

			if(armorStand != null) {

				if (time <= 50)
					armorStandLocation.add(0, 0.02, 0);

				armorStand.teleport(armorStandLocation);
				armorStand.setHeadPose(armorStand.getHeadPose().add(0, rotSpeed, 0));
				rotSpeed += 0.0030;

				drawCircle(armorStandLocation.clone().add(0, 0.6, 0), 0.3);

			}

			for(int i = 0; i < 3; i++) {
				Color color = XParticle.randomColor();
				UtilParticles.display(color.getRed(), color.getBlue(), color.getGreen(), getRandomLocation(pc1, pc2));
			}

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

		music = new Animation6_Music(getCubeletBox().getLocation());

		setColors(Arrays.asList(Color.YELLOW, Color.YELLOW));

		pc1 = getCubeletBox().getLocation().clone().add(-2.5, -1, -2.5);
		pc2 = getCubeletBox().getLocation().clone().add(2.5, 3.5, 2.5);

	}

	@Override
	public void onStop() {

		try {
			music.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			for(Animation6_Rabbit rabbit : rabbits) {
				rabbit.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(rabbit.getEntity())) {
					Entity entity = rabbit.getEntity();
					if(entity != null) entity.remove();
					getMain().getAnimationHandler().getEntities().remove(entity);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

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

		for(Animation6_Rabbit rabbit : rabbits) {
			rabbit.cancel();
			if(getMain().getAnimationHandler().getEntities().contains(rabbit.getEntity())) {
				Entity entity = rabbit.getEntity();
				if(entity != null) entity.remove();
				getMain().getAnimationHandler().getEntities().remove(entity);
			}
		}

	}

	private void drawCircle(Location loc, double radius) {
		for (int i = 0; i < 10; i++) {
			double angle = 2 * Math.PI * i / 10;
			Location point = loc.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle));
			Particles.REDSTONE.display(new Particles.OrdinaryColor(Color.LIME), point, 128);
		}
	}

	public Location getRandomLocation(Location loc1, Location loc2) {
		double minX = Math.min(loc1.getX(), loc2.getX());
		double minY = Math.min(loc1.getY(), loc2.getY());
		double minZ = Math.min(loc1.getZ(), loc2.getZ());

		double maxX = Math.max(loc1.getX(), loc2.getX());
		double maxY = Math.max(loc1.getY(), loc2.getY());
		double maxZ = Math.max(loc1.getZ(), loc2.getZ());

		return new Location(loc1.getWorld(), randomDouble(minX, maxX), randomDouble(minY, maxY), randomDouble(minZ, maxZ));
	}

	public double randomDouble(double min, double max) {
		return min + ThreadLocalRandom.current().nextDouble(Math.abs(max - min + 1));
	}

}
