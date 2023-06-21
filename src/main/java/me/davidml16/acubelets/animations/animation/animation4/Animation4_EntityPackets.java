package me.davidml16.acubelets.animations.animation.animation4;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletMachine;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Animation4_EntityPackets extends BukkitRunnable {

    private final LivingEntity entity;
    private final ArmorStand armorStand;
    private final PacketContainer entitySwing;
    private final ProtocolManager protocolManager;
    private final CubeletMachine box;

    private final Random random;

    public Animation4_EntityPackets(LivingEntity entity, ArmorStand armorStand, CubeletMachine box) {
        this.entity = entity;
        this.armorStand = armorStand;
        this.random = new Random();
        this.box = box;

        this.protocolManager = Main.get().getProtocolManager();
        entitySwing = protocolManager.createPacket(PacketType.Play.Server.ANIMATION);
        entitySwing.getModifier().writeDefaults();
        entitySwing.getIntegers().write(0, entity.getEntityId()).write(1, 0);
    }

    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            protocolManager.sendServerPacket(p, entitySwing);
        }
        Sounds.playSound(this.entity.getLocation(), Sounds.MySound.DIG_STONE, 0.5F, 1F);
        Location l = armorStand.getLocation();
        l.setYaw(box.getRotation().value + random.nextInt(15 + 15) - 15);
        armorStand.teleport(l);
    }

}
