package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.CubeletReceivedEvent;
import me.davidml16.acubelets.database.types.Database;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
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

		try {
			if(!main.getDatabaseHandler().hasName(p.getName())) {
				main.getDatabaseHandler().createPlayerData(p);
				profile.setOrderBy("date");
				profile.setLootPoints(0);
				profile.setAnimation("animation2");
			} else {
				main.getDatabaseHandler().updatePlayerName(p);

				main.getDatabaseHandler().getPlayerOrderSetting(p.getUniqueId(), profile::setOrderBy);
				main.getDatabaseHandler().getPlayerLootPoints(p.getUniqueId(), profile::setLootPoints);
				main.getDatabaseHandler().getPlayerAnimation(p.getUniqueId(), profile::setAnimation);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		try {
			main.getDatabaseHandler().removeExpiredCubelets(p.getUniqueId());
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		main.getDatabaseHandler().getCubelets(p.getUniqueId()).thenAccept(cubelets -> {
			profile.setCubelets(cubelets);
			Bukkit.getScheduler().runTaskLater(main, () -> main.getHologramHandler().reloadHolograms(p), 2L);
		});
	}

	public void loadAllPlayerData() {
		data.clear();
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			loadPlayerData(p);
		}
	}

	public void saveAllPlayerDataAsync() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			main.getDatabaseHandler().saveProfileAsync(getData(p));
		}
	}

	public void saveAllPlayerDataSync() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			main.getDatabaseHandler().saveProfileSync(getData(p));
		}
	}

}
