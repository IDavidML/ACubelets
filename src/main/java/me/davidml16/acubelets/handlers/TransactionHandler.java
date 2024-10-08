package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.CubeletReceivedEvent;
import me.davidml16.acubelets.database.DatabaseHandler;
import me.davidml16.acubelets.menus.player.CubeletsMenu;
import me.davidml16.acubelets.menus.player.crafting.CraftingMenu;
import me.davidml16.acubelets.menus.player.gifts.GiftCubeletMenu;
import me.davidml16.acubelets.menus.player.gifts.GiftMenu;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class TransactionHandler {

    private Main main;

    public TransactionHandler(Main main) {
        this.main = main;
    }

    public void giveCubelet(String player, String type, int amount, DatabaseHandler.Callback<CubeletType> callback) throws SQLException {

        if(Bukkit.getPlayer(player) != null) {

            UUID uuid = Bukkit.getPlayer(player).getUniqueId();

            callback.done(giveCubelet(uuid, type, amount));

        } else {

            try {

                main.getDatabaseHandler().getPlayerUUID(player, result -> {

                    UUID uuid = UUID.fromString(result);

                    try {

                        callback.done(giveCubelet(uuid, type, amount));

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                });

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

    }

    public CubeletType giveCubelet(UUID uuid, String type, int amount) throws SQLException {
        if (main.getCubeletTypesHandler().getTypes().containsKey(type) || type.equalsIgnoreCase("random")) {

            CubeletType cubeletType = null;

            if(!type.equalsIgnoreCase("random")) {
                cubeletType =  main.getCubeletTypesHandler().getTypeBydId(type);
            } else {
                List<CubeletType> available = new ArrayList<>(main.getCubeletTypesHandler().getTypes().values());
                cubeletType = available.get((int) (Math.random() * available.size()));
            }

            Collection<Cubelet> cubelets = new ArrayList<>();

            if (amount > 0) {

                for (int i = 1; i <= amount; i++)
                    cubelets.add(new Cubelet(cubeletType));

                main.getDatabaseHandler().addCubelets(uuid, cubelets);

                if(Bukkit.getPlayer(uuid) != null) {

                    Player target = Bukkit.getPlayer(uuid);

                    main.getPlayerDataHandler().getData(Objects.requireNonNull(target)).getCubelets().addAll(cubelets);

                    Bukkit.getScheduler().runTask(main, () -> {

                        main.getMenuHandler().reloadAllMenus(target, CubeletsMenu.class);
                        main.getMenuHandler().reloadAllMenus(target, CraftingMenu.class);
                        main.getMenuHandler().reloadAllMenus(target, GiftMenu.class);
                        main.getMenuHandler().reloadAllMenus(target, GiftCubeletMenu.class);

                        main.getHologramImplementation().reloadHolograms(target);

                        Bukkit.getPluginManager().callEvent(new CubeletReceivedEvent(target, main.getCubeletTypesHandler().getTypeBydId(type), amount));

                    });

                }

            }

            return cubeletType;
        }

        return null;
    }

    public void removeCubelet(String player, String type, int amount) throws SQLException {

        if(Bukkit.getPlayer(player) != null) {

            UUID uuid = Bukkit.getPlayer(player).getUniqueId();

            removeCubelet(uuid, type, amount);

        } else {

            try {

                main.getDatabaseHandler().getPlayerUUID(player, result -> {

                    UUID uuid = UUID.fromString(result);

                    try {

                        removeCubelet(uuid, type, amount);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                });

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

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

                    Bukkit.getScheduler().runTask(main, () -> {

                        main.getMenuHandler().reloadAllMenus(target, CubeletsMenu.class);
                        main.getMenuHandler().reloadAllMenus(target, CraftingMenu.class);
                        main.getMenuHandler().reloadAllMenus(target, GiftMenu.class);

                        main.getHologramImplementation().reloadHolograms(target);

                    });

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

        if(Bukkit.getPlayer(player) != null) {

            UUID uuid = Bukkit.getPlayer(player).getUniqueId();

            givePoints(uuid, amount);

        } else {

            try {

                main.getDatabaseHandler().getPlayerUUID(player, result -> {

                    UUID uuid = UUID.fromString(result);

                    try {

                        givePoints(uuid, amount);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                });

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

    }

    public void givePoints(UUID uuid, int amount) throws SQLException {

        if(Bukkit.getPlayer(uuid) != null) {

            main.getPlayerDataHandler().getData(uuid).setLootPoints(main.getPlayerDataHandler().getData(uuid).getLootPoints() + amount);

            Bukkit.getScheduler().runTask(main, () -> {

                Player target = Bukkit.getPlayer(uuid);
                main.getMenuHandler().reloadAllMenus(target, CubeletsMenu.class);
                main.getMenuHandler().reloadAllMenus(target, CraftingMenu.class);
                main.getMenuHandler().reloadAllMenus(target, GiftMenu.class);
                main.getMenuHandler().reloadAllMenus(target, GiftCubeletMenu.class);

            });

        }

        main.getDatabaseHandler().getPlayerLootPoints(uuid, done -> {
            main.getDatabaseHandler().setPlayerLootPoints(uuid, done + amount);
        });

    }

    public void removePoints(String player, int amount) throws SQLException {

        if(Bukkit.getPlayer(player) != null) {

            UUID uuid = Bukkit.getPlayer(player).getUniqueId();

            removePoints(uuid, amount);

        } else {

            try {

                main.getDatabaseHandler().getPlayerUUID(player, result -> {

                    UUID uuid = UUID.fromString(result);

                    try {

                        removePoints(uuid, amount);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                });

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

    }

    public void removePoints(UUID uuid, int amount) throws SQLException {

        if(Bukkit.getPlayer(uuid) != null) {
            if ((main.getPlayerDataHandler().getData(uuid).getLootPoints() - amount) < 0)
                main.getPlayerDataHandler().getData(uuid).setLootPoints(0);
            else
                main.getPlayerDataHandler().getData(uuid).setLootPoints(main.getPlayerDataHandler().getData(uuid).getLootPoints() - amount);

            Bukkit.getScheduler().runTask(main, () -> {

                Player target = Bukkit.getPlayer(uuid);
                main.getMenuHandler().reloadAllMenus(target, CubeletsMenu.class);
                main.getMenuHandler().reloadAllMenus(target, CraftingMenu.class);
                main.getMenuHandler().reloadAllMenus(target, GiftMenu.class);
                main.getMenuHandler().reloadAllMenus(target, GiftCubeletMenu.class);

            });

        }

        main.getDatabaseHandler().getPlayerLootPoints(uuid, done -> {
            if((done - amount) < 0) {
                main.getDatabaseHandler().setPlayerLootPoints(uuid, 0);
            }
            else {
                main.getDatabaseHandler().setPlayerLootPoints(uuid, done - amount);
            }
        });

    }

    public void setPoints(String player, int amount) throws SQLException {

        if(Bukkit.getPlayer(player) != null) {

            UUID uuid = Bukkit.getPlayer(player).getUniqueId();

            setPoints(uuid, amount);

        } else {

            try {

                main.getDatabaseHandler().getPlayerUUID(player, result -> {

                    UUID uuid = UUID.fromString(result);

                    try {

                        setPoints(uuid, amount);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                });

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

    }

    public void setPoints(UUID uuid, int amount) throws SQLException {

        if(Bukkit.getPlayer(uuid) != null) {

            main.getPlayerDataHandler().getData(uuid).setLootPoints(amount);

            Bukkit.getScheduler().runTask(main, () -> {

                Player target = Bukkit.getPlayer(uuid);
                main.getMenuHandler().reloadAllMenus(target, CubeletsMenu.class);
                main.getMenuHandler().reloadAllMenus(target, CraftingMenu.class);
                main.getMenuHandler().reloadAllMenus(target, GiftMenu.class);
                main.getMenuHandler().reloadAllMenus(target, GiftCubeletMenu.class);

            });

        }

        main.getDatabaseHandler().setPlayerLootPoints(uuid, amount);

    }

}
