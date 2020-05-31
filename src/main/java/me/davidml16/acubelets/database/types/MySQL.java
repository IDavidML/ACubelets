package me.davidml16.acubelets.database.types;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MySQL implements Database {

    private HikariDataSource hikari;

    private final String host;
    private final String user;
    private final String password;
    private final String database;
    private final int port;

    private final Main main;

    public MySQL(Main main) {
        this.main = main;
        this.host = main.getConfig().getString("MySQL.Host");
        this.user = main.getConfig().getString("MySQL.User");
        this.password = main.getConfig().getString("MySQL.Password");
        this.database = main.getConfig().getString("MySQL.Database");
        this.port = main.getConfig().getInt("MySQL.Port");
    }

    @Override
    public void close() {
        if(hikari != null) {
            hikari.close();
        }
    }

    @Override
    public void open() {
        if (hikari != null)  return;

        try {
            HikariConfig config = new HikariConfig();
            config.setPoolName("    ACubelets Pool");
            config.setJdbcUrl("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database);
            config.setUsername(this.user);
            config.setPassword(this.password);
            config.setMaximumPoolSize(75);
            config.setMinimumIdle(4);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            hikari = new HikariDataSource(config);

            hikari.getConnection();

            Main.log.sendMessage(ColorUtil.translate("    &aMySQL has been enabled!"));
        } catch (HikariPool.PoolInitializationException | SQLException e) {
            Main.log.sendMessage(ColorUtil.translate("    &cMySQL has an error on the conection! Now trying with SQLite..."));
            main.getDatabase().changeToSQLite();
        }
    }

    public void loadTables() {
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = hikari.getConnection();
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
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        PreparedStatement statement2 = null;
        Connection connection2 = null;
        try {
            connection2 = hikari.getConnection();
            statement2 = connection2.prepareStatement("CREATE TABLE IF NOT EXISTS ac_playernames (`UUID` varchar(40) NOT NULL, `NAME` varchar(40), PRIMARY KEY (`UUID`));");
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
            if(connection2 != null) {
                try {
                    connection2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean hasName(String name) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = hikari.getConnection();
            ps = connection.prepareStatement("SELECT * FROM ac_playernames WHERE NAME = '" + name + "';");
            rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(ps != null) ps.close();
            if(rs != null) rs.close();
            if(connection != null) connection.close();
        }

        return false;
    }

    public void updatePlayerName(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            Connection connection = null;
            try {
                connection = hikari.getConnection();
                ps = connection.prepareStatement("REPLACE INTO ac_playernames (UUID,NAME) VALUES(?,?)");
                ps.setString(1, p.getUniqueId().toString());
                ps.setString(2, p.getName());
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
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
    }

    public String getPlayerUUID(String name) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection =  hikari.getConnection();
            ps = connection.prepareStatement("SELECT * FROM ac_playernames WHERE NAME = '" + name + "';");
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("UUID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(ps != null) ps.close();
            if(rs != null) rs.close();
            if(connection != null) connection.close();
        }

        return "";
    }

    @Override
    public void addCubelet(UUID uuid, String type, Long received, Long expire) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            Connection connection = null;
            try {
                connection = hikari.getConnection();
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
                if (connection != null) {
                    try {
                        connection.close();
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
            Connection connection = null;
            try {
                connection = hikari.getConnection();
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
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void removeCubelet(UUID uuid, UUID cubeletUUID) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            Connection connection = null;
            try {
                connection = hikari.getConnection();
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
                if (connection != null) {
                    try {
                        connection.close();
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
            Connection connection = null;
            try {
                connection = hikari.getConnection();
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
                if (connection != null) {
                    try {
                        connection.close();
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
            Connection connection = null;
            try {
                connection = hikari.getConnection();
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
                if (connection != null) {
                    try {
                        connection.close();
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
            Connection connection = null;
            try {
                connection = hikari.getConnection();
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
                if (connection != null) {
                    try {
                        connection.close();
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
            Connection connection = null;

            try {
                connection = hikari.getConnection();
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
                if(connection != null) {
                    try {
                        connection.close();
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
        Connection connection = null;
        try {
            connection =  hikari.getConnection();
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
            if(connection != null) connection.close();
        }

        return 0;
    }

}