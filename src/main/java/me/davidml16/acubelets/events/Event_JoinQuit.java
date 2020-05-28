package me.davidml16.acubelets.events;

import me.davidml16.acubelets.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class Event_JoinQuit implements Listener {

    private Main main;
    public Event_JoinQuit(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        main.getPlayerDataHandler().loadPlayerData(p);
        main.getDatabaseHandler().updatePlayerName(p);
        main.setPlayerCount(main.getPlayerCount() + 1);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        main.getCubeletsGUI().getOpened().remove(p.getUniqueId());
        main.getTypeConfigGUI().getOpened().remove(p.getUniqueId());
        main.getRewardsGUI().getOpened().remove(p.getUniqueId());
        main.getRaritiesGUI().getOpened().remove(p.getUniqueId());
        main.getAnimationsGUI().getOpened().remove(p.getUniqueId());
        main.getEditBoxGUI().getOpened().remove(p.getUniqueId());
        main.getCraftingGUI().getOpened().remove(p.getUniqueId());
        main.getCraftingConfirmationGUI().getOpened().remove(p.getUniqueId());
        main.getRewardsPreviewGUI().getOpened().remove(p.getUniqueId());
        main.getPlayerDataHandler().getPlayersData().remove(p.getUniqueId());
        main.getHologramHandler().removeHolograms(p);

        main.getDatabaseHandler().updatePlayerName(p);
        main.setPlayerCount(main.getPlayerCount() - 1);

        try {
            main.getDatabaseHandler().removeExpiredCubelets(p.getUniqueId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @EventHandler
    public void worldChange(PlayerChangedWorldEvent e) {
        main.getHologramHandler().reloadHolograms(e.getPlayer());
    }

}
