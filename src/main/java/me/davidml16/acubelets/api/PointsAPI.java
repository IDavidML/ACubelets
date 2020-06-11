package me.davidml16.acubelets.api;

import me.davidml16.acubelets.Main;

import java.sql.SQLException;

public class PointsAPI {

    public static void give(String player, int amount) {
        try {
            Main.get().getPlayerDataHandler().givePoints(player, amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void remove(String player, int amount) {
        try {
            Main.get().getPlayerDataHandler().removePoints(player, amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void set(String player, int amount) {
        try {
            Main.get().getPlayerDataHandler().setPoints(player, amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
