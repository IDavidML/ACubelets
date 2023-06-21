package me.davidml16.acubelets.animations.animation.animation17;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.ASSpawner;
import me.davidml16.acubelets.animations.Animation;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Animation17_Task extends Animation {

	public Animation17_Task(Main main, AnimationSettings animationSettings) {
		super(main, animationSettings);
	}

	private ArmorStand armorStand;
	private Location armorStandLocation;

	private Set<Animation17_Snowball> snowballs = new HashSet<>();

	private LivingEntity snowman;
	private Location snowmanLocation;

	private double boxLocIncrease = -0.75;
	private double circleSize = 0.0D;
	private int circleStep = 0;
	private float rotation;

	@Override
	public void onTick(int time) {

		if(time == 18) {

			snowman = (LivingEntity) getCubeletBox().getLocation().getWorld().spawnEntity(getLocationRotation(0), EntityType.SNOWMAN);

			snowman.setCollidable(false);
			snowman.setRemoveWhenFarAway(false);
			snowman.setMetadata("ACUBELETS", new FixedMetadataValue(getMain(), Boolean.TRUE));

			NBTEditor.set(snowman, (byte) 1, "Silent");
			NBTEditor.set(snowman, (byte) 1, "Invulnerable");
			NBTEditor.set(snowman, (byte) 1, "NoAI");

			snowmanLocation = snowman.getLocation();
			snowmanLocation.setYaw(getCubeletBox().getRotation().value);
			snowmanLocation.setPitch(0);
			snowman.teleport(snowmanLocation);

		}

		if(time == 24) {

			armorStand = ASSpawner.spawn(
					getMain(),
					getCubeletBox().getLocation().clone().add(0.5, -0.3, 0.5),
					SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWRmZDc3MjRjNjlhMDI0ZGNmYzYwYjE2ZTAwMzM0YWI1NzM4ZjRhOTJiYWZiOGZiYzc2Y2YxNTMyMmVhMDI5MyJ9fX0="),
					false,
					false,
					true
			);
			getMain().getAnimationHandler().getEntities().add(armorStand);

			armorStandLocation = armorStand.getLocation();

		}

		if (time >= 24 && time <= 50) {

			Location newBoxLoc = getBoxLocation().clone();
			this.boxLocIncrease += 0.030D;
			newBoxLoc.add(0.0D, this.boxLocIncrease, 0.0D);
			newBoxLoc.setYaw(rotation);

			UtilParticles.display(Particles.FIREWORKS_SPARK, armorStand.getLocation().clone().add(0, 0.75, 0), 1);

			armorStand.teleport(newBoxLoc);

		} else if (time >= 24 && time <= 85) {

			this.boxLocIncrease += 0.025D;

			Location newBoxLoc = getBoxLocation().clone();
			newBoxLoc.add(0.0D, this.boxLocIncrease, 0.0D);

			if (time <= 65)
				this.circleSize += 0.07D;
			else
				this.circleSize -= 0.075D;

			if (this.circleSize < 0.0D)
				this.circleSize = 0.0D;

			List<Location> teleportLocs = LocationUtils.getCircle(newBoxLoc, this.circleSize, 50);
			Location teleportLoc = ((Location)teleportLocs.get(this.circleStep)).clone();

			teleportLoc.setYaw(rotation);

			Sounds.playSound(armorStand.getLocation(), Sounds.MySound.NOTE_PLING, 0.5F, 3F);

			UtilParticles.display(Particles.FIREWORKS_SPARK, armorStand.getLocation().clone().add(0, 0.75, 0), 1);
			armorStand.teleport(teleportLoc);

			this.circleStep++;

			if (this.circleStep == teleportLocs.size())
				this.circleStep = 0;

		}

		if(time == 30) {

			Animation17_Snowball snowball1 = new Animation17_Snowball(getMain(), getCubeletBox().getLocation().clone().add(0.5, -1.5, 0.5), 3, 100, true, 0);
			snowball1.runTaskTimer(getMain(), 0L, 1L);
			snowballs.add(snowball1);

			Animation17_Snowball snowball2 = new Animation17_Snowball(getMain(), getCubeletBox().getLocation().clone().add(0.5, -1.5, 0.5), 3, 100, true, 25);
			snowball2.runTaskTimer(getMain(), 0L, 1L);
			snowballs.add(snowball2);

			Animation17_Snowball snowball3 = new Animation17_Snowball(getMain(), getCubeletBox().getLocation().clone().add(0.5, -1.5, 0.5), 3, 100, true, 50);
			snowball3.runTaskTimer(getMain(), 0L, 1L);
			snowballs.add(snowball3);

			Animation17_Snowball snowball4 = new Animation17_Snowball(getMain(), getCubeletBox().getLocation().clone().add(0.5, -1.5, 0.5), 3, 100, true, 75);
			snowball4.runTaskTimer(getMain(), 0L, 1L);
			snowballs.add(snowball4);

		}

		if(time == 113) {

			doPreRewardReveal();

		}

		if(time == 156) removeRandomBall();
		if(time == 159) removeRandomBall();
		if(time == 162) removeRandomBall();
		if(time == 165) removeRandomBall();

		if(time == 135)
			Sounds.playSound(getCubeletBox().getLocation(), Sounds.MySound.LEVEL_UP, 0.5F, 1F);

	}

	@Override
	public void onStart() {

		rotation = getRotation(false).value;

		setAnimationBlocks(new Animation17_Blocks(getCubeletBox().getLocation()));
		startAnimationBlocks(0L);

		addRunnable("music", new Animation17_Music(getCubeletBox().getLocation()));
		startRunnable("music", 25L, 3L);

		setColors(Arrays.asList(Color.WHITE, Color.AQUA));

	}

	@Override
	public void onStop() {

		cancelRunnables();

		if(snowman != null) snowman.remove();

		stopAnimationBlocks();

		if(getMain().getAnimationHandler().getEntities().contains(armorStand)) {
			if(armorStand != null) armorStand.remove();
			getMain().getAnimationHandler().getEntities().remove(armorStand);
		}

		try {
			for(Animation17_Snowball snowball : snowballs) {
				snowball.cancel();
				if(getMain().getAnimationHandler().getEntities().contains(snowball.getArmorStand())) {
					ArmorStand snowballArmorStand = snowball.getArmorStand();
					if(snowballArmorStand != null) snowballArmorStand.remove();
					getMain().getAnimationHandler().getEntities().remove(snowballArmorStand);
				}
			}
		} catch(IllegalStateException | NullPointerException ignored) {}

	}

	@Override
	public void onPreRewardReveal() {

		cancelRunnable("music");

		Sounds.playSound(armorStand.getLocation(), Sounds.MySound.EXPLODE, 0.5F, 1F);

		getMain().getFireworkUtil().spawn(
				getCubeletBox().getLocation().clone().add(0.5, 1.50, 0.5),
				FireworkEffect.Type.BALL_LARGE,
				getColors().get(0),
				getColors().get(1)
		);

	}

	@Override
	public void onRewardReveal() {

		snowman.remove();

		armorStand.remove();
		armorStand = null;

	}

	public void removeRandomBall() {
		for(Animation17_Snowball snowball : snowballs) {
			snowball.cancel();
			if(getMain().getAnimationHandler().getEntities().contains(snowball.getArmorStand())) {
				Entity entity = snowball.getArmorStand();
				if(entity != null) {
					getMain().getFireworkUtil().spawn(entity.getLocation(), FireworkEffect.Type.BURST, getColors().toArray(new Color[getColors().size()]));
					entity.remove();
				}
				getMain().getAnimationHandler().getEntities().remove(entity);
			}
			snowballs.remove(snowball);
			break;
		}
	}
	
}
