package me.davidml16.acubelets.events;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
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
        main.getHologramHandler().loadHolograms(p);
        main.getPlayerDataHandler().loadPlayerData(p);
        main.setPlayerCount(main.getPlayerCount() + 1);

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
        main.getHologramHandler().removeHolograms(p);

        main.getDatabaseHandler().saveProfileAsync(main.getPlayerDataHandler().getData(p));

        main.setPlayerCount(main.getPlayerCount() - 1);

        main.getPlayerDataHandler().getPlayersData().remove(p.getUniqueId());

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
