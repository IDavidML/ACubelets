package me.davidml16.acubelets.events;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.inventory.ItemStack;

public class Event_Block implements Listener {

    private Main main;

    public Event_Block(Main main) {
        this.main = main;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {

        ItemStack item = event.getItemInHand();

        if(item == null) return;

        if(!NBTEditor.contains(item, "keyType")) return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        if(player == null) return;

        Bukkit.getScheduler().runTaskLater(main, () -> player.updateInventory(), 20L);

    }

    @EventHandler
    public void onEntityBlockForm(EntityBlockFormEvent paramEntityBlockFormEvent) {
        if (paramEntityBlockFormEvent.getEntity().hasMetadata("ACUBELETS")) {
            paramEntityBlockFormEvent.setCancelled(true);
        }
    }

}
