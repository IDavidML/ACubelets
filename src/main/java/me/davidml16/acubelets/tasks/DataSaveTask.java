package me.davidml16.acubelets.tasks;

import me.davidml16.acubelets.Main;
import org.bukkit.Bukkit;

public class DataSaveTask {

	private int id;

	private Main main;
	public DataSaveTask(Main main) {
		this.main = main;
	}

	class Task implements Runnable {
		@Override
		public void run() {
			if(main.getPlayerCount() > 0)
				main.getPlayerDataHandler().saveAllPlayerDataAsync();
		}
	}
	
	public int getId() { return id; }

	@SuppressWarnings("deprecation")
	public void start() {
		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 6000, 6000);
	}
	
	public void stop() {
		Bukkit.getServer().getScheduler().cancelTask(id);
	}
	
}
