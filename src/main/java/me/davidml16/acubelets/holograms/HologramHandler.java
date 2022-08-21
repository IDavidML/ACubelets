package me.davidml16.acubelets.holograms;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.holograms.implementations.DecentHologramsImpl;
import me.davidml16.acubelets.holograms.implementations.HolographicDisplaysImpl;
import me.davidml16.acubelets.objects.ColorAnimation;
import me.davidml16.acubelets.objects.CubeletOpener;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class HologramHandler {

    private Main main;

    private HologramImplementation implementation;

    private ColorAnimation colorAnimation;
    private String actualColor;

    private int visibilityDistance = 75;

    public HologramHandler(Main main) {

        this.main = main;
        this.colorAnimation = new ColorAnimation();
        this.actualColor = "&c";

        if(main.getServer().getPluginManager().isPluginEnabled("HolographicDisplays"))
            implementation = new HolographicDisplaysImpl(main, this);
//        else if(main.getServer().getPluginManager().isPluginEnabled("DecentHolograms"))
//            implementation = new DecentHologramsImpl(main, this);

    }

    public HologramImplementation getImplementation() {
        return implementation;
    }

    public ColorAnimation getColorAnimation() {
        return colorAnimation;
    }

    public void setColorAnimation(ColorAnimation colorAnimation) {
        this.colorAnimation = colorAnimation;
    }

    public String getActualColor() {
        return actualColor;
    }

    public void setActualColor(String actualColor) {
        this.actualColor = actualColor;
    }

    public int getVisibilityDistance() {
        return visibilityDistance;
    }

    public void setVisibilityDistance(int visibilityDistance) {
        this.visibilityDistance = visibilityDistance;
    }

    public List<String> getLines(Player p) {
        List<String> lines = new ArrayList<String>();

        int available = 0;
        if(main.getPlayerDataHandler().getPlayersData().containsKey(p.getUniqueId()))
            available = main.getPlayerDataHandler().getData(p).getCubelets().size();

        if(available > 0) {
            for(String line : main.getLanguageHandler().getMessageList("Holograms.CubeletAvailable")) {
                lines.add(Utils.translate(line
                        .replaceAll("%blink%", actualColor)
                        .replaceAll("%cubelets_available%", String.valueOf(available))
                ));
            }
        } else {
            for(String line : main.getLanguageHandler().getMessageList("Holograms.NoCubeletAvailable")) {
                lines.add(Utils.translate(line
                        .replaceAll("%blink%", actualColor)
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
                        .replaceAll("%cubelet_type%", Matcher.quoteReplacement(Utils.removeColors(reward.getParentCubelet().getName())))
                        .replaceAll("%reward_rarity%", reward.getRarity().getName())
                ));
            }
        } else {
            for(String line : main.getLanguageHandler().getMessageList("Holograms.Reward.New.Other")) {
                lines.add(Utils.translate(line
                        .replaceAll("%player%", opening.getName())
                        .replaceAll("%reward_name%", Matcher.quoteReplacement(reward.getName()))
                        .replaceAll("%cubelet_type%", Matcher.quoteReplacement(Utils.removeColors(reward.getParentCubelet().getName())))
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
                    .replaceAll("%cubelet_type%", Matcher.quoteReplacement(Utils.removeColors(reward.getParentCubelet().getName())))
                    .replaceAll("%reward_rarity%", reward.getRarity().getName())
            ));
        }

        return lines;
    }

}
