package me.davidml16.acubelets.animations.seasonal.xmas;

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

public class AnimationXmas_Task implements Animation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public AnimationXmas_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private ArmorStand present;

	private AnimationXmas_Blocks blocks;
	private AnimationXmas_Tree tree;
	private AnimationXmas_PlaceSound placeSound;
	private AnimationXmas_Bell bell;

	private List<Color> colors;
	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;

	private Location boxLocation;

	private Location corner1, corner2, corner3, corner4, pc1, pc2;
	private Reward reward;

	private RepeatingTask hologramAnimation;

	class Task implements Runnable {
		int time = 0;

		int step = 0;
		float stepY = 0;
		float radius = 2.5f;

		@Override
		public void run() {

			if(time >= 65 && time < 120) {
				if(time % 2 == 0) UtilParticles.display(Particles.NOTE,
						getRandomLocation(cubeletBox.getLocation().clone().add(1, 0, 1), cubeletBox.getLocation().clone().add(-1, 3, -1)));
			}

			if(time == 120) {
				if (tree != null) tree.restore();

				try { bell.cancel();
				} catch (IllegalStateException | NullPointerException ignored) { }

				present = ASSpawner.spawn(main, cubeletBox.getLocation().clone().add(0.5, 3, 0.5), cubeletType.getIcon(), false, true);
				main.getAnimationHandler().getEntities().add(present);
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 4.25, 0.5), FireworkEffect.Type.BURST, Color.YELLOW, Color.YELLOW);
			}

			if(time == 130) {
				present.setGravity(false);
				present.teleport(cubeletBox.getLocation().clone().add(0.5, -0.625, 0.5));
			}

			if(time == 153) {
				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.STAR, colors.get(0), colors.get(1));
			}

			if(time == 155) {
				present.remove();
				present = null;
				cubeletBox.setLastReward(reward);
				main.getHologramHandler().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
			}

			if(time == 195) {
				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);
			}

			if (time > 155 && time < 295) {
				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			}

			if(time >= 295) {
				stop();

				main.getCubeletRewardHandler().giveReward(cubeletBox, reward);
				MessageUtils.sendLootMessage(cubeletBox, cubeletType, reward);

				cubeletBox.setState(CubeletBoxState.EMPTY);
				cubeletBox.setPlayerOpening(null);
				main.getHologramHandler().reloadHologram(cubeletBox);
			}

			if(time % 2 == 0) UtilParticles.display(Particles.FIREWORKS_SPARK, getRandomLocation(pc1, pc2));

			time++;
		}
	}
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		blocks = new AnimationXmas_Blocks(box.getLocation());
		blocks.runTaskTimer(main, 0L, 5L);

		placeSound = new AnimationXmas_PlaceSound(box.getLocation().clone().add(0.5, 0, 0.5));
		placeSound.runTaskTimer(main, 0L, 5L);

		bell = new AnimationXmas_Bell(box.getLocation().clone().add(0.5, 0, 0.5));
		bell.runTaskTimer(main, 65L, 3L);

		tree = new AnimationXmas_Tree(main, placeSound, box.getLocation().clone().add(0.5, 0, 0.5));
		tree.runTaskTimer(main, 18L, 5L);

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.RED, Color.WHITE);

		corner1 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner2 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner3 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.95);
		corner4 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.95);
		pc1 = cubeletBox.getLocation().clone().add(-3, -1, -3);
		pc2 = cubeletBox.getLocation().clone().add(3, 4, 3);
		boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			reward = main.getCubeletRewardHandler().processReward(cubeletType);
		});
	}
	
	public void stop() {
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

		if(main.getAnimationHandler().getEntities().contains(present)) {
			if(present != null) present.remove();
			main.getAnimationHandler().getEntities().remove(present);
		}

		if(hologramAnimation != null) hologramAnimation.canncel();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
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
