package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.interfaces.RarityComparator;
import me.davidml16.acubelets.interfaces.RewardComparator;
import me.davidml16.acubelets.interfaces.RewardIDComparator;
import me.davidml16.acubelets.objects.rewards.*;
import me.davidml16.acubelets.utils.XSeries.XItemStack;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CubeletType {

    private Main main;

    private String id;
    private String name;

    private Long expireTime;

    private ItemStack icon;
    private List<String> loreAvailable;
    private List<String> loreExpired;
    private List<String> description;

    private String animation;

    private Map<String, List<Reward>> rewards;
    private Map<String, Rarity> rarities;

    public CubeletType(Main main, String id, String name) {
        this.main = main;
        this.id = id;
        this.name = name;
        this.rewards = new HashMap<>();
        this.rarities = new HashMap<>();
        this.expireTime = 0L;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, List<Reward>> getRewards() { return rewards; }

    public List<Reward> getAllRewards() {
        List<Reward> rewards = new ArrayList<>();
        List<Rarity> rarities = new ArrayList<>(getRarities().values());

        if(main.isRewardSorting()) rarities.sort(new RarityComparator());

        for (Rarity rarity : rarities) {

            List<Reward> rarityRewards = new ArrayList<>(getRewards().getOrDefault(rarity.getId(), new ArrayList<>()));
            if(main.isRewardSorting()) rarityRewards.sort(new RewardComparator());

            rewards.addAll(rarityRewards);

        }

        if(!main.isRewardSorting())
            rewards.sort(new RewardIDComparator());

        return rewards;
    }

    public Reward getReward(String id) {
        for(Reward reward : getAllRewards())
            if(reward.getId().equalsIgnoreCase(id))
                return reward;
        return  null;
    }

    public List<String> getRaritiesIDs() {
        List<Rarity> rts = new ArrayList<>(getRarities().values());
        rts.sort(new RarityComparator());

        List<String> rarities = new ArrayList<>();
        for(Rarity rarity : rts) {
            rarities.add(rarity.getId());
        }

        return rarities;
    }

    public void setRewards(Map<String, List<Reward>> rewards) { this.rewards = rewards; }

    public Map<String, Rarity> getRarities() { return rarities; }

    public void setRarities(Map<String, Rarity> rarities) { this.rarities = rarities; }

    public ItemStack getIcon() { return icon; }

    public void setIcon(ItemStack icon) { this.icon = icon; }

    public List<String> getLoreAvailable() {
        return loreAvailable;
    }

    public void setLoreAvailable(List<String> loreAvailable) {
        this.loreAvailable = loreAvailable;
    }

    public List<String> getLoreExpired() {
        return loreExpired;
    }

    public void setLoreExpired(List<String> loreExpired) {
        this.loreExpired = loreExpired;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public void addReward(String rarity, Reward reward) {
        Map<String, List<Reward>> rewardsAll = getRewards();
        List<Reward> commandRewards;
        if(getRewards().get(rarity) == null) commandRewards = new ArrayList<>();
        else commandRewards = getRewards().get(rarity);
        commandRewards.add(reward);
        rewardsAll.put(reward.getRarity().getId(), commandRewards);
        setRewards(rewardsAll);
    }

    @Override
    public String toString() {
        return "CubeletType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                ", rewards=" + rewards +
                ", rarities=" + rarities +
                '}';
    }

    public void saveType() {
        FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);

        config.set("type.rarities", new ArrayList<>());
        if (config.contains("type.rarities")) {
            List<Rarity> rts = new ArrayList<>(rarities.values());
            rts.sort(new RarityComparator());
            for (Rarity rarity : rts) {
                config.set("type.rarities." + rarity.getId() + ".name", rarity.getName());
                config.set("type.rarities." + rarity.getId() + ".chance", rarity.getChance());
                config.set("type.rarities." + rarity.getId() + ".duplicatePointsRange", rarity.getDuplicatePointsRange());
            }
        }

        config.set("type.rewards", new ArrayList<>());
        if (config.contains("type.rewards")) {
            List<Reward> rewards = getAllRewards();
            for (int i = 0; i < rewards.size(); i++) {
                Reward reward = rewards.get(i);
                config.set("type.rewards.reward_" + String.valueOf(i) + ".name", reward.getName());
                config.set("type.rewards.reward_" + String.valueOf(i) + ".rarity", reward.getRarity().getId());

                if(reward instanceof CommandReward)
                    config.set("type.rewards.reward_" + String.valueOf(i) + ".command", ((CommandReward) reward).getCommands());
                else if(reward instanceof PermissionReward)
                    config.set("type.rewards.reward_" + String.valueOf(i) + ".permission", ((PermissionReward) reward).getPermission());
                else if(reward instanceof ItemReward) {
                    List<Item> items = ((ItemReward) reward).getItems();
                    config.set("type.rewards.reward_" + String.valueOf(i) + ".item", new ArrayList<>());
                    for (int j = 0; j < items.size(); j++) {
                        if(!main.isSerializeBase64())
                            XItemStack.serializeItem(items.get(j).getItemStack(), config, "type.rewards.reward_" + i + ".item.item_" + j);
                        else
                            config.set("type.rewards.reward_" + i + ".item.item_" + j, XItemStack.itemStackToBase64(items.get(j).getItemStack()));
                    }
                }

                if(!main.isSerializeBase64())
                    XItemStack.serializeIcon(reward.getIcon(), config, "type.rewards.reward_" + i + ".icon", true);
                else
                    config.set("type.rewards.reward_" + i + ".icon", XItemStack.itemStackToBase64(reward.getIcon()));
            }
        }

        main.getCubeletTypesHandler().saveConfig(id);
    }

}
