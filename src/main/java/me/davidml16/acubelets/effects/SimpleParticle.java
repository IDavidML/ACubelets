package me.davidml16.acubelets.effects;

import me.davidml16.acubelets.utils.StringUtils;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleParticle {

    private final Particle particle;
    private final Object data;

    public SimpleParticle(@NotNull Particle particle, @Nullable Object data) {
        this.particle = particle;
        this.data = data;
    }

    @NotNull
    public static SimpleParticle of(@NotNull Particle particle) {
        return SimpleParticle.of(particle, null);
    }

    @NotNull
    public static SimpleParticle of(@NotNull Particle particle, @Nullable Object data) {
        return new SimpleParticle(particle, data);
    }

    @NotNull
    public static SimpleParticle itemCrack(@NotNull Material material) {
        return new SimpleParticle(Particle.ITEM_CRACK, new ItemStack(material));
    }

    @NotNull
    public static SimpleParticle blockCrack(@NotNull Material material) {
        return new SimpleParticle(Particle.BLOCK_CRACK, material.createBlockData());
    }

    @NotNull
    public static SimpleParticle blockDust(@NotNull Material material) {
        return new SimpleParticle(Particle.BLOCK_DUST, material.createBlockData());
    }

    @NotNull
    public static SimpleParticle fallingDust(@NotNull Material material) {
        return new SimpleParticle(Particle.FALLING_DUST, material.createBlockData());
    }

    @NotNull
    public static SimpleParticle redstone(@NotNull Color color, float size) {
        return new SimpleParticle(Particle.REDSTONE, new Particle.DustOptions(color, size));
    }

    @NotNull
    public Particle getParticle() {
        return particle;
    }

    @Nullable
    public Object getData() {
        return data;
    }

    public void play(@NotNull Location location, double speed, int amount) {
        this.play(location, 0D, speed, amount);
    }

    public void play(@NotNull Location location, double offsetAll, double speed, int amount) {
        this.play(location, offsetAll, offsetAll, offsetAll, speed, amount);
    }

    public void play(@NotNull Location location, double xOffset, double yOffset, double zOffset, double speed, int amount) {
        this.play(null, location, xOffset, yOffset, zOffset, speed, amount);
    }

    public void play(@NotNull Player player, @NotNull Location location, double speed, int amount) {
        this.play(player, location, 0D, speed, amount);
    }

    public void play(@NotNull Player player, @NotNull Location location, double offsetAll, double speed, int amount) {
        this.play(player, location, offsetAll, offsetAll, offsetAll, speed, amount);
    }

    public void play(@Nullable Player player, @NotNull Location location, double xOffset, double yOffset, double zOffset, double speed, int amount) {
        if (player == null) {
            World world = location.getWorld();
            if (world == null) return;
            try {
                world.spawnParticle(this.getParticle(), location, amount, xOffset, yOffset, zOffset, speed, this.getData());
            } catch (Exception e) {}
        } else {
            try {
                player.spawnParticle(this.getParticle(), location, amount, xOffset, yOffset, zOffset, speed, this.getData());
            } catch (Exception e) {}
        }
    }

    @NotNull
    public SimpleParticle parseData(@NotNull String from) {
        String[] split = from.split(" ");
        Class<?> dataType = this.getParticle().getDataType();
        Object data = null;
        if (dataType == BlockData.class) {
            Material material = Material.getMaterial(from.toUpperCase());
            data = material != null ? material.createBlockData() : Material.STONE.createBlockData();
        }
        else if (dataType == Particle.DustOptions.class) {
            Color color = StringUtils.parseColor(split[0]);
            double size = split.length >= 2 ? StringUtils.getDouble(split[1], 1D) : 1D;
            data = new Particle.DustOptions(color, (float) size);
        }
        else if (dataType == Particle.DustTransition.class) {
            Color colorStart = StringUtils.parseColor(split[0]);
            Color colorEnd = split.length >= 2 ? StringUtils.parseColor(split[1]) : colorStart;
            double size = split.length >= 3 ? StringUtils.getDouble(split[2], 1D) : 1D;
            data = new Particle.DustTransition(colorStart, colorEnd, 1.0f);
        }
        else if (dataType == ItemStack.class) {
            Material material = Material.getMaterial(from.toUpperCase());
            if (material != null && !material.isAir()) data = new ItemStack(material);
            else data = new ItemStack(Material.STONE);
        }
        else if (dataType != Void.class) return SimpleParticle.redstone(Color.AQUA, 1);

        return SimpleParticle.of(this.getParticle(), data);
    }

    @Override
    public String toString() {
        return "SimpleParticle{" +
                "particle=" + particle +
                ", data=" + data +
                '}';
    }
}