package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Profile {

	private Main main;

	private UUID uuid;

	private CubeletBox boxOpened;

	private List<Cubelet> cubelets;

	private long lootPoints;

	private String orderBy;

	private String animation;

	public Profile(Main main, UUID uuid) {
		this.main = main;
		this.uuid = uuid;
		this.cubelets = new ArrayList<>();
		this.boxOpened = null;
		this.orderBy = "date";
		this.lootPoints = 0;
		this.animation = "animation2";
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public List<Cubelet> getCubelets() { return cubelets; }

	public void setCubelets(List<Cubelet> cubelets) { this.cubelets = cubelets; }

	public CubeletBox getBoxOpened() { return boxOpened; }

	public void setBoxOpened(CubeletBox boxOpened) { this.boxOpened = boxOpened; }

	public String getOrderBy() { return orderBy; }

	public void setOrderBy(String orderBy) { this.orderBy = orderBy; }

	public long getLootPoints() { return lootPoints; }

	public void setLootPoints(long lootPoints) { this.lootPoints = lootPoints; }

	public String getAnimation() { return animation; }

	public void setAnimation(String animation) { this.animation = animation; }

	@Override
	public String toString() {
		return "Profile{" +
				"main=" + main +
				", uuid=" + uuid +
				", boxOpened=" + boxOpened +
				", cubelets=" + cubelets +
				", orderBy='" + orderBy + '\'' +
				'}';
	}

}
