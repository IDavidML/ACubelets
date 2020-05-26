package me.davidml16.acubelets.utils.economy;

import org.bukkit.entity.Player;

public interface ACEconomy {

    double getBalance(Player player);

    void addBalance(Player player, double amount);

    void removeBalance(Player player, double amount);

    ACEconomy setup();

}
