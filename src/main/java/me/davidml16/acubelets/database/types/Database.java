package me.davidml16.acubelets.database.types;

import me.davidml16.acubelets.objects.Cubelet;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Database {

    void close();

    void open();

    void loadTables();

    void addCubelet(UUID uuid, String type, Long received, Long expire) throws SQLException;

    void addCubelet(UUID uuid, UUID cubeletUUID, String type, Long received, Long expire) throws SQLException;

    void removeCubelet(UUID uuid, UUID cubeletUUID) throws SQLException;

    void removeCubelet(String type) throws SQLException;

    void removeExpiredCubelets(UUID uuid) throws SQLException;

    CompletableFuture<List<Cubelet>> getCubelets(UUID uuid);

}
