package me.davidml16.acubelets.database.types;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.Utils;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlConnection implements DatabaseConnection{

    private Main main;

    private HikariDataSource hikari;

    private final String host;
    private final String user;
    private final String password;
    private final String database;
    private final int port;

    public MySqlConnection(Main main) {

        this.main = main;

        this.host = main.getConfig().getString("MySQL.Host");
        this.user = main.getConfig().getString("MySQL.User");
        this.password = main.getConfig().getString("MySQL.Password");
        this.database = main.getConfig().getString("MySQL.Database");
        this.port = main.getConfig().getInt("MySQL.Port");

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

            Main.log.sendMessage(Utils.translate("    &aMySQL has been enabled!"));

        } catch (HikariPool.PoolInitializationException | SQLException e) {

            Main.log.sendMessage(Utils.translate("    &cMySQL has an error on the conection! Now trying with SQLite..."));
            main.getDatabase().changeToSQLite();

        }

    }

    @Override
    public Connection getConnection() {

        if(hikari != null)
            return null;

        try {
            return hikari.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public void close(Connection connection) {

        if(connection == null)
            return;

        try {

            connection.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    @Override
    public void stop() {

        if(hikari == null)
            return;

        hikari.close();

    }

}
