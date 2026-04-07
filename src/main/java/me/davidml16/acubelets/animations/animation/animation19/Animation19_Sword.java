package me.davidml16.acubelets.animations.animation.animation19;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.List;

public class Animation19_Sword extends BukkitRunnable {

	@Getter
    private final ArmorStand armorStand;

	private final List<Location> locations;

	@Setter
    @Getter
    private int step;

	private final double yDif;

	public Animation19_Sword(Main main, Location center, float radius, int points, boolean small, int step) {
		this.locations = LocationUtils.getCircle(center, radius, points);
		this.step = step;
		this.yDif = 0.0;

		ArmorStand armorStand = center.getWorld().spawn(locations.get(step), ArmorStand.class);
		armorStand.setSilent(true);
		armorStand.setVisible(false);
		armorStand.setItemInHand(XMaterial.DIAMOND_SWORD.parseItem());
		armorStand.setRightArmPose(new EulerAngle(Math.toRadians(-95f), Math.toRadians(-90f), Math.toRadians(80f)));
		armorStand.setGravity(false);
		armorStand.setSmall(small);
		armorStand.setMarker(false);
		armorStand.setRemoveWhenFarAway(false);
		armorStand.setCustomNameVisible(false);
		armorStand.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));
		armorStand.teleport(locations.get(step));

		main.getAnimationHandler().getEntities().add(armorStand);

		Sounds.playSound(armorStand.getLocation(), Sounds.MySound.ANVIL_LAND, 0.5F, 3);

		this.armorStand = armorStand;
	}

    public void run() {
		Location loc2 = locations.get(step);

		if (step + 1 >= locations.size())
			loc2.setDirection(locations.get(0).clone().add(0, yDif, 0).toVector().subtract(loc2.toVector()));
		else loc2.setDirection(locations.get(step + 1).clone().add(0, yDif, 0).toVector().subtract(loc2.toVector()));

		armorStand.teleport(loc2);

		if (step % 2 == 0) {
			Location particleLoc = armorStand.getLocation()
					.add(armorStand.getLocation().getDirection().normalize().multiply(-3))
					.add(0, 1, 0);

			Particle.Spell spellData = new Particle.Spell(Color.fromRGB(255, 255, 255), 1);

			armorStand.getWorld().spawnParticle(
					Particle.EFFECT,
					particleLoc,
					3,
					0.5,
					0.5,
					0.5,
					0.01,
					spellData
			);
		}

		this.step++;
		if (step >= locations.size()) step = 0;

	}

}
