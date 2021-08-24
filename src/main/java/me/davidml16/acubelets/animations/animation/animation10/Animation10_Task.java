package me.davidml16.acubelets.animations.animation.animation10;

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
import java.util.Objects;

public class Animation10_Task implements Animation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public Animation10_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private ArmorStand armorStand;
	private CubeletBox cubeletBox;
	private CubeletType cubeletType;
	private Animation10_Spiral spiral;
	private List<Color> colors;

	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;

	private Location corner1, corner2, corner3, corner4;

	private Location boxLocation, armorStandLocation;

	private Reward reward;

	private RepeatingTask hologramAnimation;

	class Task implements Runnable {

		int time = 0;
		@Override
		public void run() {

			if(time > 0 && time < 83) {
				Objects.requireNonNull(armorStand).setHeadPose(armorStand.getHeadPose().add(0, 0.26, 0));
			}

			if(time > 0 && time < 20)
				Objects.requireNonNull(armorStandLocation).add(0, 0.35, 0);
			else if(time > 20 && time < 30)
				Objects.requireNonNull(armorStandLocation).add(0, 0.25, 0);
			else if(time > 30 && time < 40)
				Objects.requireNonNull(armorStandLocation).add(0, 0.15, 0);
			else if(time > 40 && time < 50)
				Objects.requireNonNull(armorStandLocation).add(0, 0.05, 0);

			if(time > 0 && time < 70) armorStand.teleport(armorStandLocation);

			if(time == 50) {
				Location eye = armorStand.getEyeLocation().add(0.0D, 0.4D, 0.0D);
				for (Location location2 : LocationUtils.getCircle(armorStand.getLocation().clone().add(0, 0.75,0), 0.25D, 50)) {
					Vector direction = location2.toVector().subtract(armorStand.getLocation().clone().add(0, 0.75,0).toVector()).normalize();
					UtilParticles.display(Particles.CLOUD, direction, eye, 0.3F);
				}
				Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.FIREWORK_BLAST, 0.5f, 0);
			} else if(time == 70) {
				Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.IRONGOLEM_DEATH, 0.5f, 0);
				spiral.cancel();
			} else if(time > 70 && time < 83) {
				Objects.requireNonNull(armorStandLocation).add(0, -0.85, 0);
				armorStand.teleport(armorStandLocation);
				main.getFireworkUtil().spawn(armorStandLocation.clone().add(0, 1.75, 0), FireworkEffect.Type.BURST, colors.get(0), colors.get(1));
			} else if(time == 83) {
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 2, 0.5), FireworkEffect.Type.STAR, colors.get(0), colors.get(1));
				Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.EXPLODE, 0.5f, 0);
			} else if(time == 85) {
				cubeletBox.setLastReward(reward);
				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				main.getHologramImplementation().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;
			} else if(time == 125) {
				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);
			} else if (time > 85 && time < 205) {
				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			} else if(time >= 205) {
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
		boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

		armorStand = ASSpawner.spawn(main, box, type, false, false, boxLocation.clone().add(0, -1.25, 0));
		armorStandLocation = armorStand.getLocation();
		Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.FIREWORK_LAUNCH, 0.5f, 0);

		spiral = new Animation10_Spiral(armorStand);
		spiral.runTaskTimer(main, 0L, 1L);

		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.ORANGE, Color.WHITE);

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
		spiral.cancel();

		main.getAnimationHandler().getTasks().remove(this);

		if(hologramAnimation != null) hologramAnimation.cancel();

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getEntities().remove(armorStand);
		}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}
	
}
