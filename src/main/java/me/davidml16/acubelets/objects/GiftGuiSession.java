package me.davidml16.acubelets.objects;

import java.util.UUID;

public class GiftGuiSession {

    private UUID player;
    private UUID target;
    private int page;

    public GiftGuiSession(UUID player, UUID target, int page) {
        this.player = player;
        this.target = target;
        this.page = page;
    }

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public UUID getTarget() {
        return target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
