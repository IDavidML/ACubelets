package me.davidml16.acubelets.database.types;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class SQLite implements Database {

    private Connection connection;

    private final Main main;

    public SQLite(Main main) {
        this.main = main;
    }

    @Override
    public void close() {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void open() {
        if (connection != null)  return;

        File file = new File(main.getDataFolder(), "playerData.db");
        String URL = "jdbc:sqlite:" + file;

        synchronized (this) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
                Main.log.sendMessage(Utils.translate("    &aSQLite has been enabled!"));
            } catch (SQLException | ClassNotFoundException e) {
                Main.log.sendMessage(Utils.translate("    &cSQLite has an error on the conection! Plugin disabled : Database needed"));
                Bukkit.getPluginManager().disablePlugin(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("ACubelets")));
            }
        }
    }

    public void loadTables() {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS ac_cubelets (`UUID` varchar(40) NOT NULL, `cubeletUUID` varchar(40) NOT NULL, `type` VARCHAR(15) NOT NULL, `received` bigint NOT NULL DEFAULT 0, `expire` bigint NOT NULL DEFAULT 0, PRIMARY KEY (`UUID`, `cubeletUUID`));");
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
        }

        PreparedStatement statement2 = null;
        try {
            statement2 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS ac_players (`UUID` varchar(40) NOT NULL, `NAME` varchar(40), `LOOT_POINTS` integer(25), `ORDER_BY` varchar(10), `ANIMATION` varchar(25) NOT NULL DEFAULT 'animation2', PRIMARY KEY (`UUID`));");
            statement2.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(statement2 != null) {
                try {
                    statement2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        PreparedStatement statement3 = null;
        try {
            statement3 = connection.prepareStatement("ALTER TABLE ac_players ADD `ANIMATION` varchar(25) NOT NULL DEFAULT 'animation2'");
            statement3.execute();
        } catch (SQLException ignored) {
        } finally {
            if(statement3 != null) {
                try {
                    statement3.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean hasName(String name) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("SELECT * FROM ac_players WHERE NAME = '" + name + "';");
            rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(ps != null) ps.close();
            if(rs != null) rs.close();
        }

        return false;
    }

    public void createPlayerData(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    public void updatePlayerName(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    public void getPlayerAnimation(UUID uuid, Callback<String> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(main, (Runnable) () -> {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
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
            }
        });
    }

    public void setPlayerAnimation(UUID uuid, String animation) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("UPDATE ac_players SET `ANIMATION` = ? WHERE `UUID` = ?");
            ps.setString(1, animation);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(ps != null) ps.close();
        }
    }

    public void getPlayerOrderSetting(UUID uuid, Callback<String> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(main, (Runnable) () -> {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
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
            }
        });
    }

    public void getPlayerLootPoints(UUID uuid, Callback<Long> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(main, (Runnable) () -> {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
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
            }
        });
    }

    public void setPlayerOrderSetting(UUID uuid, String orderBy) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("UPDATE ac_players SET `ORDER_BY` = ? WHERE `UUID` = ?");
            ps.setString(1, orderBy);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(ps != null) ps.close();
        }
    }

    public void setPlayerLootPoints(UUID uuid, long amount) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    public void saveProfileAsync(Profile profile) {

        String name = Bukkit.getPlayer(profile.getUuid()).getName();

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    public void saveProfileSync(Profile profile) {
        String name = Bukkit.getPlayer(profile.getUuid()).getName();
        PreparedStatement ps = null;
        try {
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
        }
    }

    public String getPlayerUUID(String name) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("SELECT * FROM ac_players WHERE NAME = '" + name + "';");
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("UUID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(ps != null) ps.close();
            if(rs != null) rs.close();
        }

        return "";
    }

    @Override
    public void addCubelet(UUID uuid, String type, Long received, Long expire) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    @Override
    public void addCubelet(UUID uuid, UUID cubeletUUID, String type, Long received, Long expire) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    @Override
    public void addCubelets(UUID uuid, Collection<Cubelet> cubelets) {
        if(XMaterial.supports(11)) {
            Bukkit.getScheduler().runTaskAsynchronously(main, () -> {

                String statement = "INSERT INTO ac_cubelets (UUID,cubeletUUID,type,received,expire) VALUES ";

                StringBuilder insertString = new StringBuilder();
                for (Cubelet cubelet : cubelets) {
                    if (insertString.toString().equalsIgnoreCase(""))
                        insertString.append("('").append(uuid.toString()).append("','").append(cubelet.getUuid()).append("','").append(cubelet.getType()).append("',").append(cubelet.getReceived()).append(",").append(cubelet.getExpire()).append(")");
                    else
                        insertString.append(", ('").append(uuid.toString()).append("','").append(cubelet.getUuid()).append("','").append(cubelet.getType()).append("',").append(cubelet.getReceived()).append(",").append(cubelet.getExpire()).append(")");
                }

                statement += insertString.toString();

                PreparedStatement ps = null;
                try {
                    ps = connection.prepareStatement(statement);
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
                }
            });
        } else {
            for (Cubelet cubelet : cubelets) {
                addCubelet(uuid, cubelet.getUuid(), cubelet.getType(), cubelet.getReceived(), cubelet.getExpire());
            }
        }
    }

    @Override
    public void removeCubelet(UUID uuid, UUID cubeletUUID) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    @Override
    public void removeCubelet(UUID uuid, String type, int amount) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    @Override
    public void removeCubelets(UUID uuid, Collection<Cubelet> cubelets) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {

            StringBuilder deleteString = new StringBuilder();
            for(Cubelet cubelet : cubelets) {
                if(deleteString.toString().equalsIgnoreCase("")) deleteString.append("'").append(cubelet.getUuid().toString()).append("'");
                else deleteString.append(",'").append(cubelet.getUuid().toString()).append("'");
            }

            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    @Override
    public void removeCubelets(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    @Override
    public void removeCubelet(String type) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    @Override
    public void removeExpiredCubelets(UUID uuid) {
        long actualTime = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    @Override
    public CompletableFuture<List<Cubelet>> getCubelets(UUID uuid) {
        CompletableFuture<List<Cubelet>> result = new CompletableFuture<>();

        long actualTime = System.currentTimeMillis();

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            List<Cubelet> cubelets = new ArrayList<>();
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
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
            }
        });
        return result;
    }

    public int getCubeletBalance(UUID uuid, String cubeletType) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("SELECT COUNT(*) AS amount FROM ac_cubelets WHERE UUID = '" + uuid.toString() + "' AND type = '" + cubeletType + "';");
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(ps != null) ps.close();
            if(rs != null) rs.close();
        }

        return 0;
    }

}
