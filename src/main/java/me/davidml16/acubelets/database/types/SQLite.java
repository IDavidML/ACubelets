package me.davidml16.acubelets.database.types;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.data.Cubelet;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class SQLite implements Database {

    private Connection connection;

    private Main main;

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
                Main.log.sendMessage(ColorUtil.translate("    &aSQLite has been enabled!"));
            } catch (SQLException | ClassNotFoundException e) {
                Main.log.sendMessage(ColorUtil.translate("    &cSQLite has an error on the conection! Plugin disabled : Database needed"));
                Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("ACubelets"));
            }
        }
    }

    public void loadTables() {
        PreparedStatement statement = null;
        try {
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
        }
    }

    @Override
    public void addCubelet(UUID uuid, String type, Long date) throws SQLException {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    @Override
    public void addCubelet(UUID uuid, UUID cubeletUUID, String type, Long date) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    @Override
    public void removeCubelet(UUID uuid, UUID cubeletUUID) throws SQLException {
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
    public Cubelet getCubelet(UUID uuid, UUID cubeletUUID) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
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
        }

        return null;
    }

    @Override
    public List<Cubelet> getCubeletByType(UUID uuid, String type) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Cubelet> cubelets = new ArrayList<Cubelet>();
        try {
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
