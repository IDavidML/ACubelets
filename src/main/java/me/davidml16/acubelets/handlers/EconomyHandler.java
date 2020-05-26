package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.utils.economy.ACEconomy;
import me.davidml16.acubelets.utils.economy.ACVault;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EconomyHandler {

    private ACEconomy economy;

    public void load() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) return;
        economy = new ACVault().setup();
    }

    public boolean isLoaded() {
        return economy != null;
    }

    public double getBalance(Player player) {
        if (this.economy == null) return 0.0;
        return this.economy.getBalance(player);
    }

    public void addBalance(Player player, double amount) {
        if (this.economy == null) return;
        this.economy.addBalance(player, amount);
    }

    public void removeBalance(Player player, double amount) {
        if (this.economy == null) return;
        this.economy.removeBalance(player, amount);
    }

}
