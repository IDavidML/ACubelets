package me.davidml16.acubelets.tasks;

import me.davidml16.acubelets.Main;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataCacheTask {

	private int id;

	private Main main;
	public DataCacheTask(Main main) {
		this.main = main;
	}

	class Task implements Runnable {
		@Override
		public void run() {

			if(main.getPlayerCount() < 0) return;

			long currentTime = System.currentTimeMillis();

			List<UUID> removedUUIDs = new ArrayList<>();

			for(UUID uuid : main.getPlayerDataHandler().getDisconnectCacheTime().keySet()) {

				long playerDisconnectTime = main.getPlayerDataHandler().getDisconnectTime(uuid);

				if((currentTime - playerDisconnectTime) < 60000) continue;

				removedUUIDs.add(uuid);

			}

			for(UUID uuid : removedUUIDs) {

				main.getPlayerDataHandler().getPlayersData().remove(uuid);
				main.getPlayerDataHandler().getDisconnectCacheTime().remove(uuid);

			}

		}
	}
	
	public int getId() { return id; }

	@SuppressWarnings("deprecation")
	public void start() {
		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 600, 600);
	}
	
	public void stop() {
		Bukkit.getServer().getScheduler().cancelTask(id);
	}
	
}
