package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.interfaces.Reward;
import me.davidml16.acubelets.objects.*;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.RepeatingTask;
import me.davidml16.acubelets.utils.XSeries.XItemStack;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CubeletRewardHandler {

	private Main main;
	public CubeletRewardHandler(Main main) {
		this.main = main;
	}

	public void loadRewards() {
		Main.log.sendMessage(Utils.translate("  &eLoading rewards:"));

		int loaded = 0;
		for(CubeletType cubeletType : main.getCubeletTypesHandler().getTypes().values()) {
			loaded += loadReward(cubeletType, true);
		}

		if(loaded == 0)
			Main.log.sendMessage(Utils.translate("    &cNo cubelet rewards has been loaded!"));

		Main.log.sendMessage(Utils.translate(""));
	}

	public int loadReward(CubeletType cubeletType, boolean log) {
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

		if(log) Main.log.sendMessage(Utils.translate("    &a'" + cubeletType.getId() + "' &7- " + (rewardsLoaded > 0 ? "&a" : "&c") + rewardsLoaded + " rewards"));

		return rewardsLoaded;
	}

	private boolean validRewardData(FileConfiguration config, String rewardID) {
		return config.contains("type.rewards." + rewardID + ".name")
				&& config.contains("type.rewards." + rewardID + ".rarity")
				&& config.contains("type.rewards." + rewardID + ".icon");
	}

	public Reward processReward(CubeletType cubeletType) {
		List<Rarity> rarities = getAvailableRarities(cubeletType);

		Rarity randomRarity = chooseOnWeight(rarities);
		if(cubeletType.getRewards().containsKey(randomRarity.getId())) {
			List<Reward> commandRewards = cubeletType.getRewards().get(randomRarity.getId());

			if(commandRewards.size() <= 0) return processReward(cubeletType);

			int randomElementIndex = ThreadLocalRandom.current().nextInt(commandRewards.size()) % commandRewards.size();

			return commandRewards.get(randomElementIndex);
		}
		return null;
	}

	public void giveReward(CubeletBox cubeletBox, Reward reward) {
		if (reward instanceof CommandReward) {
			for (String command : ((CommandReward) reward).getCommands()) {
				Bukkit.getServer().dispatchCommand(main.getServer().getConsoleSender(), command.replaceAll("%player%", cubeletBox.getPlayerOpening().getName()));
			}
		} else if(reward instanceof PermissionReward) {
			if(main.isDuplicationEnabled() && isDuplicated(cubeletBox, reward)) {
				Bukkit.getServer().dispatchCommand(
						main.getServer().getConsoleSender(),
						main.getDuplicationPointsCommand()
								.replaceAll("%player%", cubeletBox.getPlayerOpening().getName())
								.replaceAll("%points%", ""+cubeletBox.getLastDuplicationPoints()));
			} else {
				Bukkit.getServer().dispatchCommand(
						main.getServer().getConsoleSender(),
						main.getDuplicationPermissionCommand()
								.replaceAll("%player%", cubeletBox.getPlayerOpening().getName())
								.replaceAll("%permission%", ((PermissionReward) reward).getPermission()));
			}
		}
	}

	public RepeatingTask permissionReward(CubeletBox cubeletBox, Reward reward) {
		if(isDuplicated(cubeletBox, reward)) {

			int min = Math.min(Integer.parseInt(reward.getRarity().getDuplicatePointsRange().split("-")[0]),
					Integer.parseInt(reward.getRarity().getDuplicatePointsRange().split("-")[1]));
			int max = Math.max(Integer.parseInt(reward.getRarity().getDuplicatePointsRange().split("-")[0]),
					Integer.parseInt(reward.getRarity().getDuplicatePointsRange().split("-")[1]));

			int randomPoints = ThreadLocalRandom.current().nextInt(min, max);
			cubeletBox.setLastDuplicationPoints(randomPoints);
			return main.getHologramHandler().duplicationRewardHologram(cubeletBox, reward);
		}
		return null;
	}

	public boolean isDuplicated(CubeletBox cubeletBox, Reward reward) {
		if(reward instanceof PermissionReward) {
			Player player = Bukkit.getPlayer(cubeletBox.getPlayerOpening().getUuid());

			if(player == null) return false;

			if(player.hasPermission(((PermissionReward) reward).getPermission())) return true;

			return false;
		}
		return false;
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
