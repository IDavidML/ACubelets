package me.davidml16.acubelets.animations.animation.animation14;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Animation14_Task extends Animation {

	public Animation14_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private Set<Animation14_Bee> bees = new HashSet<>();

	private double rotSpeed = 0.001;

	private static final ItemStack BEE = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTlkYzNmMDBlY2FiMjI0OWJiNmExNmM4YzUxMTVjZWI5ZjIzMjA1YTBkNTVjYzBlOWJhYmQyNTYyZjc5NTljNCJ9fX0=");

	@Override
	public void onTick(int time) {

		if(time == 45) {

			armorStand = ASSpawner.spawn(
					getMain(),
					getCubeletBox().getLocation().clone().add(0.5, -0.3, 0.5),
					SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmI5OTYwZWFkZTJkY2I2ZTc0NGM3ZDA4NGRlZjAwNTk5ZmU3MzI3ZTM5NzNjZDJjYTBhZjRhMmExZTZlYWMwOCJ9fX0="),
					false,
					false,
					true
			);

			armorStandLocation = armorStand.getLocation();
			getMain().getAnimationHandler().getEntities().add(armorStand);

		}

		if(time == 6) {

			Animation14_Bee bee = new Animation14_Bee(getMain(), BEE, getCubeletBox().getLocation().clone().add(0.5, 3, 0.5),
					3f, 120, true, 0);

			bee.runTaskTimer(getMain(), 0L, 1L);
			bees.add(bee);

		} else if(time == 12) {

			Animation14_Bee bee = new Animation14_Bee(getMain(), BEE, getCubeletBox().getLocation().clone().add(0.5, 3, 0.5),
					3f, 120, true, 30);

			bee.runTaskTimer(getMain(), 0L, 1L);
			bees.add(bee);

		} else if(time == 18) {

			Animation14_Bee bee = new Animation14_Bee(getMain(), BEE, getCubeletBox().getLocation().clone().add(0.5, 3, 0.5),
					3f, 120, true, 60);

			bee.runTaskTimer(getMain(), 0L, 1L);
			bees.add(bee);

		} else if(time == 24) {

			Animation14_Bee bee = new Animation14_Bee(getMain(), BEE, getCubeletBox().getLocation().clone().add(0.5, 3, 0.5),
					3f, 120, true, 90);

			bee.runTaskTimer(getMain(), 0L, 1L);
			bees.add(bee);

		} else if(time == 30) {

			Animation14_Bee bee = new Animation14_Bee(getMain(), BEE, getCubeletBox().getLocation().clone().add(0.5, 3, 0.5),
					3f, 120, true, 119);

			bee.runTaskTimer(getMain(), 0L, 1L);
			bees.add(bee);

		}

		if(time > 45 && time < 165) {

			if (armorStand != null) {

				if(time < 94)
					armorStandLocation.add(0, 0.021, 0);

				if(time > 85)
					armorStand.setHeadPose(armorStand.getHeadPose().add(0, rotSpeed, 0));

				armorStand.teleport(armorStandLocation);

			}

			rotSpeed += 0.0030;

		}

		if(time == 123) {

			doPreRewardReveal();

		}

		if(time == 145)
			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

	}

	@Override
	public void onStart() {

		setAnimationBlocks(new Animation14_Blocks(getCubeletBox().getLocation()));
		startAnimationBlocks(0L);

		addRunnable("music", new Animation14_Music(getCubeletBox().getLocation()));
		startRunnable("music", 45L, 3L);

		setColors(Arrays.asList(Color.YELLOW, Color.WHITE));

	}

	@Override
	public void onStop() {

		cancelRunnables();

		stopAnimationBlocks();

		try {
			for(Animation14_Bee bee : bees) {
				bee.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(bee.getArmorStand())) {
					ArmorStand beeArmorStand = bee.getArmorStand();
					if(beeArmorStand != null) beeArmorStand.remove();
					getMain().getAnimationHandler().getEntities().remove(beeArmorStand);
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

		cancelRunnable("music");

		Sounds.playSound(armorStand.getLocation(), Sounds.MySound.EXPLODE, 0.5F, 1F);

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 1.50, 0.5),
				FireworkEffect.Type.BALL_LARGE,
				getColors().get(0),
				getColors().get(1)
		);

	}

	@Override
	public void onRewardReveal() {

		armorStand.remove();
		armorStand = null;

	}

}
