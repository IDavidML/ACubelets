package me.davidml16.acubelets.animations.animation.animation5;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Animation5_Task extends Animation {

	public Animation5_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private Set<Animation5_Ball> balls = new HashSet<>();

	private double rotSpeed = 0.1;

	@Override
	public void onTick(int time) {

		if(time == 35) {

			Animation5_Ball ball = new Animation5_Ball(getMain(), getBoxLocation().clone().add(2, 0, 2));
			ball.runTaskTimer(getMain(), 0L, 1L);
			balls.add(ball);

		} else if(time == 45) {

			Animation5_Ball ball = new Animation5_Ball(getMain(), getBoxLocation().clone().add(-2, 0, 2));
			ball.runTaskTimer(getMain(), 0L, 1L);
			balls.add(ball);

		} else if(time == 55) {

			Animation5_Ball ball = new Animation5_Ball(getMain(), getBoxLocation().clone().add(-2, 0, -2));
			ball.runTaskTimer(getMain(), 0L, 1L);
			balls.add(ball);

		} else if(time == 65) {

			Animation5_Ball ball = new Animation5_Ball(getMain(), getBoxLocation().clone().add(2, 0, -2));
			ball.runTaskTimer(getMain(), 0L, 1L);
			balls.add(ball);

		} else if(time == 75) {

			startRunnable("music", 0L, 4L);

			armorStand = ASSpawner.spawn(getMain(), getCubeletBox(), getCubeletType(), false);
			armorStandLocation = armorStand.getLocation();
			getMain().getAnimationHandler().getEntities().add(armorStand);

		} else if(time > 75 && time < 175) {

			if(armorStand != null) {

				if (time <= 125)
					armorStandLocation.add(0, 0.02, 0);

				armorStand.teleport(armorStandLocation);
				armorStand.setHeadPose(armorStand.getHeadPose().add(0, rotSpeed, 0));
				rotSpeed += 0.0030;

			}

		}

		if(time == 173) {

			doPreRewardReveal();

		}

	}

	@Override
	public void onStart() {

		setAnimationBlocks(new Animation5_Blocks(getCubeletBox().getLocation()));
		startAnimationBlocks(0L);

		addRunnable("music", new Animation5_Music(getCubeletBox().getLocation()));

		setColors(Arrays.asList(Color.YELLOW, Color.WHITE));
		
	}

	@Override
	public void onStop() {

		cancelRunnables();

		try {
			for(Animation5_Ball ball : balls) {
				ball.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(ball.getArmorStand())) {
					ArmorStand ballStand = ball.getArmorStand();
					if(ballStand != null) ballStand.remove();
					getMain().getAnimationHandler().getEntities().remove(ballStand);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		stopAnimationBlocks();

		if(getMain().getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			getMain().getAnimationHandler().getEntities().remove(armorStand);
		}

	}

	@Override
	public void onPreRewardReveal() {

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 1.50, 0.5),
				FireworkEffect.Type.BALL_LARGE,
				getColors().get(0),
				getColors().get(1)
		);

	}

	@Override
	public void onRewardReveal() {

		cancelRunnable("music");

		armorStand.remove();
		armorStand = null;

		for(Animation5_Ball ball : balls) {

			ball.cancel();

			if(getMain().getAnimationHandler().getEntities().contains(ball.getArmorStand())) {
				ArmorStand ballStand = ball.getArmorStand();
				if(ballStand != null) ballStand.remove();
				getMain().getAnimationHandler().getEntities().remove(ballStand);
			}

		}

	}

}
