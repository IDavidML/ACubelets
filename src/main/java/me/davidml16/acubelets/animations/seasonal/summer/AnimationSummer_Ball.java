package me.davidml16.acubelets.animations.seasonal.summer;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnimationSummer_Ball extends BukkitRunnable {

    private final Location spawnLoc;
    private ArmorStand armorStand;

    public AnimationSummer_Ball(Main main, Location spawnLoc) {
        this.spawnLoc = spawnLoc;

        ArmorStand armorStand = spawnLoc.getWorld().spawn(spawnLoc.clone().add(0, -1.5, 0), ArmorStand.class);
        if(XMaterial.supports(10)) armorStand.setSilent(true);
        armorStand.setVisible(false);
        armorStand.setGravity(true);
        armorStand.setHelmet(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE1YWIwNWVhMjU0YzMyZTNjNDhmM2ZkY2Y5ZmQ5ZDc3ZDNjYmEwNGU2YjVlYzJlNjhiM2NiZGNmYWMzZmQifX19"));
        armorStand.setSmall(false);
        armorStand.setBasePlate(false);
        armorStand.setRemoveWhenFarAway(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));

        Method getHandle = null;
        try {
            getHandle = armorStand.getClass().getMethod("getHandle");
            Object armorS = getHandle.invoke(armorStand);
            Field field = armorS.getClass().getField("noclip");
            field.setAccessible(true);
            field.setBoolean(armorS, true);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        armorStand.teleport(spawnLoc.clone().add(0, -2.5, 0));

        main.getAnimationHandler().getArmorStands().add(armorStand);

        Sounds.playSound(spawnLoc, Sounds.MySound.CHICKEN_EGG_POP, 1F, 0);

        this.armorStand = armorStand;
    }

    public ArmorStand getArmorStand() { return armorStand; }

    public void run() {
        armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.05, 0.15));

        if (armorStand.getLocation().getY() <= (spawnLoc.getY() - 1.25))
            armorStand.setVelocity(new Vector(0, 0.85, 0));
    }
}
