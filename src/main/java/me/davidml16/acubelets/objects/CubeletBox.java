package me.davidml16.acubelets.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.davidml16.acubelets.enums.CubeletBoxState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CubeletBox {

    private Location location;
    private double blockHeight;

    private HashMap<UUID, Hologram> holograms;

    private Player playerOpening;

    private CubeletBoxState state;

    private Reward lastReward;

    public CubeletBox(Location location, double blockHeight) {
        this.location = location;
        this.holograms = new HashMap<>();
        this.state = CubeletBoxState.EMPTY;
        this.playerOpening = null;
        this.lastReward = null;
        this.blockHeight = blockHeight;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getBlockHeight() { return blockHeight; }

    public void setBlockHeight(double blockHeight) { this.blockHeight = blockHeight; }

    public HashMap<UUID, Hologram> getHolograms() {
        return holograms;
    }

    public CubeletBoxState getState() { return state; }

    public void setState(CubeletBoxState state) { this.state = state; }

    public Player getPlayerOpening() {
        return playerOpening;
    }

    public void setPlayerOpening(Player playerOpening) {
        this.playerOpening = playerOpening;
    }

    public Reward getLastReward() { return lastReward; }

    public void setLastReward(Reward lastReward) { this.lastReward = lastReward; }

    @Override
    public String toString() {
        return "CubeletBox{" +
                "location=" + location +
                ", state=" + state.toString() +
                '}';
    }

}
