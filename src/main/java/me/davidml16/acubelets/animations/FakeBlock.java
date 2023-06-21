package me.davidml16.acubelets.animations;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class FakeBlock {

    private Location location;

    private XMaterial xMaterial;
    private Material material;

    private XMaterial altXMaterial;
    private Material altMaterial;

    private BlockFace blockFace;

    public FakeBlock(Location location, XMaterial xMaterial) {
        this.location = location;
        this.xMaterial = xMaterial;
    }

    public FakeBlock(Location location, XMaterial xMaterial, XMaterial altMaterial) {
        this.location = location;
        this.xMaterial = xMaterial;
        this.altXMaterial = altMaterial;
    }

    public FakeBlock(Location location, XMaterial xMaterial, XMaterial altXMaterial, Material altMaterial) {
        this.location = location;
        this.xMaterial = xMaterial;
        this.altXMaterial = altXMaterial;
        this.altMaterial = altMaterial;
    }

    public FakeBlock(Location location, XMaterial xMaterial, BlockFace blockFace) {
        this.location = location;
        this.xMaterial = xMaterial;
        this.blockFace = blockFace;
    }

    public FakeBlock(Location location, XMaterial xMaterial, XMaterial altMaterial, BlockFace blockFace) {
        this.location = location;
        this.xMaterial = xMaterial;
        this.altXMaterial = altMaterial;
        this.blockFace = blockFace;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public XMaterial getXMaterial() {
        return xMaterial;
    }

    public void setXMaterial(XMaterial xMaterial) {
        this.xMaterial = xMaterial;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public XMaterial getAltXMaterial() {
        return altXMaterial;
    }

    public void setAltXMaterial(XMaterial altXMaterial) {
        this.altXMaterial = altXMaterial;
    }

    public Material getAltMaterial() {
        return altMaterial;
    }

    public void setAltMaterial(Material altMaterial) {
        this.altMaterial = altMaterial;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public boolean hasBlockFace() {
        return blockFace != null;
    }

    public void setBlockFace(BlockFace blockFace) {
        this.blockFace = blockFace;
    }

}
