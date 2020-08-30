package me.davidml16.acubelets.animations.normal.animation9;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.rewards.PermissionReward;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Animation9_Task implements Animation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public Animation9_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private ArmorStand armorStand;
	private CubeletBox cubeletBox;
	private CubeletType cubeletType;
	private Animation9_Music music;
	private Animation9_Charge charge;
	private List<Color> colors;

	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;

	private Location corner1, corner2, corner3, corner4;

	private Location boxLocation;

	private Reward reward;

	private RepeatingTask hologramAnimation;

	class Task implements Runnable {

		int time = 0;
		private double boxLocIncrease = -1.25;
		private double circleSize = 0.0D;
		private int circleStep = 0;
		float rotation = ASSpawner.getRotation(cubeletBox, false).value;

		@Override
		public void run() {
			time++;

			if (time <= 25) {
				Location newBoxLoc = boxLocation.clone();
				this.boxLocIncrease += 0.050D;
				newBoxLoc.add(0.0D, this.boxLocIncrease, 0.0D);
				newBoxLoc.setYaw(rotation);

				UtilParticles.display(Particles.FIREWORKS_SPARK, armorStand.getLocation().clone().add(0, 1.25, 0), 1);

				armorStand.teleport(newBoxLoc);
			} else if (time <= 60) {
				this.boxLocIncrease += 0.040D;

				Location newBoxLoc = boxLocation.clone();
				newBoxLoc.add(0.0D, this.boxLocIncrease, 0.0D);

				if (this.time <= 40) { this.circleSize += 0.07D; }
				else { this.circleSize -= 0.075D; }
				if (this.circleSize < 0.0D) this.circleSize = 0.0D;

				List<Location> teleportLocs = LocationUtils.getCircle(newBoxLoc, this.circleSize, 50);
				Location teleportLoc = ((Location)teleportLocs.get(this.circleStep)).clone();

				teleportLoc.setYaw(rotation);

				Sounds.playSound(armorStand.getLocation(), Sounds.MySound.NOTE_PLING, 0.5F, 3F);

				UtilParticles.display(Particles.FIREWORKS_SPARK, armorStand.getLocation().clone().add(0, 1.25, 0), 1);
				armorStand.teleport(teleportLoc);

				this.circleStep++;
				if (this.circleStep == teleportLocs.size()) this.circleStep = 0;
			}

			if(time == 65) chargeParticles();

			if(time == 100) music.cancel();

			if(time == 105) {
				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				main.getFireworkUtil().spawn(armorStand.getLocation().clone().add(0, 1.75, 0), FireworkEffect.Type.BALL, colors.get(0), colors.get(1));
				Sounds.playSound(armorStand.getLocation(), Sounds.MySound.EXPLODE, 0.5F, 1F);
			} else if(time == 125) {
				cubeletBox.setLastReward(reward);
				main.getHologramHandler().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				Sounds.playSound(armorStand.getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);
			} else if(time == 165) {
				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);
			} else if (time > 125 && time < 245) {
				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			} else if(time >= 245) {
				stop();

				main.getCubeletRewardHandler().giveReward(cubeletBox, reward);
				MessageUtils.sendLootMessage(cubeletBox, cubeletType, reward);

				cubeletBox.setState(CubeletBoxState.EMPTY);
				cubeletBox.setPlayerOpening(null);
				main.getHologramHandler().reloadHologram(cubeletBox);
			}

		}
	}
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		this.cubeletType = type;
		this.cubeletBox = box;
		boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

		armorStand = ASSpawner.spawn(main, box, type, false, false, boxLocation.clone().add(0, -1.25, 0));

		music = new Animation9_Music(box.getLocation());
		music.runTaskTimer(main, 5L, 3L);

		charge = new Animation9_Charge(box.getLocation());
		charge.runTaskTimer(main, 65L, 3L);

		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.BLACK, Color.ORANGE);

		corner1 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner2 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner3 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.95);
		corner4 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.95);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);
		main.getAnimationHandler().getEntities().add(armorStand);

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			reward = main.getCubeletRewardHandler().processReward(cubeletType);
		});
	}
	
	public void stop() {
		music.cancel();
		charge.cancel();

		main.getAnimationHandler().getTasks().remove(this);

		if(hologramAnimation != null) hologramAnimation.canncel();

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getEntities().remove(armorStand);
		}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}

	private void chargeParticles() {
		Random random = new Random();
		Location loc = armorStand.getLocation().clone().add(0, 3, 0);
		for (int i = 0; i < 500; i++) {
			Location randomLoc = loc.clone();
			randomLoc.add((random.nextDouble() - 0.5D) / 2.0D, (new Random().nextDouble() - 0.5D) / 2.0D, (random.nextDouble() - 0.5D) / 2.0D);

			Vector vector = randomLoc.toVector().subtract(loc.toVector()).normalize();
			Vector direction = vector.multiply(1.5D + new Random().nextDouble() * 5.0D);
			loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,  loc, 0, direction.getX(), direction.getY(), direction.getZ(), 5.0D);
		}
	}
	
}
