package me.davidml16.acubelets.animations.animation.animation22;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.cryptomorin.xseries.XMaterial;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.animations.FakeBlock;
import me.davidml16.acubelets.animations.animation.animation17.Animation17_Snowball;
import me.davidml16.acubelets.animations.animation.animation21.Animation21_RotatingParticle;
import me.davidml16.acubelets.animations.animation.animation6.Animation6_Music;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Animation22_Task extends Animation {

	public Animation22_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private Set<Animation22_FloatingBlock> floatingBlocks = new HashSet<>();

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private double rotSpeed = 0.001;

	@Override
	public void onTick(int time) {

		if(time == 6) {

			Animation22_FloatingBlock floatingBlock1 = new Animation22_FloatingBlock(getMain(), getCubeletBox().getLocation().clone(), getCubeletBox().getLocation().clone().add(2, 1, 2), getCorner3());
			floatingBlock1.runTaskTimer(getMain(), 0L, 1L);
			floatingBlocks.add(floatingBlock1);

			Animation22_FloatingBlock floatingBlock2 = new Animation22_FloatingBlock(getMain(), getCubeletBox().getLocation().clone(), getCubeletBox().getLocation().clone().add(2, 1, -2), getCorner2());
			floatingBlock2.runTaskTimer(getMain(), 0L, 1L);
			floatingBlocks.add(floatingBlock2);

			Animation22_FloatingBlock floatingBlock3 = new Animation22_FloatingBlock(getMain(), getCubeletBox().getLocation().clone(), getCubeletBox().getLocation().clone().add(-2, 1, 2), getCorner4());
			floatingBlock3.runTaskTimer(getMain(), 0L, 1L);
			floatingBlocks.add(floatingBlock3);

			Animation22_FloatingBlock floatingBlock4 = new Animation22_FloatingBlock(getMain(), getCubeletBox().getLocation().clone(), getCubeletBox().getLocation().clone().add(-2, 1, -2), getCorner1());
			floatingBlock4.runTaskTimer(getMain(), 0L, 1L);
			floatingBlocks.add(floatingBlock4);

		}

		if(time == 15) {

			armorStand = ASSpawner.spawn(
					getMain(),
					getCubeletBox().getLocation().clone().add(0.5, -0.3, 0.5),
					SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc2MzhjMzhkMzlhNjdkODk2MjY3NDNmMDZiOWM3YmU1YzUwY2Y4MzM1ZDJlOGYzOWViMWRhZDBkZjBmNzNkNiJ9fX0="),
					false,
					false,
					true
			);

			startRunnable("music", 0L, 1L);

			armorStandLocation = armorStand.getLocation();

			getMain().getAnimationHandler().getEntities().add(armorStand);

		}

		if(time > 15 && time < 120) {

			if (armorStand != null) {

				if(time < 70)
					armorStandLocation.add(0, 0.021, 0);

				if(time > 60) {
					armorStand.setHeadPose(armorStand.getHeadPose().add(0, rotSpeed, 0));
				}

				armorStand.teleport(armorStandLocation);

			}

			rotSpeed += 0.0060;

		}

		if(time == 120) doPreRewardReveal();

		if(time == 143) removeRandomFloatingBlock();
		if(time == 146) removeRandomFloatingBlock();
		if(time == 149) removeRandomFloatingBlock();
		if(time == 152) removeRandomFloatingBlock();

	}

	@Override
	public void onStart() {

		addRunnable("music", new Animation22_Music(getCubeletBox().getLocation()));

		armorStand = ASSpawner.spawn(getMain(), getCubeletBox(), getCubeletType(), false, false, getBoxLocation().clone().add(0, -1.25, 0));
		getMain().getAnimationHandler().getEntities().add(armorStand);

		setAnimationBlocks(new Animation22_Blocks(getCubeletBox().getLocation()));
		startAnimationBlocks(3L);

		setColors(Arrays.asList(Color.WHITE, Color.PURPLE));

	}

	@Override
	public void onStop() {

		stopAnimationBlocks();

		cancelRunnables();

		try {
			for(Animation22_FloatingBlock floatingBlock : floatingBlocks) {
				floatingBlock.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(floatingBlock.getFallingBlock())) {
					FallingBlock fallingBlock = floatingBlock.getFallingBlock();
					if(fallingBlock != null) fallingBlock.remove();
					getMain().getAnimationHandler().getEntities().remove(fallingBlock);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

		if(getMain().getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			getMain().getAnimationHandler().getEntities().remove(armorStand);
		}

	}

	@Override
	public void onPreRewardReveal() {

		cancelRunnable("music");

		Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.ENDERDRAGON_GROWL, 0.5F, 1F);

		if(getMain().getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			getMain().getAnimationHandler().getEntities().remove(armorStand);
			armorStand = null;
		}

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 0.35, 0.5),
				FireworkEffect.Type.BURST,
				getColors().get(0),
				getColors().get(1)
		);

	}

	@Override
	public void onRewardReveal() {

	}

	@Override
	public void onRewardDuplication() {}

	public void removeRandomFloatingBlock() {
		for(Animation22_FloatingBlock floatingBlock : floatingBlocks) {
			floatingBlock.cancel();
			if(getMain().getAnimationHandler().getEntities().contains(floatingBlock.getFallingBlock())) {
				Entity entity = floatingBlock.getFallingBlock();
				if(entity != null) {
					getCubeletBox().getLocation().getWorld().strikeLightningEffect(entity.getLocation().clone().add(0.5, 1, 0.5));
					entity.remove();
				}
				getMain().getAnimationHandler().getEntities().remove(entity);
			}
			floatingBlocks.remove(floatingBlock);
			break;
		}
	}
	
}
