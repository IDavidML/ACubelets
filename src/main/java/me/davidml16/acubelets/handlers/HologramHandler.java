package me.davidml16.acubelets.handlers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.objects.ColorAnimation;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.CubeletOpener;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.RepeatingTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

public class HologramHandler {

    private Main main;

    private ColorAnimation colorAnimation;
    private String actualColor;

    private static final double LINE_HEIGHT = 0.36;
    private static final double LINE_HEIGHT_REWARD = 0.32;

    public HologramHandler(Main main) {
        this.main = main;
        this.colorAnimation = new ColorAnimation();
        this.actualColor = "&c";
    }

    public void loadHolograms() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            loadHolograms(p);
        }
    }

    public void loadHolograms(Player p) {
        for(CubeletBox box : main.getCubeletBoxHandler().getBoxes().values()) {
            loadHolograms(p, box);
        }
    }

    public void loadHolograms(CubeletBox box) {
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
        this.actualColor = colorAnimation.nextColor();
        for(Player p : Bukkit.getOnlinePlayers()) {
            reloadHolograms(p);
        }
    }

    public String getColor() {
        return this.actualColor;
    }
    public ColorAnimation getColorAnimation() { return colorAnimation; }

    public void loadHolograms(Player p, CubeletBox box) {
        Hologram hologram = HologramsAPI.createHologram(main, box.getLocation().clone().add(0.5, 1.025 + (box.getBlockHeight() + 0.1875), 0.5));
        VisibilityManager visibilityManager = hologram.getVisibilityManager();

        visibilityManager.showTo(p);
        visibilityManager.setVisibleByDefault(false);

        if(box.getState() == CubeletBoxState.EMPTY) {

            List<String> lines = getLines(p);
            int max = Math.max(main.getLanguageHandler().getMessageList("Holograms.CubeletAvailable").size(), main.getLanguageHandler().getMessageList("Holograms.NoCubeletAvailable").size());

            for (String line : lines) {
                hologram.appendTextLine(line);
            }

            hologram.teleport(box.getLocation().clone().add(0.5, (max * LINE_HEIGHT) + (box.getBlockHeight() + 0.1875), 0.5));
        } else if(box.getState() == CubeletBoxState.REWARD) {
            List<String> lines = getLinesReward(p, box.getPlayerOpening(), box.getLastReward());

            hologram.teleport(box.getLocation().clone().add(0.5, (lines.size() * LINE_HEIGHT_REWARD) + (box.getBlockHeight() + 0.1875), 0.5));

            for (String line : lines) {
                if(!line.contains("%reward_icon%"))
                    hologram.appendTextLine(line);
                else
                    hologram.appendItemLine(box.getLastReward().getIcon());
            }
        }

        box.getHolograms().put(p.getUniqueId(), hologram);
    }

    public void reloadHolograms(Player p) {
        for(CubeletBox box : main.getCubeletBoxHandler().getBoxes().values()) {
            reloadHologram(p, box);
        }
    }

    public void reloadHologram(CubeletBox box) {
        for (Hologram hologram : box.getHolograms().values()) {
            hologram.clearLines();
        }

        for(Player p : Bukkit.getOnlinePlayers()) {
            reloadHologram(p, box);
        }
    }

    public void moveHologram(CubeletBox box) {
        int max = Math.max(main.getLanguageHandler().getMessageList("Holograms.CubeletAvailable").size(), main.getLanguageHandler().getMessageList("Holograms.NoCubeletAvailable").size());
        for (Hologram hologram : box.getHolograms().values()) {
            hologram.teleport(box.getLocation().clone().add(0.5, (max * LINE_HEIGHT) + (box.getBlockHeight() + 0.1875), 0.5));
        }
    }

    public void rewardHologram(CubeletBox box, Reward reward) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if (box.getHolograms().containsKey(p.getUniqueId())) {
                Hologram hologram = box.getHolograms().get(p.getUniqueId());
                hologram.clearLines();

                List<String> lines = getLinesReward(p, box.getPlayerOpening(), reward);

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

    public RepeatingTask duplicationRewardHologram(CubeletBox box, Reward reward) {
        int duplicationPoints = box.getLastDuplicationPoints();

        return new RepeatingTask(main, 0, 1) {

            final int pointsPerTick = duplicationPoints / 40;
            int pointsToShow = 0;

            @Override
            public void run() {

                List<String> lines = getLinesRewardDuplicated(reward, pointsToShow);
                for (Player p : Bukkit.getOnlinePlayers()) {

                    if(!main.isDuplicationVisibleAllPlayers())
                        if(!box.getPlayerOpening().getUuid().equals(p.getUniqueId())) continue;

                    if (box.getHolograms().containsKey(p.getUniqueId())) {
                        Hologram hologram = box.getHolograms().get(p.getUniqueId());
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

    public void reloadHologram(Player p, CubeletBox box) {
        if (!Objects.equals(box.getLocation().getWorld(), p.getLocation().getWorld())) return;
        if (box.getLocation().distance(p.getLocation()) > 75) return;

        if (box.getState() == CubeletBoxState.EMPTY) {
            if (box.getHolograms().containsKey(p.getUniqueId())) {
                List<String> lines = getLines(p);
                Hologram hologram = box.getHolograms().get(p.getUniqueId());

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

    public void removeHolograms(Player p) {
        for(CubeletBox box : main.getCubeletBoxHandler().getBoxes().values()) {
            if(box.getHolograms().containsKey(p.getUniqueId())) {
                box.getHolograms().get(p.getUniqueId()).delete();
                box.getHolograms().remove(p.getUniqueId());
            }
        }
    }

    public List<String> getLines(Player p) {
        List<String> lines = new ArrayList<String>();

        int available = 0;
        if(main.getPlayerDataHandler().getPlayersData().containsKey(p.getUniqueId()))
            available = main.getPlayerDataHandler().getData(p).getCubelets().size();

        if(available > 0) {
            for(String line : main.getLanguageHandler().getMessageList("Holograms.CubeletAvailable")) {
                lines.add(Utils.translate(line
                        .replaceAll("%blink%", getColor())
                        .replaceAll("%cubelets_available%", String.valueOf(available))
                ));
            }
        } else {
            for(String line : main.getLanguageHandler().getMessageList("Holograms.NoCubeletAvailable")) {
                lines.add(Utils.translate(line
                        .replaceAll("%blink%", getColor())
                        .replaceAll("%cubelets_available%", String.valueOf(available))
                ));
            }
        };
        return lines;
    }

    public List<String> getLinesReward(Player p, CubeletOpener opening, Reward reward) {
        List<String> lines = new ArrayList<String>();

        if (p.getUniqueId().equals(opening.getUuid())) {
            for(String line : main.getLanguageHandler().getMessageList("Holograms.Reward.New.Me")) {
                lines.add(Utils.translate(line
                        .replaceAll("%player%", opening.getName())
                        .replaceAll("%reward_name%", Matcher.quoteReplacement(reward.getName()))
                        .replaceAll("%reward_rarity%", reward.getRarity().getName())
                ));
            }
        } else {
            for(String line : main.getLanguageHandler().getMessageList("Holograms.Reward.New.Other")) {
                lines.add(Utils.translate(line
                        .replaceAll("%player%", opening.getName())
                        .replaceAll("%reward_name%", Matcher.quoteReplacement(reward.getName()))
                        .replaceAll("%reward_rarity%", reward.getRarity().getName())
                ));
            }
        }

        return lines;
    }

    public List<String> getLinesRewardDuplicated(Reward reward, int duplicatePoints) {
        List<String> lines = new ArrayList<String>();

        for(String line : main.getLanguageHandler().getMessageList("Holograms.Reward.Duplicate")) {
            lines.add(Utils.translate(line
                    .replaceAll("%points%", ""+duplicatePoints)
                    .replaceAll("%reward_name%", Matcher.quoteReplacement(reward.getName()))
                    .replaceAll("%reward_rarity%", reward.getRarity().getName())
            ));
        }

        return lines;
    }

}