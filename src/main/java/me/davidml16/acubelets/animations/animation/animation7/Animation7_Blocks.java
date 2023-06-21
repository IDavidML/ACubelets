package me.davidml16.acubelets.animations.animation.animation7;

import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.animations.AnimationBlocks;
import me.davidml16.acubelets.animations.FakeBlock;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class Animation7_Blocks extends AnimationBlocks {

    public Animation7_Blocks(Location location) {

        super(location);

        // FIRST CROSS
        setStepFakeBlocks(1, new FakeBlock[] {
                new FakeBlock(getLocation(0), XMaterial.BEDROCK),
                new FakeBlock(getLocation(1), XMaterial.BEDROCK),
                new FakeBlock(getLocation(2), XMaterial.BEDROCK),
                new FakeBlock(getLocation(3), XMaterial.BEDROCK),
                new FakeBlock(getLocation(4), XMaterial.BEDROCK)
        });

        // CORNERS
        setStepFakeBlocks(2, new FakeBlock[] {
                new FakeBlock(getLocation(5), XMaterial.BEDROCK),
                new FakeBlock(getLocation(6), XMaterial.BEDROCK),
                new FakeBlock(getLocation(7), XMaterial.BEDROCK),
                new FakeBlock(getLocation(8), XMaterial.BEDROCK)
        });

        // STAIRS
        setStepFakeBlocks(3, new FakeBlock[] {
                new FakeBlock(getLocation(9), XMaterial.NETHER_BRICK_STAIRS, BlockFace.WEST),
                new FakeBlock(getLocation(10), XMaterial.NETHER_BRICK_STAIRS, BlockFace.EAST),
                new FakeBlock(getLocation(11), XMaterial.NETHER_BRICK_STAIRS, BlockFace.NORTH),
                new FakeBlock(getLocation(12), XMaterial.NETHER_BRICK_STAIRS, BlockFace.SOUTH)
        });

        // SIDES OF STAIRS
        setStepFakeBlocks(4, new FakeBlock[] {
                new FakeBlock(getLocation(13), XMaterial.NETHER_BRICK_SLAB),
                new FakeBlock(getLocation(14), XMaterial.NETHER_BRICK_SLAB),
                new FakeBlock(getLocation(15), XMaterial.NETHER_BRICK_SLAB),
                new FakeBlock(getLocation(16), XMaterial.NETHER_BRICK_SLAB),
                new FakeBlock(getLocation(17), XMaterial.NETHER_BRICK_SLAB),
                new FakeBlock(getLocation(18), XMaterial.NETHER_BRICK_SLAB),
                new FakeBlock(getLocation(19), XMaterial.NETHER_BRICK_SLAB),
                new FakeBlock(getLocation(20), XMaterial.NETHER_BRICK_SLAB)
        });

        // CORNER PILLARS 0
        setStepFakeBlocks(5, new FakeBlock[] {
                new FakeBlock(getLocation(21), XMaterial.OBSIDIAN),
                new FakeBlock(getLocation(22), XMaterial.OBSIDIAN),
                new FakeBlock(getLocation(23), XMaterial.OBSIDIAN),
                new FakeBlock(getLocation(24), XMaterial.OBSIDIAN)
        });

        // CORNER PILLARS 1
        setStepFakeBlocks(6, new FakeBlock[] {
                new FakeBlock(getLocation(25), XMaterial.NETHER_BRICK_SLAB),
                new FakeBlock(getLocation(26), XMaterial.NETHER_BRICK_SLAB),
                new FakeBlock(getLocation(27), XMaterial.NETHER_BRICK_SLAB),
                new FakeBlock(getLocation(28), XMaterial.NETHER_BRICK_SLAB)
        });

    }

}
