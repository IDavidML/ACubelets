package me.davidml16.acubelets.database.types;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.data.Cubelet;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.Bukkit;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MySQL implements Database {

    private HikariDataSource hikari;

    private String host, user, password, database;
    private int port;

    private Main main;

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
            config.setPoolName("    AParkour Pool");
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
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS ac_cubelets (`UUID` varchar(40) NOT NULL, `cubeletUUID` varchar(40) NOT NULL, `type` VARCHAR(15) NOT NULL, `date` bigint NOT NULL, PRIMARY KEY (`UUID`, `cubeletUUID`));");
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
    }

    @Override
    public void addCubelet(UUID uuid, String type, Long date) throws SQLException {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            Connection connection = null;
            try {
                connection = hikari.getConnection();
                ps = connection.prepareStatement("INSERT INTO ac_cubelets (UUID,cubeletUUID,type,date) VALUES(?,?,?,?)");
                ps.setString(1, uuid.toString());
                ps.setString(2, UUID.randomUUID().toString());
                ps.setString(3, type);
                ps.setLong(4, date);
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
    public void addCubelet(UUID uuid, UUID cubeletUUID, String type, Long date) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            Connection connection = null;
            try {
                connection = hikari.getConnection();
                ps = connection.prepareStatement("INSERT INTO ac_cubelets (UUID,cubeletUUID,type,date) VALUES(?,?,?,?)");
                ps.setString(1, uuid.toString());
                ps.setString(2, cubeletUUID.toString());
                ps.setString(3, type);
                ps.setLong(4, date);
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
    public void removeCubelet(UUID uuid, UUID cubeletUUID) throws SQLException {
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
    public void removeCubelet(String type) throws SQLException {
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
    public Cubelet getCubelet(UUID uuid, UUID cubeletUUID) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            connection = hikari.getConnection();
            ps = connection.prepareStatement("SELECT * FROM ac_cubelets WHERE UUID = '" + uuid.toString() + "' AND cubeletUUID = '" + cubeletUUID + "';");

            rs = ps.executeQuery();
            while (rs.next()) {
                return new Cubelet(UUID.fromString(rs.getString("cubeletUUID")), rs.getString("type"), rs.getLong("date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(ps != null) ps.close();
            if(rs != null) rs.close();
            if(connection != null) connection.close();
        }

        return null;
    }

    @Override
    public List<Cubelet> getCubeletByType(UUID uuid, String type) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        List<Cubelet> cubelets = new ArrayList<Cubelet>();
        try {
            connection = hikari.getConnection();
            ps = connection.prepareStatement("SELECT * FROM ac_cubelets WHERE UUID = '" + uuid.toString() + "' AND type = '" + type + "';");

            rs = ps.executeQuery();
            while (rs.next()) {
                cubelets.add(new Cubelet(UUID.fromString(rs.getString("cubeletUUID")), rs.getString("type"), rs.getLong("date")));
            }

            return cubelets;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(ps != null) ps.close();
            if(rs != null) rs.close();
            if(connection != null) connection.close();
        }

        return cubelets;
    }

    @Override
    public CompletableFuture<List<Cubelet>> getCubelets(UUID uuid) {
        CompletableFuture<List<Cubelet>> result = new CompletableFuture<>();
        List<Cubelet> cubelets = new ArrayList<>();
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            for(String type : main.getCubeletTypesHandler().getTypes().keySet()) {
                try {
                    cubelets.addAll(getCubeletByType(uuid, type));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            result.complete(cubelets);
        });
        return result;
    }

}