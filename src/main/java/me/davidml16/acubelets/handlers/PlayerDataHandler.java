package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.data.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataHandler {

	public HashMap<UUID, Profile> data = new HashMap<UUID, Profile>();

	private Main main;
	public PlayerDataHandler(Main main) {
		this.main = main;
	}

	public HashMap<UUID, Profile> getPlayersData() {
		return data;
	}

	public Profile getData(Player p) {
		if (data.containsKey(p.getUniqueId()))
			return data.get(p.getUniqueId());
		return null;
	}

	public Profile getData(UUID uuid) {
		if (data.containsKey(uuid))
			return data.get(uuid);
		return null;
	}

	public boolean playerExists(Player p) {
		return data.containsKey(p.getUniqueId());
	}

	public void loadPlayerData(Player p) {
		Profile profile = new Profile(main, p.getUniqueId());
		data.put(p.getUniqueId(), profile);

		main.getDatabaseHandler().getCubelets(p.getUniqueId()).thenAccept(profile::setCubelets);
	}

	public void loadAllPlayerData() {
		data.clear();
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			loadPlayerData(p);
		}
	}

}
