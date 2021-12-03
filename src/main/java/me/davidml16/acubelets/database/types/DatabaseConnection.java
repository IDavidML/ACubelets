package me.davidml16.acubelets.database.types;

import java.sql.Connection;

public interface DatabaseConnection {

    void open();

    Connection getConnection();

    void close(Connection connection);

    void stop();

}
