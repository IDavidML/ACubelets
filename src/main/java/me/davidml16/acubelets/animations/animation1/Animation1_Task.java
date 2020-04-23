package me.davidml16.acubelets.animations.animation1;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.data.CubeletBox;
import me.davidml16.acubelets.data.CubeletType;
import me.davidml16.acubelets.data.Reward;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;

public class Animation1_Task implements Animation {

	private int id;

	private Main main;
	public Animation1_Task(Main main) {
		this.main = main;
	}

	private ArmorStand armorStand;
	private CubeletBox cubeletBox;
	private CubeletType cubeletType;
	private Animation1_Music music;

	class Task implements Runnable {
		int time = 0;
		@Override
		public void run() {
			Location loc = armorStand.getLocation().clone();

			if(time <= 50) {
				loc.add(0, 0.02, 0);
			}

			armorStand.teleport(loc);

			armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.16, 0));

			time++;

			if(time >= 100) {
				stop();
				armorStand = null;

				Reward reward = main.getCubeletRewardHandler().processReward(cubeletBox.getPlayerOpening(), cubeletType);
				main.getHologramHandler().rewardHologram(cubeletBox, reward);

				Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
					for (Hologram hologram : cubeletBox.getHolograms().values()) {
						hologram.clearLines();
					}
					cubeletBox.setUsing(false);
					cubeletBox.setPlayerOpening(null);
					main.getHologramHandler().reloadHologram(cubeletBox);
				}, 100);

			}
		}
	}
	
	public int getId() { return id; }

	@SuppressWarnings("deprecation")
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

		music = new Animation1_Music(box.getLocation());
		music.runTaskTimer(main, 0L, 4L);

		this.cubeletType = type;
		this.cubeletBox = box;

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);
		main.getAnimationHandler().getArmorStands().add(armorStand);
	}
	
	public void stop() {
		main.getAnimationHandler().getTasks().remove(this);

		music.cancel();
		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getArmorStands().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getArmorStands().remove(armorStand);
		}
	}
	
}
