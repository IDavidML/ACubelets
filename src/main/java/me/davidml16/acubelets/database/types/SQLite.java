package me.davidml16.acubelets.database.types;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.utils.ColorUtil;
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
                Main.log.sendMessage(ColorUtil.translate("    &aSQLite has been enabled!"));
            } catch (SQLException | ClassNotFoundException e) {
                Main.log.sendMessage(ColorUtil.translate("    &cSQLite has an error on the conection! Plugin disabled : Database needed"));
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
            statement2 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS ac_playernames (`UUID` varchar(40) NOT NULL, `NAME` varchar(40), PRIMARY KEY (`UUID`));");
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
    }

    public boolean hasName(String name) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
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
        }

        return false;
    }

    public void updatePlayerName(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            PreparedStatement ps = null;
            try {
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
            }
        });
    }

    public String getPlayerUUID(String name) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
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

}
