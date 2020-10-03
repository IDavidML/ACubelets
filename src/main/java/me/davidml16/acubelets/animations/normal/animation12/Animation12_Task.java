package me.davidml16.acubelets.animations.normal.animation12;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.animations.normal.animation9.Animation9_Charge;
import me.davidml16.acubelets.animations.seasonal.galaxy.AnimationGalaxy_Planet;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.enums.Rotation;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.rewards.PermissionReward;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Animation12_Task implements Animation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public Animation12_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private ArmorStand glassStand, liquidStand;
	private Location glassLocation, liquidLocation;

	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private Animation12_Blocks blocks;
	private Animation12_Charge charge;

	private List<ItemStack> wools = new ArrayList<>();
	private int actualWool = 0;

	private List<Color> colors;
	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;

	private Set<Animation12_Orbit> orbits = new HashSet<>();

	private Location witchLocation, boxLocation;

	private Location corner1, corner2, corner3, corner4;

	private LivingEntity witch;

	private Reward reward;

	private RepeatingTask hologramAnimation;

	class Task implements Runnable {
		int time = 0;
		double rotSpeed = 0.001;
		@Override
		public void run() {

			if(time == 45) {
				glassStand = ASSpawner.spawn(main, cubeletBox.getLocation().clone().add(0.5, -1.1, 0.5), XMaterial.GLASS.parseItem(), false, false, false);
				glassLocation = glassStand.getLocation();
				main.getAnimationHandler().getEntities().add(glassStand);

				liquidStand = ASSpawner.spawn(main, cubeletBox.getLocation().clone().add(0.5, -0.3, 0.5), XMaterial.RED_WOOL.parseItem(), false, false, true);
				liquidLocation = liquidStand.getLocation();
				main.getAnimationHandler().getEntities().add(liquidStand);

                witch = (LivingEntity) cubeletBox.getLocation().getWorld().spawnEntity(getLocationRotation(), EntityType.WITCH);

                if (XMaterial.supports(9)) witch.setCollidable(false);
                witch.setRemoveWhenFarAway(false);
                witch.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));

                NBTEditor.set(witch, (byte) 1, "Silent");
                NBTEditor.set(witch, (byte) 1, "Invulnerable");
                NBTEditor.set(witch, (byte) 1, "NoAI");

                witchLocation = witch.getLocation();
                witchLocation.setYaw(cubeletBox.getRotation().value);
                witchLocation.setPitch(0);
                witch.teleport(witchLocation);

				Animation12_Orbit orbit1 = new Animation12_Orbit(main, glassLocation.clone().add(0, 3.5, 0), 3, 90, true, 0);
				orbit1.runTaskTimer(main, 0L, 1L);
				orbits.add(orbit1);

				Animation12_Orbit orbit2 = new Animation12_Orbit(main, glassLocation.clone().add(0, 3.5, 0), 3, 90, true, 45);
				orbit2.runTaskTimer(main, 0L, 1L);
				orbits.add(orbit2);
			}

			if(time > 45 && time < 155) {
				if (glassStand != null) {
					if(time < 94)
						glassLocation.add(0, 0.021, 0);
					if(time > 85)
						glassStand.setHeadPose(glassStand.getHeadPose().add(0, rotSpeed, 0));

					glassStand.teleport(glassLocation);
				}
				if (liquidStand != null) {
					if(time < 94)
						liquidLocation.add(0, 0.021, 0);
					if(time > 85)
						liquidStand.setHeadPose(liquidStand.getHeadPose().add(0, rotSpeed, 0));

					liquidStand.teleport(liquidLocation);

					if(time % 2 == 0) {
						if(time < 94)Sounds.playSound(liquidStand.getLocation(), Sounds.MySound.ORB_PICKUP, 0.5F, (float) ThreadLocalRandom.current().nextDouble(1, 3));
						liquidStand.setHelmet(wools.get(actualWool));
						actualWool++;
						if (actualWool >= wools.size()) actualWool = 0;
					}
				}
				rotSpeed += 0.0030;
			}

			if(time == 100) chargeParticles();

			if(time == 153) {
				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				Sounds.playSound(liquidStand.getLocation(), Sounds.MySound.EXPLODE, 0.5F, 1F);
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 1.50, 0.5), FireworkEffect.Type.BALL_LARGE, colors.get(0), colors.get(1));
			}

			if(time == 155) {
				witch.remove();
				cubeletBox.setLastReward(reward);
				main.getHologramHandler().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);
				glassStand.remove();
				glassStand = null;
				liquidStand.remove();
				liquidStand = null;
			}

			if(time == 175) Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

			if(time == 195) {
				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);
			}

			if(time > 155 && time < 295) {
				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);
			}

			if(time == 295) {
				stop();
				blocks.restore();

				main.getCubeletRewardHandler().giveReward(cubeletBox, reward);
				MessageUtils.sendLootMessage(cubeletBox, cubeletType, reward);

				cubeletBox.setState(CubeletBoxState.EMPTY);
				cubeletBox.setPlayerOpening(null);
				main.getHologramHandler().reloadHologram(cubeletBox);
			}

			if(time > 45 && time < 145) {
				UtilParticles.drawParticleLine(corner1.clone().add(0, -0.5,0), corner2.clone().add(0, -0.5,0), Particles.SPELL_WITCH, 5, 0, 0, 0);
				UtilParticles.drawParticleLine(corner2.clone().add(0, -0.5,0), corner3.clone().add(0, -0.5,0), Particles.SPELL_WITCH, 5, 0, 0, 0);
				UtilParticles.drawParticleLine(corner3.clone().add(0, -0.5,0), corner4.clone().add(0, -0.5,0), Particles.SPELL_WITCH, 5, 0, 0, 0);
				UtilParticles.drawParticleLine(corner1.clone().add(0, -0.5,0), corner4.clone().add(0, -0.5,0), Particles.SPELL_WITCH, 5, 0, 0, 0);
			}

			time++;
		}
	}

	public Location getLocationRotation() {
		switch (cubeletBox.getRotation()) {
			case SOUTH:
				Location s = cubeletBox.getLocation().clone().add(0.5, 0, -0.5);
				s.setYaw(Rotation.SOUTH.value);
				return s;
			case NORTH:
				Location n = cubeletBox.getLocation().clone().add(0.5, 0, 1.5);
				n.setYaw(Rotation.NORTH.value);
				return n;
			case EAST:
				Location e = cubeletBox.getLocation().clone().add(-0.5, 0, 0.5);
				e.setYaw(Rotation.EAST.value);
				return e;
			case WEST:
				Location w = cubeletBox.getLocation().clone().add(1.5, 0, 0.5);
				w.setYaw(Rotation.WEST.value);
				return w;
		}
		return null;
	}
	
	public int getId() { return id; }

	public void start(CubeletBox box, CubeletType type) {
		blocks = new Animation12_Blocks(box.getLocation());
		blocks.runTaskTimer(main, 0L, 6L);

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.PURPLE, Color.FUCHSIA);

		corner1 = cubeletBox.getLocation().clone().add(0.05, 1 - 0.325, 0.05);
		corner2 = cubeletBox.getLocation().clone().add(0.95, 1 - 0.325, 0.05);
		corner3 = cubeletBox.getLocation().clone().add(0.95, 1 - 0.325, 0.95);
		corner4 = cubeletBox.getLocation().clone().add(0.05, 1 - 0.325, 0.95);
		boxLocation = cubeletBox.getLocation().clone().add(0.5, 0, 0.5);

		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Task(), 0L, 1);

		wools.add(XMaterial.WHITE_WOOL.parseItem());
		wools.add(XMaterial.RED_WOOL.parseItem());
		wools.add(XMaterial.GREEN_WOOL.parseItem());
		wools.add(XMaterial.BLACK_WOOL.parseItem());
		wools.add(XMaterial.BLUE_WOOL.parseItem());
		wools.add(XMaterial.BROWN_WOOL.parseItem());
		wools.add(XMaterial.GRAY_WOOL.parseItem());
		wools.add(XMaterial.LIME_WOOL.parseItem());
		wools.add(XMaterial.MAGENTA_WOOL.parseItem());
		wools.add(XMaterial.ORANGE_WOOL.parseItem());
		wools.add(XMaterial.PINK_WOOL.parseItem());
		wools.add(XMaterial.PURPLE_WOOL.parseItem());
		wools.add(XMaterial.YELLOW_WOOL.parseItem());

		main.getAnimationHandler().getTasks().add(this);

		charge = new Animation12_Charge(box.getLocation());
		charge.runTaskTimer(main, 105L, 3L);

		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			reward = main.getCubeletRewardHandler().processReward(cubeletType);
		});
	}
	
	public void stop() {
		blocks.cancel();

		if(hologramAnimation != null) hologramAnimation.canncel();
		if(blocks != null) blocks.restore();

		main.getAnimationHandler().getTasks().remove(this);

		Bukkit.getServer().getScheduler().cancelTask(id);

		if(main.getAnimationHandler().getEntities().contains(glassStand)) {
			if(glassStand != null) glassStand.remove();
			main.getAnimationHandler().getEntities().remove(glassStand);
		}

		if(main.getAnimationHandler().getEntities().contains(liquidStand)) {
			if(liquidStand != null) liquidStand.remove();
			main.getAnimationHandler().getEntities().remove(liquidStand);
		}

		try {
			for(Animation12_Orbit orbit : orbits) {
				orbit.cancel();
				if(main.getAnimationHandler().getEntities().contains(orbit.getArmorStand())) {
					ArmorStand armorStandOrbit = orbit.getArmorStand();
					if(armorStandOrbit != null) armorStandOrbit.remove();
					main.getAnimationHandler().getEntities().remove(armorStandOrbit);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		Bukkit.getPluginManager().callEvent(new CubeletOpenEvent(cubeletBox.getPlayerOpening(), cubeletType));
	}

	private void chargeParticles() {
		Random random = new Random();
		Location loc = liquidStand.getLocation().clone().add(0, 1, 0);
		for (int i = 0; i < 500; i++) {
			Location randomLoc = loc.clone();
			randomLoc.add((random.nextDouble() - 0.5D) / 2.0D, (new Random().nextDouble() - 0.5D) / 2.0D, (random.nextDouble() - 0.5D) / 2.0D);

			Vector vector = randomLoc.toVector().subtract(loc.toVector()).normalize();
			Vector direction = vector.multiply(1.5D + new Random().nextDouble() * 5.0D);

			for(int j = 0; j < 3; j++)
				UtilParticles.display(Particles.PORTAL, direction, loc, 5);

		}
	}
	
}
