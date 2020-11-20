package me.davidml16.acubelets.events;

import me.davidml16.acubelets.utils.NBTEditor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class Event_Block implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {

        ItemStack item = event.getItemInHand();

        if(NBTEditor.contains(item, "keyType")) {
            event.setCancelled(true);
        }

    }

}
