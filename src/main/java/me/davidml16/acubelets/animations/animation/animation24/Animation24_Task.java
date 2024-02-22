package me.davidml16.acubelets.animations.animation.animation24;

import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Animation24_Task extends Animation {

	public Animation24_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;

	private List<Animation24_Item> items = new ArrayList<>();
	private List<Location> circleLocations;

	private BukkitTask spawnItemsRunnable, removeItemsRunnable;

	private double boxLocIncrease = -1.50;
	private int circleInitialStep = 0;
	private float rotation;
	private int speedRate = 0;
	private int increaseSpeedTimer = 0;
	private int reduceSpeedTimer = 0;

	private Reward selectedReward;
	private int rouletteRewardIndex = 0;
	private int rouletteTickTimer = 0;

	@Override
	public void onTick(int time) {

		if (time <= 45) {
			this.boxLocIncrease += 0.085D;
			Location newBoxLoc = getBoxLocation().clone();
			newBoxLoc.add(0.0D, this.boxLocIncrease, 0.0D);
			armorStand.teleport(newBoxLoc);

			UtilParticles.display(Particles.CLOUD, armorStand.getLocation().clone().add(0, 1.25, 0), 1);

		}

		if (time <= 325) {
			armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.26, 0));
		}

		if(time == 90) {
			spawnItemsRunnable.cancel();
			for(Animation24_Item item : items) {
				item.setActive(true);
			}
		}

		if(time > 100 && time <= 155) {
			if(increaseSpeedTimer >= 5) {
				increaseSpeedTimer = 0;
				speedRate++;

				for(Animation24_Item item : items) {
					item.setSpeedRate(speedRate);
				}
			}

			increaseSpeedTimer++;
		}

		for(Animation24_Item item : items) {
			item.Tick();
		}

		if(time > 100 && speedRate > 0) {
			if(rouletteTickTimer >= (13 - speedRate)) {
				rouletteTickTimer = 0;
				Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.NOTE_STICKS, 0.5F, 2F);

				rouletteRewardIndex++;
				if(rouletteRewardIndex >= items.size()) rouletteRewardIndex = 0;

				selectedReward = items.get(rouletteRewardIndex).getReward();
			}

			rouletteTickTimer++;
		}

		if(time > 220) {
			if(reduceSpeedTimer >= 10) {
				reduceSpeedTimer = 0;

				if(speedRate > 0) speedRate--;

				for(Animation24_Item item : items) {
					item.setSpeedRate(speedRate);
				}
			}

			reduceSpeedTimer++;
		}

		if(time == 325) {
			setReward(selectedReward);
			doPreRewardReveal();
		}

		if(time == 345) Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

		if(time == 449) {
			removeItemsRunnable = Bukkit.getScheduler().runTaskTimer(getMain(), () -> {
				if(items.size() > 0) {
					removeRandomItem();
				}
			}, 0, 2);
		}
	}

	@Override
	public void onStart() {

		getCubeletBox().setLocation(getCubeletBox().getLocation().add(0, 2.75, 0));
		getMain().getHologramImplementation().showTextAndIcon(getCubeletBox(), Arrays.asList("", "", "%reward_icon%"), XMaterial.AIR.parseItem());

		rotation = getRotation(false).value;

		circleLocations = LocationUtils.getCircleVertical(getCubeletBox().getOriginalLocation().clone().add(0.5, 3, 0.5), 2.5D, 360, getRotation(false));

		setColors(Arrays.asList(Color.RED, Color.BLACK));

		armorStand = ASSpawner.spawn(getMain(), getCubeletBox().getOriginalLocation().clone().add(0.5, -1.5, 0.5),
				XMaterial.CHEST.parseItem(), false, false, false);

		Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.FIREWORK_LAUNCH, 0.5f, 0);

		getMain().getAnimationHandler().getEntities().add(armorStand);

		spawnItemsRunnable = Bukkit.getScheduler().runTaskTimer(getMain(), () -> {
			if(items.size() < 8) {
				Animation24_Item item = new Animation24_Item(getMain(), getMain().getCubeletRewardHandler().processReward(getCubeletType()), getCubeletBox().getOriginalLocation().clone().add(0.5, 2, 0.5), circleLocations, circleInitialStep);
				item.Tick();

				items.add(item);

				circleInitialStep += 45;
			}
		}, 55, 2);
	}

	@Override
	public void onStop() {
		if(spawnItemsRunnable != null) spawnItemsRunnable.cancel();
		if(removeItemsRunnable != null) removeItemsRunnable.cancel();

		getCubeletBox().resetLocation();

		try {
			for(Animation24_Item item : items) {
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
				FireworkEffect.Type.STAR,
				getColors().toArray(new Color[getColors().size()])
		);

	}

	@Override
	public void onRewardReveal() {
		armorStand.remove();
		armorStand = null;

		spawnItemsRunnable.cancel();

		getCubeletBox().setLocation(getCubeletBox().getOriginalLocation().clone().add(0, 1.65, 0));
	}

	@Override
	public void onRewardDuplication() {
		getCubeletBox().setLocation(getCubeletBox().getOriginalLocation().clone().add(0, 2.5, 0));
	}

	public void removeRandomItem() {
		Animation24_Item item = items.get(0);
		if(getMain().getAnimationHandler().getEntities().contains(item.getArmorStandItem())) {
			Entity entity = item.getArmorStandItem();
			if(entity != null) Sounds.playSound(entity.getLocation(), Sounds.MySound.PISTON_RETRACT, 0.5F, 3);
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
