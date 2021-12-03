package me.davidml16.acubelets.events;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;

import java.sql.SQLException;

public class Event_JoinQuit implements Listener {

    private Main main;
    public Event_JoinQuit(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        main.getHologramImplementation().loadHolograms(p);
        main.getPlayerDataHandler().loadPlayerData(p);
        main.setPlayerCount(main.getPlayerCount() + 1);

        main.getGiftPlayerGUI().reloadAll();

        if(p.getName().equalsIgnoreCase("DavidML16")) {
            Bukkit.getScheduler().runTaskLater(main, () -> {
                PluginDescriptionFile pdf = main.getDescription();
                p.sendMessage(Utils.translate("&aServer using ACubelets. Version: &e" + pdf.getVersion()));
            }, 40L);
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        main.getGuiHandler().removeOpened(p);
        main.getHologramImplementation().removeHolograms(p);

        main.getDatabaseHandler().saveProfileAsync(main.getPlayerDataHandler().getData(p));

        main.setPlayerCount(main.getPlayerCount() - 1);

        main.getPlayerDataHandler().getPlayersData().remove(p.getUniqueId());

        main.getDatabaseHandler().removeExpiredCubelets(p.getUniqueId());

        Bukkit.getScheduler().runTaskLater(main, () -> main.getGiftPlayerGUI().reloadAll(), 1);
    }

    @EventHandler
    public void worldChange(PlayerChangedWorldEvent e) {

        Bukkit.getScheduler().runTaskLater(main, () -> {

            main.getHologramImplementation().removeHolograms(e.getPlayer());
            main.getHologramImplementation().loadHolograms(e.getPlayer());

        }, 40L);

    }

}
