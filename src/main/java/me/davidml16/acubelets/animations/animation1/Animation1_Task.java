package me.davidml16.acubelets.animations.animation1;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Reward;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.Particles;
import me.davidml16.acubelets.utils.UtilParticles;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animation1_Task implements Animation {

	private int id;

	private Main main;
	public Animation1_Task(Main main) {
		this.main = main;
	}

	private CubeletBox cubeletBox;
	private CubeletType cubeletType;
	private Location startLocation;
	private List<Color> colors;

	Location boxLocation;

	class Task implements Runnable {
		int time = 0;
		@Override
		public void run() {
			if(time >= 0 && time < 70) {
				if(time % 2 == 0) {
					main.getFireworkUtil().spawn(startLocation, FireworkEffect.Type.BURST, colors.get(0), colors.get(1));
					startLocation.add(0, -0.75, 0);
				}
			} else if(time == 70) {
				Reward reward = main.getCubeletRewardHandler().processReward(cubeletBox.getPlayerOpening(), cubeletType);
				cubeletBox.setLastReward(reward);
				main.getHologramHandler().rewardHologram(cubeletBox, reward);
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 2, 0.5), FireworkEffect.Type.BALL_LARGE, colors.get(0), colors.get(1));
				cubeletBox.setState(CubeletBoxState.REWARD);
			} else if(time > 70 && time < 170) {
				UtilParticles.display(Particles.FLAME, 0.45f, 0.25f, 0.45f, boxLocation, 10);
			} else if(time >= 170) {
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
			time++;
		}
	}
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.startLocation = box.getLocation().clone().add(0.5, 26, 0.5);
		this.colors = main.getFireworkUtil().getRandomColors();

		boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);
		main.getAnimationHandler().getTasks().add(this);
	}
	
	public void stop() {
		main.getAnimationHandler().getTasks().remove(this);
		Bukkit.getServer().getScheduler().cancelTask(id);

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}
	
}
