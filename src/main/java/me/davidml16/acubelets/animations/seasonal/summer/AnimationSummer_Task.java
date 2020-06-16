package me.davidml16.acubelets.animations.seasonal.summer;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.interfaces.Reward;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.PermissionReward;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.MessageUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.RepeatingTask;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnimationSummer_Task implements Animation {

	private int id;

	private Main main;
	public AnimationSummer_Task(Main main) {
		this.main = main;
	}

	private ArmorStand armorStand;
	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private AnimationSummer_Music music;
	private AnimationSummer_Blocks blocks;

	private Set<AnimationSummer_Ball> balls = new HashSet<>();

	private List<Color> colors;
	private ColorUtil.ColorSet<Integer, Integer, Integer> colorRarity;

	private Location boxLocation, armorStandLocation;

	private Location corner1, corner2, corner3, corner4;
	private Reward reward;

	private RepeatingTask hologramAnimation;

	class Task implements Runnable {
		int time = 0;
		double rotSpeed = 0.1;
		@Override
		public void run() {

			if(time == 40) {
				AnimationSummer_Ball ball = new AnimationSummer_Ball(main, boxLocation.clone().add(2, 0, 2));
				ball.runTaskTimer(main, 0L, 1L);
				balls.add(ball);
			} else if(time == 50) {
				AnimationSummer_Ball ball = new AnimationSummer_Ball(main, boxLocation.clone().add(-2, 0, 2));
				ball.runTaskTimer(main, 0L, 1L);
				balls.add(ball);
			} else if(time == 60) {
				AnimationSummer_Ball ball = new AnimationSummer_Ball(main, boxLocation.clone().add(-2, 0, -2));
				ball.runTaskTimer(main, 0L, 1L);
				balls.add(ball);
			} else if(time == 70) {
				AnimationSummer_Ball ball = new AnimationSummer_Ball(main, boxLocation.clone().add(2, 0, -2));
				ball.runTaskTimer(main, 0L, 1L);
				balls.add(ball);
			} else if(time == 80) {
				music.runTaskTimer(main, 0L, 4L);

				armorStand = ASSpawner.spawn(main, cubeletBox, cubeletType, false);
				armorStandLocation = armorStand.getLocation();
				main.getAnimationHandler().getEntities().add(armorStand);
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

				for(AnimationSummer_Ball ball : balls) {
					ball.cancel();
					if(main.getAnimationHandler().getEntities().contains(ball.getArmorStand())) {
						ArmorStand ballStand = ball.getArmorStand();
						if(ballStand != null) ballStand.remove();
						main.getAnimationHandler().getEntities().remove(ballStand);
					}
				}
			} else if(time == 220) {
				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);
			} else if (time > 180 && time < 320) {
				UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());

				UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			} else if(time >= 320) {
				stop();

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
		blocks = new AnimationSummer_Blocks(box.getLocation());
		blocks.runTaskTimer(main, 0L, 7L);

		music = new AnimationSummer_Music(box.getLocation());

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
		main.getAnimationHandler().getEntities().add(armorStand);

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
			for(AnimationSummer_Ball ball : balls) {
				ball.cancel();
				if(main.getAnimationHandler().getEntities().contains(ball.getArmorStand())) {
					ArmorStand ballStand = ball.getArmorStand();
					if(ballStand != null) ballStand.remove();
					main.getAnimationHandler().getEntities().remove(ballStand);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(blocks != null) blocks.restore();

		if(hologramAnimation != null) hologramAnimation.canncel();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getEntities().remove(armorStand);
		}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}
	
}
