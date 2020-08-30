package me.davidml16.acubelets.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.enums.Rotation;
import me.davidml16.acubelets.objects.rewards.Reward;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class CubeletBox {

    private Location location;

    private double blockHeight;
    private double permanentBlockHeight;

    private Rotation rotation;

    private HashMap<UUID, Hologram> holograms;

    private CubeletOpener playerOpening;

    private CubeletBoxState state;

    private Reward lastReward;
    private int lastDuplicationPoints;

    public CubeletBox(Location location, double blockHeight, double permanentBlockHeight, Rotation rotation) {
        this.location = location;
        this.holograms = new HashMap<>();
        this.state = CubeletBoxState.EMPTY;
        this.playerOpening = null;
        this.lastReward = null;
        this.lastDuplicationPoints = 0;
        this.blockHeight = blockHeight;
        this.permanentBlockHeight = permanentBlockHeight;
        this.rotation = rotation;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getBlockHeight() { return blockHeight; }

    public void setBlockHeight(double blockHeight) { this.blockHeight = blockHeight; }

    public double getPermanentBlockHeight() { return permanentBlockHeight; }

    public void setPermanentBlockHeight(double permanentBlockHeight) { this.permanentBlockHeight = permanentBlockHeight; }

    public HashMap<UUID, Hologram> getHolograms() {
        return holograms;
    }

    public CubeletBoxState getState() { return state; }

    public void setState(CubeletBoxState state) { this.state = state; }

    public CubeletOpener getPlayerOpening() {
        return playerOpening;
    }

    public void setPlayerOpening(CubeletOpener playerOpening) {
        this.playerOpening = playerOpening;
    }

    public Reward getLastReward() { return lastReward; }

    public void setLastReward(Reward lastReward) { this.lastReward = lastReward; }

    public int getLastDuplicationPoints() { return lastDuplicationPoints; }

    public void setLastDuplicationPoints(int lastDuplicationPoints) { this.lastDuplicationPoints = lastDuplicationPoints; }

    public Rotation getRotation() { return rotation; }

    public void setRotation(Rotation rotation) { this.rotation = rotation; }
    
    public boolean isWaiting() { return this.state == CubeletBoxState.EMPTY; }

    @Override
    public String toString() {
        return "CubeletBox{" +
                "location=" + location +
                ", state=" + state.toString() +
                '}';
    }

}
