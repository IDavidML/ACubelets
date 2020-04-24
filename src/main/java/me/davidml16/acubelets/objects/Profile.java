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

	public Profile(Main main, UUID uuid) {
		this.main = main;
		this.uuid = uuid;
		this.cubelets = new ArrayList<>();
		this.boxOpened = null;
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

	@Override
	public String toString() {
		return "Profile{" +
				"uuid=" + uuid +
				", cubelets=" + cubelets +
				'}';
	}

}
