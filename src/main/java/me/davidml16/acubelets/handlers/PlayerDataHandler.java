package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.CubeletReceivedEvent;
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

		try {
			if(!main.getDatabaseHandler().hasName(p.getName())) {
				main.getDatabaseHandler().createPlayerData(p);
				profile.setOrderBy("date");
				profile.setLootPoints(0);
			} else {
				main.getDatabaseHandler().updatePlayerName(p);
				profile.setOrderBy(main.getDatabaseHandler().getPlayerOrderSetting(p.getUniqueId()));
				profile.setLootPoints(main.getDatabaseHandler().getPlayerLootPoints(p.getUniqueId()));
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
			Bukkit.getScheduler().runTaskLater(main, () -> main.getHologramHandler().loadHolograms(p), 2L);
		});

		data.put(p.getUniqueId(), profile);
	}

	public void loadAllPlayerData() {
		data.clear();
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			loadPlayerData(p);
		}
	}

	public void givePoints(String player, int amount) throws SQLException {
		UUID uuid;

		if(Bukkit.getPlayer(player) != null)
			uuid = Bukkit.getPlayer(player).getUniqueId();
		else
			uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));

		givePoints(uuid, amount);
	}

	public void givePoints(UUID uuid, int amount) throws SQLException {
		if(Bukkit.getPlayer(uuid) != null)
			data.get(uuid).setLootPoints(data.get(uuid).getLootPoints() + amount);
		else {
			long actualBalance = main.getDatabaseHandler().getPlayerLootPoints(uuid);
			main.getDatabaseHandler().setPlayerLootPoints(uuid, actualBalance + amount);
		}

	}

	public void removePoints(String player, int amount) throws SQLException {
		UUID uuid;

		if(Bukkit.getPlayer(player) != null)
			uuid = Bukkit.getPlayer(player).getUniqueId();
		else
			uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));

		removePoints(uuid, amount);
	}

	public void removePoints(UUID uuid, int amount) throws SQLException {
		if(Bukkit.getPlayer(uuid) != null)
			if((data.get(uuid).getLootPoints() - amount) < 0)
				data.get(uuid).setLootPoints(0);
			else
				data.get(uuid).setLootPoints(data.get(uuid).getLootPoints() - amount);
		else {
			long actualBalance = main.getDatabaseHandler().getPlayerLootPoints(uuid);

			if((actualBalance - amount) < 0)
				main.getDatabaseHandler().setPlayerLootPoints(uuid, 0);
			else
				main.getDatabaseHandler().setPlayerLootPoints(uuid, actualBalance - amount);
		}
	}

	public void setPoints(String player, int amount) throws SQLException {
		UUID uuid;

		if(Bukkit.getPlayer(player) != null)
			uuid = Bukkit.getPlayer(player).getUniqueId();
		else
			uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));

		setPoints(uuid, amount);
	}

	public void setPoints(UUID uuid, int amount) throws SQLException {
		if(Bukkit.getPlayer(uuid) != null)
			data.get(uuid).setLootPoints(amount);
		else {
			main.getDatabaseHandler().setPlayerLootPoints(uuid, amount);
		}
	}

}
