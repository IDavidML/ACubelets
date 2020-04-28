package me.davidml16.acubelets.api;

import me.davidml16.acubelets.Main;
import org.bukkit.entity.Player;

public class CubeletsAPI {

    public static void giveCubelet(Player player, String type, int amount) {
        Main.get().getCubeletTypesHandler().giveCubelet(player, type, amount);
    }

}
