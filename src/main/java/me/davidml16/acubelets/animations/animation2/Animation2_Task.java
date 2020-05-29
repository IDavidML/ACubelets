package me.davidml16.acubelets.animations.animation2;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Reward;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.MessageUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

public class Animation2_Task implements Animation {

	private int id;

	private Main main;
	public Animation2_Task(Main main) {
		this.main = main;
	}

	private ArmorStand armorStand;
	private CubeletBox cubeletBox;
	private CubeletType cubeletType;
	private Animation2_Music music;
	private List<Color> colors;

	private ColorUtil.ColorSet<Integer, Integer, Integer> colorRarity;

	private Location corner1, corner2, corner3, corner4;

	private Location boxLocation;
	private Location armorStandLocation;

	private Reward reward;

	class Task implements Runnable {
		int time = 0;
		@Override
		public void run() {
			if(armorStand != null) {
				if (time <= 50) armorStandLocation.add(0, 0.02, 0);
				armorStand.teleport(armorStandLocation);
				armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.159, 0));
			}

			time++;
			if(time == 98) {
				colorRarity = ColorUtil.getRGBbyColor(ColorUtil.getColorByText(reward.getRarity().getName()));
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.STAR, colors.get(0), colors.get(1));
			} else if(time == 100) {
				music.cancel();
				cubeletBox.setLastReward(reward);
				main.getHologramHandler().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;
			} else if (time > 100 && time < 220) {
				UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());

				UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			} else if(time >= 220) {
				stop();

				Bukkit.getServer().dispatchCommand(main.getServer().getConsoleSender(),
						reward.getCommand().replaceAll("%player%", cubeletBox.getPlayerOpening().getName()));
				MessageUtils.sendLootMessage(cubeletBox.getPlayerOpening(), cubeletType, reward);

				cubeletBox.setState(CubeletBoxState.EMPTY);
				cubeletBox.setPlayerOpening(null);
				main.getHologramHandler().reloadHologram(cubeletBox);
			}

		}
	}
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		armorStand = ASSpawner.spawn(main, box, type, true);

		armorStandLocation = armorStand.getLocation();

		music = new Animation2_Music(box.getLocation());
		music.runTaskTimer(main, 0L, 4L);

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = main.getFireworkUtil().getRandomColors();

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
		music.cancel();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getArmorStands().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getArmorStands().remove(armorStand);
		}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}
	
}
