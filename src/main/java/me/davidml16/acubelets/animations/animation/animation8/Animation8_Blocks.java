package me.davidml16.acubelets.animations.animation.animation8;

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

public class Animation8_Blocks extends AnimationBlocks {

    public Animation8_Blocks(Location location) {

        super(location);

        // FIRST CROSS
        setStepFakeBlocks(1, new FakeBlock[] {
                new FakeBlock(getLocation(0), XMaterial.GREEN_WOOL),
                new FakeBlock(getLocation(1), XMaterial.GREEN_WOOL),
                new FakeBlock(getLocation(2), XMaterial.GREEN_WOOL),
                new FakeBlock(getLocation(3), XMaterial.GREEN_WOOL),
                new FakeBlock(getLocation(4), XMaterial.GREEN_WOOL)
        });

        // CORNERS
        setStepFakeBlocks(2, new FakeBlock[] {
                new FakeBlock(getLocation(5), XMaterial.RED_WOOL),
                new FakeBlock(getLocation(6), XMaterial.RED_WOOL),
                new FakeBlock(getLocation(7), XMaterial.RED_WOOL),
                new FakeBlock(getLocation(8), XMaterial.RED_WOOL)
        });

        // STAIRS
        setStepFakeBlocks(3, new FakeBlock[] {
                new FakeBlock(getLocation(9), XMaterial.SPRUCE_STAIRS, BlockFace.WEST),
                new FakeBlock(getLocation(10), XMaterial.SPRUCE_STAIRS, BlockFace.EAST),
                new FakeBlock(getLocation(11), XMaterial.SPRUCE_STAIRS, BlockFace.NORTH),
                new FakeBlock(getLocation(12), XMaterial.SPRUCE_STAIRS, BlockFace.SOUTH)
        });

        // SIDES OF STAIRS
        setStepFakeBlocks(4, new FakeBlock[] {
                new FakeBlock(getLocation(13), XMaterial.SPRUCE_SLAB),
                new FakeBlock(getLocation(14), XMaterial.SPRUCE_SLAB),
                new FakeBlock(getLocation(15), XMaterial.SPRUCE_SLAB),
                new FakeBlock(getLocation(16), XMaterial.SPRUCE_SLAB),
                new FakeBlock(getLocation(17), XMaterial.SPRUCE_SLAB),
                new FakeBlock(getLocation(18), XMaterial.SPRUCE_SLAB),
                new FakeBlock(getLocation(19), XMaterial.SPRUCE_SLAB),
                new FakeBlock(getLocation(20), XMaterial.SPRUCE_SLAB)
        });

        // CORNER PILLARS 0
        setStepFakeBlocks(5, new FakeBlock[] {
                new FakeBlock(getLocation(21), XMaterial.SPRUCE_LOG),
                new FakeBlock(getLocation(22), XMaterial.SPRUCE_LOG),
                new FakeBlock(getLocation(23), XMaterial.SPRUCE_LOG),
                new FakeBlock(getLocation(24), XMaterial.SPRUCE_LOG)
        });

        // CORNER PILLARS 1
        setStepFakeBlocks(6, new FakeBlock[] {
                new FakeBlock(getLocation(25), XMaterial.SPRUCE_SLAB),
                new FakeBlock(getLocation(26), XMaterial.SPRUCE_SLAB),
                new FakeBlock(getLocation(27), XMaterial.SPRUCE_SLAB),
                new FakeBlock(getLocation(28), XMaterial.SPRUCE_SLAB)
        });

    }

}
