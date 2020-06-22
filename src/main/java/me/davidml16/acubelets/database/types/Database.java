package me.davidml16.acubelets.database.types;

import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.Profile;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Database {

    void close();

    void open();

    void loadTables();

    boolean hasName(String name) throws SQLException;

    void createPlayerData(Player p);

    void updatePlayerName(Player p);

    String getPlayerUUID(String name) throws SQLException;

    long getPlayerLootPoints(UUID uuid) throws SQLException;

    void setPlayerLootPoints(UUID uuid, long amount) throws SQLException;

    void setPlayerOrderSetting(UUID uuid, String orderBy) throws SQLException;

    void saveProfileAsync(Profile profile);

    void saveProfileSync(Profile profile);

    String getPlayerOrderSetting(UUID uuid) throws SQLException;

    void addCubelet(UUID uuid, String type, Long received, Long expire) throws SQLException;

    void addCubelet(UUID uuid, UUID cubeletUUID, String type, Long received, Long expire) throws SQLException;

    void removeCubelet(UUID uuid, UUID cubeletUUID) throws SQLException;

    void removeCubelet(UUID uuid, String type, int amount);

    void removeCubelet(String type) throws SQLException;

    void removeExpiredCubelets(UUID uuid) throws SQLException;

    CompletableFuture<List<Cubelet>> getCubelets(UUID uuid);

    int getCubeletBalance(UUID uuid, String cubeletType) throws SQLException;

}
