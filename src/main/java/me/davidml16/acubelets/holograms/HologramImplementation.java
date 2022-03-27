package me.davidml16.acubelets.holograms;

import me.davidml16.acubelets.objects.CubeletMachine;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.RepeatingTask;
import org.bukkit.entity.Player;

public interface HologramImplementation {

    void loadHolograms();

    void loadHolograms(Player p);

    void loadHolograms(CubeletMachine box);

    void removeHolograms();

    void removeHolograms(Player p);

    void removeHolograms(CubeletMachine box);

    void loadHolograms(Player p, CubeletMachine box);

    void reloadHolograms();

    void reloadHolograms(Player p);

    void reloadHologram(CubeletMachine box);

    void reloadHologram(Player p, CubeletMachine box);

    void clearLines(CubeletMachine box);

    void clearHolograms(CubeletMachine box);

    void moveHologram(CubeletMachine box);

    void rewardHologram(CubeletMachine box, Reward reward);

    RepeatingTask duplicationRewardHologram(CubeletMachine box, Reward reward);

}
