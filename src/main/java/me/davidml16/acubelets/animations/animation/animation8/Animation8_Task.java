package me.davidml16.acubelets.animations.animation.animation8;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.rewards.PermissionReward;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Animation8_Task extends Animation {

	public Animation8_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand present;

	private Animation8_Blocks blocks;
	private Animation8_Tree tree;
	private Animation8_PlaceSound placeSound;
	private Animation8_Bell bell;

	private Location pc1, pc2;

	@Override
	public void onTick(int time) {

		if(time >= 65 && time < 120) {

			if(time % 2 == 0) UtilParticles.display(Particles.NOTE,
					getRandomLocation(getCubeletBox().getLocation().clone().add(1, 0, 1), getCubeletBox().getLocation().clone().add(-1, 3, -1)));

		}

		if(time == 120) {

			if (tree != null)
				tree.restore();

			try {
				bell.cancel();
			} catch (IllegalStateException | NullPointerException ignored) { }

			present = ASSpawner.spawn(getMain(), getCubeletBox().getLocation().clone().add(0.5, 3, 0.5), getCubeletType().getIcon(), false, true);
			getMain().getAnimationHandler().getEntities().add(present);
			getMain().getFireworkUtil().spawn(getCubeletBox().getLocation().clone().add(0.5, 4.25, 0.5), FireworkEffect.Type.BURST, Color.YELLOW, Color.YELLOW);

		}

		if(time == 130) {

			present.setGravity(false);
			present.teleport(getCubeletBox().getLocation().clone().add(0.5, -0.625, 0.5));

		}

		if(time == 153) {

			doPreRewardReveal();

		}

		if(time % 2 == 0) UtilParticles.display(Particles.FIREWORKS_SPARK, getRandomLocation(pc1, pc2));

	}

	@Override
	public void onStart() {

		blocks = new Animation8_Blocks(getCubeletBox().getLocation());
		blocks.runTaskTimer(getMain(), 0L, 5L);

		placeSound = new Animation8_PlaceSound(getCubeletBox().getLocation().clone().add(0.5, 0, 0.5));
		placeSound.runTaskTimer(getMain(), 0L, 5L);

		bell = new Animation8_Bell(getCubeletBox().getLocation().clone().add(0.5, 0, 0.5));
		bell.runTaskTimer(getMain(), 65L, 3L);

		tree = new Animation8_Tree(getMain(), placeSound, getCubeletBox().getLocation().clone().add(0.5, 0, 0.5));
		tree.runTaskTimer(getMain(), 18L, 5L);

		setColors(Arrays.asList(Color.RED, Color.WHITE));

		pc1 = getCubeletBox().getLocation().clone().add(-3, -1, -3);
		pc2 = getCubeletBox().getLocation().clone().add(3, 4, 3);

	}

	@Override
	public void onStop() {

		blocks.cancel();
		tree.cancel();

		try {
			placeSound.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			bell.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(blocks != null) blocks.restore();
		if(tree != null) tree.restore();

		if(getMain().getAnimationHandler().getEntities().contains(present)) {
			if(present != null) present.remove();
			getMain().getAnimationHandler().getEntities().remove(present);
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

		present.remove();
		present = null;

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
