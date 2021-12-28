package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.Main;

import java.util.UUID;

public class GiftGuiSession {

    private UUID player;
    private UUID target;

    private String targetName;

    private CubeletType cubeletType;
    private int cubeletAmount;

    private long available;

    private boolean openedByCommand;

    public GiftGuiSession(UUID player, UUID target, String targetName, boolean openedByCommand) {
        this.player = player;
        this.target = target;
        this.openedByCommand = openedByCommand;
        this.targetName = targetName;
        this.cubeletAmount = 1;
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

    public CubeletType getCubeletType() {
        return cubeletType;
    }

    public void setCubeletType(CubeletType cubeletType) {

        this.cubeletType = cubeletType;

        Profile profile = Main.get().getPlayerDataHandler().getData(player);
        this.available = profile.getCubelets().stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(cubeletType.getId())).count();

    }

    public int getCubeletAmount() {
        return cubeletAmount;
    }

    public void setCubeletAmount(int cubeletAmount) {
        this.cubeletAmount = cubeletAmount;
    }

    public long getAvailable() { return available; }

    public void setAvailable(long available) { this.available = available; }

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
