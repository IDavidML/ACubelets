package me.davidml16.acubelets.animations;

import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;

public class AnimationSettings implements Comparable<AnimationSettings> {

    private String id;
    private String displayName;
    private int animationNumber;

    private ItemStack displayItem;

    private boolean enabled;
    private boolean outlineParticles;
    private boolean floorParticles;
    private boolean aroundParticles;
    private boolean needPermission;

    public AnimationSettings(String id) {
        this.id = id;
        this.displayName = "";
        this.animationNumber = 0;
        this.displayItem = XMaterial.BARRIER.parseItem();
        this.enabled = true;
        this.outlineParticles = false;
        this.floorParticles = false;
        this.aroundParticles = false;
        this.needPermission = true;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ItemStack getDisplayItem() { return displayItem != null ? displayItem : XMaterial.BARRIER.parseItem(); }

    public void setDisplayItem(ItemStack displayItem) { this.displayItem = displayItem; }

    public int getAnimationNumber() { return animationNumber; }

    public void setAnimationNumber(int animationNumber) { this.animationNumber = animationNumber; }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isOutlineParticles() { return outlineParticles; }

    public void setOutlineParticles(boolean outlineParticles) { this.outlineParticles = outlineParticles; }

    public boolean isFloorParticles() { return floorParticles; }

    public void setFloorParticles(boolean floorParticles) { this.floorParticles = floorParticles; }

    public boolean isAroundParticles() { return aroundParticles; }

    public void setAroundParticles(boolean aroundParticles) { this.aroundParticles = aroundParticles; }

    public boolean isNeedPermission() { return needPermission; }

    public void setNeedPermission(boolean needPermission) { this.needPermission = needPermission; }

    @Override
    public int compareTo(AnimationSettings o) {
        return getAnimationNumber() - o.getAnimationNumber();
    }

    @Override
    public String toString() {
        return "AnimationSettings{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", animationNumber=" + animationNumber +
                ", displayItem=" + displayItem +
                ", outlineParticles=" + outlineParticles +
                ", floorParticles=" + floorParticles +
                ", aroundParticles=" + aroundParticles +
                ", needPermission=" + needPermission +
                '}';
    }

}
