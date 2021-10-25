package me.davidml16.acubelets.animations.animation.animation6;

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
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.MessageUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.RepeatingTask;
import me.davidml16.acubelets.utils.XSeries.XParticle;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Animation6_Task implements Animation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public Animation6_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private ArmorStand armorStand;
	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private Animation6_Music music;

	private Set<Animation6_Rabbit> rabbits = new HashSet<>();

	private List<Color> colors;
	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;

	private Location boxLocation, armorStandLocation;

	private Location corner1, corner2, corner3, corner4, pc1, pc2;
	private Reward reward;

	private RepeatingTask hologramAnimation;

	class Task implements Runnable {
		int time = 0;
		double rotSpeed = 0.1;
		@Override
		public void run() {

			if(time == 0) {
				Animation6_Rabbit ball = new Animation6_Rabbit(main, boxLocation.clone().add(1, 0, 1), 135);
				ball.runTaskTimer(main, 0L, 1L);
				rabbits.add(ball);

				music.runTaskTimer(main, 0L, 1L);

				armorStand = ASSpawner.spawn(main, cubeletBox, cubeletType, false);
				armorStandLocation = armorStand.getLocation();
				main.getAnimationHandler().getEntities().add(armorStand);
			} else if(time == 10) {
				Animation6_Rabbit rabbit = new Animation6_Rabbit(main, boxLocation.clone().add(-1, 0, 1), -135);
				rabbit.runTaskTimer(main, 0L, 1L);
				rabbits.add(rabbit);
			} else if(time == 20) {
				Animation6_Rabbit rabbit = new Animation6_Rabbit(main, boxLocation.clone().add(-1, 0, -1), -45);
				rabbit.runTaskTimer(main, 0L, 1L);
				rabbits.add(rabbit);
			} else if(time == 30) {
				Animation6_Rabbit rabbit = new Animation6_Rabbit(main, boxLocation.clone().add(1, 0, -1), 45);
				rabbit.runTaskTimer(main, 0L, 1L);
				rabbits.add(rabbit);
			}

			if(time > 0 && time < 100) {
				if(armorStand != null) {
					if (time <= 50) {
						armorStandLocation.add(0, 0.02, 0);
					}
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
				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.STAR, colors.get(0), colors.get(1));
			} else if(time == 100) {
				music.cancel();
				cubeletBox.setLastReward(reward);
				main.getHologramImplementation().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;

				for(Animation6_Rabbit rabbit : rabbits) {
					rabbit.cancel();
					if(main.getAnimationHandler().getEntities().contains(rabbit.getEntity())) {
						Entity entity = rabbit.getEntity();
						if(entity != null) entity.remove();
						main.getAnimationHandler().getEntities().remove(entity);
					}
				}
			} else if(time == 140) {
				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);
			} else if (time > 100 && time < 240) {
				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			} else if(time >= 240) {
				stop();

				main.getCubeletRewardHandler().giveReward(cubeletBox, reward);
				MessageUtils.sendLootMessage(cubeletBox, cubeletType, reward);

				cubeletBox.setState(CubeletBoxState.EMPTY);
				cubeletBox.setPlayerOpening(null);
				main.getHologramImplementation().reloadHologram(cubeletBox);
			}

			time++;
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
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		music = new Animation6_Music(box.getLocation());

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.YELLOW, Color.YELLOW);

		corner1 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner2 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner3 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.95);
		corner4 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.95);
		pc1 = cubeletBox.getLocation().clone().add(-2.5, -1, -2.5);
		pc2 = cubeletBox.getLocation().clone().add(2.5, 3.5, 2.5);
		boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);
		main.getAnimationHandler().getEntities().add(armorStand);

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			reward = main.getCubeletRewardHandler().processReward(cubeletType);
		});
	}
	
	public void stop() {
		try {
			music.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			for(Animation6_Rabbit rabbit : rabbits) {
				rabbit.cancel();
				if(main.getAnimationHandler().getEntities().contains(rabbit.getEntity())) {
					Entity entity = rabbit.getEntity();
					if(entity != null) entity.remove();
					main.getAnimationHandler().getEntities().remove(entity);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(hologramAnimation != null) hologramAnimation.cancel();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getEntities().remove(armorStand);
		}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}
	
}