package me.davidml16.acubelets.animations.animation5;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.animation2.Animation2_Music;
import me.davidml16.acubelets.animations.animation4.Animation4_Blocks;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.enums.Rotation;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Reward;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.MessageUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.XSeries.ParticleDisplay;
import me.davidml16.acubelets.utils.XSeries.XParticle;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Animation5_Task implements Animation {

	private int id;

	private Main main;
	public Animation5_Task(Main main) {
		this.main = main;
	}

	private ArmorStand armorStand;
	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private Animation5_Music music;
	private Animation5_Blocks blocks;

	private Set<Animation5_Ball> balls = new HashSet<>();

	private List<Color> colors;
	private ColorUtil.ColorSet<Integer, Integer, Integer> colorRarity;

	private Location boxLocation, armorStandLocation;

	private Location corner1, corner2, corner3, corner4;
	private Reward reward;

	class Task implements Runnable {
		int time = 0;
		double rotSpeed = 0.1;
		@Override
		public void run() {

			if(time == 40) {
				Animation5_Ball ball = new Animation5_Ball(main, boxLocation.clone().add(2, 0, 2));
				ball.runTaskTimer(main, 0L, 1L);
				balls.add(ball);
			} else if(time == 50) {
				Animation5_Ball ball = new Animation5_Ball(main, boxLocation.clone().add(-2, 0, 2));
				ball.runTaskTimer(main, 0L, 1L);
				balls.add(ball);
			} else if(time == 60) {
				Animation5_Ball ball = new Animation5_Ball(main, boxLocation.clone().add(-2, 0, -2));
				ball.runTaskTimer(main, 0L, 1L);
				balls.add(ball);
			} else if(time == 70) {
				Animation5_Ball ball = new Animation5_Ball(main, boxLocation.clone().add(2, 0, -2));
				ball.runTaskTimer(main, 0L, 1L);
				balls.add(ball);
			} else if(time == 80) {
				music.runTaskTimer(main, 0L, 4L);

				armorStand = ASSpawner.spawn(main, cubeletBox, cubeletType, false);
				armorStandLocation = armorStand.getLocation();
				main.getAnimationHandler().getArmorStands().add(armorStand);
			} else if(time > 80 && time < 180) {
				if(armorStand != null) {
					if (time <= 130) {
						armorStandLocation.add(0, 0.02, 0);
					}
					armorStand.teleport(armorStandLocation);
					armorStand.setHeadPose(armorStand.getHeadPose().add(0, rotSpeed, 0));
					rotSpeed += 0.0030;
				}
			}

			if(time == 178) {
				colorRarity = ColorUtil.getRGBbyColor(ColorUtil.getColorByText(reward.getRarity().getName()));
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.BALL_LARGE, colors.get(0), colors.get(1));
			} else if(time == 180) {
				music.cancel();
				cubeletBox.setLastReward(reward);
				main.getHologramHandler().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;

				for(Animation5_Ball ball : balls) {
					ball.cancel();
					if(main.getAnimationHandler().getArmorStands().contains(ball.getArmorStand())) {
						ArmorStand ballStand = ball.getArmorStand();
						if(ballStand != null) ballStand.remove();
						main.getAnimationHandler().getArmorStands().remove(ballStand);
					}
				}

			} else if (time > 180 && time < 300) {
				UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());

				UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			} else if(time >= 300) {
				stop();

				Bukkit.getServer().dispatchCommand(main.getServer().getConsoleSender(),
						reward.getCommand().replaceAll("%player%", cubeletBox.getPlayerOpening().getName()));
				MessageUtils.sendLootMessage(cubeletBox.getPlayerOpening(), cubeletType, reward);

				cubeletBox.setState(CubeletBoxState.EMPTY);
				cubeletBox.setPlayerOpening(null);
				main.getHologramHandler().reloadHologram(cubeletBox);
			}

			time++;
		}
	}
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		blocks = new Animation5_Blocks(box.getLocation());
		blocks.runTaskTimer(main, 0L, 7L);

		music = new Animation5_Music(box.getLocation());

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
		main.getAnimationHandler().getArmorStands().add(armorStand);

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			reward = main.getCubeletRewardHandler().processReward(cubeletBox.getPlayerOpening(), cubeletType);
		});
	}
	
	public void stop() {
		blocks.cancel();

		try {
			music.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			for(Animation5_Ball ball : balls) {
				ball.cancel();
				if(main.getAnimationHandler().getArmorStands().contains(ball.getArmorStand())) {
					ArmorStand ballStand = ball.getArmorStand();
					if(ballStand != null) ballStand.remove();
					main.getAnimationHandler().getArmorStands().remove(ballStand);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(blocks != null) blocks.restore();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getArmorStands().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getArmorStands().remove(armorStand);
		}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}
	
}
