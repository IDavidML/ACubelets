package me.davidml16.acubelets.animations.animation.animation18;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;

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
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Animation18_Task extends Animation {

	public Animation18_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;

	private List<Animation18_Item> items = new ArrayList<>();

	private BukkitTask spawnItemsRunnable, removeItemsRunnable;

	private double boxLocIncrease = -1.50;
	private double circleSize = 0.0D;
	private int circleStep = 0;
	private float rotation;

	@Override
	public void onTick(int time) {

		if (time <= 25) {

			Location newBoxLoc = getBoxLocation().clone();
			this.boxLocIncrease += 0.060D;
			newBoxLoc.add(0.0D, this.boxLocIncrease, 0.0D);
			newBoxLoc.setYaw(rotation);

			UtilParticles.display(Particles.FIREWORKS_SPARK, armorStand.getLocation().clone().add(0, 1.25, 0), 1);

			armorStand.teleport(newBoxLoc);

		} else if (time <= 60) {

			this.boxLocIncrease += 0.040D;

			Location newBoxLoc = getBoxLocation().clone();
			newBoxLoc.add(0.0D, this.boxLocIncrease, 0.0D);

			if (time <= 40)
				this.circleSize += 0.07D;
			else
				this.circleSize -= 0.075D;

			if (this.circleSize < 0.0D)
				this.circleSize = 0.0D;

			List<Location> teleportLocs = LocationUtils.getCircle(newBoxLoc, this.circleSize, 50);
			Location teleportLoc = teleportLocs.get(this.circleStep).clone();

			teleportLoc.setYaw(rotation);

			Sounds.playSound(armorStand.getLocation(), Sounds.MySound.NOTE_PLING, 0.5F, 3F);

			UtilParticles.display(Particles.FIREWORKS_SPARK, armorStand.getLocation().clone().add(0, 1.25, 0), 1);
			armorStand.teleport(teleportLoc);

			this.circleStep++;

			if (this.circleStep == teleportLocs.size())
				this.circleStep = 0;

		}

		if(time == 150)
			spawnItemsRunnable.cancel();

		if(time == 283) {

			doPreRewardReveal();

		}

		if(time == 305)
			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

	}

	@Override
	public void onStart() {

		rotation = getRotation(false).value;

		setColors(Arrays.asList(Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.ORANGE, Color.FUCHSIA, Color.AQUA, Color.WHITE));

		armorStand = ASSpawner.spawn(getMain(), getCubeletBox().getLocation().clone().add(0.5, -1.5, 0.5),
				XMaterial.CHEST.parseItem(), false, false, false);

		getMain().getAnimationHandler().getEntities().add(armorStand);

		spawnItemsRunnable = Bukkit.getScheduler().runTaskTimer(getMain(), () -> {
			if(items.size() < 9) {
				Animation18_Item item = new Animation18_Item(getMain(), getMain().getCubeletRewardHandler().processReward(getCubeletType()),
						armorStand.getLocation().clone().add(0, 0.75, 0));
				item.runTaskTimer(getMain(), 0L, 1L);
				items.add(item);
			}
		}, 65, 8);

		removeItemsRunnable = Bukkit.getScheduler().runTaskTimer(getMain(), () -> {
			if(items.size() > 1) {
				removeRandomItem();
			} else {
				setReward(items.get(0).getReward());
			}
		}, 200, 5);

	}

	@Override
	public void onStop() {

		if(spawnItemsRunnable != null) spawnItemsRunnable.cancel();
		if(removeItemsRunnable != null) removeItemsRunnable.cancel();

		try {
			for(Animation18_Item item : items) {
				item.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(item.getArmorStandItem())) {
					Entity entity = item.getArmorStandItem();
					if(entity != null) entity.remove();
					getMain().getAnimationHandler().getEntities().remove(entity);
				}
				if(getMain().getAnimationHandler().getEntities().contains(item.getArmorStandName())) {
					Entity entity = item.getArmorStandName();
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

		spawnItemsRunnable.cancel();
		removeItemsRunnable.cancel();

		try {
			for(Animation18_Item item : items) {
				item.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(item.getArmorStandItem())) {
					Entity entity = item.getArmorStandItem();
					if(entity != null) entity.remove();
					getMain().getAnimationHandler().getEntities().remove(entity);
				}
				if(getMain().getAnimationHandler().getEntities().contains(item.getArmorStandName())) {
					Entity entity = item.getArmorStandName();
					if(entity != null) entity.remove();
					getMain().getAnimationHandler().getEntities().remove(entity);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

	}

	public void removeRandomItem() {
		Animation18_Item item = items.get((int) (Math.random() * items.size()));
		item.cancel();
		if(getMain().getAnimationHandler().getEntities().contains(item.getArmorStandItem())) {
			Entity entity = item.getArmorStandItem();
			if(entity != null) entity.remove();
			getMain().getAnimationHandler().getEntities().remove(entity);
		}
		if(getMain().getAnimationHandler().getEntities().contains(item.getArmorStandName())) {
			Entity entity = item.getArmorStandName();
			if(entity != null) entity.remove();
			getMain().getAnimationHandler().getEntities().remove(entity);
		}
		items.remove(item);
	}
	
}
