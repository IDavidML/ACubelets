package me.davidml16.acubelets.holograms.implementations;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.holograms.HologramHandler;
import me.davidml16.acubelets.holograms.HologramImplementation;
import me.davidml16.acubelets.objects.CubeletMachine;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.RepeatingTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HolographicDisplaysImpl implements HologramImplementation, Listener {

    private Main main;
    private HologramHandler hologramHandler;

    private static final double LINE_HEIGHT = 0.36;
    private static final double LINE_HEIGHT_REWARD = 0.32;

    private HashMap<CubeletMachine, HashMap<UUID, Hologram>> holograms;

    public HolographicDisplaysImpl(Main main, HologramHandler hologramHandler) {

        this.main = main;
        this.hologramHandler = hologramHandler;
        this.holograms = new HashMap<>();

    }

    public void loadHolograms() {

        for(Player p : Bukkit.getOnlinePlayers()) {

            loadHolograms(p);

        }

    }

    public void loadHolograms(Player p) {

        for(CubeletMachine box : main.getCubeletBoxHandler().getBoxes().values()) {

            loadHolograms(p, box);

        }

    }

    public void loadHolograms(CubeletMachine box) {

        for(Player p : Bukkit.getOnlinePlayers()) {

            loadHolograms(p, box);

        }

    }

    public void removeHolograms() {

        for (Hologram hologram : HologramsAPI.getHolograms(main)) {

            hologram.delete();

        }

    }

    public void reloadHolograms() {

        hologramHandler.setActualColor(hologramHandler.getColorAnimation().nextColor());

        for(Player p : Bukkit.getOnlinePlayers()) {

            reloadHolograms(p);

        }

    }

    public void loadHolograms(Player p, CubeletMachine box) {

        Hologram hologram = HologramsAPI.createHologram(main, box.getLocation().clone().add(0.5, 1.025 + (box.getBlockHeight() + 0.1875), 0.5));
        VisibilityManager visibilityManager = hologram.getVisibilityManager();

        visibilityManager.showTo(p);
        visibilityManager.setVisibleByDefault(false);

        if(box.getState() == CubeletBoxState.EMPTY) {

            List<String> lines = hologramHandler.getLines(p);
            int max = Math.max(main.getLanguageHandler().getMessageList("Holograms.CubeletAvailable").size(), main.getLanguageHandler().getMessageList("Holograms.NoCubeletAvailable").size());

            for (String line : lines) {
                hologram.appendTextLine(line);
            }

            hologram.teleport(box.getLocation().clone().add(0.5, (max * LINE_HEIGHT) + (box.getBlockHeight() + 0.1875), 0.5));

        } else if(box.getState() == CubeletBoxState.REWARD) {

            List<String> lines = hologramHandler.getLinesReward(p, box.getPlayerOpening(), box.getLastReward());

            hologram.teleport(box.getLocation().clone().add(0.5, (lines.size() * LINE_HEIGHT_REWARD) + (box.getBlockHeight() + 0.1875), 0.5));

            for (String line : lines) {
                if(!line.contains("%reward_icon%"))
                    hologram.appendTextLine(line);
                else
                    hologram.appendItemLine(box.getLastReward().getIcon());
            }

        }

        if(holograms.get(box) == null)
            holograms.put(box, new HashMap<>());

        holograms.get(box).put(p.getUniqueId(), hologram);

    }

    public void reloadHolograms(Player p) {

        for (CubeletMachine box : main.getCubeletBoxHandler().getBoxes().values()) {

            reloadHologram(p, box);

        }

    }

    public void reloadHologram(CubeletMachine box) {

        for (Hologram hologram : holograms.get(box).values()) {
            hologram.clearLines();
        }

        for(Player p : Bukkit.getOnlinePlayers()) {
            reloadHologram(p, box);
        }

    }

    public void reloadHologram(Player p, CubeletMachine box) {

        if (!Objects.equals(box.getLocation().getWorld(), p.getLocation().getWorld()) ||
                box.getLocation().distanceSquared(p.getLocation()) > hologramHandler.getVisibilityDistance())
            return;

        if (box.getState() == CubeletBoxState.EMPTY) {

            if (holograms.get(box) == null || !holograms.get(box).containsKey(p.getUniqueId()))
                return;

            if (holograms.get(box).containsKey(p.getUniqueId())) {

                List<String> lines = hologramHandler.getLines(p);
                Hologram hologram = holograms.get(box).get(p.getUniqueId());

                if (!hologram.getVisibilityManager().isVisibleTo(p))
                    hologram.getVisibilityManager().showTo(p);

                int max = Math.max(main.getLanguageHandler().getMessageList("Holograms.CubeletAvailable").size(), main.getLanguageHandler().getMessageList("Holograms.NoCubeletAvailable").size());
                if (hologram.size() != lines.size())
                    hologram.teleport(box.getLocation().clone().add(0.5, (max * LINE_HEIGHT) + (box.getBlockHeight() + 0.1875), 0.5));

                if (hologram.size() > lines.size()) {
                    for (int i = lines.size(); i < hologram.size(); i++)
                        hologram.getLine(i).removeLine();

                } else if (hologram.size() < lines.size()) {
                    for (int i = hologram.size(); i < lines.size(); i++)
                        hologram.appendTextLine(lines.get(i));
                }

                for (int i = 0; i < lines.size(); i++) {
                    if (!((TextLine) hologram.getLine(i)).getText().equalsIgnoreCase(lines.get(i)))
                        ((TextLine) hologram.getLine(i)).setText(lines.get(i));
                }

            }

        }

    }

    @Override
    public void clearLines(CubeletMachine box) {
        for (Hologram hologram : holograms.get(box).values()) {
            hologram.clearLines();
        }
    }

    @Override
    public void clearHolograms(CubeletMachine box) {
        holograms.get(box).clear();
    }

    public void moveHologram(CubeletMachine box) {

        int max = Math.max(main.getLanguageHandler().getMessageList("Holograms.CubeletAvailable").size(), main.getLanguageHandler().getMessageList("Holograms.NoCubeletAvailable").size());

        if(holograms.get(box) == null)
            return;

        for (Hologram hologram : holograms.get(box).values()) {
            hologram.teleport(box.getLocation().clone().add(0.5, (max * LINE_HEIGHT) + (box.getBlockHeight() + 0.1875), 0.5));
        }

    }

    public void rewardHologram(CubeletMachine box, Reward reward) {

        for(Player p : Bukkit.getOnlinePlayers()) {

            if(holograms.get(box) == null)
                continue;

            if (holograms.get(box).containsKey(p.getUniqueId())) {

                Hologram hologram = holograms.get(box).get(p.getUniqueId());
                hologram.clearLines();

                List<String> lines = hologramHandler.getLinesReward(p, box.getPlayerOpening(), reward);

                hologram.teleport(box.getLocation().clone().add(0.5, (lines.size() * LINE_HEIGHT_REWARD) + (box.getBlockHeight() + 0.1875), 0.5));

                for (String line : lines) {
                    if(!line.contains("%reward_icon%"))
                        hologram.appendTextLine(line);
                    else
                        hologram.appendItemLine(reward.getIcon());
                }

            }

        }

    }

    public RepeatingTask duplicationRewardHologram(CubeletMachine box, Reward reward) {

        int duplicationPoints = box.getLastDuplicationPoints();

        return new RepeatingTask(main, 0, 1) {

            final int pointsPerTick = duplicationPoints / 40;
            int pointsToShow = 0;

            @Override
            public void run() {

                List<String> lines = hologramHandler.getLinesRewardDuplicated(reward, pointsToShow);

                for (Player p : Bukkit.getOnlinePlayers()) {

                    if(!main.isDuplicationVisibleAllPlayers())
                        if(!box.getPlayerOpening().getUuid().equals(p.getUniqueId())) continue;

                    if(holograms.get(box) == null)
                        continue;

                    if (holograms.get(box).containsKey(p.getUniqueId())) {

                        Hologram hologram = holograms.get(box).get(p.getUniqueId());
                        hologram.teleport(box.getLocation().clone().add(0.5, (lines.size() * LINE_HEIGHT_REWARD) + (box.getBlockHeight() + 0.1875), 0.5));

                        for (int i = 0; i < lines.size(); i++) {
                            if (!lines.get(i).equalsIgnoreCase("%reward_icon%")) {
                                ((TextLine) hologram.getLine(i)).setText(lines.get(i));
                            }
                        }

                    }
                }

                if ((pointsToShow + pointsPerTick) <= duplicationPoints)
                    pointsToShow += pointsPerTick;
                else
                    pointsToShow = duplicationPoints;

            }

        };

    }

    public void removeHolograms(Player p) {

        for(CubeletMachine box : main.getCubeletBoxHandler().getBoxes().values()) {

            if(holograms.get(box) == null)
                continue;

            if(holograms.get(box).containsKey(p.getUniqueId())) {

                if(holograms.get(box).get(p.getUniqueId()) == null)
                    continue;

                holograms.get(box).get(p.getUniqueId()).delete();

                holograms.get(box).remove(p.getUniqueId());

            }

        }

    }

    @Override
    public void removeHolograms(CubeletMachine box) {

        if(holograms.get(box) == null)
            return;

        for(Hologram holo : holograms.get(box).values()) {
            holo.delete();
        }

    }

}
