package me.davidml16.acubelets.animations.animation.animation1;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import org.bukkit.*;

public class Animation1_Task extends Animation {

	public Animation1_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private Location startLocation;

	@Override
	public void onTick(int time) {

		if(!isRewardRevealed() && (time >= 0 && time < 70)) {

			if(time % 2 == 0) {

				getMain().getFireworkUtil().spawn(
						startLocation,
						FireworkEffect.Type.BURST,
						getColors().get(0),
						getColors().get(1)
				);

				startLocation.add(0, -0.75, 0);

			}

		}

		if(time == 68) {

			doPreRewardReveal();

		}

	}

	@Override
	public void onStart() {

		this.startLocation = getCubeletBox().getLocation().clone().add(0.5, 26, 0.5);

	}

	@Override
	public void onStop() { }

	@Override
	public void onPreRewardReveal() {

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 2, 0.5),
				FireworkEffect.Type.BALL_LARGE,
				getColors().get(0),
				getColors().get(1)
		);

	}

	@Override
	public void onRewardReveal() { }

}
