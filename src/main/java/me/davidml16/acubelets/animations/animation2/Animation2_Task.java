package me.davidml16.acubelets.animations.animation2;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Reward;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.Particles;
import me.davidml16.acubelets.utils.UtilParticles;
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

	Location loc1, loc2, loc3, loc4;

	class Task implements Runnable {
		int time = 0;
		@Override
		public void run() {
			if(armorStand != null) {
				Location loc = armorStand.getLocation().clone();

				if (time <= 50) {
					loc.add(0, 0.02, 0);
				}

				armorStand.teleport(loc);

				armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.16, 0));
			}

			time++;

			if(time == 100) {
				music.cancel();
				Reward reward = main.getCubeletRewardHandler().processReward(cubeletBox.getPlayerOpening(), cubeletType);
				cubeletBox.setLastReward(reward);
				colorRarity = ColorUtil.getRGBbyColor(ColorUtil.getColorByText(reward.getRarity().getName()));
				main.getHologramHandler().rewardHologram(cubeletBox, reward);
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 2, 0.5), FireworkEffect.Type.STAR, colors.get(0), colors.get(1));
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;
			} else if (time > 100 && time < 200) {
				UtilParticles.drawParticleLine(loc1, loc2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(loc2, loc3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(loc3, loc4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(loc1, loc4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
			} else if(time >= 200) {
				stop();
				for (Hologram hologram : cubeletBox.getHolograms().values()) {
					hologram.clearLines();
				}

				Bukkit.getServer().dispatchCommand(main.getServer().getConsoleSender(),
						cubeletBox.getLastReward().getCommand().replaceAll("%player%", cubeletBox.getPlayerOpening().getName()));
				main.getCubeletRewardHandler().sendLootMessage(cubeletBox.getPlayerOpening(), cubeletType, cubeletBox.getLastReward());

				cubeletBox.setState(CubeletBoxState.EMPTY);
				cubeletBox.setPlayerOpening(null);
				main.getHologramHandler().reloadHologram(cubeletBox);
			}

		}
	}
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		armorStand = box.getLocation().getWorld().spawn(box.getLocation().clone().add(0.5, -0.35, 0.5), ArmorStand.class);
		armorStand.setVisible(false);
		armorStand.setGravity(false);
		armorStand.setHelmet(type.getIcon());
		armorStand.setSmall(true);
		armorStand.setRemoveWhenFarAway(false);
		armorStand.setCustomNameVisible(false);
		armorStand.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));

		Location loc = armorStand.getLocation().clone();
		loc.setYaw(0);
		armorStand.teleport(loc);

		music = new Animation2_Music(box.getLocation());
		music.runTaskTimer(main, 0L, 4L);

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = main.getFireworkUtil().getRandomColors();

		loc1 = cubeletBox.getLocation().clone().add(0.05, 0.55, 0.05);
		loc2 = cubeletBox.getLocation().clone().add(0.95, 0.55, 0.05);
		loc3 = cubeletBox.getLocation().clone().add(0.95, 0.55, 0.95);
		loc4 = cubeletBox.getLocation().clone().add(0.05, 0.55, 0.95);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);
		main.getAnimationHandler().getArmorStands().add(armorStand);
	}
	
	public void stop() {
		if(!music.isCancelled()) music.cancel();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getArmorStands().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getArmorStands().remove(armorStand);
		}
	}
	
}
