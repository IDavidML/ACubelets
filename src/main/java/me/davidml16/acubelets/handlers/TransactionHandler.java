package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.CubeletReceivedEvent;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionHandler {

    private Main main;

    public TransactionHandler(Main main) {
        this.main = main;
    }

    public long getCubeletBalance(String name, String type) throws SQLException {
        if (main.getCubeletTypesHandler().getTypes().containsKey(type)) {
            CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(type);
            UUID uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(name));

            AtomicLong count = new AtomicLong();

            if(Bukkit.getPlayer(uuid) != null) {
                Player player = Bukkit.getPlayer(uuid);
                count.set(main.getPlayerDataHandler()
                        .getData(Objects.requireNonNull(player))
                        .getCubelets().stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(type)).count());
            } else {
                main.getDatabaseHandler().getCubelets(uuid).thenAccept(cubelets -> {
                    count.set(cubelets.size());
                });
            }

            return count.get();
        }
        return 0;
    }

    public void giveCubelet(String player, String type, int amount) throws SQLException {
        UUID uuid;

        if(Bukkit.getPlayer(player) != null)
            uuid = Bukkit.getPlayer(player).getUniqueId();
        else
            uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));

        giveCubelet(uuid, type, amount);
    }

    public void giveCubelet(UUID uuid, String type, int amount) throws SQLException {
        if (main.getCubeletTypesHandler().getTypes().containsKey(type)) {
            CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(type);

            Collection<Cubelet> cubelets = new ArrayList<>();

            if (amount > 0) {

                for (int i = 1; i <= amount; i++)
                    cubelets.add(new Cubelet(cubeletType));

                main.getDatabaseHandler().addCubelets(uuid, cubelets);

                if(Bukkit.getPlayer(uuid) != null) {
                    Player target = Bukkit.getPlayer(uuid);

                    main.getPlayerDataHandler().getData(Objects.requireNonNull(target)).getCubelets().addAll(cubelets);

                    if (main.getCubeletsGUI().getOpened().containsKey(uuid)) main.getCubeletsGUI().reloadPage(target);
                    if (main.getCraftingGUI().getOpened().contains(uuid)) main.getCraftingGUI().open(target);
                    if (main.getGiftGUI().getOpened().containsKey(uuid)) main.getGiftGUI().reloadGui(target);
                    if (main.getGiftAmountGUI().getOpened().containsKey(uuid)) main.getGiftAmountGUI().reloadGui(target);
                    main.getHologramHandler().reloadHolograms(target);

                    Bukkit.getPluginManager().callEvent(new CubeletReceivedEvent(target, main.getCubeletTypesHandler().getTypeBydId(type), amount));
                }

            }
        }
    }

    public void removeCubelet(String player, String type, int amount) throws SQLException {
        UUID uuid;

        if(Bukkit.getPlayer(player) != null)
            uuid = Bukkit.getPlayer(player).getUniqueId();
        else
            uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));

        removeCubelet(uuid, type, amount);
    }

    public void removeCubelet(UUID uuid, String type, int amount) throws SQLException {
        if (main.getCubeletTypesHandler().getTypes().containsKey(type)) {
            if (amount > 0) {
                if(Bukkit.getPlayer(uuid) != null) {

                    Player target = Bukkit.getPlayer(uuid);
                    Profile profile = main.getPlayerDataHandler().getData(target);

                    Collection<Cubelet> cubelets = new ArrayList<>();

                    for (int i = 1; i <= amount; i++) {
                        Optional<Cubelet> cb = profile.getCubelets().stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(type)).findFirst();
                        if(cb.isPresent()) {
                            Cubelet cubelet = cb.get();
                            cubelets.add(cubelet);
                            profile.getCubelets().remove(cubelet);
                        }
                    }

                    if (main.getCubeletsGUI().getOpened().containsKey(uuid)) main.getCubeletsGUI().reloadPage(target);
                    if (main.getCraftingGUI().getOpened().contains(uuid)) main.getCraftingGUI().open(target);
                    if (main.getGiftGUI().getOpened().containsKey(uuid)) main.getGiftGUI().reloadGui(target);
                    main.getHologramHandler().reloadHolograms(target);

                    main.getDatabaseHandler().removeCubelets(uuid, cubelets);

                } else {
                    main.getDatabaseHandler().removeCubelet(uuid, type, amount);
                }
            }
        }
    }

    public void transferCubelets(UUID player, UUID target, CubeletType type, int amount) {
        if (main.getCubeletTypesHandler().getTypes().containsKey(type.getId())) {

            try {
                removeCubelet(player, type.getId(), amount);
                giveCubelet(target, type.getId(), amount);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void givePoints(String player, int amount) throws SQLException {
        UUID uuid;

        if(Bukkit.getPlayer(player) != null)
            uuid = Bukkit.getPlayer(player).getUniqueId();
        else
            uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));

        givePoints(uuid, amount);
    }

    public void givePoints(UUID uuid, int amount) throws SQLException {
        if(Bukkit.getPlayer(uuid) != null) {
            main.getPlayerDataHandler().getData(uuid).setLootPoints(main.getPlayerDataHandler().getData(uuid).getLootPoints() + amount);
            if (main.getCubeletsGUI().getOpened().containsKey(uuid)) main.getCubeletsGUI().reloadPage(Bukkit.getPlayer(uuid));
            if (main.getCraftingGUI().getOpened().contains(uuid)) main.getCraftingGUI().open(Bukkit.getPlayer(uuid));
            if (main.getGiftGUI().getOpened().containsKey(uuid)) main.getGiftGUI().reloadGui(Bukkit.getPlayer(uuid));
        } else {
            main.getDatabaseHandler().getPlayerLootPoints(uuid, done -> {
                try {
                    main.getDatabaseHandler().setPlayerLootPoints(uuid, done + amount);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        }

    }

    public void removePoints(String player, int amount) throws SQLException {
        UUID uuid;

        if(Bukkit.getPlayer(player) != null)
            uuid = Bukkit.getPlayer(player).getUniqueId();
        else
            uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));

        removePoints(uuid, amount);
    }

    public void removePoints(UUID uuid, int amount) throws SQLException {
        if(Bukkit.getPlayer(uuid) != null) {
            if ((main.getPlayerDataHandler().getData(uuid).getLootPoints() - amount) < 0)
                main.getPlayerDataHandler().getData(uuid).setLootPoints(0);
            else
                main.getPlayerDataHandler().getData(uuid).setLootPoints(main.getPlayerDataHandler().getData(uuid).getLootPoints() - amount);

            if (main.getCubeletsGUI().getOpened().containsKey(uuid)) main.getCubeletsGUI().reloadPage(Bukkit.getPlayer(uuid));
            if (main.getCraftingGUI().getOpened().contains(uuid)) main.getCraftingGUI().open(Bukkit.getPlayer(uuid));
            if (main.getGiftGUI().getOpened().containsKey(uuid)) main.getGiftGUI().reloadGui(Bukkit.getPlayer(uuid));
        } else {
            main.getDatabaseHandler().getPlayerLootPoints(uuid, done -> {
                if((done - amount) < 0) {
                    try {
                        main.getDatabaseHandler().setPlayerLootPoints(uuid, 0);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else {
                    try {
                        main.getDatabaseHandler().setPlayerLootPoints(uuid, done - amount);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });


        }
    }

    public void setPoints(String player, int amount) throws SQLException {
        UUID uuid;

        if(Bukkit.getPlayer(player) != null)
            uuid = Bukkit.getPlayer(player).getUniqueId();
        else
            uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));

        setPoints(uuid, amount);
    }

    public void setPoints(UUID uuid, int amount) throws SQLException {
        if(Bukkit.getPlayer(uuid) != null) {
            main.getPlayerDataHandler().getData(uuid).setLootPoints(amount);
            if (main.getCubeletsGUI().getOpened().containsKey(uuid)) main.getCubeletsGUI().reloadPage(Bukkit.getPlayer(uuid));
            if (main.getCraftingGUI().getOpened().contains(uuid)) main.getCraftingGUI().open(Bukkit.getPlayer(uuid));
            if (main.getGiftGUI().getOpened().containsKey(uuid)) main.getGiftGUI().reloadGui(Bukkit.getPlayer(uuid));
        } else {
            main.getDatabaseHandler().setPlayerLootPoints(uuid, amount);
        }
    }

}
