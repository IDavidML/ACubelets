package me.davidml16.acubelets.animations.animation.animation19;

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
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class IAnimation19_Task implements IAnimation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public IAnimation19_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private ArmorStand armorStand;
	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private Animation19_Blocks blocks;

	private List<Color> colors;
	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;

	private Location boxLocation, armorStandLocation;

	private Location corner1, corner2, corner3, corner4;
	private Reward reward;

	private List<Animation19_Sword> swords = new ArrayList<>();
	private BukkitTask spawnSwordsRunnable;

	private RepeatingTask hologramAnimation;

	class Task implements Runnable {

		int time = 0;

		@Override
		public void run() {

			if(time > 60 && time < 130) {
				Objects.requireNonNull(armorStand).setHeadPose(armorStand.getHeadPose().add(0, 0.26, 0));
			}

			if(time > 60 && time < 80)
				Objects.requireNonNull(armorStandLocation).add(0, 0.35, 0);
			else if(time > 80 && time < 90)
				Objects.requireNonNull(armorStandLocation).add(0, 0.25, 0);
			else if(time > 90 && time < 100)
				Objects.requireNonNull(armorStandLocation).add(0, 0.15, 0);
			else if(time > 100 && time < 110)
				Objects.requireNonNull(armorStandLocation).add(0, 0.05, 0);

			if(time > 60 && time < 130) armorStand.teleport(armorStandLocation);

			if(time == 130) {

				Sounds.playSound(armorStand.getLocation(), Sounds.MySound.ANVIL_LAND, 0.5F, 0);
				armorStand.setHelmet(XMaterial.ANVIL.parseItem());

			}

			if(time > 130 && time < 143) {

				Objects.requireNonNull(armorStandLocation).add(0, -0.85, 0);
				armorStand.teleport(armorStandLocation);

			}

			if (time == 60) {

				armorStand = ASSpawner.spawn(main, cubeletBox, cubeletType, false, false, boxLocation.clone().add(0, -1.25, 0));
				armorStandLocation = armorStand.getLocation();
				Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.FIREWORK_LAUNCH, 0.5f, 0);

			} else if(time == 143) {

				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.BALL_LARGE, colors.get(0), colors.get(1));

				try {
					for(Animation19_Sword sword : swords) {
						sword.cancel();
						if(main.getAnimationHandler().getEntities().contains(sword.getArmorStand())) {
							ArmorStand swordArmorStand = sword.getArmorStand();
							if(swordArmorStand != null) swordArmorStand.remove();
							main.getAnimationHandler().getEntities().remove(swordArmorStand);
						}
					}
				} catch(IllegalStateException | NullPointerException ignored) {}

			} else if(time == 145) {

				if(spawnSwordsRunnable != null) spawnSwordsRunnable.cancel();

				cubeletBox.setLastReward(reward);
				main.getHologramImplementation().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);

				if(armorStand != null) {
					armorStand.remove();
					armorStand = null;
				}

			} else if(time == 185) {

				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);

			} else if (time > 145 && time < 285) {

				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);

			} else if(time >= 285) {

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

		blocks = new Animation19_Blocks(box.getLocation());
		blocks.runTaskTimer(main, 0L, 6L);

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.WHITE, Color.PURPLE);

		corner1 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner2 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner3 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.95);
		corner4 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.95);
		boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

		spawnSwordsRunnable = Bukkit.getScheduler().runTaskTimer(main, () -> {
			if(swords.size() < 8) {
				Animation19_Sword sword = new Animation19_Sword(main, cubeletBox.getLocation().clone().add(0.5, 3, 0.5), 2, 66, false, 0);
				sword.runTaskTimer(main, 0L, 1L);
				swords.add(sword);
			}
		}, 5, 8);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);
		main.getAnimationHandler().getEntities().add(armorStand);

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			reward = main.getCubeletRewardHandler().processReward(cubeletType);
		});

	}
	
	public void stop() {

		blocks.cancel();

		if(blocks != null) blocks.restore();

		if(spawnSwordsRunnable != null) spawnSwordsRunnable.cancel();
		if(hologramAnimation != null) hologramAnimation.cancel();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getEntities().remove(armorStand);
		}

		try {
			for(Animation19_Sword sword : swords) {
				sword.cancel();
				if(main.getAnimationHandler().getEntities().contains(sword.getArmorStand())) {
					ArmorStand swordArmorStand = sword.getArmorStand();
					if(swordArmorStand != null) swordArmorStand.remove();
					main.getAnimationHandler().getEntities().remove(swordArmorStand);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));

	}
	
}
