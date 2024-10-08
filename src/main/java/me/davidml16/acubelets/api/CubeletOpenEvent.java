package me.davidml16.acubelets.api;

import me.davidml16.acubelets.objects.CubeletOpener;
import me.davidml16.acubelets.objects.CubeletType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CubeletOpenEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private CubeletOpener player;
    private CubeletType cubeletType;

    public CubeletOpenEvent(CubeletOpener player, CubeletType cubeletType) {
        this.player = player;
        this.cubeletType = cubeletType;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public CubeletOpener getPlayer() {
        return this.player;
    }

    public CubeletType getCubeletType() {
        return cubeletType;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
