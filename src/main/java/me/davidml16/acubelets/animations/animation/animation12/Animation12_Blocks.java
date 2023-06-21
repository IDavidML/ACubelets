package me.davidml16.acubelets.animations.animation.animation12;

import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.animations.AnimationBlocks;
import me.davidml16.acubelets.animations.FakeBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Animation12_Blocks extends AnimationBlocks {

    public Animation12_Blocks(Location location) {

        super(location);

        // CAULDRON
        setStepFakeBlocks(1, new FakeBlock[] {
                new FakeBlock(location.clone().add(0, 0, 0), XMaterial.CAULDRON, XMaterial.CAULDRON, Material.matchMaterial("CAULDRON"))
        });

        // FIRST CROSS
        setStepFakeBlocks(2, new FakeBlock[] {
                new FakeBlock(getLocation(0), XMaterial.SPRUCE_PLANKS),
                new FakeBlock(getLocation(1), XMaterial.SPRUCE_PLANKS),
                new FakeBlock(getLocation(2), XMaterial.SPRUCE_PLANKS),
                new FakeBlock(getLocation(3), XMaterial.SPRUCE_PLANKS),
                new FakeBlock(getLocation(4), XMaterial.SPRUCE_PLANKS)
        });

        // CORNERS
        setStepFakeBlocks(3, new FakeBlock[] {
                new FakeBlock(getLocation(5), XMaterial.DARK_OAK_PLANKS),
                new FakeBlock(getLocation(6), XMaterial.DARK_OAK_PLANKS),
                new FakeBlock(getLocation(7), XMaterial.DARK_OAK_PLANKS),
                new FakeBlock(getLocation(8), XMaterial.DARK_OAK_PLANKS)
        });

        // STAIRS
        setStepFakeBlocks(4, new FakeBlock[] {
                new FakeBlock(getLocation(9), XMaterial.DARK_OAK_STAIRS, BlockFace.WEST),
                new FakeBlock(getLocation(10), XMaterial.DARK_OAK_STAIRS, BlockFace.EAST),
                new FakeBlock(getLocation(11), XMaterial.DARK_OAK_STAIRS, BlockFace.NORTH),
                new FakeBlock(getLocation(12), XMaterial.DARK_OAK_STAIRS, BlockFace.SOUTH)
        });

        // SIDES OF STAIRS
        setStepFakeBlocks(5, new FakeBlock[] {
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
        setStepFakeBlocks(6, new FakeBlock[] {
                new FakeBlock(getLocation(21), XMaterial.SPRUCE_LOG),
                new FakeBlock(getLocation(22), XMaterial.SPRUCE_LOG),
                new FakeBlock(getLocation(23), XMaterial.SPRUCE_LOG),
                new FakeBlock(getLocation(24), XMaterial.SPRUCE_LOG)
        });

        // CORNER PILLARS 1
        setStepFakeBlocks(7, new FakeBlock[] {
                new FakeBlock(getLocation(25), XMaterial.DARK_OAK_SLAB),
                new FakeBlock(getLocation(26), XMaterial.DARK_OAK_SLAB),
                new FakeBlock(getLocation(27), XMaterial.DARK_OAK_SLAB),
                new FakeBlock(getLocation(28), XMaterial.DARK_OAK_SLAB)
        });

    }

}
