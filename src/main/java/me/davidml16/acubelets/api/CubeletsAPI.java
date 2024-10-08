package me.davidml16.acubelets.api;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.database.DatabaseHandler;
import me.davidml16.acubelets.objects.CubeletType;

import java.sql.SQLException;

public class CubeletsAPI {

    private static Main main;

    public CubeletsAPI(Main main) {
        CubeletsAPI.main = main;
    }

    public static CubeletType giveCubelet(String player, String type, int amount, DatabaseHandler.Callback<CubeletType> callback) {

        try {

            main.getTransactionHandler().giveCubelet(player, type, amount, cubeletType -> {

                callback.done(cubeletType);

            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static void removeCubelet(String player, String type, int amount) {
        try {
            main.getTransactionHandler().removeCubelet(player, type, amount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
