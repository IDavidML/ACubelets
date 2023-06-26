package me.davidml16.acubelets.animations.animation.animation22;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.cryptomorin.xseries.XMaterial;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.animations.FakeBlock;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class Animation22_Task extends Animation {

	public Animation22_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	@Override
	public void onTick(int time) {

		if(time == 120) {

			doPreRewardReveal();

		}

	}

	@Override
	public void onStart() {

		setAnimationBlocks(new Animation22_Blocks(getCubeletBox().getLocation()));
		startAnimationBlocks(6L);

		setColors(Arrays.asList(Color.WHITE, Color.PURPLE));

	}

	@Override
	public void onStop() {

		stopAnimationBlocks();

	}

	@Override
	public void onPreRewardReveal() {

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
	
}
