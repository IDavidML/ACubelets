package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.data.CubeletType;
import me.davidml16.acubelets.data.Reward;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class CubeletRewardHandler {

	private Main main;
	public CubeletRewardHandler(Main main) {
		this.main = main;
	}

	public void loadRewards() {
		Main.log.sendMessage(ColorUtil.translate("  &eLoading rewards:"));
		for(CubeletType cubeletType : main.getCubeletTypesHandler().getTypes().values()) {
			FileConfiguration config = main.getCubeletTypesHandler().getConfig(cubeletType.getId());
			Map<String, List<Reward>> rewards = new HashMap<>();
			if (config.contains("type.rewards")) {
				if (config.getConfigurationSection("type.rewards") != null) {
					for (String rewardid : config.getConfigurationSection("type.rewards").getKeys(false)) {
						if(validRewardData(config, rewardid)) {
							String rarity = config.getString("type.rewards." + rewardid + ".rarity");
							if (cubeletType.getRarities().containsKey(rarity)) {
								String name = config.getString("type.rewards." + rewardid + ".name");
								String command = config.getString("type.rewards." + rewardid + ".command");

								List<Reward> rewardsRarity;
								if (rewards.get(rarity) == null) {
									rewardsRarity = new ArrayList<>();
								} else {
									rewardsRarity = rewards.get(rarity);
								}
								rewardsRarity.add(new Reward(rewardid, name, rarity, command));

								rewards.put(rarity, rewardsRarity);
							}
						}
					}
				}
			}
			cubeletType.setRewards(rewards);
			Main.log.sendMessage(ColorUtil.translate("    &a'" + cubeletType.getId() + "' &7- " + (cubeletType.getRewards().size() > 0 ? "&a" : "&c") + cubeletType.getRewards().size() + " rewards"));
		}

		Main.log.sendMessage(ColorUtil.translate(""));
	}

	private boolean validRewardData(FileConfiguration config, String rewardID) {
		return config.contains("type.rewards." + rewardID + ".name")
				&& config.contains("type.rewards." + rewardID + ".rarity")
				&& config.contains("type.rewards." + rewardID + ".command");
	}

}
