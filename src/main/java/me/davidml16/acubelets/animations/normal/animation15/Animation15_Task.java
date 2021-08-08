package me.davidml16.acubelets.animations.normal.animation15;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.rewards.PermissionReward;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.XSeries.ParticleDisplay;
import me.davidml16.acubelets.utils.XSeries.XParticle;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Animation15_Task implements Animation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public Animation15_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private ArmorStand armorStand;

	private Animation15_Serpent serpent;
	private Animation15_Blocks blocks;
	private Animation15_Spiral spiral;

	private Set<Animation15_RotatingFlame> orbits = new HashSet<>();

	private BukkitTask blackHole;

	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private List<Color> colors;
	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;
	private Location boxLocation, armorStandLocation;

	private Location corner1, corner2, corner3, corner4;

	private Reward reward;

	private RepeatingTask hologramAnimation;

	class Task implements Runnable {
		int time = 0;
		double rotSpeed = 0.001;
		@Override
		public void run() {

			if (time > 0 && time < 10)
				Objects.requireNonNull(armorStandLocation).add(0, 0.25, 0);
			else if (time > 10 && time < 20)
				Objects.requireNonNull(armorStandLocation).add(0, 0.20, 0);
			else if (time > 20 && time < 30)
				Objects.requireNonNull(armorStandLocation).add(0, 0.15, 0);
			else if (time > 30 && time < 40)
				Objects.requireNonNull(armorStandLocation).add(0, 0.10, 0);
			else if (time > 40 && time < 50)
				Objects.requireNonNull(armorStandLocation).add(0, 0.05, 0);

			if(armorStand != null && time < 152) {
				armorStand.teleport(armorStandLocation);
				Objects.requireNonNull(armorStand).setHeadPose(armorStand.getHeadPose().add(0, 0.26, 0));
			}

			if(time == 45) {
				serpent = new Animation15_Serpent(cubeletBox.getLocation().clone().add(0, 1, 0).setDirection(new Vector(-1, 0, -1)), false);
				serpent.runTaskTimer(main, 0, 1);

				Location eye = armorStand.getEyeLocation().add(0.0D, 0.4D, 0.0D);
				for (Location location2 : LocationUtils.getCircle(armorStand.getLocation().clone().add(0, 0.75,0), 0.25D, 50)) {
					Vector direction = location2.toVector().subtract(armorStand.getLocation().clone().add(0, 0.75,0).toVector()).normalize();
					UtilParticles.display(Particles.FLAME, direction, eye, 0.3F);
				}
				Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.FIREWORK_BLAST, 0.5f, 0);

				try {
					blackHole = XParticle.blackhole(main, 8, 4, 200, 2, 999999, new ParticleDisplay(Particle.SMOKE_LARGE, cubeletBox.getLocation().clone().add(0.5, 7, 0.5), 1));
				} catch (NoClassDefFoundError ignore) {}

				Animation15_RotatingFlame orbit1 = new Animation15_RotatingFlame(main, cubeletBox.getLocation().clone().add(0.5, 6, 0.5), 5f, 120, true, 0);
				orbit1.runTaskTimer(main, 0L, 1L);
				orbits.add(orbit1);

				Animation15_RotatingFlame orbit2 = new Animation15_RotatingFlame(main, cubeletBox.getLocation().clone().add(0.5, 6, 0.5), 5f, 120, true, 40);
				orbit2.runTaskTimer(main, 0L, 1L);
				orbits.add(orbit2);

				Animation15_RotatingFlame orbit3 = new Animation15_RotatingFlame(main, cubeletBox.getLocation().clone().add(0.5, 6, 0.5), 5f, 120, true, 80);
				orbit3.runTaskTimer(main, 0L, 1L);
				orbits.add(orbit3);

				spiral.cancel();
			}

			if (time == 150) {
				if(blackHole != null) blackHole.cancel();
				try {
					for(Animation15_RotatingFlame orbit : orbits) {
						orbit.cancel();
						if(main.getAnimationHandler().getEntities().contains(orbit.getArmorStand())) {
							ArmorStand armorStandOrbit = orbit.getArmorStand();
							if(armorStandOrbit != null) armorStandOrbit.remove();
							main.getAnimationHandler().getEntities().remove(armorStandOrbit);
						}
					}
				} catch(IllegalStateException | NullPointerException ignored) {}

				Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.IRONGOLEM_DEATH, 0.5f, 0);
				UtilParticles.display(Particles.EXPLOSION_LARGE, armorStand.getLocation().clone().add(0, 1.25, 0), 2);

				armorStand.setGravity(true);
				if(!VersionUtil.supports(17)) {
					Method getHandle = null;
					try {
						getHandle = armorStand.getClass().getMethod("getHandle");
						Object armorS = getHandle.invoke(armorStand);
						Field field = armorS.getClass().getField("noclip");
						field.setAccessible(true);
						field.setBoolean(armorS, true);
					} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
						e.printStackTrace();
					}
				}

			} else if(time > 150 && time < 165) {
				UtilParticles.display(Particles.FLAME, 0.10F, 0.25F, 0.15F, armorStand.getLocation().clone().add(0, 1.5, 0), 5);
			}

			if(time == 163) {
				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				Sounds.playSound(armorStand.getLocation(), Sounds.MySound.EXPLODE, 0.5F, 1F);
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.BURST, colors.get(0), colors.get(1));
			}

			if(time == 165) {
				cubeletBox.setLastReward(reward);
				main.getHologramImplementation().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;
			}

			if(time == 185) Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

			if(time == 205) {
				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);
			}

			if(time > 165 && time < 305) {
				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			}

			if(time == 305) {
				stop();

				main.getCubeletRewardHandler().giveReward(cubeletBox, reward);
				MessageUtils.sendLootMessage(cubeletBox, cubeletType, reward);

				cubeletBox.setState(CubeletBoxState.EMPTY);
				cubeletBox.setPlayerOpening(null);
				main.getHologramImplementation().reloadHologram(cubeletBox);
			}

			time++;
		}
	}
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		blocks = new Animation15_Blocks(box.getLocation());
		blocks.runTaskTimer(main, 0L, 6L);

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.BLACK, Color.ORANGE);

		corner1 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner2 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner3 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.95);
		corner4 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.95);
		boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

		armorStand = ASSpawner.spawn(main, cubeletBox.getLocation().clone().add(0.5, -0.3, 0.5),
				SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTlhNWExZTY5YjRmODEwNTYyNTc1MmJjZWUyNTM0MDY2NGIwODlmYTFiMmY1MjdmYTkxNDNkOTA2NmE3YWFkMiJ9fX0="), false, false, true);
		armorStandLocation = armorStand.getLocation();
		main.getAnimationHandler().getEntities().add(armorStand);
		Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.FIREWORK_LAUNCH, 0.5f, 0);

		spiral = new Animation15_Spiral(armorStand);
		spiral.runTaskTimer(main, 0L, 1L);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			reward = main.getCubeletRewardHandler().processReward(cubeletType);
		});
	}
	
	public void stop() {
		if(serpent != null) serpent.cancel();
		if(blackHole != null) blackHole.cancel();
		if(hologramAnimation != null) hologramAnimation.canncel();

		try {
			for(Animation15_RotatingFlame orbit : orbits) {
				orbit.cancel();
				if(main.getAnimationHandler().getEntities().contains(orbit.getArmorStand())) {
					ArmorStand armorStandOrbit = orbit.getArmorStand();
					if(armorStandOrbit != null) armorStandOrbit.remove();
					main.getAnimationHandler().getEntities().remove(armorStandOrbit);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		spiral.cancel();

		blocks.cancel();
		if(blocks != null) blocks.restore();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getEntities().remove(armorStand);
		}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}
	
}
