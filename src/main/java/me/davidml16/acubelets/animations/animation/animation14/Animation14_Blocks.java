package me.davidml16.acubelets.animations.animation.animation14;

import me.davidml16.acubelets.animations.AnimationBlocks;
import me.davidml16.acubelets.animations.FakeBlock;
import me.davidml16.acubelets.utils.CuboidRegion;


import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class Animation14_Blocks extends AnimationBlocks {

    public Animation14_Blocks(Location location) {

        super(location);

        // FIRST CROSS
        setStepFakeBlocks(1, new FakeBlock[] {
                new FakeBlock(getLocation(0), XMaterial.GRASS_BLOCK),
                new FakeBlock(getLocation(1), XMaterial.GRASS_BLOCK),
                new FakeBlock(getLocation(2), XMaterial.GRASS_BLOCK),
                new FakeBlock(getLocation(3), XMaterial.GRASS_BLOCK),
                new FakeBlock(getLocation(4), XMaterial.GRASS_BLOCK)
        });

        // CORNERS
        setStepFakeBlocks(2, new FakeBlock[] {
                new FakeBlock(getLocation(5), XMaterial.GRASS_BLOCK),
                new FakeBlock(getLocation(6), XMaterial.GRASS_BLOCK),
                new FakeBlock(getLocation(7), XMaterial.GRASS_BLOCK),
                new FakeBlock(getLocation(8), XMaterial.GRASS_BLOCK)
        });

        // STAIRS
        setStepFakeBlocks(3, new FakeBlock[] {
                new FakeBlock(getLocation(9), XMaterial.OAK_STAIRS, BlockFace.WEST),
                new FakeBlock(getLocation(10), XMaterial.OAK_STAIRS, BlockFace.EAST),
                new FakeBlock(getLocation(11), XMaterial.OAK_STAIRS, BlockFace.NORTH),
                new FakeBlock(getLocation(12), XMaterial.OAK_STAIRS, BlockFace.SOUTH)
        });

        // SIDES OF STAIRS
        setStepFakeBlocks(4, new FakeBlock[] {
                new FakeBlock(getLocation(13), XMaterial.OAK_SLAB),
                new FakeBlock(getLocation(14), XMaterial.OAK_SLAB),
                new FakeBlock(getLocation(15), XMaterial.OAK_SLAB),
                new FakeBlock(getLocation(16), XMaterial.OAK_SLAB),
                new FakeBlock(getLocation(17), XMaterial.OAK_SLAB),
                new FakeBlock(getLocation(18), XMaterial.OAK_SLAB),
                new FakeBlock(getLocation(19), XMaterial.OAK_SLAB),
                new FakeBlock(getLocation(20), XMaterial.OAK_SLAB)
        });

        // CORNER PILLARS 0
        setStepFakeBlocks(5, new FakeBlock[] {
                new FakeBlock(getLocation(21), XMaterial.OAK_LOG),
                new FakeBlock(getLocation(22), XMaterial.OAK_LOG),
                new FakeBlock(getLocation(23), XMaterial.OAK_LOG),
                new FakeBlock(getLocation(24), XMaterial.OAK_LOG)
        });

        // CORNER PILLARS 1
        setStepFakeBlocks(6, new FakeBlock[] {
                new FakeBlock(getLocation(25), XMaterial.OAK_SLAB),
                new FakeBlock(getLocation(26), XMaterial.OAK_SLAB),
                new FakeBlock(getLocation(27), XMaterial.OAK_SLAB),
                new FakeBlock(getLocation(28), XMaterial.OAK_SLAB)
        });

        // FLOWERS 1
        setStepFakeBlocks(7, new FakeBlock[] {
                new FakeBlock(location.clone().add(1, 0, 1), XMaterial.POPPY),
                new FakeBlock(location.clone().add(1, 0, 0), XMaterial.GRASS, XMaterial.GRASS, Material.matchMaterial("LONG_GRASS"))
        });

        // FLOWERS 2
        setStepFakeBlocks(8, new FakeBlock[] {
                new FakeBlock(location.clone().add(1, 0, -1), XMaterial.DANDELION),
                new FakeBlock(location.clone().add(0, 0, 1), XMaterial.GRASS, XMaterial.GRASS, Material.matchMaterial("LONG_GRASS"))
        });

        // FLOWERS 3
        setStepFakeBlocks(9, new FakeBlock[] {
                new FakeBlock(location.clone().add(-1, 0, -1), XMaterial.POPPY),
                new FakeBlock(location.clone().add(0, 0, -1), XMaterial.GRASS, XMaterial.GRASS, Material.matchMaterial("LONG_GRASS"))
        });

        // FLOWERS 4
        setStepFakeBlocks(10, new FakeBlock[] {
                new FakeBlock(location.clone().add(-1, 0, 1), XMaterial.POPPY),
                new FakeBlock(location.clone().add(-1, 0, 0), XMaterial.GRASS, XMaterial.GRASS, Material.matchMaterial("LONG_GRASS"))
        });

    }

}
