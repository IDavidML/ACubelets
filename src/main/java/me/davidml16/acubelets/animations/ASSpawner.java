package me.davidml16.acubelets.animations;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.enums.Rotation;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.EulerAngle;

public class ASSpawner {

    public static ArmorStand spawn(Main main, CubeletBox box, CubeletType type, boolean opposite) {
        Location loc = box.getLocation().clone().add(0.5, -0.35, 0.5);
        loc.setYaw(getRotation(box, opposite).value);

        ArmorStand armorStand = box.getLocation().getWorld().spawn(loc, ArmorStand.class);

        if(XMaterial.supports(10)) armorStand.setSilent(true);

        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setHelmet(type.getIcon());
        armorStand.setSmall(true);
        armorStand.setMarker(true);
        armorStand.setRemoveWhenFarAway(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));

        armorStand.teleport(loc);

        return armorStand;
    }

    private static Rotation getRotation(CubeletBox box, boolean opposite) {
        if(box.getRotation() == Rotation.NORTH)
            return opposite ? Rotation.SOUTH : Rotation.NORTH;
        else if(box.getRotation() == Rotation.SOUTH)
            return opposite ? Rotation.NORTH : Rotation.SOUTH;
        else if(box.getRotation() == Rotation.EAST)
            return opposite ? Rotation.WEST : Rotation.EAST;
        else if(box.getRotation() == Rotation.WEST)
            return opposite ? Rotation.EAST : Rotation.WEST;
        return null;
    }

}
