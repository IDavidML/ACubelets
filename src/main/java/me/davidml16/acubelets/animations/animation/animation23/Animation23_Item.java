package me.davidml16.acubelets.animations.animation.animation23;

import com.cryptomorin.xseries.XMaterial;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.List;

public class Animation23_Item {

    private ArmorStand armorStand;

    private Reward reward;

    private boolean active;
    private int speedRate;
    private int circleStep;

    private List<Location> locations;

    public Animation23_Item(Main main, Reward reward, Location center, List<Location> locations, int initialStep, boolean headChecker) {
        this.reward = reward;
        this.active = false;
        this.circleStep = initialStep;
        this.locations = locations;
        this.speedRate = 0;

        ArmorStand armorStandItem = center.getWorld().spawn(locations.get(initialStep), ArmorStand.class);
        NBTEditor.set( armorStandItem, ( byte ) 1, "Silent" );
        armorStandItem.setSilent(true);
        armorStandItem.setVisible(false);
        armorStandItem.setGravity(false);
        armorStandItem.setHelmet(headChecker ? XMaterial.RED_CONCRETE.parseItem() : XMaterial.BLACK_CONCRETE.parseItem());
        armorStandItem.setSmall(false);
        armorStandItem.setMarker(false);
        armorStandItem.setRemoveWhenFarAway(false);
        armorStandItem.setCustomNameVisible(false);
        armorStandItem.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));

        main.getAnimationHandler().getEntities().add(armorStandItem);

        Sounds.playSound(armorStandItem.getLocation(), Sounds.MySound.CHICKEN_EGG_POP, 0.5F, 3);

        this.armorStand = armorStandItem;
    }

    public ArmorStand getArmorStand() { return armorStand; }

    public Reward getReward() { return reward; }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSpeedRate() {
        return speedRate;
    }
    public void setSpeedRate(int speedRate) {
        this.speedRate = speedRate;
    }

    public void Tick() {
        armorStand.teleport(locations.get(circleStep));

        if (!active) return;
        if(speedRate <= 0) return;

        this.circleStep += speedRate;
        if(circleStep >= locations.size()) {
            circleStep = circleStep - locations.size();
        }
    }

}
