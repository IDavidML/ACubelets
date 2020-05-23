package me.davidml16.acubelets.animations.animation4;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Reward;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.MessageUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Animation4_Task implements Animation {

	private int id;

	private Main main;
	public Animation4_Task(Main main) {
		this.main = main;
	}

	private ArmorStand armorStand;
	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private Animation4_Music music;
	private Animation4_Blocks blocks;
	private Animation4_Arch arch;
	private Animation4_ArchSounds archSounds;
	private Animation4_EntityPackets entityPackets;
	private Animation4_3x3Particles particles;

	private List<Color> colors;
	private ColorUtil.ColorSet<Integer, Integer, Integer> colorRarity;

	private Location armorStandLocation;

	private Location corner1, corner2, corner3, corner4;

	private LivingEntity pigman;

	private Reward reward;

	class Task implements Runnable {
		int time = 0;
		@Override
		public void run() {

			if(time == 50) {
				arch.runTaskTimer(main, 0L, 1L);
				archSounds.runTaskTimer(main, 0L, 5L);
			} else if(time == 100) {
				archSounds.cancel();
			} else if(time == 105) {
				music.runTaskTimer(main, 0L, 5L);

				armorStand = ASSpawner.spawn(main, cubeletBox.getLocation(), cubeletType);
				armorStandLocation = armorStand.getLocation();
				main.getAnimationHandler().getArmorStands().add(armorStand);
			} else if(time > 105 && time < 145) {
				if (armorStand != null) {
					if(time < 144)armorStandLocation.add(0, 0.025, 0);
					armorStand.teleport(armorStandLocation);
					armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.16, 0));
				}
			} else if(time == 150) {
				music.cancel();

				pigman = (LivingEntity) cubeletBox.getLocation().getWorld().spawnEntity(cubeletBox.getLocation().clone().add(0.5, 3, -0.5), EntityType.PIG_ZOMBIE);
				if(XMaterial.supports(9)) pigman.setCollidable(false);
				if(XMaterial.supports(9)) pigman.setInvulnerable(true);
				if(XMaterial.supports(10)) pigman.setSilent(true);
                ((PigZombie )pigman).setBaby(false);
                ((PigZombie )pigman).setAngry(false);
				pigman.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));
			} else if(time == 160) {
				if(XMaterial.supports(9)) pigman.setAI(false);
				else pigman.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200000, 200, false, false));

				Location l = pigman.getLocation().clone();
				l.setYaw(0);
				l.setPitch(0);
				pigman.teleport(l);

				particles = new Animation4_3x3Particles(pigman.getLocation());
				particles.runTaskTimer(main, 0L, 10L);
			} else if(time == 180) {
				entityPackets = new Animation4_EntityPackets(pigman, armorStand);
				entityPackets.runTaskTimer(main, 0L, 5L);
			} else if(time == 278) {
				colorRarity = ColorUtil.getRGBbyColor(ColorUtil.getColorByText(reward.getRarity().getName()));
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.BALL_LARGE, colors.get(0), colors.get(1));
			} else if(time == 280) {
				pigman.remove();
				music.cancel();
				particles.cancel();
				entityPackets.cancel();
				Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.ZOMBIE_WOODBREAK, 0.5F, 1F);
				cubeletBox.setLastReward(reward);
				main.getHologramHandler().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;
			} else if(time > 280 && time < 400) {
				UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());

				UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, cubeletBox.getLocation(), 2);
			} else if(time == 400) {
				stop();
				blocks.restore();

				Bukkit.getServer().dispatchCommand(main.getServer().getConsoleSender(),
						reward.getCommand().replaceAll("%player%", cubeletBox.getPlayerOpening().getName()));
				MessageUtils.sendLootMessage(cubeletBox.getPlayerOpening(), cubeletType, reward);

				cubeletBox.setState(CubeletBoxState.EMPTY);
				cubeletBox.setPlayerOpening(null);
				main.getHologramHandler().reloadHologram(cubeletBox);
			}

			time++;
		}
	}
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		music = new Animation4_Music(box.getLocation());

		blocks = new Animation4_Blocks(box.getLocation());
		blocks.runTaskTimer(main, 0L, 7L);

		arch = new Animation4_Arch(box.getLocation());

		archSounds = new Animation4_ArchSounds(box.getLocation());

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.RED, Color.RED);

		corner1 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner2 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner3 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.95);
		corner4 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.95);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			reward = main.getCubeletRewardHandler().processReward(cubeletBox.getPlayerOpening(), cubeletType);
		});
	}
	
	public void stop() {
		blocks.cancel();

		try {
			music.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			arch.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			archSounds.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			entityPackets.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			particles.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(blocks != null) blocks.restore();

		if(pigman != null) pigman.remove();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getArmorStands().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getArmorStands().remove(armorStand);
		}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}
	
}
