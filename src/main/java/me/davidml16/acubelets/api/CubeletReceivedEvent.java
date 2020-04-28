package me.davidml16.acubelets.api;

import me.davidml16.acubelets.objects.CubeletType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CubeletReceivedEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private Player p;
    private CubeletType cubeletType;
    private int amount;

    public CubeletReceivedEvent(Player p, CubeletType cubeletType, int amount) {
        this.p = p;
        this.cubeletType = cubeletType;
        this.amount = amount;
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

    public int getAmount() { return amount; }

    public HandlerList getHandlers() {
        return handlers;
    }

}
