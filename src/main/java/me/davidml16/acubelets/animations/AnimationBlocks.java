package me.davidml16.acubelets.animations;

import me.davidml16.acubelets.utils.Cuboid;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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

                this.boxLocation.clone().add(0, -1, 0), this.boxLocation.clone().add(1, -1, 0), this.boxLocation.clone().add(-1, -1, 0),
                this.boxLocation.clone().add(0, -1, 1), this.boxLocation.clone().add(0, -1, -1),

                this.boxLocation.clone().add(1, -1, 1), this.boxLocation.clone().add(1, -1, -1), this.boxLocation.clone().add(-1, -1, 1),
                this.boxLocation.clone().add(-1, -1, -1),

                this.boxLocation.clone().add(2, -1, 0), this.boxLocation.clone().add(-2, -1, 0), this.boxLocation.clone().add(0, -1, 2),
                this.boxLocation.clone().add(0, -1,-2),

                this.boxLocation.clone().add(2, -1, 1), this.boxLocation.clone().add(2, -1, -1), this.boxLocation.clone().add(-2, -1, 1),
                this.boxLocation.clone().add(-2, -1, -1), this.boxLocation.clone().add(1, -1, 2), this.boxLocation.clone().add(-1, -1, 2),
                this.boxLocation.clone().add(1, -1, -2), this.boxLocation.clone().add(-1, -1, -2),

                this.boxLocation.clone().add(2, -1, 2), this.boxLocation.clone().add(2, -1, -2), this.boxLocation.clone().add(-2, -1, 2),
                this.boxLocation.clone().add(-2, -1, -2),

                this.boxLocation.clone().add(2, 0, 2), this.boxLocation.clone().add(2, 0, -2),
                this.boxLocation.clone().add(-2, 0, 2), this.boxLocation.clone().add(-2, 0, -2),

                this.boxLocation.clone().add(2, 1, 2), this.boxLocation.clone().add(2, 1, -2),
                this.boxLocation.clone().add(-2, 1, 2), this.boxLocation.clone().add(-2, 1, -2)

        };

    }

    @Override
    public void run() {

        int actualStep = step / 6;

        FakeBlock[] fakeBlocks = getStepFakeBlocks(actualStep);

        for(Player player : Bukkit.getOnlinePlayers()) {

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

        if(XMaterial.supports(13)) {

            BlockData blockData = Bukkit.createBlockData(fakeBlock.getXMaterial().parseMaterial());

            if(fakeBlock.hasBlockFace())
                ((Directional) blockData).setFacing(fakeBlock.getBlockFace());

            player.sendBlockChange(fakeBlock.getLocation(), blockData);

        } else {

            Material finalMaterial;
            byte finalData;

            if(fakeBlock.getAltXMaterial() == null && fakeBlock.getAltMaterial() == null) {
                finalMaterial = fakeBlock.getXMaterial().parseMaterial();
                finalData = fakeBlock.getXMaterial().getData();
            } else if(fakeBlock.getAltXMaterial() != null && fakeBlock.getAltMaterial() == null) {
                finalMaterial = fakeBlock.getAltXMaterial().parseMaterial();
                finalData = fakeBlock.getAltXMaterial().getData();
            } else {
                finalMaterial = fakeBlock.getAltMaterial();
                finalData = fakeBlock.getAltXMaterial().getData();
            }

            if(!fakeBlock.hasBlockFace()) {

                player.sendBlockChange(fakeBlock.getLocation(), finalMaterial, finalData);

            } else {

                byte data = 0;

                BlockFace facing = fakeBlock.getBlockFace();

                if(facing == BlockFace.WEST){
                    data = 0x1;
                } else if(facing == BlockFace.EAST){
                    data = 0x0;
                } else if(facing == BlockFace.NORTH){
                    data = 0x3;
                } else if(facing == BlockFace.SOUTH){
                    data = 0x2;
                }

                player.sendBlockChange(fakeBlock.getLocation(), finalMaterial, data);

            }

        }

    }



}
