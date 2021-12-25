package me.davidml16.acubelets.animations;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.CubeletEndEvent;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.enums.Rotation;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.MessageUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.RepeatingTask;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animation {

    private Main main;

    private AnimationSettings animationSettings;

    private CubeletBox cubeletBox;
    private CubeletType cubeletType;
    private Reward reward;

    private List<Color> colors;
    private Utils.ColorSet<Integer, Integer, Integer> colorRarity;

    private Location corner1, corner2, corner3, corner4;
    private Location boxLocation;

    private int animationTick;
    private int animationRevealTick;

    private int animationId;

    private boolean rewardRevealed;

    private boolean showOutlineParticles, showFloorParticles, showAroundParticles;

    private RepeatingTask hologramAnimation;

    public Animation(Main main, AnimationSettings animationSettings) {

        this.main = main;
        this.animationSettings = animationSettings;

        this.animationTick = 0;
        this.animationRevealTick = 0;

        this.rewardRevealed = false;

        this.showOutlineParticles = true;
        this.showFloorParticles = true;
        this.showAroundParticles = false;

        this.colors = main.getFireworkUtil().getRandomColors();

    }

    public void setCubeletBox(CubeletBox box) {

        this.cubeletBox = box;

        this.boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

        this.corner1 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.05);
        this.corner2 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.05);
        this.corner3 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.95);
        this.corner4 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.95);

    }

    public void setCubeletType(CubeletType cubeletType) {

        this.cubeletType = cubeletType;

        setReward(main.getCubeletRewardHandler().processReward(cubeletType));

    }

    public void start() {

        this.cubeletBox.setState(CubeletBoxState.ANIMATION);

        this.animationId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, () -> {

            onTick(animationTick);
            animationTick++;

            if(rewardRevealed) {

                if(animationRevealTick == 2) {

                    doRewardReveal();

                } else if(animationRevealTick == 42) {

                    doRewardDuplication();

                } else if (animationRevealTick > 2 && animationRevealTick < 142) {

                    doShowBoxParticles();

                } else if(animationRevealTick >= 142) {

                    stop();

                }

                animationRevealTick++;

            }

        }, 0L, 1);

        Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));

        main.getAnimationHandler().getTasks().add(this);

        onStart();

    }

    public void stop() {

        if(hologramAnimation != null) hologramAnimation.cancel();

        main.getAnimationHandler().getTasks().remove(this);
        Bukkit.getServer().getScheduler().cancelTask(animationId);

        Bukkit.getPluginManager().callEvent(new CubeletEndEvent(cubeletBox.getPlayerOpening(), cubeletType));

        main.getCubeletRewardHandler().giveReward(cubeletBox, cubeletType, reward);

        cubeletBox.setState(CubeletBoxState.EMPTY);
        cubeletBox.setPlayerOpening(null);
        main.getHologramImplementation().reloadHologram(cubeletBox);

        onStop();

    }

    public void doPreRewardReveal() {

        rewardRevealed = true;

        onPreRewardReveal();

    }

    public void doRewardReveal() {

        main.getHologramImplementation().rewardHologram(cubeletBox, reward);
        cubeletBox.setState(CubeletBoxState.REWARD);

        onRewardReveal();

    }

    public void doRewardDuplication() {

        if(!main.isDuplicationEnabled())
            return;

        hologramAnimation = main.getCubeletRewardHandler().duplicationTask(cubeletBox, reward);

    }

    public void doShowBoxParticles() {

        if(showOutlineParticles && animationSettings.isOutlineParticles()) {
            UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
            UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
            UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
            UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
        }

        if(showFloorParticles && animationSettings.isFloorParticles())
            UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);

        if(showAroundParticles && animationSettings.isAroundParticles())
            UtilParticles.display(Particles.FLAME, 0.45f, 0.25f, 0.45f, boxLocation, 10);

    }

    public abstract void onTick(int time);

    public abstract void onStart();

    public abstract void onStop();

    public abstract void onPreRewardReveal();

    public abstract void onRewardReveal();

    public int getAnimationId() {
        return animationId;
    }

    public void setAnimationId(int animationId) {
        this.animationId = animationId;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public AnimationSettings getAnimationSettings() {
        return animationSettings;
    }

    public void setAnimationSettings(AnimationSettings animationSettings) {
        this.animationSettings = animationSettings;
    }

    public CubeletBox getCubeletBox() {
        return cubeletBox;
    }

    public CubeletType getCubeletType() {
        return cubeletType;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {

        this.reward = reward;

        this.cubeletBox.setLastReward(reward);

        int min = Math.min(Integer.parseInt(reward.getRarity().getDuplicatePointsRange().split("-")[0]),
                Integer.parseInt(reward.getRarity().getDuplicatePointsRange().split("-")[1]));
        int max = Math.max(Integer.parseInt(reward.getRarity().getDuplicatePointsRange().split("-")[0]),
                Integer.parseInt(reward.getRarity().getDuplicatePointsRange().split("-")[1]));

        int randomPoints = ThreadLocalRandom.current().nextInt(min, max);

        this.cubeletBox.setLastDuplicationPoints(randomPoints);

        setColorRarity(Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName())));

    }

    public List<Color> getColors() {
        return colors;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    public Utils.ColorSet<Integer, Integer, Integer> getColorRarity() {
        return colorRarity;
    }

    public void setColorRarity(Utils.ColorSet<Integer, Integer, Integer> colorRarity) {
        this.colorRarity = colorRarity;
    }

    public Location getCorner1() {
        return corner1;
    }

    public void setCorner1(Location corner1) {
        this.corner1 = corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public void setCorner2(Location corner2) {
        this.corner2 = corner2;
    }

    public Location getCorner3() {
        return corner3;
    }

    public void setCorner3(Location corner3) {
        this.corner3 = corner3;
    }

    public Location getCorner4() {
        return corner4;
    }

    public void setCorner4(Location corner4) {
        this.corner4 = corner4;
    }

    public Location getBoxLocation() {
        return boxLocation;
    }

    public void setBoxLocation(Location boxLocation) {
        this.boxLocation = boxLocation;
    }

    public int getAnimationTick() {
        return animationTick;
    }

    public void setAnimationTick(int animationTick) {
        this.animationTick = animationTick;
    }

    public RepeatingTask getHologramAnimation() {
        return hologramAnimation;
    }

    public void setHologramAnimation(RepeatingTask hologramAnimation) {
        this.hologramAnimation = hologramAnimation;
    }

    public int getAnimationRevealTick() {
        return animationRevealTick;
    }

    public void setAnimationRevealTick(int animationRevealTick) {
        this.animationRevealTick = animationRevealTick;
    }

    public boolean isRewardRevealed() {
        return rewardRevealed;
    }

    public void setRewardRevealed(boolean rewardRevealed) {
        this.rewardRevealed = rewardRevealed;
    }

    public boolean isShowOutlineParticles() {
        return showOutlineParticles;
    }

    public void setShowOutlineParticles(boolean showOutlineParticles) {
        this.showOutlineParticles = showOutlineParticles;
    }

    public boolean isShowFloorParticles() {
        return showFloorParticles;
    }

    public void setShowFloorParticles(boolean showFloorParticles) {
        this.showFloorParticles = showFloorParticles;
    }

    public boolean isShowAroundParticles() {
        return showAroundParticles;
    }

    public void setShowAroundParticles(boolean showAroundParticles) {
        this.showAroundParticles = showAroundParticles;
    }

    public Location getLocationRotation(double y) {

        switch (cubeletBox.getRotation()) {

            case SOUTH:

                Location s = cubeletBox.getLocation().clone().add(0.5, y, -0.5);
                s.setYaw(Rotation.SOUTH.value);
                return s;

            case NORTH:

                Location n = cubeletBox.getLocation().clone().add(0.5, y, 1.5);
                n.setYaw(Rotation.NORTH.value);
                return n;

            case EAST:

                Location e = cubeletBox.getLocation().clone().add(-0.5, y, 0.5);
                e.setYaw(Rotation.EAST.value);
                return e;

            case WEST:

                Location w = cubeletBox.getLocation().clone().add(1.5, y, 0.5);
                w.setYaw(Rotation.WEST.value);
                return w;

        }

        return null;

    }

    public Rotation getRotation(boolean opposite) {
        if(getCubeletBox().getRotation() == Rotation.NORTH)
            return opposite ? Rotation.SOUTH : Rotation.NORTH;
        else if(getCubeletBox().getRotation() == Rotation.SOUTH)
            return opposite ? Rotation.NORTH : Rotation.SOUTH;
        else if(getCubeletBox().getRotation() == Rotation.EAST)
            return opposite ? Rotation.WEST : Rotation.EAST;
        else if(getCubeletBox().getRotation() == Rotation.WEST)
            return opposite ? Rotation.EAST : Rotation.WEST;
        return null;
    }

}
