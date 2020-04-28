package me.davidml16.acubelets.api;

import me.davidml16.acubelets.objects.CubeletType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CubeletEndEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private Player p;
    private CubeletType cubeletType;

    public CubeletEndEvent(Player p, CubeletType cubeletType) {
        this.p = p;
        this.cubeletType = cubeletType;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.p;
    }

    public CubeletType getCubeletType() {
        return cubeletType;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
