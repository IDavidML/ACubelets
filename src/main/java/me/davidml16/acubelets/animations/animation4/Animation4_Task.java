package me.davidml16.acubelets.animations.animation4;

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
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.List;

public class Animation4_Task implements Animation {

	private int id;

	private Main main;
	public Animation4_Task(Main main) {
		this.main = main;
	}

	private ArmorStand armorStand;
	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private Animation4_Music music;
	private Animation4_Blocks blocks;
	private Animation4_Arch arch;
	private Animation4_ArchSounds archSounds;

	private List<Color> colors;
	private ColorUtil.ColorSet<Integer, Integer, Integer> colorRarity;

	private Location armorStandLocation;

	private Reward reward;

	class Task implements Runnable {
		int time = 0;
		@Override
		public void run() {

			if(time == 50) {
				arch.runTaskTimer(main, 0L, 1L);
				archSounds.runTaskTimer(main, 0L, 5L);
			} else if(time == 100) {
				archSounds.cancel();
			} else if(time == 105) {
				music.runTaskTimer(main, 0L, 4L);

				armorStand = ASSpawner.spawn(main, cubeletBox.getLocation(), cubeletType);
				armorStandLocation = armorStand.getLocation();
				main.getAnimationHandler().getArmorStands().add(armorStand);
			} else if(time > 105 && time < 203) {
				if (armorStand != null) {
					if (time <= 145) armorStandLocation.add(0, 0.02, 0);
					armorStand.teleport(armorStandLocation);
					armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.16, 0));
				}
			} else if(time == 203) {
				colorRarity = ColorUtil.getRGBbyColor(ColorUtil.getColorByText(reward.getRarity().getName()));
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.BALL_LARGE, colors.get(0), colors.get(1));
			} else if(time == 205) {
				music.cancel();
				cubeletBox.setLastReward(reward);
				main.getHologramHandler().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;
			} else if(time == 325) {
				stop();
				blocks.restore();

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
		music = new Animation4_Music(box.getLocation());

		blocks = new Animation4_Blocks(box.getLocation());
		blocks.runTaskTimer(main, 0L, 7L);

		arch = new Animation4_Arch(box.getLocation());

		archSounds = new Animation4_ArchSounds(box.getLocation());

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.RED, Color.RED);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			reward = main.getCubeletRewardHandler().processReward(cubeletBox.getPlayerOpening(), cubeletType);
		});
	}
	
	public void stop() {
		blocks.cancel();
		music.cancel();
		arch.cancel();
		archSounds.cancel();

		blocks.restore();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getArmorStands().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getArmorStands().remove(armorStand);
		}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}
	
}
