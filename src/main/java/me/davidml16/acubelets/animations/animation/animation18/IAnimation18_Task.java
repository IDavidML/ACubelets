package me.davidml16.acubelets.animations.animation.animation18;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
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
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class IAnimation18_Task implements IAnimation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public IAnimation18_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private ArmorStand armorStand;

	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private List<Color> colors;
	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;
	private Location boxLocation, armorStandLocation;

	private Location corner1, corner2, corner3, corner4;

	private List<Animation18_Item> items = new ArrayList<>();

	private Reward reward;

	private BukkitTask spawnItemsRunnable, removeItemsRunnable;

	private RepeatingTask hologramAnimation;

	class Task implements Runnable {

		int time = 0;
		private double boxLocIncrease = -1.50;
		private double circleSize = 0.0D;
		private int circleStep = 0;
		float rotation = ASSpawner.getRotation(cubeletBox, false).value;

		@Override
		public void run() {

			if (time <= 25) {
				Location newBoxLoc = boxLocation.clone();
				this.boxLocIncrease += 0.060D;
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
				Location teleportLoc = teleportLocs.get(this.circleStep).clone();

				teleportLoc.setYaw(rotation);

				Sounds.playSound(armorStand.getLocation(), Sounds.MySound.NOTE_PLING, 0.5F, 3F);

				UtilParticles.display(Particles.FIREWORKS_SPARK, armorStand.getLocation().clone().add(0, 1.25, 0), 1);
				armorStand.teleport(teleportLoc);

				this.circleStep++;
				if (this.circleStep == teleportLocs.size()) this.circleStep = 0;
			}

			if(time == 150) spawnItemsRunnable.cancel();

			if(time == 283) {
				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				Sounds.playSound(armorStand.getLocation(), Sounds.MySound.EXPLODE, 0.5F, 1F);
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.BALL_LARGE, colors.toArray(new Color[colors.size()]));
			}

			if(time == 285) {
				cubeletBox.setLastReward(reward);
				main.getHologramImplementation().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;

				spawnItemsRunnable.cancel();
				removeItemsRunnable.cancel();

				try {
					for(Animation18_Item item : items) {
						item.cancel();
						if(main.getAnimationHandler().getEntities().contains(item.getArmorStandItem())) {
							Entity entity = item.getArmorStandItem();
							if(entity != null) entity.remove();
							main.getAnimationHandler().getEntities().remove(entity);
						}
						if(main.getAnimationHandler().getEntities().contains(item.getArmorStandName())) {
							Entity entity = item.getArmorStandName();
							if(entity != null) entity.remove();
							main.getAnimationHandler().getEntities().remove(entity);
						}
					}
				} catch(IllegalStateException | NullPointerException ignored) {}
			}

			if(time == 305) Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

			if(time == 325) {
				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);
			}

			if(time > 285 && time < 425) {
				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			}

			if(time == 425) {
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

	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.ORANGE, Color.FUCHSIA, Color.AQUA, Color.WHITE);

		armorStand = ASSpawner.spawn(main, cubeletBox.getLocation().clone().add(0.5, -1.5, 0.5),
				XMaterial.CHEST.parseItem(), false, false, false);
		armorStandLocation = armorStand.getLocation();
		main.getAnimationHandler().getEntities().add(armorStand);

		corner1 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner2 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner3 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.95);
		corner4 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.95);
		boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

		spawnItemsRunnable = Bukkit.getScheduler().runTaskTimer(main, () -> {
			if(items.size() < 9) {
				Animation18_Item item = new Animation18_Item(main, main.getCubeletRewardHandler().processReward(cubeletType),
						armorStand.getLocation().clone().add(0, 0.75, 0));
				item.runTaskTimer(main, 0L, 1L);
				items.add(item);
			}
		}, 65, 8);

		removeItemsRunnable = Bukkit.getScheduler().runTaskTimer(main, () -> {
			if(items.size() > 1) {
				removeRandomItem();
			} else {
				reward = items.get(0).getReward();
			}
		}, 200, 5);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);
	}

	public void stop() {
		if(hologramAnimation != null) hologramAnimation.cancel();
		if(spawnItemsRunnable != null) spawnItemsRunnable.cancel();
		if(removeItemsRunnable != null) removeItemsRunnable.cancel();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		try {
			for(Animation18_Item item : items) {
				item.cancel();
				if(main.getAnimationHandler().getEntities().contains(item.getArmorStandItem())) {
					Entity entity = item.getArmorStandItem();
					if(entity != null) entity.remove();
					main.getAnimationHandler().getEntities().remove(entity);
				}
				if(main.getAnimationHandler().getEntities().contains(item.getArmorStandName())) {
					Entity entity = item.getArmorStandName();
					if(entity != null) entity.remove();
					main.getAnimationHandler().getEntities().remove(entity);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(main.getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getEntities().remove(armorStand);
		}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}

	public void removeRandomItem() {
		Animation18_Item item = items.get((int) (Math.random() * items.size()));
		item.cancel();
		if(main.getAnimationHandler().getEntities().contains(item.getArmorStandItem())) {
			Entity entity = item.getArmorStandItem();
			if(entity != null) entity.remove();
			main.getAnimationHandler().getEntities().remove(entity);
		}
		if(main.getAnimationHandler().getEntities().contains(item.getArmorStandName())) {
			Entity entity = item.getArmorStandName();
			if(entity != null) entity.remove();
			main.getAnimationHandler().getEntities().remove(entity);
		}
		items.remove(item);
	}
	
}
