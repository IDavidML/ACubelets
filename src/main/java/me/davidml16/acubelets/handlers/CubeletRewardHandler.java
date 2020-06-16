package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.interfaces.Reward;
import me.davidml16.acubelets.objects.*;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.XSeries.XItemStack;
import org.bukkit.Bukkit;
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
								ItemStack rewardIcon = XItemStack.deserialize(config, "type.rewards." + rewardid + ".icon");

								List<Reward> rewardsRarity;
								if (rewards.get(rarity) == null) {
									rewardsRarity = new ArrayList<>();
								} else {
									rewardsRarity = rewards.get(rarity);
								}

								if(config.contains("type.rewards." + rewardid + ".command")) {

									List<String> commands = new ArrayList<>();
									if (config.get("type.rewards." + rewardid + ".command") instanceof ArrayList)
										commands.addAll(config.getStringList("type.rewards." + rewardid + ".command"));
									else
										commands.add(config.getString("type.rewards." + rewardid + ".command"));

									rewardsRarity.add(new CommandReward(rewardid, name, cubeletType.getRarities().get(rarity), commands, rewardIcon));

								} else if(config.contains("type.rewards." + rewardid + ".permission")) {

									String permission = config.getString("type.rewards." + rewardid + ".permission");

									rewardsRarity.add(new PermissionReward(rewardid, name, cubeletType.getRarities().get(rarity), permission, rewardIcon));

								}

								rewards.put(rarity, rewardsRarity);

								rewardsLoaded++;
							}
						}
					}
				}
			}
			cubeletType.setRewards(rewards);
			cubeletType.saveType();

			Main.log.sendMessage(ColorUtil.translate("    &a'" + cubeletType.getId() + "' &7- " + (rewardsLoaded > 0 ? "&a" : "&c") + rewardsLoaded + " rewards"));
		}

		Main.log.sendMessage(ColorUtil.translate(""));
	}

	private boolean validRewardData(FileConfiguration config, String rewardID) {
		return config.contains("type.rewards." + rewardID + ".name")
				&& config.contains("type.rewards." + rewardID + ".rarity")
				&& config.contains("type.rewards." + rewardID + ".icon");
	}

	public Reward processReward(Player p, CubeletType cubeletType) {
		List<Rarity> rarities = getAvailableRarities(cubeletType);

		Rarity randomRarity = chooseOnWeight(rarities);
		if(cubeletType.getRewards().containsKey(randomRarity.getId())) {
			List<Reward> commandRewards = cubeletType.getRewards().get(randomRarity.getId());

			int randomElementIndex = ThreadLocalRandom.current().nextInt(commandRewards.size()) % commandRewards.size();
			Reward randomReward = commandRewards.get(randomElementIndex);
			return randomReward;
		} else {
			p.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() +
					" &cThere has been a problem with your reward, please notify the server staff."));
		}
		return null;
	}

	public void giveReward(CubeletBox cubeletBox, Reward reward) {
		if (reward instanceof CommandReward) {
			for (String command : ((CommandReward) reward).getCommands()) {
				Bukkit.getServer().dispatchCommand(main.getServer().getConsoleSender(), command.replaceAll("%player%", cubeletBox.getPlayerOpening().getName()));
			}
		} else if(reward instanceof PermissionReward) {
			Bukkit.getServer().dispatchCommand(
					main.getServer().getConsoleSender(),
					main.getDuplicationCommand()
							.replaceAll("%player%", cubeletBox.getPlayerOpening().getName())
							.replaceAll("%permission%", ((PermissionReward) reward).getPermission()));
		}
	}

	public void permissionReward(CubeletBox cubeletBox, Reward reward) {
		if(cubeletBox.getPlayerOpening().hasPermission(((PermissionReward) reward).getPermission())) {

			int min = Math.min(Integer.parseInt(main.getDuplicationPointsRange().split("-")[0]),
					Integer.parseInt(main.getDuplicationPointsRange().split("-")[1]));
			int max = Math.max(Integer.parseInt(main.getDuplicationPointsRange().split("-")[0]),
					Integer.parseInt(main.getDuplicationPointsRange().split("-")[1]));

			int randomPoints = ThreadLocalRandom.current().nextInt(min, max);
			cubeletBox.setLastDuplicationPoints(randomPoints);
			main.getHologramHandler().duplicationRewardHologram(cubeletBox, reward);
		}
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
