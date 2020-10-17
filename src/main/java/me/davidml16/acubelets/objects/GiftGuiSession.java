package me.davidml16.acubelets.objects;

import java.util.UUID;

public class GiftGuiSession {

    private UUID player;
    private UUID target;

    private String targetName;

    private int page;

    private boolean openedByCommand;

    public GiftGuiSession(UUID player, UUID target, String targetName, int page, boolean openedByCommand) {
        this.player = player;
        this.target = target;
        this.page = page;
        this.openedByCommand = openedByCommand;
        this.targetName = targetName;
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

    public boolean isOpenedByCommand() {
        return openedByCommand;
    }

    public void setOpenedByCommand(boolean openedByCommand) {
        this.openedByCommand = openedByCommand;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}
