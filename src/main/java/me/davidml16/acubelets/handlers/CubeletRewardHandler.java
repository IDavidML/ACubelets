package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.data.CubeletType;
import me.davidml16.acubelets.data.Rarity;
import me.davidml16.acubelets.data.Reward;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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
			int rewardsLoaded = 0;

			if (config.contains("type.rewards")) {
				if (config.getConfigurationSection("type.rewards") != null) {
					for (String rewardid : config.getConfigurationSection("type.rewards").getKeys(false)) {
						if(validRewardData(config, rewardid)) {
							String rarity = config.getString("type.rewards." + rewardid + ".rarity");
							if (cubeletType.getRarities().containsKey(rarity)) {
								String name = config.getString("type.rewards." + rewardid + ".name");
								String command = config.getString("type.rewards." + rewardid + ".command");
								String material = config.getString("type.rewards." + rewardid + ".icon");

								List<Reward> rewardsRarity;
								if (rewards.get(rarity) == null) {
									rewardsRarity = new ArrayList<>();
								} else {
									rewardsRarity = rewards.get(rarity);
								}

								String[] parts = material.split(":");
								ItemStack icon = null;
								if(parts.length == 1) {
									icon = new ItemStack(Material.matchMaterial(parts[0]));
								} else if(parts.length == 2) {
									icon = new ItemBuilder(Material.matchMaterial(parts[0]), 1, Byte.parseByte(parts[1])).toItemStack();
								}

								rewardsRarity.add(new Reward(rewardid, name, cubeletType.getRarities().get(rarity), command, icon));
								rewards.put(rarity, rewardsRarity);

								rewardsLoaded++;
							}
						}
					}
				}
			}
			cubeletType.setRewards(rewards);
			Main.log.sendMessage(ColorUtil.translate("    &a'" + cubeletType.getId() + "' &7- " + (rewardsLoaded > 0 ? "&a" : "&c") + rewardsLoaded + " rewards"));
		}

		Main.log.sendMessage(ColorUtil.translate(""));
	}

	private boolean validRewardData(FileConfiguration config, String rewardID) {
		return config.contains("type.rewards." + rewardID + ".name")
				&& config.contains("type.rewards." + rewardID + ".rarity")
				&& config.contains("type.rewards." + rewardID + ".command")
				&& config.contains("type.rewards." + rewardID + ".icon");
	}

	public Reward processReward(Player p, CubeletType cubeletType) {
		List<Rarity> rarities = getAvailableRarities(cubeletType);

		Rarity randomRarity = chooseOnWeight(rarities);
		if(cubeletType.getRewards().containsKey(randomRarity.getId())) {
			List<Reward> rewards = cubeletType.getRewards().get(randomRarity.getId());

			int randomElementIndex = ThreadLocalRandom.current().nextInt(rewards.size()) % rewards.size();
			Reward randomReward = rewards.get(randomElementIndex);

			Bukkit.getServer().dispatchCommand(main.getServer().getConsoleSender(), randomReward.getCommand().replaceAll("%player%", p.getName()));

			return randomReward;
		} else {
			p.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() +
					" &cThere has been a problem with your reward, please notify the server staff."));
		}
		return null;
	}

	public List<Rarity> getAvailableRarities(CubeletType cubeletType) {
		List<Rarity> rarities = new ArrayList<>();
		for(String idRarity : cubeletType.getRewards().keySet()) {
			rarities.add(cubeletType.getRarities().get(idRarity));
		}
		return rarities;
	}


	public Rarity chooseOnWeight(List<Rarity> items) {
		double completeWeight = 0.0;
		for (Rarity rarity : items)
			completeWeight += rarity.getChance();
		double r = Math.random() * completeWeight;
		double countWeight = 0.0;
		for (Rarity item : items) {
			countWeight += item.getChance();
			if (countWeight >= r)
				return item;
		}
		throw new RuntimeException("Should never be shown.");
	}

}
