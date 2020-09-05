package me.davidml16.acubelets.api;

import me.davidml16.acubelets.Main;

import java.sql.SQLException;

public class PointsAPI {

    private static Main main;

    public PointsAPI(Main main) {
        PointsAPI.main = main;
    }

    public static void give(String player, int amount) {
        try {
            main.getTransactionHandler().givePoints(player, amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void remove(String player, int amount) {
        try {
            main.getTransactionHandler().removePoints(player, amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void set(String player, int amount) {
        try {
            main.getTransactionHandler().setPoints(player, amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
