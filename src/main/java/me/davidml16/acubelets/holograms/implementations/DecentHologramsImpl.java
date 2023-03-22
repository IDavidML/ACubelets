package me.davidml16.acubelets.holograms.implementations;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import eu.decentsoftware.holograms.api.utils.items.HologramItem;
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

import java.util.*;

public class DecentHologramsImpl implements HologramImplementation, Listener {

    private Main main;
    private HologramHandler hologramHandler;

    private static final double LINE_HEIGHT = 0.36;
    private static final double LINE_HEIGHT_REWARD = 0.32;

    private HashMap<CubeletMachine, HashMap<UUID, Hologram>> holograms;

    public DecentHologramsImpl(Main main, HologramHandler hologramHandler) {

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

        for (Hologram hologram : Hologram.getCachedHolograms()) {

            hologram.hideAll();
            hologram.destroy();

        }

    }

    public void reloadHolograms() {

        hologramHandler.setActualColor(hologramHandler.getColorAnimation().nextColor());

        for(Player p : Bukkit.getOnlinePlayers()) {

            reloadHolograms(p);

        }

    }

    public void loadHolograms(Player p, CubeletMachine box) {

        Hologram hologram = DHAPI.createHologram(UUID.randomUUID().toString(), box.getLocation().clone().add(0.5, 1.025 + (box.getBlockHeight() + 0.1875), 0.5));
        hologram.setDefaultVisibleState(false);
        hologram.setShowPlayer(p);

        if(box.getState() == CubeletBoxState.EMPTY) {

            int max = Math.max(main.getLanguageHandler().getMessageList("Holograms.CubeletAvailable").size(), main.getLanguageHandler().getMessageList("Holograms.NoCubeletAvailable").size());

            DHAPI.setHologramLines(hologram, hologramHandler.getLines(p));
            DHAPI.moveHologram(hologram, box.getLocation().clone().add(0.5, (max * LINE_HEIGHT) + (box.getBlockHeight() + 0.1875), 0.5));

        } else if(box.getState() == CubeletBoxState.REWARD) {

            List<String> linesOld = hologramHandler.getLinesReward(p, box.getPlayerOpening(), box.getLastReward());
            List<String> linesNew = new ArrayList<>();

            for (String line : linesOld) {
                if(!line.contains("%reward_icon%"))
                    linesNew.add(line);
                else
                    linesNew.add("#ICON:" + HologramItem.fromItemStack(box.getLastReward().getIcon()).getContent());
            }

            DHAPI.setHologramLines(hologram, linesNew);
            DHAPI.moveHologram(hologram, box.getLocation().clone().add(0.5, (linesNew.size() * LINE_HEIGHT_REWARD) + (box.getBlockHeight() + 0.1875), 0.5));

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

        for(Player p : Bukkit.getOnlinePlayers()) {
            reloadHologram(p, box);
        }

    }

    public void reloadHologram(Player p, CubeletMachine box) {

        if (!Objects.equals(box.getLocation().getWorld(), p.getLocation().getWorld()) ||
                box.getLocation().distanceSquared(p.getLocation()) > hologramHandler.getVisibilityDistance())
            return;

        if (holograms.get(box) == null || !holograms.get(box).containsKey(p.getUniqueId()))
            return;

        Hologram hologram = holograms.get(box).get(p.getUniqueId());

        if (box.getState() == CubeletBoxState.EMPTY) {

            if (holograms.get(box).containsKey(p.getUniqueId())) {

                List<String> lines = hologramHandler.getLines(p);

                int max = Math.max(main.getLanguageHandler().getMessageList("Holograms.CubeletAvailable").size(), main.getLanguageHandler().getMessageList("Holograms.NoCubeletAvailable").size());

                DHAPI.setHologramLines(hologram, lines);

                DHAPI.moveHologram(hologram, box.getLocation().clone().add(0.5, (max * LINE_HEIGHT) + (box.getBlockHeight() + 0.1875), 0.5));

            }

        }

    }

    @Override
    public void clearLines(CubeletMachine box) {
        for (Hologram hologram : holograms.get(box).values()) {
            HologramPage page = hologram.getPage(0);
            while(page.size() > 0) {
                page.removeLine(page.size() - 1);
            }
            hologram.realignLines();
            hologram.updateAll();
            hologram.save();
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
            DHAPI.moveHologram(hologram, box.getLocation().clone().add(0.5, (max * LINE_HEIGHT) + (box.getBlockHeight() + 0.1875), 0.5));
        }

    }

    public void rewardHologram(CubeletMachine box, Reward reward) {

        for(Player p : Bukkit.getOnlinePlayers()) {

            if(holograms.get(box) == null)
                continue;

            if (holograms.get(box).containsKey(p.getUniqueId())) {

                Hologram hologram = holograms.get(box).get(p.getUniqueId());
                List<String> lines = hologramHandler.getLinesReward(p, box.getPlayerOpening(), reward);
                List<String> formattedLines = new ArrayList<>();

                for (String line : lines) {
                    if(line.contains("%reward_icon%"))
                        formattedLines.add("#ICON:" + HologramItem.fromItemStack(box.getLastReward().getIcon()).getContent());
                    else
                        formattedLines.add(line);
                }

                DHAPI.setHologramLines(hologram, formattedLines);
                DHAPI.moveHologram(hologram, box.getLocation().clone().add(0.5, (lines.size() * LINE_HEIGHT_REWARD) + (box.getBlockHeight() + 0.1875), 0.5));

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
                        HologramPage page = hologram.getPage(0);

                        for (int i = 0; i < page.getLines().size(); i++) {
                            String text = page.getLine(i).getContent();
                            if (!(text.contains("#ICON:")))
                                DHAPI.setHologramLine(hologram, i, lines.get(i));
                        }

                        DHAPI.moveHologram(hologram, box.getLocation().clone().add(0.5, (lines.size() * LINE_HEIGHT_REWARD) + (box.getBlockHeight() + 0.1875), 0.5));

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
            holo.destroy();
        }

    }

}
