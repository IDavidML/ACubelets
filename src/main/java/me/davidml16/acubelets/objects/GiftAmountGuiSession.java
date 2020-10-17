package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.Main;

import java.util.UUID;

public class GiftAmountGuiSession {

    private UUID player;
    private UUID target;

    private String targetName;

    private CubeletType cubeletType;
    private int cubeletAmount;

    private long available;

    private boolean openedByCommand;

    public GiftAmountGuiSession(UUID player, UUID target, String targetName, CubeletType cubeletType, boolean openedByCommand) {
        this.player = player;
        this.target = target;
        this.cubeletType = cubeletType;
        this.cubeletAmount = 1;
        this.openedByCommand = openedByCommand;
        this.targetName = targetName;

        Profile profile = Main.get().getPlayerDataHandler().getData(player);
        this.available = profile.getCubelets().stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(cubeletType.getId())).count();
    }

    public GiftAmountGuiSession(UUID player, UUID target, String targetName, CubeletType cubeletType, int cubeletAmount, boolean openedByCommand) {
        this.player = player;
        this.target = target;
        this.cubeletType = cubeletType;
        this.cubeletAmount = cubeletAmount;
        this.openedByCommand = openedByCommand;
        this.targetName = targetName;

        Profile profile = Main.get().getPlayerDataHandler().getData(player);
        this.available = profile.getCubelets().stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(cubeletType.getId())).count();
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
