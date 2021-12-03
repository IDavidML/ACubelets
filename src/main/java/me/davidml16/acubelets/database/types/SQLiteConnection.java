package me.davidml16.acubelets.database.types;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class SQLiteConnection implements DatabaseConnection{

    private Main main;

    private Connection connection;

    public SQLiteConnection(Main main) {

        this.main = main;

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

    @Override
    public Connection getConnection() {

        return connection != null ? connection : null;

    }

    @Override
    public void close(Connection connection) { }

    @Override
    public void stop() {

        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }
    
}
