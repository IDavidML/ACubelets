package me.davidml16.acubelets.animations.animation.animation8;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Animation8_Task extends Animation {

	public Animation8_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand present;

	private Location pc1, pc2;

	@Override
	public void onTick(int time) {

		if(time >= 65 && time < 120) {

			if(time % 2 == 0) UtilParticles.display(Particles.NOTE,
					getRandomLocation(getCubeletBox().getLocation().clone().add(1, 0, 1), getCubeletBox().getLocation().clone().add(-1, 3, -1)));

		}

		if(time == 120) {

			BukkitRunnable tree = getRunnable("tree");
			if (tree != null)
				((Animation8_Tree) tree).restore();

			cancelRunnable("bell");

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

		setAnimationBlocks(new Animation8_Blocks(getCubeletBox().getLocation()));
		startAnimationBlocks(0L);

		addRunnable("placeSound", new Animation8_PlaceSound(getCubeletBox().getLocation().clone().add(0.5, 0, 0.5)));
		startRunnable("placeSound", 0L, 5L);

		addRunnable("bell", new Animation8_Bell(getCubeletBox().getLocation().clone().add(0.5, 0, 0.5)));
		startRunnable("bell", 65L, 3L);

		addRunnable("tree", new Animation8_Tree(getMain(), (Animation8_PlaceSound) getRunnable("placeSound"), getCubeletBox().getLocation().clone().add(0.5, 0, 0.5)));
		startRunnable("tree", 18L, 5L);

		setColors(Arrays.asList(Color.RED, Color.WHITE));

		pc1 = getCubeletBox().getLocation().clone().add(-3, -1, -3);
		pc2 = getCubeletBox().getLocation().clone().add(3, 4, 3);

	}

	@Override
	public void onStop() {

		cancelRunnables();

		stopAnimationBlocks();

		BukkitRunnable tree = getRunnable("tree");
		if (tree != null)
			((Animation8_Tree) tree).restore();

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

	@Override
	public void onRewardDuplication() {}

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
