package me.davidml16.acubelets.animations.animation.animation19;

import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Animation19_Task extends Animation {

	public Animation19_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private List<Animation19_Sword> swords = new ArrayList<>();
	private BukkitTask spawnSwordsRunnable;

	@Override
	public void onTick(int time) {

		if(time > 60 && time < 130)
			Objects.requireNonNull(armorStand).setHeadPose(armorStand.getHeadPose().add(0, 0.26, 0));

		if(time > 60 && time < 80)
			Objects.requireNonNull(armorStandLocation).add(0, 0.35, 0);
		else if(time > 80 && time < 90)
			Objects.requireNonNull(armorStandLocation).add(0, 0.25, 0);
		else if(time > 90 && time < 100)
			Objects.requireNonNull(armorStandLocation).add(0, 0.15, 0);
		else if(time > 100 && time < 110)
			Objects.requireNonNull(armorStandLocation).add(0, 0.05, 0);

		if(time > 60 && time < 130)
			armorStand.teleport(armorStandLocation);

		if(time == 130) {

			Sounds.playSound(armorStand.getLocation(), Sounds.MySound.ANVIL_LAND, 0.5F, 0);
			armorStand.setHelmet(XMaterial.ANVIL.parseItem());

		}

		if(time > 130 && time < 143) {

			Objects.requireNonNull(armorStandLocation).add(0, -0.85, 0);
			armorStand.teleport(armorStandLocation);

		}

		if (time == 60) {

			armorStand = ASSpawner.spawn(getMain(), getCubeletBox(), getCubeletType(), false, false, getBoxLocation().clone().add(0, -1.25, 0));
			getMain().getAnimationHandler().getEntities().add(armorStand);

			armorStandLocation = armorStand.getLocation();
			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.FIREWORK_LAUNCH, 0.5f, 0);

		} else if(time == 143) {

			doPreRewardReveal();

		}

	}

	@Override
	public void onStart() {

		setAnimationBlocks(new Animation19_Blocks(getCubeletBox().getLocation()));
		startAnimationBlocks(0L);

		setColors(Arrays.asList(Color.WHITE, Color.PURPLE));

		spawnSwordsRunnable = Bukkit.getScheduler().runTaskTimer(getMain(), () -> {
			if(swords.size() < 8) {
				Animation19_Sword sword = new Animation19_Sword(getMain(), getCubeletBox().getLocation().clone().add(0.5, 3, 0.5), 2, 66, false, 0);
				sword.runTaskTimer(getMain(), 0L, 1L);
				swords.add(sword);
			}
		}, 5, 8);

	}

	@Override
	public void onStop() {

		stopAnimationBlocks();

		if(spawnSwordsRunnable != null) spawnSwordsRunnable.cancel();

		if(getMain().getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			getMain().getAnimationHandler().getEntities().remove(armorStand);
		}

		try {
			for(Animation19_Sword sword : swords) {
				sword.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(sword.getArmorStand())) {
					ArmorStand swordArmorStand = sword.getArmorStand();
					if(swordArmorStand != null) swordArmorStand.remove();
					getMain().getAnimationHandler().getEntities().remove(swordArmorStand);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

	}

	@Override
	public void onPreRewardReveal() {

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 1.50, 0.5),
				FireworkEffect.Type.BALL_LARGE,
				getColors().get(0),
				getColors().get(1)
		);

		try {
			for(Animation19_Sword sword : swords) {
				sword.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(sword.getArmorStand())) {
					ArmorStand swordArmorStand = sword.getArmorStand();
					if(swordArmorStand != null) swordArmorStand.remove();
					getMain().getAnimationHandler().getEntities().remove(swordArmorStand);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

	}

	@Override
	public void onRewardReveal() {

		if(spawnSwordsRunnable != null) spawnSwordsRunnable.cancel();

		if(armorStand != null) {
			armorStand.remove();
			armorStand = null;
		}

	}

	@Override
	public void onRewardDuplication() {}

}
