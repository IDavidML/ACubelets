package me.davidml16.acubelets.animations.seasonal.galaxy;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.animations.normal.animation10.Animation10_Spiral;
import me.davidml16.acubelets.animations.seasonal.summer.AnimationSummer_Ball;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.rewards.PermissionReward;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AnimationGalaxy_Task implements Animation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public AnimationGalaxy_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private ArmorStand armorStand;
	private CubeletBox cubeletBox;
	private CubeletType cubeletType;
	private AnimationGalaxy_Spiral spiral;
	private List<Color> colors;

	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;

	private Set<AnimationGalaxy_Planet> planets = new HashSet<>();

	private Location corner1, corner2, corner3, corner4;

	private Location boxLocation, armorStandLocation;

	private Reward reward;

	private RepeatingTask hologramAnimation;

    private final static Random RANDOM = new Random();

	private static final ItemStack PLANET_SUN = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTkwYjYwZGZjZGE2YzBkNDRjMWZhMTMzMDJjZWZjZjhiMjM3ZjgxZDdlZjliNzIwNDFmNmJlYWE4M2M0ZWQwNyJ9fX0=");
	private static final ItemStack PLANET_MERCURY = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzE3OGYyMzU3NjhlZDJmMGYxYTA3YWIwMmI5MmY1OTljYzQyYzc0MWQyZjczM2U1MzY4YjVhMTA0ODRiM2NiIn19fQ==");
	private static final ItemStack PLANET_VENUS = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMGVmMTQ3ZGRjOTA4ZTY4MjVjMjI5OTk3YWE1Mjk3NjFmNTE2OTFhMTFjOTU1MTI5YTIzMzYzMmQ1NTQ4NzVlIn19fQ==");
	private static final ItemStack PLANET_EARTH = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY5MTk2YjMzMGM2Yjg5NjJmMjNhZDU2MjdmYjZlY2NlNDcyZWFmNWM5ZDQ0Zjc5MWY2NzA5YzdkMGY0ZGVjZSJ9fX0=");
    private static final ItemStack PLANET_MARS = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM3ODU3ZTE2OWVkMzdiMjQ4OTVjM2ZkZTQyNjJkYWU2ZTg3NDI4NjFlYjczZWRhMTU0M2NiNGMwM2E2N2UzIn19fQ==");
    private static final ItemStack PLANET_JUPITER = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhhYWE4YTM1NjFlODBlZjFmOTU2MWYxNzIxMWU3NzBkZTE4YTlmOThjMjY5MWVjZjlkNjk2NTU5YTFiOTE4YyJ9fX0=");
    private static final ItemStack PLANET_SATURN = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjY1Y2QzYTI0ZjE5MzM3MWVlYmFjOWE3MWM0OGY0MDhhOTM1YWZjNGI0MzVmMWZiN2I5ODQzZTY1ODcyOThmIn19fQ==");
    private static final ItemStack PLANET_URANUS = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWE2ZGQ3NWY0MWU0MjY4ZTBhMTI2OTA1MDkwN2FhNjc0NmZmZDM3YTRhOTI5ZTczMjUyNDY0MmMzMzZiYyJ9fX0=");
    private static final ItemStack PLANET_NEPTUNE = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdkNjZmOTM5MDlhNmQ0NjQxYzY1MzA4MmUwNDc0OTY5MWRlODJjZjc3MjMyYmQyMGFiMzJhZGY0ZiJ9fX0=");

	class Task implements Runnable {

		int time = 0;
		@Override
		public void run() {

			if (time > 0 && time < 10) {
				Objects.requireNonNull(armorStandLocation).add(0, 0.20, 0);
				Objects.requireNonNull(armorStand).setHeadPose(armorStand.getHeadPose().add(0, 0.17, 0));
			} else if (time > 10 && time < 20) {
				Objects.requireNonNull(armorStandLocation).add(0, 0.15, 0);
				Objects.requireNonNull(armorStand).setHeadPose(armorStand.getHeadPose().add(0, 0.14, 0));
			} else if (time > 20 && time < 30) {
				Objects.requireNonNull(armorStandLocation).add(0, 0.10, 0);
				Objects.requireNonNull(armorStand).setHeadPose(armorStand.getHeadPose().add(0, 0.11, 0));
			} else if (time > 30 && time < 40) {
				Objects.requireNonNull(armorStandLocation).add(0, 0.05, 0);
				Objects.requireNonNull(armorStand).setHeadPose(armorStand.getHeadPose().add(0, 0.08, 0));
			} else {
				if(armorStand != null) {
					Objects.requireNonNull(armorStand).setHeadPose(armorStand.getHeadPose().add(0, 0.05, 0));
					if(time > 40)
						UtilParticles.display(Particles.FLAME, 0.25F, 0.25F, 0.25F, armorStand.getLocation().clone().add(0, 1.75, 0), 1);
				}
			}

			if(time < 160)
				armorStand.teleport(armorStandLocation);

			if(time == 40) {
				spiral.cancel();
				int randomPoints = r_i(160, 360);
				AnimationGalaxy_Planet mercury = new AnimationGalaxy_Planet(main, PLANET_MERCURY, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
                        1.0f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));
				mercury.runTaskTimer(main, 0L, 1L);
				planets.add(mercury);
				removeLater(mercury, 115);
			} else if(time == 42) {
				int randomPoints = r_i(160, 360);
				AnimationGalaxy_Planet venus = new AnimationGalaxy_Planet(main, PLANET_VENUS, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
                        1.5f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));
				venus.runTaskTimer(main, 0L, 1L);
				planets.add(venus);
				removeLater(venus, 110);
			} else if(time == 44) {
				int randomPoints = r_i(160, 360);
				AnimationGalaxy_Planet earth = new AnimationGalaxy_Planet(main, PLANET_EARTH, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
                        2.0f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));
				earth.runTaskTimer(main, 0L, 1L);
				planets.add(earth);
				removeLater(earth, 105);
            } else if(time == 46) {
				int randomPoints = r_i(160, 360);
                AnimationGalaxy_Planet mars = new AnimationGalaxy_Planet(main, PLANET_MARS, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
                        2.5f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));
                mars.runTaskTimer(main, 0L, 1L);
                planets.add(mars);
				removeLater(mars, 100);
            } else if(time == 48) {
				int randomPoints = r_i(160, 360);
                AnimationGalaxy_Planet jupiter = new AnimationGalaxy_Planet(main, PLANET_JUPITER, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
                        3.0f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));
                jupiter.runTaskTimer(main, 0L, 1L);
                planets.add(jupiter);
				removeLater(jupiter, 95);
            } else if(time == 50) {
				int randomPoints = r_i(160, 360);
                AnimationGalaxy_Planet saturn = new AnimationGalaxy_Planet(main, PLANET_SATURN, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
                        3.5f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));
                saturn.runTaskTimer(main, 0L, 1L);
                planets.add(saturn);
				removeLater(saturn, 90);
            } else if(time == 52) {
				int randomPoints = r_i(160, 360);
                AnimationGalaxy_Planet uranus = new AnimationGalaxy_Planet(main, PLANET_URANUS, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
                        4.0f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));
                uranus.runTaskTimer(main, 0L, 1L);
                planets.add(uranus);
				removeLater(uranus, 85);
            } else if(time == 54) {
				int randomPoints = r_i(160, 360);
                AnimationGalaxy_Planet neptune = new AnimationGalaxy_Planet(main, PLANET_NEPTUNE, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
                        4.5f, randomPoints, true, r_d(0.05, 0.2), r_i(0, randomPoints));
                neptune.runTaskTimer(main, 0L, 1L);
                planets.add(neptune);
				removeLater(neptune, 80);
            }

			if (time == 160) {
				Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.IRONGOLEM_DEATH, 0.5f, 0);
				UtilParticles.display(Particles.EXPLOSION_LARGE, armorStand.getLocation().clone().add(0, 1.25, 0), 2);

				armorStand.setGravity(true);
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

			} else if(time > 160 && time < 173) {
				UtilParticles.display(Particles.FLAME, 0.10F, 0.25F, 0.15F, armorStand.getLocation().clone().add(0, 1.5, 0), 5);
			}

			if(time == 171) {
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 2, 0.5), FireworkEffect.Type.BALL_LARGE, colors.get(0), colors.get(1));
				Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.EXPLODE, 0.5f, 0);
			} else if(time == 173) {
				cubeletBox.setLastReward(reward);
				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				main.getHologramHandler().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;
			} else if(time == 213) {
				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);
			} else if (time > 173 && time < 293) {
				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			} else if(time >= 293) {
				stop();

				main.getCubeletRewardHandler().giveReward(cubeletBox, reward);
				MessageUtils.sendLootMessage(cubeletBox, cubeletType, reward);

				cubeletBox.setState(CubeletBoxState.EMPTY);
				cubeletBox.setPlayerOpening(null);
				main.getHologramHandler().reloadHologram(cubeletBox);
			}

			time++;
		}
	}
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		this.cubeletType = type;
		this.cubeletBox = box;
		boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

		armorStand = ASSpawner.spawn(main, box, PLANET_SUN, false, false, boxLocation.clone().add(0, -1.25, 0));
		armorStandLocation = armorStand.getLocation();
		Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.FIREWORK_LAUNCH, 0.5f, 0);

		spiral = new AnimationGalaxy_Spiral(armorStand);
		spiral.runTaskTimer(main, 0L, 1L);

		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.PURPLE, Color.FUCHSIA);

		corner1 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner2 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner3 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.95);
		corner4 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.95);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);
		main.getAnimationHandler().getEntities().add(armorStand);

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			reward = main.getCubeletRewardHandler().processReward(cubeletType);
		});
	}
	
	public void stop() {
		spiral.cancel();

		main.getAnimationHandler().getTasks().remove(this);

		if(hologramAnimation != null) hologramAnimation.canncel();

		Bukkit.getServer().getScheduler().cancelTask(id);

		try {
			for(AnimationGalaxy_Planet planet : planets) {
				planet.cancel();
				if(main.getAnimationHandler().getEntities().contains(planet.getArmorStand())) {
					ArmorStand armorStandPlanet = planet.getArmorStand();
					if(armorStandPlanet != null) armorStandPlanet.remove();
					main.getAnimationHandler().getEntities().remove(armorStandPlanet);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(main.getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getEntities().remove(armorStand);
		}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}

	private int r_i(int min, int max) {
	    return RANDOM.nextInt(max - min) + min;
    }

	private double r_d(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

	public void removeLater(AnimationGalaxy_Planet planet, long delay) {
		Bukkit.getScheduler().runTaskLater(main, () -> {
			planet.cancel();
			UtilParticles.display(Particles.SNOW_SHOVEL, planet.getArmorStand().getLocation().clone().add(0, 1, 0), 5);
			Sounds.playSound(planet.getArmorStand().getLocation(), Sounds.MySound.LAVA_POP, 0.5F, 1);
			if(main.getAnimationHandler().getEntities().contains(planet.getArmorStand())) {
				ArmorStand armorStandPlanet = planet.getArmorStand();
				if(armorStandPlanet != null) armorStandPlanet.remove();
				main.getAnimationHandler().getEntities().remove(armorStandPlanet);
			}
		}, delay);
	}
	
}
