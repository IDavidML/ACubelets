package me.davidml16.acubelets.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CubeletBox {

    private Location location;
    private HashMap<UUID, Hologram> holograms;

    private Player playerOpening;

    private boolean using;

    public CubeletBox(Location location) {
        this.location = location;
        this.holograms = new HashMap<>();
        this.using = false;
        this.playerOpening = null;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public HashMap<UUID, Hologram> getHolograms() {
        return holograms;
    }

    public boolean isUsing() {
        return using;
    }

    public void setUsing(boolean using) {
        this.using = using;
    }

    public Player getPlayerOpening() {
        return playerOpening;
    }

    public void setPlayerOpening(Player playerOpening) {
        this.playerOpening = playerOpening;
    }

    @Override
    public String toString() {
        return "CubeletBox{" +
                "location=" + location +
                ", using=" + using +
                '}';
    }

}
