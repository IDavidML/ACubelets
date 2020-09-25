package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.rewards.*;
import me.davidml16.acubelets.objects.*;
import me.davidml16.acubelets.utils.NBTEditor;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.RepeatingTask;
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
		for(CubeletType cubeletType : main.getCubeletTypesHandler().getTypes().values()) {
			loadReward(cubeletType);
		}
	}

	public void loadReward(CubeletType cubeletType) {
		FileConfiguration config = main.getCubeletTypesHandler().getConfig(cubeletType.getId());

		Map<String, List<Reward>> rewards = new HashMap<>();
		int rewardsLoaded = 0;

		if (config.contains("type.rewards")) {
			if (config.getConfigurationSection("type.rewards") != null) {
				int iterator = 0;
				for (String rewardid : config.getConfigurationSection("type.rewards").getKeys(false)) {
					if(validRewardData(config, rewardid)) {
						String rarity = config.getString("type.rewards." + rewardid + ".rarity");
						if (cubeletType.getRarities().containsKey(rarity)) {

							String name = config.getString("type.rewards." + rewardid + ".name");
							ItemStack rewardIcon = XItemStack.deserializeIcon(config, "type.rewards." + rewardid + ".icon", true);
							rewardIcon = NBTEditor.set(rewardIcon, "reward_" + iterator, "rewardID");

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

								rewardsRarity.add(new CommandReward("reward_" + iterator, name, cubeletType.getRarities().get(rarity), commands, rewardIcon, cubeletType));

							} else if(config.contains("type.rewards." + rewardid + ".permission")) {

								String permission = config.getString("type.rewards." + rewardid + ".permission");

								rewardsRarity.add(new PermissionReward("reward_" + iterator, name, cubeletType.getRarities().get(rarity), permission, rewardIcon, cubeletType));

							} else if(config.contains("type.rewards." + rewardid + ".item")) {

								List<Item> items = new ArrayList<>();
								int iterator2 = 0;
								if (config.contains("type.rewards." + rewardid + ".item")) {
									if (config.getConfigurationSection("type.rewards." + rewardid + ".item") != null) {
										for (String itemid : config.getConfigurationSection("type.rewards." + rewardid + ".item").getKeys(false)) {
											items.add(new Item("item_" + iterator2, XItemStack.deserializeItem(config, "type.rewards." + rewardid + ".item." + itemid)));
											iterator2++;
										}
									}
								}

								rewardsRarity.add(new ItemReward("reward_" + iterator, name, cubeletType.getRarities().get(rarity), items, rewardIcon, cubeletType));

							}

							rewards.put(rarity, rewardsRarity);

							iterator++;
							rewardsLoaded++;
						}
					}
				}
			}
		}

		cubeletType.setRewards(rewards);
		cubeletType.saveType();

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
			List<Reward> rewards = cubeletType.getRewards().get(randomRarity.getId());

			if(rewards.size() <= 0) return processReward(cubeletType);

			int randomElementIndex = ThreadLocalRandom.current().nextInt(rewards.size()) % rewards.size();

			return rewards.get(randomElementIndex);
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
		} else if(reward instanceof ItemReward) {
			Player target = Bukkit.getPlayer(cubeletBox.getPlayerOpening().getUuid());
			for (Item item : ((ItemReward) reward).getItems()) {
				if(target == null)
					cubeletBox.getLocation().getWorld().dropItemNaturally(cubeletBox.getLocation().clone().add(0.5, 1, 0.5), item.getItemStack().clone());
				else
					if(target.getInventory().firstEmpty() >= 0)
						target.getInventory().addItem(item.getItemStack());
					else
						target.getLocation().getWorld().dropItemNaturally(target.getLocation(), item.getItemStack().clone());
			}

			if(target != null)
				Sounds.playSound(target, target.getLocation(), Sounds.MySound.ITEM_PICKUP, 0.5F, (float) ThreadLocalRandom.current().nextDouble(1, 3));
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
