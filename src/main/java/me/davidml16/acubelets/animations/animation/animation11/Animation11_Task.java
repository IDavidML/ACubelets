package me.davidml16.acubelets.animations.animation.animation11;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Animation11_Task extends Animation {

	public Animation11_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private Set<Animation11_Planet> planets = new HashSet<>();

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

	@Override
	public void onTick(int time) {

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

			cancelRunnable("spiral");

			int randomPoints = r_i(160, 360);

			Animation11_Planet mercury = new Animation11_Planet(getMain(), PLANET_MERCURY, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
					1.0f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));

			mercury.runTaskTimer(getMain(), 0L, 1L);
			planets.add(mercury);
			removeLater(mercury, 115);

		} else if(time == 42) {

			int randomPoints = r_i(160, 360);

			Animation11_Planet venus = new Animation11_Planet(getMain(), PLANET_VENUS, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
					1.5f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));

			venus.runTaskTimer(getMain(), 0L, 1L);
			planets.add(venus);
			removeLater(venus, 110);

		} else if(time == 44) {

			int randomPoints = r_i(160, 360);

			Animation11_Planet earth = new Animation11_Planet(getMain(), PLANET_EARTH, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
					2.0f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));

			earth.runTaskTimer(getMain(), 0L, 1L);
			planets.add(earth);
			removeLater(earth, 105);

		} else if(time == 46) {

			int randomPoints = r_i(160, 360);

			Animation11_Planet mars = new Animation11_Planet(getMain(), PLANET_MARS, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
					2.5f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));

			mars.runTaskTimer(getMain(), 0L, 1L);
			planets.add(mars);
			removeLater(mars, 100);

		} else if(time == 48) {

			int randomPoints = r_i(160, 360);

			Animation11_Planet jupiter = new Animation11_Planet(getMain(), PLANET_JUPITER, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
					3.0f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));

			jupiter.runTaskTimer(getMain(), 0L, 1L);
			planets.add(jupiter);
			removeLater(jupiter, 95);

		} else if(time == 50) {

			int randomPoints = r_i(160, 360);

			Animation11_Planet saturn = new Animation11_Planet(getMain(), PLANET_SATURN, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
					3.5f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));

			saturn.runTaskTimer(getMain(), 0L, 1L);
			planets.add(saturn);
			removeLater(saturn, 90);

		} else if(time == 52) {

			int randomPoints = r_i(160, 360);

			Animation11_Planet uranus = new Animation11_Planet(getMain(), PLANET_URANUS, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
					4.0f, randomPoints, true,  r_d(0.05, 0.2), r_i(0, randomPoints));

			uranus.runTaskTimer(getMain(), 0L, 1L);
			planets.add(uranus);
			removeLater(uranus, 85);

		} else if(time == 54) {

			int randomPoints = r_i(160, 360);

			Animation11_Planet neptune = new Animation11_Planet(getMain(), PLANET_NEPTUNE, armorStandLocation.clone().add(0, r_d(-1, 2), 0),
					4.5f, randomPoints, true, r_d(0.05, 0.2), r_i(0, randomPoints));

			neptune.runTaskTimer(getMain(), 0L, 1L);
			planets.add(neptune);
			removeLater(neptune, 80);

		}

		if (time == 160) {

			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.IRONGOLEM_DEATH, 0.5f, 0);
			UtilParticles.display(Particles.EXPLOSION_LARGE, armorStand.getLocation().clone().add(0, 1.25, 0), 2);

			armorStand.setGravity(true);
			ASSpawner.setEntityNoclip(armorStand);

		} else if(time > 160 && time < 173) {

			UtilParticles.display(Particles.FLAME, 0.10F, 0.25F, 0.15F, armorStand.getLocation().clone().add(0, 1.5, 0), 5);

		}

		if(time == 171) {

			doPreRewardReveal();

		}

	}

	@Override
	public void onStart() {

		armorStand = ASSpawner.spawn(getMain(), getCubeletBox(), PLANET_SUN, false, false, getBoxLocation().clone().add(0, -1.25, 0));
		getMain().getAnimationHandler().getEntities().add(armorStand);

		armorStandLocation = armorStand.getLocation();
		Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.FIREWORK_LAUNCH, 0.5f, 0);

		addRunnable("spiral", new Animation11_Spiral(armorStand));
		startRunnable("spiral", 0L, 1L);

		setColors(Arrays.asList(Color.PURPLE, Color.FUCHSIA));

	}

	@Override
	public void onStop() {

		cancelRunnables();

		try {
			for(Animation11_Planet planet : planets) {
				planet.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(planet.getArmorStand())) {
					ArmorStand armorStandPlanet = planet.getArmorStand();
					if(armorStandPlanet != null) armorStandPlanet.remove();
					getMain().getAnimationHandler().getEntities().remove(armorStandPlanet);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(getMain().getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			getMain().getAnimationHandler().getEntities().remove(armorStand);
		}

	}

	@Override
	public void onPreRewardReveal() {

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 2, 0.5),
				FireworkEffect.Type.BALL_LARGE,
				getColors().get(0),
				getColors().get(1)
		);

		Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.EXPLODE, 0.5f, 0);

	}

	@Override
	public void onRewardReveal() {

		armorStand.remove();
		armorStand = null;

	}

	private int r_i(int min, int max) {
	    return RANDOM.nextInt(max - min) + min;
    }

	private double r_d(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

	public void removeLater(Animation11_Planet planet, long delay) {
		Bukkit.getScheduler().runTaskLater(getMain(), () -> {
			planet.cancel();
			UtilParticles.display(Particles.SNOW_SHOVEL, planet.getArmorStand().getLocation().clone().add(0, 1, 0), 5);
			Sounds.playSound(planet.getArmorStand().getLocation(), Sounds.MySound.LAVA_POP, 0.5F, 1);
			if(getMain().getAnimationHandler().getEntities().contains(planet.getArmorStand())) {
				ArmorStand armorStandPlanet = planet.getArmorStand();
				if(armorStandPlanet != null) armorStandPlanet.remove();
				getMain().getAnimationHandler().getEntities().remove(armorStandPlanet);
			}
		}, delay);
	}
	
}
