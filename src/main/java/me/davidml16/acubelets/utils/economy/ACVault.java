package me.davidml16.acubelets.utils.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ACVault implements ACEconomy {

    private Economy economy;

    @Override
    public double getBalance(Player player) {
        if (this.economy == null) return 0.0;
        return this.economy.getBalance(player);
    }

    @Override
    public void addBalance(Player player, double amount) {
        if (this.economy == null) return;
        this.economy.depositPlayer(player, amount);
    }

    @Override
    public void removeBalance(Player player, double amount) {
        if (this.economy == null) return;
        this.economy.withdrawPlayer(player, amount);
    }

    @Override
    public ACEconomy setup() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) return null;

        RegisteredServiceProvider<Economy> registeredServiceProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);

        if (registeredServiceProvider == null) return null;

        this.economy = registeredServiceProvider.getProvider();
        return this;
    }
}
