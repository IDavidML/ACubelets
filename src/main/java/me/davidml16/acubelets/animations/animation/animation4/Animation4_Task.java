package me.davidml16.acubelets.animations.animation.animation4;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

public class Animation4_Task extends Animation {

	public Animation4_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private Animation4_Music music;
	private Animation4_Blocks blocks;
	private Animation4_Arch arch;
	private Animation4_ArchSounds archSounds;
	private Animation4_EntityPackets entityPackets;
	private Animation4_3x3Particles particles;

	private LivingEntity pigman;
	private Location pigmanLocation;

	@Override
	public void onTick(int time) {

		if(time == 45) {

			arch.runTaskTimer(getMain(), 0L, 1L);
			archSounds.runTaskTimer(getMain(), 0L, 5L);

		} else if(time == 95) {

			archSounds.cancel();

		} else if(time == 100) {

			music.runTaskTimer(getMain(), 0L, 5L);

			armorStand = ASSpawner.spawn(getMain(), getCubeletBox(), getCubeletType(), false);
			armorStandLocation = armorStand.getLocation();
			getMain().getAnimationHandler().getEntities().add(armorStand);

		} else if(time > 100 && time < 140) {

			if (armorStand != null) {

				if(time < 139)armorStandLocation.add(0, 0.025, 0);
				armorStand.teleport(armorStandLocation);
				armorStand.setHeadPose(armorStand.getHeadPose().add(0, 0.16, 0));

			}

		} else if(time == 145) {

			music.cancel();
			pigman = (LivingEntity) getCubeletBox().getLocation().getWorld().spawnEntity(getLocationRotation(), EntityType.PIG_ZOMBIE);

			if(XMaterial.supports(9)) pigman.setCollidable(false);
			((PigZombie )pigman).setBaby(false);
			((PigZombie )pigman).setAngry(false);
			pigman.setRemoveWhenFarAway(false);
			pigman.setMetadata("ACUBELETS", new FixedMetadataValue(getMain(), Boolean.TRUE));

			NBTEditor.set( pigman, ( byte ) 1, "Silent" );
			NBTEditor.set( pigman, ( byte ) 1, "Invulnerable" );

			particles = new Animation4_3x3Particles(pigman);
			particles.runTaskTimer(getMain(), 0L, 1L);

		} else if(time == 155) {

			NBTEditor.set( pigman, ( byte ) 1, "NoAI" );

			pigmanLocation = pigman.getLocation();
			pigmanLocation.setYaw(getCubeletBox().getRotation().value);
			pigmanLocation.setPitch(0);
			pigman.teleport(pigmanLocation);

		} else if(time == 195) {

			entityPackets = new Animation4_EntityPackets(pigman, armorStand, getCubeletBox());
			entityPackets.runTaskTimer(getMain(), 0L, 5L);

		} else if(time == 293) {

			doPreRewardReveal();

		} else if(time == 295) {

			doRewardReveal();

		} else if(time == 335) {

			doRewardDuplication();

		} else if(time > 295 && time < 435) {

			doShowBoxParticles();

		} else if(time == 435) {

			stop();

		}

	}

	@Override
	public void onStart() {

		music = new Animation4_Music(getCubeletBox().getLocation());

		blocks = new Animation4_Blocks(getCubeletBox().getLocation());
		blocks.runTaskTimer(getMain(), 0L, 6L);

		arch = new Animation4_Arch(getCubeletBox().getLocation());

		archSounds = new Animation4_ArchSounds(getCubeletBox().getLocation());

		setColors(Arrays.asList(Color.RED, Color.RED));

	}

	@Override
	public void onStop() {

		blocks.cancel();

		try {
			music.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			arch.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			archSounds.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			entityPackets.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			particles.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(blocks != null) blocks.restore();

		if(pigman != null) pigman.remove();

		if(getMain().getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			getMain().getAnimationHandler().getEntities().remove(armorStand);
		}

	}

	@Override
	public void onPreRewardHologram() {

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 1.50, 0.5),
				FireworkEffect.Type.BALL_LARGE,
				getColors().get(0),
				getColors().get(1)
		);

	}

	@Override
	public void onRewardReveal() {

		pigman.remove();
		music.cancel();
		particles.cancel();
		entityPackets.cancel();

		Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.ZOMBIE_WOODBREAK, 0.5F, 1F);

		armorStand.remove();
		armorStand = null;

	}

}
