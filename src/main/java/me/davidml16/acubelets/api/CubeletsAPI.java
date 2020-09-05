package me.davidml16.acubelets.api;

import me.davidml16.acubelets.Main;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CubeletsAPI {

    private static Main main;

    public CubeletsAPI(Main main) {
        CubeletsAPI.main = main;
    }

    public static void giveCubelet(String player, String type, int amount) {
        try {
            main.getTransactionHandler().giveCubelet(player, type, amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void removeCubelet(String player, String type, int amount) {
        try {
            main.getTransactionHandler().removeCubelet(player, type, amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
