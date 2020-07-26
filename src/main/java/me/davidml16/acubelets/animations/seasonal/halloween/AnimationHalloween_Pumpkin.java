package me.davidml16.acubelets.animations.seasonal.halloween;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.NBTEditor;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class AnimationHalloween_Pumpkin extends BukkitRunnable {

    private final Location spawnLoc;
    private ArmorStand armorStand;
    private UUID playerUUID;

    public AnimationHalloween_Pumpkin(Main main, Location spawnLoc, UUID playerUUID) {
        this.spawnLoc = spawnLoc;
        this.playerUUID = playerUUID;

        Player target = Bukkit.getPlayer(playerUUID);
        if(target != null) {
            spawnLoc.setDirection(target.getLocation().subtract(spawnLoc).toVector());
        }
        spawnLoc.add(0, -0.87, 0);

        ArmorStand armorStand = spawnLoc.getWorld().spawn(spawnLoc, ArmorStand.class);

        NBTEditor.set( armorStand, ( byte ) 1, "Silent" );

        armorStand.setVisible(false);
        armorStand.setGravity(false);
        if(XMaterial.isNewVersion())
            armorStand.setHelmet(XMaterial.CARVED_PUMPKIN.parseItem());
        else
            armorStand.setHelmet(new ItemBuilder(Material.PUMPKIN, 1, (byte) 1).toItemStack());
        armorStand.setSmall(false);
        armorStand.setBasePlate(false);
        armorStand.setRemoveWhenFarAway(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));

        UtilParticles.display(Particles.EXPLOSION_LARGE, spawnLoc.clone().add(0, 0.5, 0), 2);
        Sounds.playSound(spawnLoc, Sounds.MySound.EXPLODE, 0.5F, 1f);

        main.getAnimationHandler().getEntities().add(armorStand);

        this.armorStand = armorStand;
    }

    public ArmorStand getArmorStand() { return armorStand; }

    public void run() {
        Player target = Bukkit.getPlayer(playerUUID);
        if(target != null) {
            spawnLoc.setDirection(target.getLocation().subtract(armorStand.getLocation()).toVector());
            armorStand.teleport(spawnLoc);
        }
    }
}
