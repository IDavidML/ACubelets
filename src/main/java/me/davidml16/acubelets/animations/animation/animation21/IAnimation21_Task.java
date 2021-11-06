package me.davidml16.acubelets.animations.animation.animation21;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.api.CubeletOpenEvent;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.enums.Rotation;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.rewards.PermissionReward;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.MultiVersion.AB_12;
import me.davidml16.acubelets.utils.MultiVersion.AB_13;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class IAnimation21_Task implements IAnimation {

	private int id;

	private Main main;
	private AnimationSettings animationSettings;
	public IAnimation21_Task(Main main, AnimationSettings animationSettings) {
		this.main = main;
		this.animationSettings = animationSettings;
	}

	private ArmorStand armorStand;
	private CubeletBox cubeletBox;
	private CubeletType cubeletType;

	private Animation21_Blocks blocks;

	private List<Color> colors;
	private Utils.ColorSet<Integer, Integer, Integer> colorRarity;

	private Location boxLocation, armorStandLocation;

	private Location rotationLocation;
	private LivingEntity enderman;

	private Location corner1, corner2, corner3, corner4;
	private Reward reward;

	private RepeatingTask hologramAnimation;

	class Task implements Runnable {

		int time = 0;

		@Override
		public void run() {

			if(time == 1) {

				rotationLocation = getLocationRotation();

			}

			if(time == 50) {

				enderman = (LivingEntity) cubeletBox.getLocation().getWorld().spawnEntity(rotationLocation, EntityType.ENDERMAN);

				UtilParticles.display(Particles.FIREWORKS_SPARK, 0.35, 0.1, 0.35, enderman.getLocation(), 10);

				Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.ENDERMAN_TELEPORT, 0.5F, 2F);

				if(XMaterial.supports(9)) enderman.setCollidable(false);
				((Enderman)enderman).setCarriedBlock(Bukkit.createBlockData(XMaterial.CHEST.parseMaterial()));

				enderman.setRemoveWhenFarAway(false);
				enderman.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));

				NBTEditor.set( enderman, ( byte ) 1, "NoAI" );
				NBTEditor.set( enderman, ( byte ) 1, "Silent" );
				NBTEditor.set( enderman, ( byte ) 1, "Invulnerable" );

			}

			if(time > 50 && time < 90) {

				if(enderman != null)
					enderman.teleport(rotationLocation);

			}

			if(time == 70) {

				if(enderman != null)
					((Enderman)enderman).setCarriedBlock(Bukkit.createBlockData(XMaterial.AIR.parseMaterial()));

				Location eye = boxLocation.clone().add(0.0D, 0.4D, 0.0D);
				for (Location location2 : LocationUtils.getCircle(boxLocation.clone().add(0, 0.75,0), 0.25D, 50)) {
					Vector direction = location2.toVector().subtract(boxLocation.clone().add(0, 0.75,0).toVector()).normalize();
					UtilParticles.display(Particles.CLOUD, direction, eye, 0.3F);
				}

				placeOrientedChest(boxLocation);
				Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.CHICKEN_EGG_POP, 0.5F, 0F);

			}

			if(time == 90) {

				if(enderman != null) {

					UtilParticles.display(Particles.FIREWORKS_SPARK, 0.35, 0.1, 0.35, enderman.getLocation(), 10);
					enderman.remove();

				}

				Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.ENDERMAN_TELEPORT, 0.5F, 2F);

			}


			if(time == 120) {

				setChestOpened(boxLocation.getBlock(), true);
				Sounds.playSound(cubeletBox.getLocation(), Sounds.MySound.CHEST_OPEN, 0.5F, 1F);

			} else if(time == 123) {

				colorRarity = Utils.getRGBbyColor(Utils.getColorByText(reward.getRarity().getName()));
				main.getFireworkUtil().spawn(cubeletBox.getLocation().clone().add(0.5, 0.35, 0.5), FireworkEffect.Type.BALL_LARGE, colors.get(0), colors.get(1));

			} else if(time == 125) {

				cubeletBox.setLastReward(reward);
				main.getHologramImplementation().rewardHologram(cubeletBox, reward);
				cubeletBox.setState(CubeletBoxState.REWARD);

				if(armorStand != null) {
					armorStand.remove();
					armorStand = null;
				}

			} else if(time == 165) {

				if(main.isDuplicationEnabled())
					if(reward instanceof PermissionReward)
						hologramAnimation = main.getCubeletRewardHandler().permissionReward(cubeletBox, reward);

			} else if (time > 125 && time < 265) {

				if(animationSettings.isOutlineParticles()) {
					UtilParticles.drawParticleLine(corner1, corner2, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner2, corner3, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner3, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
					UtilParticles.drawParticleLine(corner1, corner4, Particles.REDSTONE, 10, colorRarity.getRed(), colorRarity.getGreen(), colorRarity.getBlue());
				}

				if(animationSettings.isFloorParticles())
					UtilParticles.display(Particles.FLAME, 1f, 0f, 1f, boxLocation, 2);

			} else if(time >= 265) {

				boxLocation.clone().getBlock().setType(XMaterial.AIR.parseMaterial());

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

		blocks = new Animation21_Blocks(box.getLocation());
		blocks.runTaskTimer(main, 6L, 6L);

		this.cubeletType = type;
		this.cubeletBox = box;
		this.cubeletBox.setState(CubeletBoxState.ANIMATION);
		this.colors = Arrays.asList(Color.BLACK, Color.PURPLE);

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

	public void placeOrientedChest(Location loc) {

		BlockFace blockFace;

		switch (cubeletBox.getRotation()) {
			case SOUTH:
				blockFace = BlockFace.SOUTH;
				break;
			case EAST:
				blockFace = BlockFace.EAST;
				break;
			case WEST:
				blockFace = BlockFace.WEST;
				break;
			default:
				blockFace = BlockFace.NORTH;
				break;
		}

		if(XMaterial.supports(13)) {
			AB_13.placeOrientedStair(loc, XMaterial.CHEST.parseMaterial(), blockFace);
		} else {
			AB_12.placeOrientedStair(loc, XMaterial.CHEST.parseMaterial(), blockFace);
		}

	}

	public void setChestOpened(Block block, boolean opened) {
		PacketContainer libPacket = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);
		libPacket.getBlockPositionModifier().write(0, new BlockPosition(block.getX(), block.getY(), block.getZ()));
		libPacket.getIntegers().write(0, 1);
		libPacket.getIntegers().write(1, opened ? 1 : 0);
		libPacket.getBlocks().write(0, block.getType());
		int distanceSquared = 64 * 64;
		Location loc = block.getLocation();
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		try {
			for (Player player : block.getWorld().getPlayers()) {
				if (player.getLocation().distanceSquared(loc) < distanceSquared) {
					manager.sendServerPacket(player, libPacket);
				}
			}
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
}
