package me.davidml16.acubelets.animations.animation.animation21;

import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.animations.AnimationBlocks;
import me.davidml16.acubelets.animations.FakeBlock;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class Animation21_Blocks extends AnimationBlocks {

    public Animation21_Blocks(Location location) {

        super(location);

        // FIRST CROSS
        setStepFakeBlocks(1, new FakeBlock[] {
                new FakeBlock(getLocation(0), XMaterial.BLACK_CONCRETE),

                new FakeBlock(getLocation(1), XMaterial.BLACK_CONCRETE),
                new FakeBlock(getLocation(2), XMaterial.WHITE_CONCRETE),
                new FakeBlock(getLocation(3), XMaterial.BLACK_CONCRETE),
                new FakeBlock(getLocation(4), XMaterial.WHITE_CONCRETE)
        });

        // CORNERS
        setStepFakeBlocks(2, new FakeBlock[] {
                new FakeBlock(getLocation(5), XMaterial.WHITE_CONCRETE),
                new FakeBlock(getLocation(6), XMaterial.BLACK_CONCRETE),
                new FakeBlock(getLocation(7), XMaterial.WHITE_CONCRETE),
                new FakeBlock(getLocation(8), XMaterial.BLACK_CONCRETE)
        });

        // STAIRS
        setStepFakeBlocks(3, new FakeBlock[] {
                new FakeBlock(getLocation(9), XMaterial.BLACK_CONCRETE),
                new FakeBlock(getLocation(10), XMaterial.WHITE_CONCRETE),
                new FakeBlock(getLocation(11), XMaterial.BLACK_CONCRETE),
                new FakeBlock(getLocation(12), XMaterial.WHITE_CONCRETE)
        });

        // SIDES OF STAIRS
        setStepFakeBlocks(4, new FakeBlock[] {
                new FakeBlock(getLocation(13), XMaterial.BLACK_CONCRETE),
                new FakeBlock(getLocation(14), XMaterial.BLACK_CONCRETE),
                new FakeBlock(getLocation(15), XMaterial.WHITE_CONCRETE),
                new FakeBlock(getLocation(16), XMaterial.WHITE_CONCRETE),
                new FakeBlock(getLocation(17), XMaterial.BLACK_CONCRETE),
                new FakeBlock(getLocation(18), XMaterial.WHITE_CONCRETE),
                new FakeBlock(getLocation(19), XMaterial.BLACK_CONCRETE),
                new FakeBlock(getLocation(20), XMaterial.WHITE_CONCRETE)
        });

        // CORNER PILLARS 0
        setStepFakeBlocks(5, new FakeBlock[] {
                new FakeBlock(getLocation(21), XMaterial.QUARTZ_PILLAR),
                new FakeBlock(getLocation(22), XMaterial.QUARTZ_PILLAR),
                new FakeBlock(getLocation(23), XMaterial.QUARTZ_PILLAR),
                new FakeBlock(getLocation(24), XMaterial.QUARTZ_PILLAR)
        });

        // CORNER PILLARS 1
        setStepFakeBlocks(6, new FakeBlock[] {
                new FakeBlock(getLocation(25), XMaterial.QUARTZ_SLAB),
                new FakeBlock(getLocation(26), XMaterial.QUARTZ_SLAB),
                new FakeBlock(getLocation(27), XMaterial.QUARTZ_SLAB),
                new FakeBlock(getLocation(28), XMaterial.QUARTZ_SLAB)
        });

    }

}
