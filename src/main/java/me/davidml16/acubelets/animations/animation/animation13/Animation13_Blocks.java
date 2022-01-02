package me.davidml16.acubelets.animations.animation.animation13;

import me.davidml16.acubelets.animations.AnimationBlocks;
import me.davidml16.acubelets.animations.FakeBlock;
import me.davidml16.acubelets.utils.CuboidRegion;
import me.davidml16.acubelets.utils.MultiVersion.AB_12;
import me.davidml16.acubelets.utils.MultiVersion.AB_13;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class Animation13_Blocks extends AnimationBlocks {

    public Animation13_Blocks(Location location) {

        super(location);

        // FIRST CROSS
        setStepFakeBlocks(1, new FakeBlock[] {
                new FakeBlock(getLocation(0), XMaterial.DARK_PRISMARINE),
                new FakeBlock(getLocation(1), XMaterial.PRISMARINE),
                new FakeBlock(getLocation(2), XMaterial.PRISMARINE),
                new FakeBlock(getLocation(3), XMaterial.PRISMARINE),
                new FakeBlock(getLocation(4), XMaterial.PRISMARINE)
        });

        // CORNERS
        setStepFakeBlocks(2, new FakeBlock[] {
                new FakeBlock(getLocation(5), XMaterial.PRISMARINE_BRICKS),
                new FakeBlock(getLocation(6), XMaterial.PRISMARINE_BRICKS),
                new FakeBlock(getLocation(7), XMaterial.PRISMARINE_BRICKS),
                new FakeBlock(getLocation(8), XMaterial.PRISMARINE_BRICKS)
        });

        // STAIRS
        setStepFakeBlocks(3, new FakeBlock[] {
                new FakeBlock(getLocation(9), XMaterial.DARK_PRISMARINE_STAIRS, XMaterial.COBBLESTONE_STAIRS, BlockFace.WEST),
                new FakeBlock(getLocation(10), XMaterial.DARK_PRISMARINE_STAIRS, XMaterial.COBBLESTONE_STAIRS, BlockFace.EAST),
                new FakeBlock(getLocation(11), XMaterial.DARK_PRISMARINE_STAIRS, XMaterial.COBBLESTONE_STAIRS, BlockFace.NORTH),
                new FakeBlock(getLocation(12), XMaterial.DARK_PRISMARINE_STAIRS, XMaterial.COBBLESTONE_STAIRS, BlockFace.SOUTH)
        });

        // SIDES OF STAIRS
        setStepFakeBlocks(4, new FakeBlock[] {
                new FakeBlock(getLocation(13), XMaterial.PRISMARINE_SLAB, XMaterial.COBBLESTONE_SLAB),
                new FakeBlock(getLocation(14), XMaterial.PRISMARINE_SLAB, XMaterial.COBBLESTONE_SLAB),
                new FakeBlock(getLocation(15), XMaterial.PRISMARINE_SLAB, XMaterial.COBBLESTONE_SLAB),
                new FakeBlock(getLocation(16), XMaterial.PRISMARINE_SLAB, XMaterial.COBBLESTONE_SLAB),
                new FakeBlock(getLocation(17), XMaterial.PRISMARINE_SLAB, XMaterial.COBBLESTONE_SLAB),
                new FakeBlock(getLocation(18), XMaterial.PRISMARINE_SLAB, XMaterial.COBBLESTONE_SLAB),
                new FakeBlock(getLocation(19), XMaterial.PRISMARINE_SLAB, XMaterial.COBBLESTONE_SLAB),
                new FakeBlock(getLocation(20), XMaterial.PRISMARINE_SLAB, XMaterial.COBBLESTONE_SLAB)
        });

        // CORNER PILLARS 0
        setStepFakeBlocks(5, new FakeBlock[] {
                new FakeBlock(getLocation(21), XMaterial.DARK_PRISMARINE),
                new FakeBlock(getLocation(22), XMaterial.DARK_PRISMARINE),
                new FakeBlock(getLocation(23), XMaterial.DARK_PRISMARINE),
                new FakeBlock(getLocation(24), XMaterial.DARK_PRISMARINE)
        });

        // CORNER PILLARS 1
        setStepFakeBlocks(6, new FakeBlock[] {
                new FakeBlock(getLocation(25), XMaterial.PRISMARINE_SLAB, XMaterial.COBBLESTONE_SLAB),
                new FakeBlock(getLocation(26), XMaterial.PRISMARINE_SLAB, XMaterial.COBBLESTONE_SLAB),
                new FakeBlock(getLocation(27), XMaterial.PRISMARINE_SLAB, XMaterial.COBBLESTONE_SLAB),
                new FakeBlock(getLocation(28), XMaterial.PRISMARINE_SLAB, XMaterial.COBBLESTONE_SLAB)
        });

    }

}
