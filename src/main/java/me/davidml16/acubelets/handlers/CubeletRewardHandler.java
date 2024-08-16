package me.davidml16.acubelets.handlers;

import com.cryptomorin.xseries.XItemStack;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.player.LootHistoryMenu;
import me.davidml16.acubelets.objects.CubeletMachine;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.objects.Rarity;
import me.davidml16.acubelets.objects.loothistory.LootHistory;
import me.davidml16.acubelets.objects.loothistory.RewardHistory;
import me.davidml16.acubelets.objects.rewards.CommandObject;
import me.davidml16.acubelets.objects.rewards.ItemObject;
import me.davidml16.acubelets.objects.rewards.PermissionObject;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
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

							ItemStack rewardIcon = null;
							if (config.get("type.rewards." + rewardid + ".icon") instanceof MemorySection) {
								rewardIcon = XItemStack.deserialize(Utils.getConfigurationSection(config, "type.rewards." + rewardid + ".icon"));
							} else {
								try {
									rewardIcon = ItemStack64.itemStackFromBase64(config.getString("type.rewards." + rewardid + ".icon"));
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

							try {
								rewardIcon = NBTEditor.set(rewardIcon, "reward_" + iterator, "rewardID");
							} catch (IllegalArgumentException exception) {
								exception.printStackTrace();
								continue;
							}

							List<Reward> rewardsRarity;
							if (rewards.get(rarity) == null) {
								rewardsRarity = new ArrayList<>();
							} else {
								rewardsRarity = rewards.get(rarity);
							}

							List<CommandObject> commands = new ArrayList<>();
							List<PermissionObject> permissions = new ArrayList<>();
							List<ItemObject> items = new ArrayList<>();

							if(config.contains("type.rewards." + rewardid + ".command"))
								getRewardCommands(config, commands, rewardid);

							if(config.contains("type.rewards." + rewardid + ".permission"))
								getRewardPermissions(config, permissions, rewardid);

							if(config.contains("type.rewards." + rewardid + ".item"))
								getRewardItems(config, items, rewardid);

							Reward reward = new Reward("reward_" + iterator, name, cubeletType.getRarities().get(rarity), rewardIcon, cubeletType);
							reward.setCommands(commands);
							reward.setPermissions(permissions);
							reward.setItems(items);

							if(config.contains("type.rewards." + rewardid + ".rewardUUID"))
								reward.setRewardUUID(UUID.fromString(config.getString("type.rewards." + rewardid + ".rewardUUID")));

							reward.setBypassDuplicationSystem(config.getBoolean("type.rewards." + rewardid + ".bypassDuplicationSystem", false));

							rewardsRarity.add(reward);

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

	private void getRewardCommands(FileConfiguration config, List<CommandObject> list, String rewardid) {

		int i = 0;
		if (config.get("type.rewards." + rewardid + ".command") instanceof ArrayList) {
			for(String cmd : config.getStringList("type.rewards." + rewardid + ".command")) {
				list.add(new CommandObject("command-" + i, cmd));
				i++;
			}
		} else {
			list.add(new CommandObject("command-" + i, config.getString("type.rewards." + rewardid + ".command")));
		}

	}

	private void getRewardPermissions(FileConfiguration config, List<PermissionObject> list, String rewardid) {

		int i = 0;
		if (config.get("type.rewards." + rewardid + ".permission") instanceof ArrayList) {
			for(String permission : config.getStringList("type.rewards." + rewardid + ".permission")) {
				list.add(new PermissionObject("permission-" + i, permission));
				i++;
			}
		} else {
			list.add(new PermissionObject("permission-" + i, config.getString("type.rewards." + rewardid + ".permission")));
		}

	}

	private void getRewardItems(FileConfiguration config, List<ItemObject> list, String rewardid) {

		int i = 0;
		if (config.contains("type.rewards." + rewardid + ".item")) {
			if (config.getConfigurationSection("type.rewards." + rewardid + ".item") != null) {
				for (String itemid : config.getConfigurationSection("type.rewards." + rewardid + ".item").getKeys(false)) {
					if (config.get("type.rewards." + rewardid + ".item." + itemid) instanceof MemorySection) {
						list.add(new ItemObject("item-" + i, XItemStack.deserialize(Utils.getConfigurationSection(config, "type.rewards." + rewardid + ".item." + itemid))));
					} else {
						try {
							list.add(new ItemObject("item-" + i, ItemStack64.itemStackFromBase64(config.getString("type.rewards." + rewardid + ".item." + itemid))));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					i++;
				}
			}
		}

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

	public void giveReward(CubeletMachine cubeletMachine, CubeletType cubeletType, Reward reward) {

		UUID playerUUID = cubeletMachine.getPlayerOpening().getUuid();

		Player target = Bukkit.getPlayer(playerUUID);

		if(main.isSetting("Rewards.Broadcast"))
			MessageUtils.sendBroadcastMessage(main, cubeletMachine, cubeletType, reward);

		RewardHistory rewardHistory = new RewardHistory(reward.getRewardUUID(), reward.getName(), reward.getIcon());
		LootHistory lootHistory = new LootHistory(playerUUID, cubeletType.getName(), System.currentTimeMillis(), rewardHistory);

		if(main.isSetting("Rewards.Duplication.Enabled") && isDuplicated(cubeletMachine, reward)) {

			Bukkit.getServer().dispatchCommand(
					main.getServer().getConsoleSender(),
					main.getSetting("Rewards.Duplication.PointsCommand")
							.replaceAll("%player%", cubeletMachine.getPlayerOpening().getName())
							.replaceAll("%points%", ""+ cubeletMachine.getLastDuplicationPoints()));

			MessageUtils.sendLootMessage(cubeletMachine, cubeletType, reward);

			main.getDatabaseHandler().addLootHistory(playerUUID, lootHistory);

			if(target != null) {
				Profile profile = main.getPlayerDataHandler().getData(target);
				profile.getLootHistory().add(lootHistory);
			}

			return;

		}

		for (CommandObject commandObject : reward.getCommands())
			Bukkit.getServer().dispatchCommand(
					main.getServer().getConsoleSender(),
					commandObject.getCommand()
							.replaceAll("%player%", cubeletMachine.getPlayerOpening().getName())
			);

		for (PermissionObject permissionObject : reward.getPermissions())
			Bukkit.getServer().dispatchCommand(
					main.getServer().getConsoleSender(),
					main.getSetting("Rewards.PermissionCommand")
							.replaceAll("%player%", cubeletMachine.getPlayerOpening().getName())
							.replaceAll("%permission%", permissionObject.getPermission())
			);

		for (ItemObject itemObject : reward.getItems()) {
			if(target == null)
				cubeletMachine.getLocation().getWorld().dropItemNaturally(cubeletMachine.getLocation().clone().add(0.5, 1, 0.5), itemObject.getItemStack().clone());
			else
				if(target.getInventory().firstEmpty() >= 0)
					target.getInventory().addItem(itemObject.getItemStack());
				else
					target.getLocation().getWorld().dropItemNaturally(target.getLocation(), itemObject.getItemStack().clone());
		}

		if(!reward.getItems().isEmpty()) {
			if(target != null)
				Sounds.playSound(target, target.getLocation(), Sounds.MySound.ITEM_PICKUP, 0.5F, (float) ThreadLocalRandom.current().nextDouble(1, 3));
		}

		MessageUtils.sendLootMessage(cubeletMachine, cubeletType, reward);

		main.getDatabaseHandler().addLootHistory(playerUUID, lootHistory);

		if(target != null) {
			Profile profile = main.getPlayerDataHandler().getData(target);
			profile.getLootHistory().add(lootHistory);
			main.getMenuHandler().reloadAllMenus(target, LootHistoryMenu.class);
		}

	}

	public RepeatingTask duplicationTask(CubeletMachine cubeletMachine, Reward reward) {

		if(isDuplicated(cubeletMachine, reward)) {

			return main.getHologramImplementation().duplicationRewardHologram(cubeletMachine, reward);

		}

		return null;

	}

	public boolean isDuplicated(CubeletMachine cubeletMachine, Reward reward) {

		Profile profile = main.getPlayerDataHandler().getData(cubeletMachine.getPlayerOpening().getUuid());

		if(profile == null) return true;

		if(reward.isBypassDuplicationSystem()) return false;

		LootHistory lootHistory = profile.getLootHistory().stream().filter(history -> history.getRewardHistory().getUUID().toString().equalsIgnoreCase(reward.getRewardUUID().toString())).findFirst().orElse(null);

		return lootHistory != null;

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
