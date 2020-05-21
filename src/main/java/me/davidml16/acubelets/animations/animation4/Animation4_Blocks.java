package me.davidml16.acubelets.animations.animation4;

import me.davidml16.acubelets.utils.CuboidRegion;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animation4_Blocks extends BukkitRunnable {

    private final Location location;
    private int tick;

    private final List<BlockState> blockStates;
    private final List<BlockState> blockStatesCopy;

    private Random random = new Random();

    public Animation4_Blocks(Location location) {
        this.location = location;
        this.tick = 0;

        this.blockStates = new ArrayList<>();
        this.blockStatesCopy = new ArrayList<>();

        CuboidRegion cr = new CuboidRegion(this.location.clone().add(-1, -1, -1), this.location.clone().add(1, -1, 1));
        for(Block block : cr.getSideBlocks()) blockStates.add(block.getState());
        for(Block block : cr.getSideBlocks()) blockStatesCopy.add(block.getState());

        blockStates.remove(this.location.clone().add(0, -1, 0).getBlock().getState());
        blockStatesCopy.remove(this.location.clone().add(0, -1, 0).getBlock().getState());
    }

    public void run() {
        if(blockStatesCopy.size() > 0) {
            BlockState randomState = blockStatesCopy.get(random.nextInt(blockStatesCopy.size()));
            randomState.setType(Material.NETHERRACK);
            randomState.update(true);
            blockStatesCopy.remove(randomState);
        } else {
            cancel();
        }
    }

    public void restore() {
        for(BlockState state : blockStates) state.update(true);
    }

}
