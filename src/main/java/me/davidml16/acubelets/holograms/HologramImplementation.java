package me.davidml16.acubelets.holograms;

import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.RepeatingTask;
import org.bukkit.entity.Player;

public interface HologramImplementation {

    void loadHolograms();

    void loadHolograms(Player p);

    void loadHolograms(CubeletBox box);

    void removeHolograms();

    void removeHolograms(Player p);

    void removeHolograms(CubeletBox box);

    void loadHolograms(Player p, CubeletBox box);

    void reloadHolograms();

    void reloadHolograms(Player p);

    void reloadHologram(CubeletBox box);

    void reloadHologram(Player p, CubeletBox box);

    void clearLines(CubeletBox box);

    void clearHolograms(CubeletBox box);

    void moveHologram(CubeletBox box);

    void rewardHologram(CubeletBox box, Reward reward);

    RepeatingTask duplicationRewardHologram(CubeletBox box, Reward reward);

}
