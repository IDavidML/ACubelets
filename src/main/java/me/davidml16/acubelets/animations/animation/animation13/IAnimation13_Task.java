package me.davidml16.acubelets.animations.animation.animation13;

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
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.*;

public class IAnimation13_Task implements IAnimation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public IAnimation13_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private ArmorStand armorStand;

	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private Animation13_Blocks blocks;

	private List<Color> colors;
	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;

	private Set<Animation13_Gwen> guardians = new HashSet<>();

	private Location boxLocation, armorStandLocation;

	private Location corner1, corner2, corner3, corner4;

	private Reward reward;

	private RepeatingTask hologramAnimation;

	class Task implements Runnable {
		int time = 0;
		double rotSpeed = 0.001;
		@Override
		public void run() {

			if(time == 45) {
				armorStand = ASSpawner.spawn(main, cubeletBox.getLocation().clone().add(0.5, -0.3, 0.5),
						SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTExM2VlNjEwODQxZGVkMjE1YWNkMmI0Y2FhZWVmODdkZmQ2ZTNkNDc2OGU3YWI0ZTE5ZWI3NmIzZDgxMjFjZiJ9fX0="), false, false, true);
				armorStandLocation = armorStand.getLocation();
				main.getAnimationHandler().getEntities().add(armorStand);
			}

			if(time == 47) {
				Animation13_Gwen gwen1 = new Animation13_Gwen(main, armorStand, armorStand.getLocation().clone().add(0, 5, 0), 3, 180, 44);
				guardians.add(gwen1);
			}

			if(time == 49) {
				Animation13_Gwen gwen2 = new Animation13_Gwen(main, armorStand, armorStand.getLocation().clone().add(0, 5, 0), 3, 180, 89);
				guardians.add(gwen2);
			}

			if(time == 51) {
				Animation13_Gwen gwen3 = new Animation13_Gwen(main, armorStand, armorStand.getLocation().clone().add(0, 5, 0), 3, 180, 134);
				guardians.add(gwen3);
			}

			if(time == 53) {
				Animation13_Gwen gwen4 = new Animation13_Gwen(main, armorStand, armorStand.getLocation().clone().add(0, 5, 0), 3, 180, 179);
				guardians.add(gwen4);
			}

			if(time == 55) {
				try {
					for(Animation13_Gwen guardian : guardians) {
						guardian.runTaskTimer(main, 0L, 1L);
					}
				} catch(IllegalStateException | NullPointerException ignored) {}
			}

			if(time > 45 && time < 165) {
				if (armorStand != null) {
					if(time < 94)
						armorStandLocation.add(0, 0.021, 0);
					if(time > 85)
						armorStand.setHeadPose(armorStand.getHeadPose().add(0, rotSpeed, 0));

					armorStand.teleport(armorStandLocation);
				}
				rotSpeed += 0.0030;
			}

			if(time == 163) {
				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				Sounds.playSound(armorStand.getLocation(), Sounds.MySound.EXPLODE, 0.5F, 1F);
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.BALL_LARGE, colors.get(0), colors.get(1));
			}

			if(time == 165) {
				try {
					for(Animation13_Gwen guardian : guardians) {
						guardian.cancel();
						if(main.getAnimationHandler().getEntities().contains(guardian.getEntity())) {
							Entity entity = guardian.getEntity();
							if(entity != null) entity.remove();
							main.getAnimationHandler().getEntities().remove(entity);
						}
					}
				} catch(IllegalStateException | NullPointerException ignored) {}

				cubeletBox.setLastReward(reward);
				main.getHologramImplementation().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;
			}

			if(time == 185) Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

			if(time == 205) {
				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);
			}

			if(time > 165 && time < 305) {
				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			}

			if(time == 305) {
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
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		blocks = new Animation13_Blocks(box.getLocation());
		blocks.runTaskTimer(main, 0L, 6L);

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.BLUE, Color.AQUA);

		corner1 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner2 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner3 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.95);
		corner4 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.95);
		boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			reward = main.getCubeletRewardHandler().processReward(cubeletType);
		});
	}
	
	public void stop() {
		blocks.cancel();

		if(hologramAnimation != null) hologramAnimation.cancel();
		if(blocks != null) blocks.restore();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getEntities().remove(armorStand);
		}

		try {
			for(Animation13_Gwen guardian : guardians) {
				guardian.cancel();
				if(main.getAnimationHandler().getEntities().contains(guardian.getEntity())) {
					Entity entity = guardian.getEntity();
					if(entity != null) entity.remove();
					main.getAnimationHandler().getEntities().remove(entity);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}
	
}
