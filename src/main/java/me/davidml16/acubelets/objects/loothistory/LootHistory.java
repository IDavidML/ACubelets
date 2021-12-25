package me.davidml16.acubelets.objects.loothistory;

import java.util.UUID;

public class LootHistory {

    private int id;
    private UUID playerUUID;

    private String cubeletName;
    private Long received;

    private RewardHistory rewardHistory;

    public LootHistory(UUID playerUUID, String cubeletName, Long received, RewardHistory rewardHistory) {
        this(-1, playerUUID, cubeletName, received, rewardHistory);
    }

    public LootHistory(int id, UUID playerUUID, String cubeletName, Long received, RewardHistory rewardHistory) {
        this.id = id;
        this.playerUUID = playerUUID;
        this.cubeletName = cubeletName;
        this.received = received;
        this.rewardHistory = rewardHistory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public Long getReceived() {
        return received;
    }

    public void setReceived(Long received) {
        this.received = received;
    }

    public String getCubeletName() {
        return cubeletName;
    }

    public void setCubeletName(String cubeletName) {
        this.cubeletName = cubeletName;
    }

    public RewardHistory getRewardHistory() {
        return rewardHistory;
    }

    public void setRewardHistory(RewardHistory rewardHistory) {
        this.rewardHistory = rewardHistory;
    }

}
