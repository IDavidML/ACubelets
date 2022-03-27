package me.davidml16.acubelets.events;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.player.gifts.GiftPlayerMenu;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;

public class Event_JoinQuit implements Listener {

    private Main main;
    public Event_JoinQuit(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        main.getHologramImplementation().loadHolograms(player);
        main.getPlayerDataHandler().loadPlayerData(player);
        main.setPlayerCount(main.getPlayerCount() + 1);

        main.getPlayerDataHandler().getDisconnectCacheTime().remove(player.getUniqueId());

        main.getMenuHandler().reloadAllMenus(GiftPlayerMenu.class);

        if(player.getName().equalsIgnoreCase("DavidML16")) {
            Bukkit.getScheduler().runTaskLater(main, () -> {
                PluginDescriptionFile pdf = main.getDescription();
                player.sendMessage(Utils.translate("&aServer using ACubelets. Version: &e" + pdf.getVersion()));
            }, 40L);
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        Player player = e.getPlayer();

        main.getMenuHandler().getOpenedMenus().remove(player);
        main.getHologramImplementation().removeHolograms(player);

        main.getDatabaseHandler().saveProfileAsync(main.getPlayerDataHandler().getData(player));

        main.setPlayerCount(main.getPlayerCount() - 1);

        main.getPlayerDataHandler().addDisconnectTime(player.getUniqueId(), System.currentTimeMillis());

        main.getDatabaseHandler().removeExpiredCubelets(player.getUniqueId());

        main.getMenuHandler().reloadAllMenus(GiftPlayerMenu.class);

    }

    @EventHandler
    public void worldChange(PlayerChangedWorldEvent e) {

        Bukkit.getScheduler().runTaskLater(main, () -> {

            main.getHologramImplementation().removeHolograms(e.getPlayer());
            main.getHologramImplementation().loadHolograms(e.getPlayer());

        }, 40L);

    }

}
