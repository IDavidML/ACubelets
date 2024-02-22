package me.davidml16.acubelets.animations.animation.animation24;

import com.cryptomorin.xseries.XMaterial;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

public class Animation24_Item {

    private final ArmorStand armorStandItem;
    private final ArmorStand armorStandName;

    private Reward reward;

    private boolean active;
    private int speedRate;
    private int circleStep;

    private List<Location> locations;

    public Animation24_Item(Main main, Reward reward, Location center, List<Location> locations, int initialStep) {
        this.reward = reward;
        this.active = false;
        this.circleStep = initialStep;
        this.locations = locations;
        this.speedRate = 0;

        Location targetLocation = locations.get(initialStep);

        ArmorStand armorStandItem = center.getWorld().spawn(locations.get(initialStep), ArmorStand.class);
        NBTEditor.set( armorStandItem, ( byte ) 1, "Silent" );
        armorStandItem.setSilent(true);
        armorStandItem.setVisible(false);
        armorStandItem.setGravity(false);
        armorStandItem.setHelmet(reward.getIcon());
        armorStandItem.setSmall(true);
        armorStandItem.setMarker(false);
        armorStandItem.setRemoveWhenFarAway(false);
        armorStandItem.setCustomNameVisible(false);
        armorStandItem.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));
        main.getAnimationHandler().getEntities().add(armorStandItem);

        ArmorStand armorStandName = center.getWorld().spawn(targetLocation.clone().add(0, 0.35, 0), ArmorStand.class);
        NBTEditor.set( armorStandName, ( byte ) 1, "Silent" );
        armorStandName.setSilent(true);
        armorStandName.setVisible(false);
        armorStandName.setGravity(false);
        armorStandName.setHelmet(null);
        armorStandName.setSmall(true);
        armorStandName.setMarker(false);
        armorStandName.setRemoveWhenFarAway(false);
        armorStandName.setCustomName(Utils.translate(reward.getName()));
        armorStandName.setCustomNameVisible(true);
        armorStandName.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));
        armorStandName.teleport(center.clone().add(0, 0.45, 0));
        main.getAnimationHandler().getEntities().add(armorStandName);

        Sounds.playSound(armorStandItem.getLocation(), Sounds.MySound.PISTON_EXTEND, 0.5F, 3);

        this.armorStandItem = armorStandItem;
        this.armorStandName = armorStandName;
    }

    public ArmorStand getArmorStandItem() { return armorStandItem; }
    public ArmorStand getArmorStandName() { return armorStandName; }

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
        Location targetLocation = locations.get(circleStep);

        armorStandItem.teleport(targetLocation);
        armorStandName.teleport(targetLocation.clone().add(0, 0.35, 0));

        if (!active) return;
        if(speedRate <= 0) return;

        this.circleStep += speedRate;
        if(circleStep >= locations.size()) {
            circleStep = circleStep - locations.size();
        }
    }

}
