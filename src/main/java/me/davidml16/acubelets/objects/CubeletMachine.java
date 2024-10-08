package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.effects.MachineEffectModel;
import me.davidml16.acubelets.effects.SimpleParticle;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.enums.Rotation;
import me.davidml16.acubelets.objects.rewards.Reward;
import org.bukkit.Location;
import org.bukkit.Particle;

public class CubeletMachine {

    private Location originalLocation;
    private Location location;

    private double blockHeight;
    private double permanentBlockHeight;

    private Rotation rotation;

    private CubeletOpener playerOpening;

    private CubeletBoxState state;

    private MachineEffectModel blockEffectModel;
    private SimpleParticle blockEffectParticle;

    private Reward lastReward;
    private int lastDuplicationPoints;

    public CubeletMachine(Location location, double blockHeight, double permanentBlockHeight, Rotation rotation) {
        this.originalLocation = location.clone();
        this.location = location.clone();
        this.state = CubeletBoxState.EMPTY;
        this.playerOpening = null;
        this.lastReward = null;
        this.lastDuplicationPoints = 0;
        this.blockHeight = blockHeight;
        this.permanentBlockHeight = permanentBlockHeight;
        this.rotation = rotation;
        this.blockEffectModel = MachineEffectModel.NONE;
        this.blockEffectParticle = SimpleParticle.of(Particle.FLAME);
    }

    public Location getLocation() {
        return location;
    }

    public Location getOriginalLocation() {
        return originalLocation;
    }

    public void resetLocation() {
        this.location = originalLocation.clone();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getBlockHeight() { return blockHeight; }

    public void setBlockHeight(double blockHeight) { this.blockHeight = blockHeight; }

    public double getPermanentBlockHeight() { return permanentBlockHeight; }

    public void setPermanentBlockHeight(double permanentBlockHeight) { this.permanentBlockHeight = permanentBlockHeight; }

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

    public MachineEffectModel getBlockEffectModel() {
        return blockEffectModel;
    }

    public void setBlockEffectModel(MachineEffectModel blockEffectModel) {
        this.blockEffectModel = blockEffectModel;
    }

    public SimpleParticle getBlockEffectParticle() {
        return blockEffectParticle;
    }

    public void setBlockEffectParticle(SimpleParticle blockEffectParticle) {
        this.blockEffectParticle = blockEffectParticle;
    }

    public boolean isWaiting() { return this.state == CubeletBoxState.EMPTY; }

    @Override
    public String toString() {
        return "CubeletBox{" +
                "location=" + location +
                ", state=" + state.toString() +
                '}';
    }

}
