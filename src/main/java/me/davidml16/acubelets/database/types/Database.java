package me.davidml16.acubelets.database.types;

import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.Profile;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Database {

    interface Callback<T> {
        void done(T valor);
    }

    void close();

    void open();

    void loadTables();

    boolean hasName(String name) throws SQLException;

    void createPlayerData(Player p);

    void updatePlayerName(Player p);

    String getPlayerUUID(String name) throws SQLException;

    void getPlayerLootPoints(UUID uuid, Callback<Long> callback) throws SQLException;

    void setPlayerLootPoints(UUID uuid, long amount) throws SQLException;

    void setPlayerOrderSetting(UUID uuid, String orderBy) throws SQLException;

    void saveProfileAsync(Profile profile);

    void saveProfileSync(Profile profile);

    void getPlayerOrderSetting(UUID uuid, Callback<String> callback) throws SQLException;

    void addCubelet(UUID uuid, String type, Long received, Long expire) throws SQLException;

    void addCubelet(UUID uuid, UUID cubeletUUID, String type, Long received, Long expire) throws SQLException;

    void addCubelets(UUID uuid, Collection<Cubelet> cubelets) throws SQLException;

    void removeCubelet(UUID uuid, UUID cubeletUUID) throws SQLException;

    void removeCubelet(UUID uuid, String type, int amount);

    void removeCubelet(String type) throws SQLException;

    void removeCubelets(UUID uuid, Collection<Cubelet> cubelets) throws SQLException;

    void removeExpiredCubelets(UUID uuid) throws SQLException;

    CompletableFuture<List<Cubelet>> getCubelets(UUID uuid);

    int getCubeletBalance(UUID uuid, String cubeletType) throws SQLException;

}
