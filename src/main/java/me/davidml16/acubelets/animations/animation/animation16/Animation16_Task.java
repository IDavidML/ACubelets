package me.davidml16.acubelets.animations.animation.animation16;

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
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class Animation16_Task extends Animation {

	public Animation16_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private List<ItemStack> wools = new ArrayList<>();
	private int actualWool = 0;

	private Set<Animation16_Sheep> sheeps = new HashSet<>();

	private Animation16_Charge charge;

	private double rotSpeed = 0.001;

	@Override
	public void onTick(int time) {

		if(time == 6) {

			Animation16_Sheep sheep1 = new Animation16_Sheep(getMain(), getCubeletBox().getLocation().clone().add(0.5, -0.5, 0.5),
					3.5f, 90, 0);
			sheep1.runTaskTimer(getMain(), 0L, 1L);
			sheeps.add(sheep1);

			Animation16_Sheep sheep2 = new Animation16_Sheep(getMain(), getCubeletBox().getLocation().clone().add(0.5, -0.5, 0.5),
					3.5f, 90, 30);
			sheep2.runTaskTimer(getMain(), 0L, 1L);
			sheeps.add(sheep2);

			Animation16_Sheep sheep3 = new Animation16_Sheep(getMain(), getCubeletBox().getLocation().clone().add(0.5, -0.5, 0.5),
					3.5f, 90, 60);
			sheep3.runTaskTimer(getMain(), 0L, 1L);
			sheeps.add(sheep3);

		}

		if(time > 0 && time < 105) {

			if (armorStand != null) {

				if(time < 49)
					armorStandLocation.add(0, 0.021, 0);

				if(time > 40)
					armorStand.setHeadPose(armorStand.getHeadPose().add(0, rotSpeed, 0));

				armorStand.teleport(armorStandLocation);

				if(time % 2 == 0) {

					if(time < 49)
						Sounds.playSound(armorStand.getLocation(), Sounds.MySound.ORB_PICKUP, 0.5F, (float) 1);

					armorStand.setHelmet(wools.get(actualWool));

					actualWool++;

					if (actualWool >= wools.size())
						actualWool = 0;

				}

			}

			rotSpeed += 0.0030;

		}

		if(time == 50) chargeParticles();

		if(time == 94) removeRandomSheep();
		if(time == 97) removeRandomSheep();
		if(time == 100) removeRandomSheep();

		if(time == 103) {

			doPreRewardReveal();

		}

		if(time == 125)
			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

	}

	@Override
	public void onStart() {

		setColors(Arrays.asList(Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.ORANGE, Color.FUCHSIA, Color.AQUA, Color.WHITE));

		charge = new Animation16_Charge(getCubeletBox().getLocation());
		charge.runTaskTimer(getMain(), 50L, 3L);

		armorStand = ASSpawner.spawn(
				getMain(),
				getCubeletBox().getLocation().clone().add(0.5, -0.3, 0.5),
				SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmI5OTYwZWFkZTJkY2I2ZTc0NGM3ZDA4NGRlZjAwNTk5ZmU3MzI3ZTM5NzNjZDJjYTBhZjRhMmExZTZlYWMwOCJ9fX0="),
				false,
				false,
				true
		);
		getMain().getAnimationHandler().getEntities().add(armorStand);

		armorStandLocation = armorStand.getLocation();

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

	}

	@Override
	public void onStop() {

		charge.cancel();

		try {
			for(Animation16_Sheep sheep : sheeps) {
				sheep.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(sheep.getEntity())) {
					Entity entity = sheep.getEntity();
					if(entity != null) entity.remove();
					getMain().getAnimationHandler().getEntities().remove(entity);
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

		charge.cancel();

		Sounds.playSound(armorStand.getLocation(), Sounds.MySound.EXPLODE, 0.5F, 1F);

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 1.50, 0.5),
				FireworkEffect.Type.BALL_LARGE,
				getColors().toArray(new Color[getColors().size()])
		);

	}

	@Override
	public void onRewardReveal() {

		armorStand.remove();
		armorStand = null;

	}

	private void chargeParticles() {
		Random random = new Random();
		Location loc = armorStand.getLocation().clone().add(0, 1, 0);
		for (int i = 0; i < 500; i++) {
			Location randomLoc = loc.clone();
			randomLoc.add((random.nextDouble() - 0.5D) / 2.0D, (new Random().nextDouble() - 0.5D) / 2.0D, (random.nextDouble() - 0.5D) / 2.0D);

			org.bukkit.util.Vector vector = randomLoc.toVector().subtract(loc.toVector()).normalize();
			Vector direction = vector.multiply(1.5D + new Random().nextDouble() * 5.0D);

			for(int j = 0; j < 3; j++) {
				UtilParticles.display(Particles.PORTAL, direction, loc, 5);
			}

		}
	}

	public void removeRandomSheep() {
		for(Animation16_Sheep sheep : sheeps) {
			sheep.cancel();
			if(getMain().getAnimationHandler().getEntities().contains(sheep.getEntity())) {
				Entity entity = sheep.getEntity();
				if(entity != null) {
					getMain().getFireworkUtil().spawn(entity.getLocation(), FireworkEffect.Type.BURST, getColors().toArray(new Color[getColors().size()]));
					entity.remove();
				}
				getMain().getAnimationHandler().getEntities().remove(entity);
			}
			sheeps.remove(sheep);
			break;
		}
	}
	
}
