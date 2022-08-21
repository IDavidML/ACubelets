package me.davidml16.acubelets.animations.animation.animation12;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;

import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Animation12_Task extends Animation {

	public Animation12_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand glassStand, liquidStand;
	private Location glassLocation, liquidLocation;

	private List<ItemStack> wools = new ArrayList<>();
	private int actualWool = 0;

	private Set<Animation12_Orbit> orbits = new HashSet<>();

	private LivingEntity witch;
	private Location witchLocation;

	private double rotSpeed = 0.001;

	@Override
	public void onTick(int time) {

		if(time == 45) {

			glassStand = ASSpawner.spawn(getMain(), getCubeletBox().getLocation().clone().add(0.5, -1.1, 0.5), XMaterial.GLASS.parseItem(), false, false, false);
			glassLocation = glassStand.getLocation();
			getMain().getAnimationHandler().getEntities().add(glassStand);

			liquidStand = ASSpawner.spawn(getMain(), getCubeletBox().getLocation().clone().add(0.5, -0.3, 0.5), XMaterial.RED_WOOL.parseItem(), false, false, true);
			liquidLocation = liquidStand.getLocation();
			getMain().getAnimationHandler().getEntities().add(liquidStand);

			witch = (LivingEntity) getCubeletBox().getLocation().getWorld().spawnEntity(getLocationRotation(0), EntityType.WITCH);

			if (XMaterial.supports(9)) witch.setCollidable(false);
			witch.setRemoveWhenFarAway(false);
			witch.setMetadata("ACUBELETS", new FixedMetadataValue(getMain(), Boolean.TRUE));

			NBTEditor.set(witch, (byte) 1, "Silent");
			NBTEditor.set(witch, (byte) 1, "Invulnerable");
			NBTEditor.set(witch, (byte) 1, "NoAI");

			witchLocation = witch.getLocation();
			witchLocation.setYaw(getCubeletBox().getRotation().value);
			witchLocation.setPitch(0);
			witch.teleport(witchLocation);

			Animation12_Orbit orbit1 = new Animation12_Orbit(getMain(), glassLocation.clone().add(0, 3.5, 0), 3, 90, true, 0);
			orbit1.runTaskTimer(getMain(), 0L, 1L);
			orbits.add(orbit1);

			Animation12_Orbit orbit2 = new Animation12_Orbit(getMain(), glassLocation.clone().add(0, 3.5, 0), 3, 90, true, 45);
			orbit2.runTaskTimer(getMain(), 0L, 1L);
			orbits.add(orbit2);

		}

		if(time > 45 && time < 155) {

			if (glassStand != null) {

				if(time < 94)
					glassLocation.add(0, 0.021, 0);

				if(time > 85)
					glassStand.setHeadPose(glassStand.getHeadPose().add(0, rotSpeed, 0));

				glassStand.teleport(glassLocation);

			}

			if (liquidStand != null) {

				if(time < 94)
					liquidLocation.add(0, 0.021, 0);

				if(time > 85)
					liquidStand.setHeadPose(liquidStand.getHeadPose().add(0, rotSpeed, 0));

				liquidStand.teleport(liquidLocation);

				if(time % 2 == 0) {
					if(time < 94)Sounds.playSound(liquidStand.getLocation(), Sounds.MySound.ORB_PICKUP, 0.5F, (float) ThreadLocalRandom.current().nextDouble(1, 3));
					liquidStand.setHelmet(wools.get(actualWool));
					actualWool++;
					if (actualWool >= wools.size()) actualWool = 0;
				}

			}

			rotSpeed += 0.0030;

		}

		if(time == 100)
			chargeParticles();

		if(time == 153) {

			doPreRewardReveal();

		}

		if(time == 175)
			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

		if(time > 45 && time < 145) {
			UtilParticles.drawParticleLine(getCorner1().clone().add(0, -0.5,0), getCorner2().clone().add(0, -0.5,0), Particles.SPELL_WITCH, 5, 0, 0, 0);
			UtilParticles.drawParticleLine(getCorner2().clone().add(0, -0.5,0), getCorner3().clone().add(0, -0.5,0), Particles.SPELL_WITCH, 5, 0, 0, 0);
			UtilParticles.drawParticleLine(getCorner3().clone().add(0, -0.5,0), getCorner4().clone().add(0, -0.5,0), Particles.SPELL_WITCH, 5, 0, 0, 0);
			UtilParticles.drawParticleLine(getCorner1().clone().add(0, -0.5,0), getCorner4().clone().add(0, -0.5,0), Particles.SPELL_WITCH, 5, 0, 0, 0);
		}

	}

	@Override
	public void onStart() {

		setAnimationBlocks(new Animation12_Blocks(getCubeletBox().getLocation()));
		startAnimationBlocks(0L);

		setColors(Arrays.asList(Color.PURPLE, Color.FUCHSIA));

		wools.add(XMaterial.WHITE_WOOL.parseItem());
		wools.add(XMaterial.RED_WOOL.parseItem());
		wools.add(XMaterial.GREEN_WOOL.parseItem());
		wools.add(XMaterial.BLACK_WOOL.parseItem());
		wools.add(XMaterial.BLUE_WOOL.parseItem());
		wools.add(XMaterial.BROWN_WOOL.parseItem());
		wools.add(XMaterial.GRAY_WOOL.parseItem());
		wools.add(XMaterial.LIME_WOOL.parseItem());
		wools.add(XMaterial.MAGENTA_WOOL.parseItem());
		wools.add(XMaterial.ORANGE_WOOL.parseItem());
		wools.add(XMaterial.PINK_WOOL.parseItem());
		wools.add(XMaterial.PURPLE_WOOL.parseItem());
		wools.add(XMaterial.YELLOW_WOOL.parseItem());

		addRunnable("charge", new Animation12_Charge(getCubeletBox().getLocation()));
		startRunnable("charge", 105L, 3L);

	}

	@Override
	public void onStop() {

		stopAnimationBlocks();

		if(getMain().getAnimationHandler().getEntities().contains(glassStand)) {
			if(glassStand != null) glassStand.remove();
			getMain().getAnimationHandler().getEntities().remove(glassStand);
		}

		if(getMain().getAnimationHandler().getEntities().contains(liquidStand)) {
			if(liquidStand != null) liquidStand.remove();
			getMain().getAnimationHandler().getEntities().remove(liquidStand);
		}

		try {
			for(Animation12_Orbit orbit : orbits) {
				orbit.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(orbit.getArmorStand())) {
					ArmorStand armorStandOrbit = orbit.getArmorStand();
					if(armorStandOrbit != null) armorStandOrbit.remove();
					getMain().getAnimationHandler().getEntities().remove(armorStandOrbit);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

	}

	@Override
	public void onPreRewardReveal() {

		Sounds.playSound(liquidStand.getLocation(), Sounds.MySound.EXPLODE, 0.5F, 1F);

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 1.50, 0.5),
				FireworkEffect.Type.BALL_LARGE,
				getColors().get(0),
				getColors().get(1)
		);

	}

	@Override
	public void onRewardReveal() {

		witch.remove();

		glassStand.remove();
		glassStand = null;

		liquidStand.remove();
		liquidStand = null;

	}

	private void chargeParticles() {
		Random random = new Random();
		Location loc = liquidStand.getLocation().clone().add(0, 1, 0);
		for (int i = 0; i < 500; i++) {
			Location randomLoc = loc.clone();
			randomLoc.add((random.nextDouble() - 0.5D) / 2.0D, (new Random().nextDouble() - 0.5D) / 2.0D, (random.nextDouble() - 0.5D) / 2.0D);

			Vector vector = randomLoc.toVector().subtract(loc.toVector()).normalize();
			Vector direction = vector.multiply(1.5D + new Random().nextDouble() * 5.0D);

			for(int j = 0; j < 3; j++)
				UtilParticles.display(Particles.PORTAL, direction, loc, 5);

		}
	}
	
}
