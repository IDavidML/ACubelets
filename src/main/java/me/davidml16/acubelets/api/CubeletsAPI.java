package me.davidml16.acubelets.api;

import me.davidml16.acubelets.Main;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CubeletsAPI {

    public static void giveCubelet(String player, String type, int amount) {
        try {
            Main.get().getCubeletTypesHandler().giveCubelet(player, type, amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void removeCubelet(String player, String type, int amount) {
        try {
            Main.get().getCubeletTypesHandler().removeCubelet(player, type, amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
