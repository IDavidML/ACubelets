package me.davidml16.acubelets.tasks;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.effects.MachineEffect;
import me.davidml16.acubelets.effects.MachineEffectModel;
import me.davidml16.acubelets.effects.SimpleParticle;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.CubeletMachine;
import me.davidml16.acubelets.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class MachineEffectsTask {

	private int id;

	private Main main;
	public MachineEffectsTask(Main main) {
		this.main = main;
	}

	class Task implements Runnable {
		@Override
		public void run() {

			Collection<CubeletMachine> machines = main.getCubeletBoxHandler().getBoxes().values();

			machines.forEach(machine -> {
				if (machine.getBlockEffectModel() == MachineEffectModel.NONE) return;
				if (machine.getState() != CubeletBoxState.EMPTY) return;

				SimpleParticle particle = machine.getBlockEffectParticle();
				MachineEffect effect = machine.getBlockEffectModel().getEffect();

				Location location = machine.getLocation().clone();

				World world = location.getWorld();
				int chunkX = location.getBlockX() >> 4;
				int chunkZ = location.getBlockZ() >> 4;
				if (world == null || !world.isChunkLoaded(chunkX, chunkZ)) return;

				Location center = LocationUtils.getCenter(location.clone());
				effect.step(center, particle);
			});

			Arrays.asList(MachineEffectModel.values()).forEach(model -> model.getEffect().addStep());

		}
	}
	
	public int getId() { return id; }

	public void start() {
		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 20, 1);
	}
	
	public void stop() {
		Bukkit.getServer().getScheduler().cancelTask(id);
	}
	
}
