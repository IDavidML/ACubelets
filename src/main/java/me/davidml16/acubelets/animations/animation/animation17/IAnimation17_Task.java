package me.davidml16.acubelets.animations.animation.animation17;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.enums.Rotation;
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
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class IAnimation17_Task implements IAnimation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public IAnimation17_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private Animation17_Blocks blocks;
	private Animation17_Music music;

	private List<Color> colors;
	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;

	private Set<Animation17_Snowball> snowballs = new HashSet<>();

	private Location snowmanLocation, boxLocation;

	private Location corner1, corner2, corner3, corner4;

	private LivingEntity snowman;

	private Reward reward;

	private RepeatingTask hologramAnimation;

	class Task implements Runnable {
		int time = 0;
		private double boxLocIncrease = -0.75;
		private double circleSize = 0.0D;
		private int circleStep = 0;
		float rotation = ASSpawner.getRotation(cubeletBox, false).value;
		@Override
		public void run() {

			if(time == 18) {
				snowman = (LivingEntity) cubeletBox.getLocation().getWorld().spawnEntity(getLocationRotation(), EntityType.SNOWMAN);

				if (XMaterial.supports(9)) snowman.setCollidable(false);
				snowman.setRemoveWhenFarAway(false);
				snowman.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));

				NBTEditor.set(snowman, (byte) 1, "Silent");
				NBTEditor.set(snowman, (byte) 1, "Invulnerable");
				NBTEditor.set(snowman, (byte) 1, "NoAI");

				snowmanLocation = snowman.getLocation();
				snowmanLocation.setYaw(cubeletBox.getRotation().value);
				snowmanLocation.setPitch(0);
				snowman.teleport(snowmanLocation);

			}

			if(time == 24) {
				armorStand = ASSpawner.spawn(main, cubeletBox.getLocation().clone().add(0.5, -0.3, 0.5), SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWRmZDc3MjRjNjlhMDI0ZGNmYzYwYjE2ZTAwMzM0YWI1NzM4ZjRhOTJiYWZiOGZiYzc2Y2YxNTMyMmVhMDI5MyJ9fX0="), false, false, true);
				armorStandLocation = armorStand.getLocation();
				main.getAnimationHandler().getEntities().add(armorStand);
			}

			if (time >= 24 && time <= 50) {
				Location newBoxLoc = boxLocation.clone();
				this.boxLocIncrease += 0.030D;
				newBoxLoc.add(0.0D, this.boxLocIncrease, 0.0D);
				newBoxLoc.setYaw(rotation);

				UtilParticles.display(Particles.FIREWORKS_SPARK, armorStand.getLocation().clone().add(0, 0.75, 0), 1);

				armorStand.teleport(newBoxLoc);
			} else if (time >= 24 && time <= 85) {
				this.boxLocIncrease += 0.025D;

				Location newBoxLoc = boxLocation.clone();
				newBoxLoc.add(0.0D, this.boxLocIncrease, 0.0D);

				if (this.time <= 65) { this.circleSize += 0.07D; }
				else { this.circleSize -= 0.075D; }
				if (this.circleSize < 0.0D) this.circleSize = 0.0D;

				List<Location> teleportLocs = LocationUtils.getCircle(newBoxLoc, this.circleSize, 50);
				Location teleportLoc = ((Location)teleportLocs.get(this.circleStep)).clone();

				teleportLoc.setYaw(rotation);

				Sounds.playSound(armorStand.getLocation(), Sounds.MySound.NOTE_PLING, 0.5F, 3F);

				UtilParticles.display(Particles.FIREWORKS_SPARK, armorStand.getLocation().clone().add(0, 0.75, 0), 1);
				armorStand.teleport(teleportLoc);

				this.circleStep++;
				if (this.circleStep == teleportLocs.size()) this.circleStep = 0;
			}

			if(time == 30) {
				Animation17_Snowball snowball1 = new Animation17_Snowball(main, cubeletBox.getLocation().clone().add(0.5, -1.5, 0.5), 3, 100, true, 0);
				snowball1.runTaskTimer(main, 0L, 1L);
				snowballs.add(snowball1);

				Animation17_Snowball snowball2 = new Animation17_Snowball(main, cubeletBox.getLocation().clone().add(0.5, -1.5, 0.5), 3, 100, true, 25);
				snowball2.runTaskTimer(main, 0L, 1L);
				snowballs.add(snowball2);

				Animation17_Snowball snowball3 = new Animation17_Snowball(main, cubeletBox.getLocation().clone().add(0.5, -1.5, 0.5), 3, 100, true, 50);
				snowball3.runTaskTimer(main, 0L, 1L);
				snowballs.add(snowball3);

				Animation17_Snowball snowball4 = new Animation17_Snowball(main, cubeletBox.getLocation().clone().add(0.5, -1.5, 0.5), 3, 100, true, 75);
				snowball4.runTaskTimer(main, 0L, 1L);
				snowballs.add(snowball4);
			}

			if(time == 113) {
				music.cancel();
				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				Sounds.playSound(armorStand.getLocation(), Sounds.MySound.EXPLODE, 0.5F, 1F);
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.BALL_LARGE, colors.get(0), colors.get(1));
			}

			if(time == 115) {
				snowman.remove();
				cubeletBox.setLastReward(reward);
				main.getHologramImplementation().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;
			}

			if(time == 156) removeRandomBall();
			if(time == 159) removeRandomBall();
			if(time == 162) removeRandomBall();
			if(time == 165) removeRandomBall();

			if(time == 135) Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

			if(time == 155) {
				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);
			}

			if(time > 115 && time < 255) {
				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			}

			if(time == 255) {
				stop();
				blocks.restore();

				main.getCubeletRewardHandler().giveReward(cubeletBox, reward);
				MessageUtils.sendLootMessage(cubeletBox, cubeletType, reward);

				cubeletBox.setState(CubeletBoxState.EMPTY);
				cubeletBox.setPlayerOpening(null);
				main.getHologramImplementation().reloadHologram(cubeletBox);
			}

			time++;
		}
	}

	public Location getLocationRotation() {
		switch (cubeletBox.getRotation()) {
			case SOUTH:
				Location s = cubeletBox.getLocation().clone().add(0.5, 0, -0.5);
				s.setYaw(Rotation.SOUTH.value);
				return s;
			case NORTH:
				Location n = cubeletBox.getLocation().clone().add(0.5, 0, 1.5);
				n.setYaw(Rotation.NORTH.value);
				return n;
			case EAST:
				Location e = cubeletBox.getLocation().clone().add(-0.5, 0, 0.5);
				e.setYaw(Rotation.EAST.value);
				return e;
			case WEST:
				Location w = cubeletBox.getLocation().clone().add(1.5, 0, 0.5);
				w.setYaw(Rotation.WEST.value);
				return w;
		}
		return null;
	}
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		blocks = new Animation17_Blocks(box.getLocation());
		blocks.runTaskTimer(main, 0L, 6L);

		music = new Animation17_Music(box.getLocation());
		music.runTaskTimer(main, 25L, 3L);

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.WHITE, Color.AQUA);

		corner1 = cubeletBox.getLocation().clone().add(0.05, 1 - 0.325, 0.05);
		corner2 = cubeletBox.getLocation().clone().add(0.95, 1 - 0.325, 0.05);
		corner3 = cubeletBox.getLocation().clone().add(0.95, 1 - 0.325, 0.95);
		corner4 = cubeletBox.getLocation().clone().add(0.05, 1 - 0.325, 0.95);
		boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			reward = main.getCubeletRewardHandler().processReward(cubeletType);
		});
	}
	
	public void stop() {
		blocks.cancel();
		music.cancel();

		if(snowman != null) snowman.remove();

		if(hologramAnimation != null) hologramAnimation.cancel();
		if(blocks != null) blocks.restore();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getEntities().remove(armorStand);
		}

		try {
			for(Animation17_Snowball snowball : snowballs) {
				snowball.cancel();
				if(main.getAnimationHandler().getEntities().contains(snowball.getArmorStand())) {
					ArmorStand snowballArmorStand = snowball.getArmorStand();
					if(snowballArmorStand != null) snowballArmorStand.remove();
					main.getAnimationHandler().getEntities().remove(snowballArmorStand);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}

	public void removeRandomBall() {
		for(Animation17_Snowball snowball : snowballs) {
			snowball.cancel();
			if(main.getAnimationHandler().getEntities().contains(snowball.getArmorStand())) {
				Entity entity = snowball.getArmorStand();
				if(entity != null) {
					main.getFireworkUtil().spawn(entity.getLocation(), FireworkEffect.Type.BURST, colors.toArray(new Color[colors.size()]));
					entity.remove();
				}
				main.getAnimationHandler().getEntities().remove(entity);
			}
			snowballs.remove(snowball);
			break;
		}
	}
	
}
