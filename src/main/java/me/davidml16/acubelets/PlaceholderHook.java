package me.davidml16.acubelets;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Objects;

public class PlaceholderHook extends PlaceholderExpansion {

    private Main main;
    public PlaceholderHook(Main main) {
        this.main = main;
    }

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin("ACubelets") != null;
    }

    @Override
    public boolean register() {
        if (!canRegister()) {
            return false;
        }

        return super.register();
    }

    @Override
    public String getIdentifier() {
        return "acubelets";
    }

    @Override
    public String getAuthor() {
        return "DavidML16";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (player == null) return "";

        String[] identifiers = identifier.split("_");
        switch (identifiers[0]) {
            case "available":
                if(identifiers.length == 1)
                    return String.valueOf(main.getPlayerDataHandler().getData(Objects.requireNonNull(player.getPlayer())).getCubelets().size());
                else if(identifiers.length == 2) {
                    if (!main.getCubeletTypesHandler().getTypes().containsKey(identifiers[1])) return "0";

                    return String.valueOf(main.getPlayerDataHandler()
                            .getData(Objects.requireNonNull(player.getPlayer()))
                            .getCubelets().stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(identifiers[1])).count());
                }
                break;
        }
        return null;
    }
}