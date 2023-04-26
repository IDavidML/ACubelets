package me.davidml16.acubelets.api;

import me.davidml16.acubelets.objects.CubeletOpener;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.rewards.Reward;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CubeletEndEvent extends Event {

    public static final HandlerList handlers = new HandlerList();
    private CubeletOpener player;
    private CubeletType cubeletType;
    private Reward reward;
    private int duplicationPoints;

    public CubeletEndEvent(CubeletOpener player, CubeletType cubeletType, Reward lastReward, int lastDuplicationPoints) {
        this.player = player;
        this.cubeletType = cubeletType;
        this.reward = lastReward;
        this.duplicationPoints = lastDuplicationPoints;
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

    public Reward getReward() {
        return reward;
    }

    public int getDuplicationPoints() {
        return duplicationPoints;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
