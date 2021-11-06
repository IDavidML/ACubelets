package me.davidml16.acubelets.animations.animation.animation0;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import org.bukkit.FireworkEffect;

public class Animation0_Task extends Animation {

	public Animation0_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	@Override
	public void onTick(int time) {

		if(time == 3) {

			doPreRewardReveal();

		} else if(time == 5) {

			doRewardReveal();

		} else if(time == 35) {

			doRewardDuplication();

		} else if (time > 5 && time < 125) {

			doShowBoxParticles();

		} else if(time >= 125) {

			stop();

		}

	}

	@Override
	public void onStart() { }

	@Override
	public void onStop() { }

	@Override
	public void onPreRewardHologram() {

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 0.25, 0.5),
				FireworkEffect.Type.BURST,
				getColors().get(0),
				getColors().get(1)
		);

	}

	@Override
	public void onRewardReveal() { }

}
