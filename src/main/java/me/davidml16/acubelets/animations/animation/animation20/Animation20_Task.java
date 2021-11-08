package me.davidml16.acubelets.animations.animation.animation20;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
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

public class Animation20_Task extends Animation {

	public Animation20_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private Animation20_Blocks blocks;

	private Location rotationLocation;
	private LivingEntity enderman;

	@Override
	public void onTick(int time) {

		if(time == 50) {

			enderman = (LivingEntity) getCubeletBox().getLocation().getWorld().spawnEntity(rotationLocation, EntityType.ENDERMAN);

			UtilParticles.display(Particles.FIREWORKS_SPARK, 0.35, 0.1, 0.35, enderman.getLocation(), 10);

			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.ENDERMAN_TELEPORT, 0.5F, 2F);

			if(XMaterial.supports(9)) enderman.setCollidable(false);
			((Enderman)enderman).setCarriedBlock(Bukkit.createBlockData(XMaterial.CHEST.parseMaterial()));

			enderman.setRemoveWhenFarAway(false);
			enderman.setMetadata("ACUBELETS", new FixedMetadataValue(getMain(), Boolean.TRUE));

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

			Location eye = getBoxLocation().clone().add(0.0D, 0.4D, 0.0D);
			for (Location location2 : LocationUtils.getCircle(getBoxLocation().clone().add(0, 0.75,0), 0.25D, 50)) {
				Vector direction = location2.toVector().subtract(getBoxLocation().clone().add(0, 0.75,0).toVector()).normalize();
				UtilParticles.display(Particles.CLOUD, direction, eye, 0.3F);
			}

			placeOrientedChest(getBoxLocation());
			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.CHICKEN_EGG_POP, 0.5F, 0F);

		}

		if(time == 90) {

			if(enderman != null) {

				UtilParticles.display(Particles.FIREWORKS_SPARK, 0.35, 0.1, 0.35, enderman.getLocation(), 10);
				enderman.remove();

			}

			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.ENDERMAN_TELEPORT, 0.5F, 2F);

		}


		if(time == 120) {

			setChestOpened(getBoxLocation().getBlock(), true);
			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.CHEST_OPEN, 0.5F, 1F);

		} else if(time == 123) {

			doPreRewardReveal();

		}

	}

	@Override
	public void onStart() {

		rotationLocation = getLocationRotation(0);

		blocks = new Animation20_Blocks(getCubeletBox().getLocation());
		blocks.runTaskTimer(getMain(), 6L, 6L);

		setColors(Arrays.asList(Color.BLACK, Color.PURPLE));

	}

	@Override
	public void onStop() {

		getBoxLocation().clone().getBlock().setType(XMaterial.AIR.parseMaterial());

		blocks.cancel();

		if(blocks != null) blocks.restore();

	}

	@Override
	public void onPreRewardReveal() {

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 0.35, 0.5),
				FireworkEffect.Type.BALL_LARGE,
				getColors().get(0),
				getColors().get(1)
		);

	}

	@Override
	public void onRewardReveal() {

	}

	public void placeOrientedChest(Location loc) {

		BlockFace blockFace;

		switch (getCubeletBox().getRotation()) {
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
