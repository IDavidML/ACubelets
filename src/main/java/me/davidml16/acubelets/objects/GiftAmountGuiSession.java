package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.Main;

import java.util.UUID;

public class GiftAmountGuiSession {

    private UUID player;
    private UUID target;

    private CubeletType cubeletType;
    private int cubeletAmount;

    private long available;

    public GiftAmountGuiSession(UUID player, UUID target, CubeletType cubeletType) {
        this.player = player;
        this.target = target;
        this.cubeletType = cubeletType;
        this.cubeletAmount = 1;

        Profile profile = Main.get().getPlayerDataHandler().getData(player);
        this.available = profile.getCubelets().stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(cubeletType.getId())).count();
    }

    public GiftAmountGuiSession(UUID player, UUID target, CubeletType cubeletType, int cubeletAmount) {
        this.player = player;
        this.target = target;
        this.cubeletType = cubeletType;
        this.cubeletAmount = cubeletAmount;

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

}
