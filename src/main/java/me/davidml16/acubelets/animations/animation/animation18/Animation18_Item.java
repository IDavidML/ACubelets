package me.davidml16.acubelets.animations.animation.animation18;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.NBTEditor;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.List;

public class Animation18_Item extends BukkitRunnable {

    private final ArmorStand armorStandItem;
    private final ArmorStand armorStandName;

    private Location center;
    private Reward reward;

    private int step = 0;
    private int circleStep = 0;
    private double circleSize = 0.0D;

    public Animation18_Item(Main main, Reward reward, Location center) {
        this.center = center;
        this.reward = reward;

        ArmorStand armorStandItem = center.getWorld().spawn(center, ArmorStand.class);
        NBTEditor.set( armorStandItem, ( byte ) 1, "Silent" );
        if(XMaterial.supports(10)) armorStandItem.setSilent(true);
        armorStandItem.setVisible(false);
        armorStandItem.setGravity(false);
        armorStandItem.setItemInHand(reward.getIcon());
        armorStandItem.setRightArmPose(new EulerAngle( Math.toRadians( -95f ), Math.toRadians( -90f ), Math.toRadians( 80f ) ));
        armorStandItem.setSmall(false);
        armorStandItem.setMarker(false);
        armorStandItem.setRemoveWhenFarAway(false);
        armorStandItem.setCustomNameVisible(false);
        armorStandItem.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));
        armorStandItem.teleport(center);
        main.getAnimationHandler().getEntities().add(armorStandItem);

        ArmorStand armorStandName = center.getWorld().spawn(center.clone().add(0, 0.45, 0), ArmorStand.class);
        NBTEditor.set( armorStandName, ( byte ) 1, "Silent" );
        if(XMaterial.supports(10)) armorStandName.setSilent(true);
        armorStandName.setVisible(false);
        armorStandName.setGravity(false);
        armorStandName.setHelmet(null);
        armorStandName.setSmall(true);
        armorStandName.setMarker(false);
        armorStandName.setRemoveWhenFarAway(false);
        armorStandName.setCustomName(Utils.getColorByText(reward.getRarity().getName()) + reward.getName());
        armorStandName.setCustomNameVisible(true);
        armorStandName.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));
        armorStandName.teleport(center.clone().add(0, 0.45, 0));
        main.getAnimationHandler().getEntities().add(armorStandName);

        Sounds.playSound(armorStandItem.getLocation(), Sounds.MySound.ORB_PICKUP, 0.5F, 3);

        this.armorStandItem = armorStandItem;
        this.armorStandName = armorStandName;
    }

    public ArmorStand getArmorStandItem() { return armorStandItem; }
    public ArmorStand getArmorStandName() { return armorStandName; }

    public Reward getReward() { return reward; }

    public int getStep() { return step; }
    public void setStep(int step) { this.step = step; }

    public void run() {

        if (step <= 20) { this.circleSize += 0.125D; }

        List<Location> teleportLocs = LocationUtils.getCircle(center.clone(), circleSize, 73);
        Location teleportLoc = teleportLocs.get(circleStep).clone();

        if(circleStep + 1 >= teleportLocs.size())
            teleportLoc.setDirection(teleportLocs.get(0).clone().toVector().subtract(teleportLoc.toVector()));
        else
            teleportLoc.setDirection(teleportLocs.get(circleStep + 1).clone().toVector().subtract(teleportLoc.toVector()));

        armorStandItem.teleport(teleportLoc);
        armorStandName.teleport(teleportLoc.clone().add(0, 0.45, 0));

        this.circleStep++;
        if(circleStep >= teleportLocs.size()) circleStep = 0;

        this.step++;

    }

}
