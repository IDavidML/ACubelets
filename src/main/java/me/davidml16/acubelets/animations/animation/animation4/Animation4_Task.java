package me.davidml16.acubelets.animations.animation.animation4;

import com.cryptomorin.xseries.XMaterial;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.utils.Sounds;
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

	private LivingEntity pigman;
	private Location pigmanLocation;

	@Override
	public void onTick(int time) {

		if(time == 45) {

			startRunnable("arch", 0L, 1L);
			startRunnable("archSounds", 0L, 5L);

		} else if(time == 95) {

			cancelRunnable("archSounds");

		} else if(time == 100) {

			startRunnable("music", 0L, 5L);

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

			cancelRunnable("music");

			if(XMaterial.supports(16))
				pigman = (LivingEntity) getCubeletBox().getLocation().getWorld().spawnEntity(getLocationRotation(3), EntityType.valueOf("ZOMBIFIED_PIGLIN"));
			else
				pigman = (LivingEntity) getCubeletBox().getLocation().getWorld().spawnEntity(getLocationRotation(3), EntityType.valueOf("PIG_ZOMBIE"));

			pigman.setCollidable(false);
			((PigZombie )pigman).setBaby(false);
			((PigZombie )pigman).setAngry(false);
			pigman.setRemoveWhenFarAway(false);
			pigman.setMetadata("ACUBELETS", new FixedMetadataValue(getMain(), Boolean.TRUE));

			NBTEditor.set( pigman, ( byte ) 1, "Silent" );
			NBTEditor.set( pigman, ( byte ) 1, "Invulnerable" );

			addRunnable("particles", new Animation4_3x3Particles(pigman));
			startRunnable("particles", 0L, 1L);

		} else if(time == 155) {

			NBTEditor.set( pigman, ( byte ) 1, "NoAI" );

			pigmanLocation = pigman.getLocation();
			pigmanLocation.setYaw(getCubeletBox().getRotation().value);
			pigmanLocation.setPitch(0);
			pigman.teleport(pigmanLocation);

		} else if(time == 195) {

			addRunnable("entityPackets", new Animation4_EntityPackets(pigman, armorStand, getCubeletBox()));
			startRunnable("entityPackets", 0L, 5L);

		} else if(time == 293) {

			doPreRewardReveal();

		}

	}

	@Override
	public void onStart() {

		addRunnable("music", new Animation4_Music(getCubeletBox().getLocation()));

		setAnimationBlocks(new Animation4_Blocks(getCubeletBox().getLocation()));
		startAnimationBlocks(0L);

		addRunnable("arch", new Animation4_Arch(getCubeletBox().getLocation()));

		addRunnable("archSounds", new Animation4_ArchSounds(getCubeletBox().getLocation()));

		setColors(Arrays.asList(Color.RED, Color.RED));

	}

	@Override
	public void onStop() {

		stopAnimationBlocks();

		cancelRunnables();

		if(pigman != null) pigman.remove();

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

		pigman.remove();

		cancelRunnable("music");
		cancelRunnable("particles");
		cancelRunnable("entityPackets");

		Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.ZOMBIE_WOODBREAK, 0.5F, 1F);

		armorStand.remove();
		armorStand = null;

	}

}
