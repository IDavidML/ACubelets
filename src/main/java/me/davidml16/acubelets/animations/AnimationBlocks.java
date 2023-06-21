package me.davidml16.acubelets.animations;

import me.davidml16.acubelets.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public abstract class AnimationBlocks extends BukkitRunnable {

    private Set<BlockState> blockStates;
    private Location[] locations;

    private Map<Integer, FakeBlock[]> fakeBlocks;

    private Location boxLocation;

    private int step;

    public AnimationBlocks(Location boxLocation) {

        this.boxLocation = boxLocation;

        this.step = 1;

        this.blockStates = new HashSet<>();

        Cuboid cuboid = new Cuboid(boxLocation.clone().add(2, -1, 2), boxLocation.clone().add(-2, 3, -2));
        for (Block cuboidBlock : cuboid) {
            addBlockState(cuboidBlock.getState());
        }

        this.fakeBlocks = new HashMap<>();

        this.locations = new Location[] {

                this.boxLocation.clone().add(0, -1, 0), // 0 - CENTER

                this.boxLocation.clone().add(1, -1, 0), // 1 - LEFT CROSS
                this.boxLocation.clone().add(-1, -1, 0), // 2 - RIGHT CROSS
                this.boxLocation.clone().add(0, -1, 1), // 3 - UP CROSS
                this.boxLocation.clone().add(0, -1, -1), // 4 - DOWN CROSS

                this.boxLocation.clone().add(1, -1, 1), // 5 - UP LEFT BOX CORNER
                this.boxLocation.clone().add(1, -1, -1), // 6 - UP RIGHT BOX CORNER
                this.boxLocation.clone().add(-1, -1, 1), // 7 - DOWN LEFT BOX CORNER
                this.boxLocation.clone().add(-1, -1, -1), // 8 - DOWN LEFT BOX CORNER

                this.boxLocation.clone().add(2, -1, 0), // 9 - LEFT STAIR
                this.boxLocation.clone().add(-2, -1, 0), // 10 - RIGHT STAIR
                this.boxLocation.clone().add(0, -1, 2), // 11 - UP STAIR
                this.boxLocation.clone().add(0, -1,-2), // 12 - DOWN STAIR

                this.boxLocation.clone().add(2, -1, 1), // 13 - LEFT UP SLAB
                this.boxLocation.clone().add(2, -1, -1), // 14 - LEFT DOWN SLAB
                this.boxLocation.clone().add(-2, -1, 1), // 15 - RIGHT UP SLAB
                this.boxLocation.clone().add(-2, -1, -1), // 16 - RIGHT DOWN SLAB
                this.boxLocation.clone().add(1, -1, 2), // 17 - UP LEFT SLAB
                this.boxLocation.clone().add(-1, -1, 2), // 18 - UP RIGHT SLAB
                this.boxLocation.clone().add(1, -1, -2), // 19 - DOWN LEFT SLAB
                this.boxLocation.clone().add(-1, -1, -2), // 20 - DOWN RIGHT SLAB

                this.boxLocation.clone().add(2, -1, 2), // 21 - UP LEFT CORNER LEVEL 0
                this.boxLocation.clone().add(2, -1, -2), // 22 - UP RIGHT CORNER LEVEL 0
                this.boxLocation.clone().add(-2, -1, 2), // 23 - DOWN LEFT CORNER LEVEL 0
                this.boxLocation.clone().add(-2, -1, -2), // 24 - DOWN RIGHT CORNER LEVEL 0

                this.boxLocation.clone().add(2, 0, 2), // 25 - UP LEFT CORNER LEVEL 1
                this.boxLocation.clone().add(2, 0, -2), // 26 - UP RIGHT CORNER LEVEL 1
                this.boxLocation.clone().add(-2, 0, 2), // 27 - DOWN LEFT CORNER LEVEL 1
                this.boxLocation.clone().add(-2, 0, -2), // 28 - DOWN RIGHT CORNER LEVEL 1

                this.boxLocation.clone().add(2, 1, 2), // 29 - UP LEFT CORNER LEVEL 2
                this.boxLocation.clone().add(2, 1, -2), // 30 - UP RIGHT CORNER LEVEL 2
                this.boxLocation.clone().add(-2, 1, 2), // 31 - DOWN LEFT CORNER LEVEL 2
                this.boxLocation.clone().add(-2, 1, -2) // 32 - DOWN RIGHT CORNER LEVEL 2

        };

    }

    @Override
    public void run() {

        int actualStep = step / 6;

        FakeBlock[] fakeBlocks = getStepFakeBlocks(actualStep);

        if(fakeBlocks.length == 0) {
            step++;
            return;
        }

        for(Player player : Bukkit.getOnlinePlayers()) {

            if(!player.getLocation().getWorld().getName().equalsIgnoreCase(fakeBlocks[0].getLocation().getWorld().getName())) continue;

            for(FakeBlock fakeBlock : fakeBlocks)
                sendBlock(player, fakeBlock);

        }

        step++;

    }

    public void addBlockState(BlockState blockState) {

        blockStates.add(blockState);

    }

    public Location getLocation(int index) {

        if(locations.length < index)
            return null;

        return locations[index];

    }

    public Map<Integer, FakeBlock[]> getFakeBlocks() {
        return fakeBlocks;
    }

    public void setFakeBlocks(Map<Integer, FakeBlock[]> fakeBlocks) {
        this.fakeBlocks = fakeBlocks;
    }

    public FakeBlock[] getStepFakeBlocks(int step) {

        List<FakeBlock> list = new ArrayList<>();

        if(step > fakeBlocks.keySet().size())
            step = fakeBlocks.keySet().size() + 1;

        for(int i = 1; i <= step; i++) {

            if (!fakeBlocks.containsKey(i))
                continue;

            list.addAll(Arrays.asList(fakeBlocks.get(i)));

        }

        FakeBlock[] fakeBlocks = new FakeBlock[list.size()];
        list.toArray(fakeBlocks);

        return fakeBlocks;

    }

    public void setStepFakeBlocks(int step, FakeBlock[] fakeBlocks) {

        this.fakeBlocks.put(step, fakeBlocks);

    }

    public void restore() {

        for(BlockState state : blockStates) state.update(true);

        blockStates = null;
        locations = null;
        fakeBlocks = null;
        boxLocation = null;

    }

    public void sendBlock(Player player, FakeBlock fakeBlock) {

        if(fakeBlock.getLocation() == null)
            return;

        BlockData blockData = Bukkit.createBlockData(fakeBlock.getXMaterial().parseMaterial());

        if(fakeBlock.hasBlockFace())
            ((Directional) blockData).setFacing(fakeBlock.getBlockFace());

        player.sendBlockChange(fakeBlock.getLocation(), blockData);

    }



}
