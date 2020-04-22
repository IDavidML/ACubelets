package me.davidml16.acubelets.database.types;

import me.davidml16.acubelets.data.Cubelet;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Database {

    void close();

    void open();

    void loadTables();

    void addCubelet(UUID uuid, String type, Long date) throws SQLException;

    void addCubelet(UUID uuid, UUID cubeletUUID, String type, Long date) throws SQLException;

    void removeCubelet(UUID uuid, UUID cubeletUUID) throws SQLException;

    void removeCubelet(String type) throws SQLException;

    Cubelet getCubelet(UUID uuid, UUID cubeletUUID) throws SQLException;

    List<Cubelet> getCubeletByType(UUID uuid, String type) throws SQLException;

    CompletableFuture<List<Cubelet>> getCubelets(UUID uuid);

}
