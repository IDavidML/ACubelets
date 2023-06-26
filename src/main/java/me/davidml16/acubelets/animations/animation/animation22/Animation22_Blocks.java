package me.davidml16.acubelets.animations.animation.animation22;

import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.animations.AnimationBlocks;
import me.davidml16.acubelets.animations.FakeBlock;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class Animation22_Blocks extends AnimationBlocks {

    public Animation22_Blocks(Location location) {

        super(location);

        // MACHINE
        setStepFakeBlocks(1, new FakeBlock[] {
                new FakeBlock(location.clone(), XMaterial.END_PORTAL_FRAME)
        });

        // FIRST CROSS
        setStepFakeBlocks(2, new FakeBlock[] {
                new FakeBlock(getLocation(0), XMaterial.SEA_LANTERN),
                new FakeBlock(getLocation(1), XMaterial.PURPUR_PILLAR),
                new FakeBlock(getLocation(2), XMaterial.PURPUR_PILLAR),
                new FakeBlock(getLocation(3), XMaterial.PURPUR_PILLAR),
                new FakeBlock(getLocation(4), XMaterial.PURPUR_PILLAR)
        });

        // CORNERS
        setStepFakeBlocks(3, new FakeBlock[] {
                new FakeBlock(getLocation(5), XMaterial.PURPUR_BLOCK),
                new FakeBlock(getLocation(6), XMaterial.PURPUR_BLOCK),
                new FakeBlock(getLocation(7), XMaterial.PURPUR_BLOCK),
                new FakeBlock(getLocation(8), XMaterial.PURPUR_BLOCK)
        });

        // STAIRS
        setStepFakeBlocks(4, new FakeBlock[] {
                new FakeBlock(getLocation(9), XMaterial.PURPUR_STAIRS, BlockFace.WEST),
                new FakeBlock(getLocation(10), XMaterial.PURPUR_STAIRS, BlockFace.EAST),
                new FakeBlock(getLocation(11), XMaterial.PURPUR_STAIRS, BlockFace.NORTH),
                new FakeBlock(getLocation(12), XMaterial.PURPUR_STAIRS, BlockFace.SOUTH)
        });

        // SIDES OF STAIRS
        setStepFakeBlocks(5, new FakeBlock[] {
                new FakeBlock(getLocation(13), XMaterial.PURPUR_SLAB),
                new FakeBlock(getLocation(14), XMaterial.PURPUR_SLAB),
                new FakeBlock(getLocation(15), XMaterial.PURPUR_SLAB),
                new FakeBlock(getLocation(16), XMaterial.PURPUR_SLAB),
                new FakeBlock(getLocation(17), XMaterial.PURPUR_SLAB),
                new FakeBlock(getLocation(18), XMaterial.PURPUR_SLAB),
                new FakeBlock(getLocation(19), XMaterial.PURPUR_SLAB),
                new FakeBlock(getLocation(20), XMaterial.PURPUR_SLAB)
        });

        // CORNER PILLARS 0
        setStepFakeBlocks(6, new FakeBlock[] {
                new FakeBlock(getLocation(21), XMaterial.PURPUR_PILLAR),
                new FakeBlock(getLocation(22), XMaterial.PURPUR_PILLAR),
                new FakeBlock(getLocation(23), XMaterial.PURPUR_PILLAR),
                new FakeBlock(getLocation(24), XMaterial.PURPUR_PILLAR)
        });

        // CORNER PILLARS 1
        setStepFakeBlocks(7, new FakeBlock[] {
                new FakeBlock(getLocation(25), XMaterial.PURPUR_SLAB),
                new FakeBlock(getLocation(26), XMaterial.PURPUR_SLAB),
                new FakeBlock(getLocation(27), XMaterial.PURPUR_SLAB),
                new FakeBlock(getLocation(28), XMaterial.PURPUR_SLAB)
        });

    }

}
