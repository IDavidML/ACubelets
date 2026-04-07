package me.davidml16.acubelets.animations.animation.animation5;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.utils.SkullUtils;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

@Slf4j
public class Animation5_Ball extends BukkitRunnable {

	private final Location spawnLoc;
	@Getter
    private final ArmorStand armorStand;

	public Animation5_Ball(Main main, Location spawnLoc) {
		this.spawnLoc = spawnLoc;

		if (spawnLoc == null || spawnLoc.getWorld() == null) {
			log.warn("Spawn location is null or world is null. Cannot spawn the ball.");
			this.armorStand = null;
			return;
		}

		ArmorStand armorStand = spawnLoc.getWorld().spawn(spawnLoc.clone().add(0, -1.5, 0), ArmorStand.class);
		armorStand.setSilent(true);
		armorStand.setVisible(false);
		armorStand.setGravity(true);
		armorStand.setHelmet(SkullUtils.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE1YWIwNWVhMjU0YzMyZTNjNDhmM2ZkY2Y5ZmQ5ZDc3ZDNjYmEwNGU2YjVlYzJlNjhiM2NiZGNmYWMzZmQifX19"));
		armorStand.setSmall(false);
		armorStand.setBasePlate(false);
		armorStand.setRemoveWhenFarAway(false);
		armorStand.setCustomNameVisible(false);
		armorStand.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));

		ASSpawner.setEntityNoclip(armorStand);

		armorStand.teleport(spawnLoc.clone().add(0, -2.5, 0));

		main.getAnimationHandler().getEntities().add(armorStand);

		Sounds.playSound(spawnLoc, Sounds.MySound.CHICKEN_EGG_POP, 1F, 0);

		this.armorStand = armorStand;
	}

    public void run() {
		armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.05, 0.15));

		if (armorStand.isOnGround() || armorStand.getLocation().getY() <= (spawnLoc.getY() - 1.25)) {
			armorStand.setVelocity(new Vector(0, 0.5, 0));
			 Sounds.playSound(armorStand.getLocation(), Sounds.MySound.CHICKEN_EGG_POP, 0.5F, 1.5F);
		}
	}
}
