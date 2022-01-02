package me.davidml16.acubelets.database;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.database.types.DatabaseConnection;
import me.davidml16.acubelets.database.types.MySqlConnection;
import me.davidml16.acubelets.database.types.SQLiteConnection;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.objects.loothistory.LootHistory;
import me.davidml16.acubelets.objects.loothistory.RewardHistory;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.XSeries.XItemStack;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DatabaseHandler {

	public interface Callback<T> {

		void done(T valor);

	}

	private DatabaseConnection databaseConnection;

	private Main main;

	public DatabaseHandler(Main main) {
		this.main = main;
		if(main.getConfig().getBoolean("MySQL.Enabled")) {
			databaseConnection = new MySqlConnection(main);
		} else {
			databaseConnection = new SQLiteConnection(main);
		}
	}

	public void openConnection() {
		Main.log.sendMessage(Utils.translate("  "));
		Main.log.sendMessage(Utils.translate("  &eLoading database:"));
		databaseConnection.open();
	}

	public void changeToSQLite() {
		databaseConnection = new SQLiteConnection(main);
		databaseConnection.open();
	}

	public DatabaseConnection getDatabaseConnection() { return databaseConnection; }

	public void executeQuery(String sql) {

		PreparedStatement statement = null;
		Connection connection = null;

		try {
			connection = databaseConnection.getConnection();
			statement = connection.prepareStatement(sql);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			databaseConnection.close(connection);
		}

	}

	public void executeQueryError(String sql) throws SQLException {

		PreparedStatement statement = null;
		Connection connection = null;

		try {
			connection = databaseConnection.getConnection();
			statement = connection.prepareStatement(sql);
			statement.execute();
		} finally {
			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			databaseConnection.close(connection);
		}

	}

	public void loadTables() {

		executeQuery("CREATE TABLE IF NOT EXISTS ac_cubelets (`UUID` varchar(40) NOT NULL, `cubeletUUID` varchar(40) NOT NULL, `type` VARCHAR(15) NOT NULL, `received` bigint NOT NULL DEFAULT 0, `expire` bigint NOT NULL DEFAULT 0, PRIMARY KEY (`UUID`, `cubeletUUID`));");

		executeQuery("CREATE TABLE IF NOT EXISTS ac_players (`UUID` varchar(40) NOT NULL, `NAME` varchar(40), `LOOT_POINTS` integer(25), `ORDER_BY` varchar(10), `ANIMATION` varchar(25), PRIMARY KEY (`UUID`));");

		try {
			executeQueryError("CREATE TABLE IF NOT EXISTS ac_loothistory (`ID` INTEGER PRIMARY KEY AUTO_INCREMENT, `UUID` varchar(40) NOT NULL, `cubeletName` varchar(50) NOT NULL, `rewardID` varchar(50) NOT NULL, `rewardName` varchar(20) NOT NULL, `rewardIcon` LONGTEXT NOT NULL, `received` bigint NOT NULL DEFAULT 0);");
		} catch (SQLException e) {
			try {
				executeQueryError("CREATE TABLE IF NOT EXISTS ac_loothistory (`ID` INTEGER PRIMARY KEY AUTOINCREMENT, `UUID` varchar(40) NOT NULL, `cubeletName` varchar(50) NOT NULL, `rewardID` varchar(50) NOT NULL, `rewardName` varchar(20) NOT NULL, `rewardIcon` LONGTEXT NOT NULL, `received` bigint NOT NULL DEFAULT 0);");
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}

	}

	public void hasName(String name, Callback<String> callback) throws SQLException {

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {

			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection connection = null;

			try {

				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("SELECT * FROM ac_players WHERE LOWER(NAME) = '" + name.toLowerCase() + "';");
				rs = ps.executeQuery();

				if (rs.next()) {
					callback.done(rs.getString("NAME"));
				} else {
					callback.done(null);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}

		});

	}

	public void createPlayerData(Player p) {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("INSERT INTO ac_players (UUID,NAME,LOOT_POINTS,ORDER_BY,ANIMATION) VALUES(?,?,?,?,?)");
				ps.setString(1, p.getUniqueId().toString());
				ps.setString(2, p.getName());
				ps.setLong(3, 0);
				ps.setString(4, "date");
				ps.setString(5, "animation2");
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void getPlayerAnimation(UUID uuid, Callback<String> callback) {
		Bukkit.getScheduler().runTaskAsynchronously(main, (Runnable) () -> {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("SELECT * FROM ac_players WHERE UUID = '" + uuid + "';");
				rs = ps.executeQuery();

				String animation = "animation2";

				if (rs.next()) {
					animation = rs.getString("ANIMATION");
				}

				String finalAnimation = animation;
				Bukkit.getScheduler().runTask(main, () -> callback.done(finalAnimation));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				if(rs != null) {
					try {
						rs.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void setPlayerAnimation(UUID uuid, String animation) {

		Bukkit.getScheduler().runTaskAsynchronously(main, (Runnable) () -> {
			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("UPDATE ac_players SET `ANIMATION` = ? WHERE `UUID` = ?");
				ps.setString(1, animation);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}

		});

	}

	public void getPlayerOrderSetting(UUID uuid, Callback<String> callback) {
		Bukkit.getScheduler().runTaskAsynchronously(main, (Runnable) () -> {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("SELECT * FROM ac_players WHERE UUID = '" + uuid + "';");
				rs = ps.executeQuery();

				String order = "date";

				if (rs.next()) {
					order =  rs.getString("ORDER_BY");
				}

				String finalOrder = order;
				Bukkit.getScheduler().runTask(main, () -> callback.done(finalOrder));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				if(rs != null) {
					try {
						rs.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void getPlayerLootPoints(UUID uuid, Callback<Long> callback) {
		Bukkit.getScheduler().runTaskAsynchronously(main, (Runnable) () -> {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("SELECT * FROM ac_players WHERE UUID = '" + uuid + "';");
				rs = ps.executeQuery();

				long amount = 0;

				if (rs.next()) {
					amount = rs.getLong("LOOT_POINTS");
				}

				long finalAmount = amount;
				Bukkit.getScheduler().runTask(main, () -> callback.done(finalAmount));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				if(rs != null) {
					try {
						rs.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void setPlayerOrderSetting(UUID uuid, String orderBy) {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("UPDATE ac_players SET `ORDER_BY` = ? WHERE `UUID` = ?");
				ps.setString(1, orderBy);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void setPlayerLootPoints(UUID uuid, long amount) {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("UPDATE ac_players SET `LOOT_POINTS` = ? WHERE `UUID` = ?");
				ps.setLong(1, amount);
				ps.setString(2, uuid.toString());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void saveProfileAsync(final Profile profile) {

		if(profile == null) return;

		String name = Bukkit.getPlayer(profile.getUuid()).getName();

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("UPDATE ac_players SET `NAME` = ?, `LOOT_POINTS` = ?, `ORDER_BY` = ?, `ANIMATION` = ? WHERE `UUID` = ?");
				ps.setString(1, name);
				ps.setLong(2, profile.getLootPoints());
				ps.setString(3, profile.getOrderBy());
				ps.setString(4, profile.getAnimation());
				ps.setString(5, profile.getUuid().toString());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void saveProfileSync(final Profile profile) {

		if(profile == null) return;

		String name = Bukkit.getPlayer(profile.getUuid()).getName();

		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = databaseConnection.getConnection();
			ps = connection.prepareStatement("UPDATE ac_players SET `NAME` = ?, `LOOT_POINTS` = ?, `ORDER_BY` = ?, `ANIMATION` = ? WHERE `UUID` = ?");
			ps.setString(1, name);
			ps.setLong(2, profile.getLootPoints());
			ps.setString(3, profile.getOrderBy());
			ps.setString(4, profile.getAnimation());
			ps.setString(5, profile.getUuid().toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
			}
			databaseConnection.close(connection);
		}
	}

	public void updatePlayerName(Player p) {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("UPDATE ac_players SET `NAME` = ? WHERE `UUID` = ?");
				ps.setString(1, p.getName());
				ps.setString(2, p.getUniqueId().toString());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void getPlayerUUID(String name, Callback<String> callback) throws SQLException {
		Bukkit.getScheduler().runTaskAsynchronously(main, (Runnable) () -> {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("SELECT * FROM ac_players WHERE LOWER(NAME) = '" + name.toLowerCase() + "';");
				rs = ps.executeQuery();

				if (rs.next()) {
					final String uuid = rs.getString("UUID");
					Bukkit.getScheduler().runTask(main, () -> callback.done(uuid));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void addCubelet(UUID uuid, String type, Long received, Long expire) {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("INSERT INTO ac_cubelets (UUID,cubeletUUID,type,received,expire) VALUES(?,?,?,?,?)");
				ps.setString(1, uuid.toString());
				ps.setString(2, UUID.randomUUID().toString());
				ps.setString(3, type);
				ps.setLong(4, received);
				ps.setLong(5, expire);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void addCubelet(UUID uuid, UUID cubeletUUID, String type, Long received, Long expire) {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("INSERT INTO ac_cubelets (UUID,cubeletUUID,type,received,expire) VALUES(?,?,?,?,?)");
				ps.setString(1, uuid.toString());
				ps.setString(2, cubeletUUID.toString());
				ps.setString(3, type);
				ps.setLong(4, received);
				ps.setLong(5, expire);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void addCubelets(UUID uuid, Collection<Cubelet> cubelets) {
		if(XMaterial.supports(11)) {
			Bukkit.getScheduler().runTaskAsynchronously(main, () -> {

				StringBuilder insertString = new StringBuilder();
				for(Cubelet cubelet : cubelets) {
					if(insertString.toString().equalsIgnoreCase("")) insertString.append("('").append(uuid.toString()).append("','").append(cubelet.getUuid()).append("','").append(cubelet.getType()).append("',").append(cubelet.getReceived()).append(",").append(cubelet.getExpire()).append(")");
					else insertString.append(", ('").append(uuid.toString()).append("','").append(cubelet.getUuid()).append("','").append(cubelet.getType()).append("',").append(cubelet.getReceived()).append(",").append(cubelet.getExpire()).append(")");
				}

				PreparedStatement ps = null;
				Connection connection = null;
				try {
					connection = databaseConnection.getConnection();
					ps = connection.prepareStatement("INSERT INTO ac_cubelets (UUID,cubeletUUID,type,received,expire) VALUES " + insertString.toString());
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException throwables) {
							throwables.printStackTrace();
						}
					}
					databaseConnection.close(connection);
				}
			});
		} else {
			for (Cubelet cubelet : cubelets) {
				addCubelet(uuid, cubelet.getUuid(), cubelet.getType(), cubelet.getReceived(), cubelet.getExpire());
			}
		}
	}

	public void removeCubelet(UUID uuid, UUID cubeletUUID) {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("DELETE FROM ac_cubelets WHERE UUID = '" + uuid + "' AND cubeletUUID = '" + cubeletUUID + "';");
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void removeCubelet(UUID uuid, String type, int amount) {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("DELETE FROM ac_cubelets WHERE rowid IN (SELECT rowid FROM ac_cubelets WHERE UUID = '" + uuid + "' AND type = '" + type + "' LIMIT " + amount + ");");
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void removeCubelets(UUID uuid, Collection<Cubelet> cubelets) {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {

			StringBuilder deleteString = new StringBuilder();
			for(Cubelet cubelet : cubelets) {
				if(deleteString.toString().equalsIgnoreCase("")) deleteString.append("'").append(cubelet.getUuid().toString()).append("'");
				else deleteString.append(",'").append(cubelet.getUuid().toString()).append("'");
			}

			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("DELETE FROM ac_cubelets WHERE UUID = '" + uuid + "' AND cubeletUUID IN (" + deleteString + ");");
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void removeCubelets(UUID uuid) {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("DELETE FROM ac_cubelets WHERE UUID = '" + uuid + "';");
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void removeCubelet(String type) {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("DELETE FROM ac_cubelets WHERE type = '" + type + "';");
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void removeExpiredCubelets(UUID uuid) {
		long actualTime = System.currentTimeMillis();
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			PreparedStatement ps = null;
			Connection connection = null;
			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("DELETE FROM ac_cubelets WHERE UUID = '" + uuid + "' AND expire < '" + actualTime + "';");
				ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}
		});
	}

	public void getCubeletBalance(UUID uuid, String cubeletType, Callback<Long> callback) {

		Bukkit.getScheduler().runTaskAsynchronously(main, (Runnable) () -> {

			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection connection = null;

			try {
				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("SELECT COUNT(*) AS amount FROM ac_cubelets WHERE UUID = '" + uuid.toString() + "' AND type = '" + cubeletType + "';");
				rs = ps.executeQuery();

				if (rs.next()) {
					final Long ammount = Long.valueOf(rs.getInt("amount"));
					Bukkit.getScheduler().runTask(main, () -> callback.done(ammount));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				databaseConnection.close(connection);
			}

		});

	}

	public CompletableFuture<List<Cubelet>> getCubelets(UUID uuid) {

		CompletableFuture<List<Cubelet>> result = new CompletableFuture<>();

		long actualTime = System.currentTimeMillis();

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {

			List<Cubelet> cubelets = new ArrayList<>();

			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection connection = null;

			try {

				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("SELECT * FROM ac_cubelets WHERE UUID = '" + uuid.toString() + "' AND expire > '" + actualTime + "';");

				rs = ps.executeQuery();
				while (rs.next()) {
					if(main.getCubeletTypesHandler().getTypes().containsKey(rs.getString("type")))
						cubelets.add(new Cubelet(UUID.fromString(rs.getString("cubeletUUID")), rs.getString("type"), rs.getLong("received"), rs.getLong("expire")));
				}

				result.complete(cubelets);

			} catch (SQLException e) {

				e.printStackTrace();

			} finally {

				if(ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}

				if(rs != null) {
					try {
						rs.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}

				databaseConnection.close(connection);

			}
		});

		return result;

	}

	public CompletableFuture<List<LootHistory>> getLootHistory(UUID uuid) {

		CompletableFuture<List<LootHistory>> result = new CompletableFuture<>();

		long actualTime = System.currentTimeMillis();

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {

			List<LootHistory> lootHistory = new ArrayList<>();

			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection connection = null;

			try {

				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("SELECT * FROM ac_loothistory WHERE UUID = '" + uuid.toString() + "';");

				rs = ps.executeQuery();

				while (rs.next()) {

					int id = rs.getInt("ID");
					String playerUUID = rs.getString("UUID");
					String cubeletName = rs.getString("cubeletName");

					long received = rs.getLong("received");

					String rewardID = rs.getString("rewardID");
					String rewardName = rs.getString("rewardName");
					String rewardIcon = rs.getString("rewardIcon");

					RewardHistory rewardHistory = new RewardHistory(UUID.fromString(rewardID), rewardName, rewardIcon);

					lootHistory.add(new LootHistory(id, UUID.fromString(playerUUID), cubeletName, received, rewardHistory));

				}

				result.complete(lootHistory);

			} catch (SQLException e) {

				e.printStackTrace();

			} finally {

				if(ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}

				if(rs != null) {
					try {
						rs.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}

				databaseConnection.close(connection);

			}

		});

		return result;

	}

	public void addLootHistory(UUID uuid, LootHistory lootHistory) {

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {

			PreparedStatement ps = null;
			Connection connection = null;

			try {

				String iconBase64 = XItemStack.itemStackToBase64(lootHistory.getRewardHistory().getItemStack());

				String value =
						"('"
						+ uuid.toString()
						+ "','"
						+ lootHistory.getCubeletName()
						+ "','"
						+ lootHistory.getRewardHistory().getUUID()
						+ "','"
						+ lootHistory.getRewardHistory().getName()
						+ "','"
						+ iconBase64
						+ "',"
						+ lootHistory.getReceived()
						+ ")";

				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("INSERT INTO ac_loothistory (UUID,cubeletName,rewardID,rewardName,rewardIcon,received) VALUES " + value);
				ps.executeUpdate();

			} catch (SQLException e) {

				e.printStackTrace();

			} finally {

				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}

				databaseConnection.close(connection);

			}

		});

	}

	public void removeLootHistory(UUID uuid) {

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {

			PreparedStatement ps = null;
			Connection connection = null;

			try {

				connection = databaseConnection.getConnection();
				ps = connection.prepareStatement("DELETE FROM ac_loothistory WHERE UUID = '" + uuid.toString() + "';");
				ps.execute();

			} catch (SQLException e) {

				e.printStackTrace();

			} finally {

				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException throwables) {
						throwables.printStackTrace();
					}
				}

				databaseConnection.close(connection);

			}

		});

	}

}
