package me.davidml16.acubelets.animations.animation.animation7;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
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

public class Animation7_Task implements Animation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public Animation7_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private ArmorStand armorStand;
	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private Animation7_Music music;
	private Animation7_Blocks blocks;

	private Set<Animation7_Pumpkin> pumpkins = new HashSet<>();
	private Set<Entity> ghosts = new HashSet<>();

	private List<Color> colors;
	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;

	private Location boxLocation, armorStandLocation;

	private Location corner1, corner2, corner3, corner4;
	private Reward reward;

	private RepeatingTask hologramAnimation;

	class Task implements Runnable {
		int time = 0;
		double rotSpeed = 0.1;
		@Override
		public void run() {

			if(time == 35) {
				Animation7_Pumpkin pumpkin = new Animation7_Pumpkin(main, boxLocation.clone().add(2, 0, 2), cubeletBox.getPlayerOpening().getUuid());
				pumpkin.runTaskTimer(main, 0L, 1L);
				pumpkins.add(pumpkin);
			} else if(time == 45) {
				Animation7_Pumpkin pumpkin = new Animation7_Pumpkin(main, boxLocation.clone().add(-2, 0, 2), cubeletBox.getPlayerOpening().getUuid());
				pumpkin.runTaskTimer(main, 0L, 1L);
				pumpkins.add(pumpkin);
			} else if(time == 55) {
				Animation7_Pumpkin pumpkin = new Animation7_Pumpkin(main, boxLocation.clone().add(-2, 0, -2), cubeletBox.getPlayerOpening().getUuid());
				pumpkin.runTaskTimer(main, 0L, 1L);
				pumpkins.add(pumpkin);
			} else if(time == 65) {
				Animation7_Pumpkin pumpkin = new Animation7_Pumpkin(main, boxLocation.clone().add(2, 0, -2), cubeletBox.getPlayerOpening().getUuid());
				pumpkin.runTaskTimer(main, 0L, 1L);
				pumpkins.add(pumpkin);
			} else if(time == 75) {
				music.runTaskTimer(main, 0L, 4L);

				armorStand = ASSpawner.spawn(main, cubeletBox, cubeletType, false);
				armorStandLocation = armorStand.getLocation();
				main.getAnimationHandler().getEntities().add(armorStand);

				cubeletBox.getLocation().getWorld().strikeLightningEffect(cubeletBox.getLocation().clone().add(0.5, 1, 0.5));
				Sounds.playSound(armorStandLocation, Sounds.MySound.GHAST_SCREAM2, 1F, 1F);

			} else if(time > 75 && time < 175) {
				if(armorStand != null) {
					if (time <= 125) {
						armorStandLocation.add(0, 0.02, 0);
					}
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
				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.BALL_LARGE, colors.get(0), colors.get(1));

				for(Entity entity : ghosts) {
					if(entity != null) entity.remove();
					main.getAnimationHandler().getEntities().remove(entity);
				}

			} else if(time == 175) {
				music.cancel();
				cubeletBox.setLastReward(reward);
				main.getHologramImplementation().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				armorStand.remove();
				armorStand = null;
			} else if(time == 215) {
				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);
			} else if (time > 175 && time < 315) {
				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			} else if(time >= 315) {
				stop();

				main.getCubeletRewardHandler().giveReward(cubeletBox, reward);
				MessageUtils.sendLootMessage(cubeletBox, cubeletType, reward);

				cubeletBox.setState(CubeletBoxState.EMPTY);
				cubeletBox.setPlayerOpening(null);
				main.getHologramImplementation().reloadHologram(cubeletBox);
			}

			time++;
		}
	}
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		blocks = new Animation7_Blocks(box.getLocation());
		blocks.runTaskTimer(main, 0L, 6L);

		music = new Animation7_Music(box.getLocation());

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.ORANGE, Color.BLACK);

		corner1 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner2 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.05);
		corner3 = cubeletBox.getLocation().clone().add(0.95, box.getPermanentBlockHeight() - 0.325, 0.95);
		corner4 = cubeletBox.getLocation().clone().add(0.05, box.getPermanentBlockHeight() - 0.325, 0.95);
		boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		main.getAnimationHandler().getTasks().add(this);
		main.getAnimationHandler().getEntities().add(armorStand);

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			reward = main.getCubeletRewardHandler().processReward(cubeletType);
		});
	}
	
	public void stop() {
		blocks.cancel();

		try {
			music.cancel();
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			for(Animation7_Pumpkin pumpkin : pumpkins) {
				pumpkin.cancel();
				if(main.getAnimationHandler().getEntities().contains(pumpkin.getArmorStand())) {
					ArmorStand pumpkinArmorStand = pumpkin.getArmorStand();
					if(pumpkinArmorStand != null) pumpkinArmorStand.remove();
					main.getAnimationHandler().getEntities().remove(pumpkinArmorStand);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		try {
			for(Entity entity : ghosts) {
				if(entity != null) entity.remove();
				main.getAnimationHandler().getEntities().remove(entity);
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(blocks != null) blocks.restore();

		if(hologramAnimation != null) hologramAnimation.cancel();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			main.getAnimationHandler().getEntities().remove(armorStand);
		}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}


	public void removeRandomGhost() {
		for(Entity bat : ghosts) {
			if(bat instanceof Bat) {
				Entity passenger = bat.getPassenger();

				UtilParticles.display(Particles.SNOW_SHOVEL, bat.getLocation(), 5);

				if (passenger != null) passenger.remove();
				main.getAnimationHandler().getEntities().remove(passenger);
				ghosts.remove(passenger);

				if(bat != null) bat.remove();
				main.getAnimationHandler().getEntities().remove(bat);
				ghosts.remove(bat);

				break;
			}
		}
	}

	public void spawnGhost(ItemStack chestplate) {
		Bat bat = cubeletBox.getLocation().getWorld().spawn(cubeletBox.getLocation().clone().add(0.5, 1, 0.5), Bat.class);
		ArmorStand ghost = bat.getWorld().spawn(bat.getLocation(), ArmorStand.class);
		ghost.setSmall(true);
		ghost.setGravity(false);
		ghost.setVisible(false);
		ghost.setHelmet(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjhkMjE4MzY0MDIxOGFiMzMwYWM1NmQyYWFiN2UyOWE5NzkwYTU0NWY2OTE2MTllMzg1NzhlYTRhNjlhZTBiNiJ9fX0"));
		ghost.setChestplate(chestplate);
		ghost.setItemInHand(new ItemStack(XMaterial.GOLDEN_HOE.parseItem()));
		ghost.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));
		bat.setPassenger(ghost);
		bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 160, 1));

		NBTEditor.set( bat, ( byte ) 1, "Silent" );
		NBTEditor.set( bat, ( byte ) 1, "Invulnerable" );

		Sounds.playSound(bat.getLocation(), Sounds.MySound.BAT_TAKEOFF, 1F, 1F);

		ghosts.add(bat);
		ghosts.add(ghost);
	}
	
}