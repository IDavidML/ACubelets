package me.davidml16.acubelets.tasks;

import me.davidml16.acubelets.Main;
import org.bukkit.Bukkit;

public class HologramTask {
	
	private int id;

	private Main main;
	public HologramTask(Main main) {
		this.main = main;
	}

	class Task implements Runnable {
		@Override
		public void run() {
			if(main.getPlayerCount() > 0) {
				main.getHologramImplementation().reloadHolograms();
			}
		}
	}
	
	public int getId() { return id; }

	@SuppressWarnings("deprecation")
	public void start() {
		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 20, main.getConfig().getInt("Holograms.UpdateInterval"));
	}
	
	public void stop() {
		Bukkit.getServer().getScheduler().cancelTask(id);
	}
	
}
