package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationHandler;
import me.davidml16.acubelets.objects.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataHandler {

	public HashMap<UUID, Profile> data = new HashMap<UUID, Profile>();

	public HashMap<UUID, Long> disconnectCacheTime = new HashMap<UUID, Long>();

	private Main main;
	public PlayerDataHandler(Main main) {
		this.main = main;
	}

	public HashMap<UUID, Profile> getPlayersData() {
		return data;
	}

	public HashMap<UUID, Long> getDisconnectCacheTime() {
		return disconnectCacheTime;
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

	public Long getDisconnectTime(UUID uuid) {
		if (disconnectCacheTime.containsKey(uuid))
			return disconnectCacheTime.get(uuid);
		return null;
	}

	public void addDisconnectTime(UUID uuid, Long time) {
		disconnectCacheTime.put(uuid, time);
	}

	public boolean playerExists(Player p) {
		return data.containsKey(p.getUniqueId());
	}

	public void loadPlayerData(Player p) {

		Profile profile = new Profile(main, p.getUniqueId());
		data.put(p.getUniqueId(), profile);

		try {

			main.getDatabaseHandler().hasName(p.getName(), name -> {

				if(name == null) {

					main.getDatabaseHandler().createPlayerData(p);
					profile.setOrderBy("date");
					profile.setLootPoints(0);
					profile.setAnimation("animation2");

				} else {

					main.getDatabaseHandler().updatePlayerName(p);

					main.getDatabaseHandler().getPlayerOrderSetting(p.getUniqueId(), profile::setOrderBy);
					main.getDatabaseHandler().getPlayerLootPoints(p.getUniqueId(), profile::setLootPoints);
					main.getDatabaseHandler().getPlayerAnimation(p.getUniqueId(), animation -> {
						if(animation.contains("animation"))
							profile.setAnimation(animation);
						else
							profile.setAnimation(AnimationHandler.DEFAULT_ANIMATION);
					});

				}

				main.getDatabaseHandler().removeExpiredCubelets(p.getUniqueId());

				main.getDatabaseHandler().getCubelets(p.getUniqueId()).thenAccept(cubelets -> {

					profile.setCubelets(cubelets);

					Bukkit.getScheduler().runTaskLater(main, () -> main.getHologramImplementation().reloadHolograms(p), 2L);

					if(main.isLoginReminder()) {

						Bukkit.getScheduler().runTaskLater(main, () -> {
							if(cubelets.size() > 0) {
								for (String line : main.getLanguageHandler().getMessageList("Cubelet.LoginReminder")) {
									line = line.replaceAll("%amount%", Integer.toString(cubelets.size()));
									line = line.replaceAll("%player%", p.getName());
									p.sendMessage(line);
								}
							}
						}, 20L);

					}

				});

				main.getDatabaseHandler().getLootHistory(p.getUniqueId()).thenAccept(lootHistory -> profile.setLootHistory(lootHistory));

			});

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

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
