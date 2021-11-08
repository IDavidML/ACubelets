package me.davidml16.acubelets.animations.animation.animation7;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.rewards.PermissionReward;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Animation7_Task extends Animation {

	public Animation7_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private Animation7_Music music;
	private Animation7_Blocks blocks;

	private Set<Animation7_Pumpkin> pumpkins = new HashSet<>();
	private Set<Entity> ghosts = new HashSet<>();

	private double rotSpeed = 0.1;

	@Override
	public void onTick(int time) {

		if(time == 35) {

			Animation7_Pumpkin pumpkin = new Animation7_Pumpkin(getMain(), getBoxLocation().clone().add(2, 0, 2), getCubeletBox().getPlayerOpening().getUuid());
			pumpkin.runTaskTimer(getMain(), 0L, 1L);
			pumpkins.add(pumpkin);

		} else if(time == 45) {

			Animation7_Pumpkin pumpkin = new Animation7_Pumpkin(getMain(), getBoxLocation().clone().add(-2, 0, 2), getCubeletBox().getPlayerOpening().getUuid());
			pumpkin.runTaskTimer(getMain(), 0L, 1L);
			pumpkins.add(pumpkin);

		} else if(time == 55) {

			Animation7_Pumpkin pumpkin = new Animation7_Pumpkin(getMain(), getBoxLocation().clone().add(-2, 0, -2), getCubeletBox().getPlayerOpening().getUuid());
			pumpkin.runTaskTimer(getMain(), 0L, 1L);
			pumpkins.add(pumpkin);

		} else if(time == 65) {

			Animation7_Pumpkin pumpkin = new Animation7_Pumpkin(getMain(), getBoxLocation().clone().add(2, 0, -2), getCubeletBox().getPlayerOpening().getUuid());
			pumpkin.runTaskTimer(getMain(), 0L, 1L);
			pumpkins.add(pumpkin);

		} else if(time == 75) {

			music.runTaskTimer(getMain(), 0L, 4L);

			armorStand = ASSpawner.spawn(getMain(), getCubeletBox(), getCubeletType(), false);
			armorStandLocation = armorStand.getLocation();
			getMain().getAnimationHandler().getEntities().add(armorStand);

			getCubeletBox().getLocation().getWorld().strikeLightningEffect(getCubeletBox().getLocation().clone().add(0.5, 1, 0.5));
			Sounds.playSound(armorStandLocation, Sounds.MySound.GHAST_SCREAM2, 1F, 1F);

		} else if(time > 75 && time < 175) {

			if(armorStand != null) {

				if (time <= 125)
					armorStandLocation.add(0, 0.02, 0);

				armorStand.teleport(armorStandLocation);
				armorStand.setHeadPose(armorStand.getHeadPose().add(0, rotSpeed, 0));
				rotSpeed += 0.0030;

			}

			for(Entity entity : ghosts) {
				if(entity instanceof Bat)
					UtilParticles.display(Particles.CLOUD, 0.05f, 0.05f, 0.05f, entity.getLocation().add(0, 1.5, 0), 1);
			}

		}

		ItemStack chestplate = new ItemBuilder(XMaterial.LEATHER_CHESTPLATE.parseItem()).setLeatherArmorColor(Color.WHITE).toItemStack();

		if(time == 75) spawnGhost(chestplate);
		if(time == 78) spawnGhost(chestplate);
		if(time == 81) spawnGhost(chestplate);
		if(time == 84) spawnGhost(chestplate);
		if(time == 87) spawnGhost(chestplate);

		if(time == 115) removeRandomGhost();
		if(time == 125) removeRandomGhost();
		if(time == 135) removeRandomGhost();
		if(time == 145) removeRandomGhost();
		if(time == 155) removeRandomGhost();
		if(time == 165) removeRandomGhost();

		if(time == 173) {

			doPreRewardReveal();

		}

	}

	@Override
	public void onStart() {

		blocks = new Animation7_Blocks(getCubeletBox().getLocation());
		blocks.runTaskTimer(getMain(), 0L, 6L);

		music = new Animation7_Music(getCubeletBox().getLocation());

		setColors(Arrays.asList(Color.ORANGE, Color.BLACK));

	}

	@Override
	public void onStop() {

		blocks.cancel();

		try {
			music.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			for(Animation7_Pumpkin pumpkin : pumpkins) {
				pumpkin.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(pumpkin.getArmorStand())) {
					ArmorStand pumpkinArmorStand = pumpkin.getArmorStand();
					if(pumpkinArmorStand != null) pumpkinArmorStand.remove();
					getMain().getAnimationHandler().getEntities().remove(pumpkinArmorStand);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			for(Entity entity : ghosts) {
				if(entity != null) entity.remove();
				getMain().getAnimationHandler().getEntities().remove(entity);
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(blocks != null) blocks.restore();

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

		for(Entity entity : ghosts) {
			if(entity != null) entity.remove();
			getMain().getAnimationHandler().getEntities().remove(entity);
		}

	}

	@Override
	public void onRewardReveal() {

		music.cancel();

		armorStand.remove();
		armorStand = null;

	}

	public void removeRandomGhost() {
		for(Entity bat : ghosts) {
			if(bat instanceof Bat) {
				Entity passenger = bat.getPassenger();

				UtilParticles.display(Particles.SNOW_SHOVEL, bat.getLocation(), 5);

				if (passenger != null) passenger.remove();
				getMain().getAnimationHandler().getEntities().remove(passenger);
				ghosts.remove(passenger);

				if(bat != null) bat.remove();
				getMain().getAnimationHandler().getEntities().remove(bat);
				ghosts.remove(bat);

				break;
			}
		}
	}

	public void spawnGhost(ItemStack chestplate) {
		Bat bat = getCubeletBox().getLocation().getWorld().spawn(getCubeletBox().getLocation().clone().add(0.5, 1, 0.5), Bat.class);
		ArmorStand ghost = bat.getWorld().spawn(bat.getLocation(), ArmorStand.class);
		ghost.setSmall(true);
		ghost.setGravity(false);
		ghost.setVisible(false);
		ghost.setHelmet(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjhkMjE4MzY0MDIxOGFiMzMwYWM1NmQyYWFiN2UyOWE5NzkwYTU0NWY2OTE2MTllMzg1NzhlYTRhNjlhZTBiNiJ9fX0"));
		ghost.setChestplate(chestplate);
		ghost.setItemInHand(new ItemStack(XMaterial.GOLDEN_HOE.parseItem()));
		ghost.setMetadata("ACUBELETS", new FixedMetadataValue(getMain(), Boolean.TRUE));
		bat.setPassenger(ghost);
		bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 160, 1));

		NBTEditor.set( bat, ( byte ) 1, "Silent" );
		NBTEditor.set( bat, ( byte ) 1, "Invulnerable" );

		Sounds.playSound(bat.getLocation(), Sounds.MySound.BAT_TAKEOFF, 1F, 1F);

		ghosts.add(bat);
		ghosts.add(ghost);
	}
	
}
