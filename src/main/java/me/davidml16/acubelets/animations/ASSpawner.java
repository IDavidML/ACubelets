package me.davidml16.acubelets.animations;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;

public class ASSpawner {

    public static ArmorStand spawn(Main main, Location location, CubeletType type) {
        ArmorStand armorStand = location.getWorld().spawn(location.clone().add(0.5, -0.35, 0.5), ArmorStand.class);

        if (!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.9"))
            armorStand.setSilent(true);

        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setHelmet(type.getIcon());
        armorStand.setSmall(true);
        armorStand.setMarker(true);
        armorStand.setRemoveWhenFarAway(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));

        Location loc = armorStand.getLocation().clone();
        loc.setYaw(0);
        armorStand.teleport(loc);

        return armorStand;
    }
}
