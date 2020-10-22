package me.davidml16.acubelets.animations.normal.animation14;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.animations.normal.animation9.Animation9_Music;
import me.davidml16.acubelets.animations.seasonal.galaxy.AnimationGalaxy_Planet;
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
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Animation14_Task implements Animation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public Animation14_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private ArmorStand armorStand;

	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private Animation14_Blocks blocks;
	private Animation14_Music music;

	private List<Color> colors;
	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;
	private Location boxLocation, armorStandLocation;

	private Location corner1, corner2, corner3, corner4;

	private Set<Animation14_Bee> bees = new HashSet<>();

	private Reward reward;

	private RepeatingTask hologramAnimation;

	private static final ItemStack BEE = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTlkYzNmMDBlY2FiMjI0OWJiNmExNmM4YzUxMTVjZWI5ZjIzMjA1YTBkNTVjYzBlOWJhYmQyNTYyZjc5NTljNCJ9fX0=");

	class Task implements Runnable {
		int time = 0;
		double rotSpeed = 0.001;
		@Override
		public void run() {

			if(time == 45) {
				armorStand = ASSpawner.spawn(main, cubeletBox.getLocation().clone().add(0.5, -0.3, 0.5),
						SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmI5OTYwZWFkZTJkY2I2ZTc0NGM3ZDA4NGRlZjAwNTk5ZmU3MzI3ZTM5NzNjZDJjYTBhZjRhMmExZTZlYWMwOCJ9fX0="), false, false, true);
				armorStandLocation = armorStand.getLocation();
				main.getAnimationHandler().getEntities().add(armorStand);
			}

			if(time == 6) {
				Animation14_Bee bee = new Animation14_Bee(main, BEE, cubeletBox.getLocation().clone().add(0.5, 3, 0.5),
						3f, 120, true, 0);
				bee.runTaskTimer(main, 0L, 1L);
				bees.add(bee);
			} else if(time == 12) {
				Animation14_Bee bee = new Animation14_Bee(main, BEE, cubeletBox.getLocation().clone().add(0.5, 3, 0.5),
						3f, 120, true, 30);
				bee.runTaskTimer(main, 0L, 1L);
				bees.add(bee);
			} else if(time == 18) {
				Animation14_Bee bee = new Animation14_Bee(main, BEE, cubeletBox.getLocation().clone().add(0.5, 3, 0.5),
						3f, 120, true, 60);
				bee.runTaskTimer(main, 0L, 1L);
				bees.add(bee);
			} else if(time == 24) {
				Animation14_Bee bee = new Animation14_Bee(main, BEE, cubeletBox.getLocation().clone().add(0.5, 3, 0.5),
						3f, 120, true, 90);
				bee.runTaskTimer(main, 0L, 1L);
				bees.add(bee);
			} else if(time == 30) {
				Animation14_Bee bee = new Animation14_Bee(main, BEE, cubeletBox.getLocation().clone().add(0.5, 3, 0.5),
						3f, 120, true, 119);
				bee.runTaskTimer(main, 0L, 1L);
				bees.add(bee);
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

			if(time == 123) {
				music.cancel();
				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				Sounds.playSound(armorStand.getLocation(), Sounds.MySound.EXPLODE, 0.5F, 1F);
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.BALL_LARGE, colors.get(0), colors.get(1));
			}

			if(time == 125) {
				cubeletBox.setLastReward(reward);
				main.getHologramHandler().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;
			}

			if(time == 145) Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

			if(time == 165) {
				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);
			}

			if(time > 125 && time < 265) {
				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			}

			if(time == 265) {
				stop();
				blocks.restore();

				main.getCubeletRewardHandler().giveReward(cubeletBox, reward);
				MessageUtils.sendLootMessage(cubeletBox, cubeletType, reward);

				cubeletBox.setState(CubeletBoxState.EMPTY);
				cubeletBox.setPlayerOpening(null);
				main.getHologramHandler().reloadHologram(cubeletBox);
			}

			time++;
		}
	}
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		blocks = new Animation14_Blocks(box.getLocation());
		blocks.runTaskTimer(main, 0L, 6L);

		music = new Animation14_Music(box.getLocation());
		music.runTaskTimer(main, 45L, 3L);

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.YELLOW, Color.WHITE);

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
		music.cancel();

		if(hologramAnimation != null) hologramAnimation.canncel();
		if(blocks != null) blocks.restore();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		try {
			for(Animation14_Bee bee : bees) {
				bee.cancel();
				if(main.getAnimationHandler().getEntities().contains(bee.getArmorStand())) {
					ArmorStand beeArmorStand = bee.getArmorStand();
					if(beeArmorStand != null) beeArmorStand.remove();
					main.getAnimationHandler().getEntities().remove(beeArmorStand);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(main.getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getEntities().remove(armorStand);
		}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}
	
}
