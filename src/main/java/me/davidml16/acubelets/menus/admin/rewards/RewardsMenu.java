package me.davidml16.acubelets.menus.admin.rewards;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.rewards.*;
import me.davidml16.acubelets.menus.admin.type.TypeConfigMenu;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RewardsMenu extends Menu {

    public RewardsMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        String id = (String) getAttribute(AttrType.CUSTOM_ID_ATTR);

        List<Reward> rewards = getMain().getCubeletTypesHandler().getTypeBydId(id).getAllRewards();

        if(page > 0 && rewards.size() < (page * 21) + 1) {
            openPage(getPage() -1);
            return;
        }

        Inventory gui = createInventory(45, "%cubelet_type% | Rewards".replaceAll("%cubelet_type%", id));

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack newReward = new ItemBuilder(XMaterial.SUNFLOWER.parseItem()).setName(Utils.translate("&aReward creator"))
                .setLore(
                        "",
                        Utils.translate("&eClick to create a new reward "))
                .toItemStack();
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack to config")).toItemStack();

        fillBorders(edge);

        for (int i = 10; i <= 16; i++)
            gui.setItem(i, null);
        for (int i = 19; i <= 25; i++)
            gui.setItem(i, null);
        for (int i = 28; i <= 34; i++)
            gui.setItem(i, null);

        if (page > 0) {
            gui.setItem(18, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aPrevious page")).toItemStack());
        } else {
            gui.setItem(18, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (rewards.size() > (page + 1) * 21) {
            gui.setItem(26, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aNext page")).toItemStack());
        } else {
            gui.setItem(26, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (rewards.size() > 21) rewards = rewards.subList(page * 21, ((page * 21) + 21) > rewards.size() ? rewards.size() : (page * 21) + 21);

        if(rewards.size() > 0) {

            for (Reward reward : rewards) {

                gui.addItem(new ItemBuilder(reward.getIcon().clone())
                        .setName(Utils.translate("&a" + reward.getId()))
                        .setLore(
                                "",
                                Utils.translate(" &7Name: &6" + reward.getName() + " "),
                                Utils.translate(" &7Rarity: &6" + reward.getRarity().getId() + " "),
                                Utils.translate(" &7Icon: &6" + reward.getIcon().getType().name() + " "),
                                "",
                                Utils.translate(" &7Commands: &6" + reward.getCommands().size() + " "),
                                Utils.translate(" &7Permissions: &6" + reward.getPermissions().size() + " "),
                                Utils.translate(" &7Items: &6" + reward.getItems().size() + " "),
                                "",
                                Utils.translate(" &7Bypass Duplication: &6" + reward.isBypassDuplicationSystem() + " "),
                                "",
                                Utils.translate("&eLeft-Click » &aEdit commands "),
                                Utils.translate("&eMiddle-Click » &aEdit permissions "),
                                Utils.translate("&eRight-Click » &aEdit items "),
                                Utils.translate("&eShift-Left-Click » &aEdit reward properties "),
                                Utils.translate("&eShift-Right-Click » &aDelete reward ")
                        ).hideAttributes().toItemStack());

            }

        } else {

            gui.setItem(22, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cAny rewards created")).setLore(
                    "",
                    Utils.translate(" &7You dont have any "),
                    Utils.translate(" &7reward created. "),
                    ""
            ).toItemStack());

        }

        gui.setItem(39, newReward);
        gui.setItem(41, back);

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        int slot = event.getRawSlot();
        String id = (String) getAttribute(AttrType.CUSTOM_ID_ATTR);

        Player player = getOwner();

        CubeletType cubeletType = getMain().getCubeletTypesHandler().getTypeBydId(id);

        if (slot == 18 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            previousPage();

            playSound(SoundType.CLICK);

        } else if (slot == 26 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            nextPage();

            playSound(SoundType.CLICK);

        } else if (slot == 39) {

            player.closeInventory();

            new RewardConversation(getMain()).getConversation(player, cubeletType).begin();

            playSound(SoundType.ANVIL_USE);

        } else if (slot == 41) {

            TypeConfigMenu typeConfigMenu = new TypeConfigMenu(getMain(), player);
            typeConfigMenu.setAttribute(AttrType.CUSTOM_ID_ATTR, cubeletType.getId());
            typeConfigMenu.open();

        } else if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25) || (slot >= 28 && slot <= 34)) {

            if (event.getCurrentItem().getType() == Material.AIR) return;

            if (cubeletType.getAllRewards().size() == 0) return;

            String rewardID = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            Reward reward = cubeletType.getReward(rewardID);

            if(event.getClick() == ClickType.LEFT) {

                EditRewardCommandsMenu editRewardCommandsMenu = new EditRewardCommandsMenu(getMain(), player);
                editRewardCommandsMenu.setAttribute(AttrType.REWARD_ATTR, reward);
                editRewardCommandsMenu.open();

            } else if(event.getClick() == ClickType.MIDDLE) {

                EditRewardPermissionsMenu editRewardPermissionsMenu = new EditRewardPermissionsMenu(getMain(), player);
                editRewardPermissionsMenu.setAttribute(AttrType.REWARD_ATTR, reward);
                editRewardPermissionsMenu.open();

            } else if(event.getClick() == ClickType.RIGHT) {

                EditRewardItemsMenu editRewardItemsMenu = new EditRewardItemsMenu(getMain(), player);
                editRewardItemsMenu.setAttribute(AttrType.REWARD_ATTR, reward);
                editRewardItemsMenu.open();

            } else if(event.getClick() == ClickType.SHIFT_LEFT) {

                player.closeInventory();

                new EditRewardConversation(getMain()).getConversation(player, cubeletType, reward).begin();

                playSound(SoundType.ANVIL_USE);

            } else if(event.getClick() == ClickType.SHIFT_RIGHT) {

                Map<String, List<Reward>> rewardsAll = cubeletType.getRewards();
                List<Reward> commandRewards = cubeletType.getRewards().get(reward.getRarity().getId());
                commandRewards.remove(reward);
                rewardsAll.put(reward.getRarity().getId(), commandRewards);
                cubeletType.setRewards(rewardsAll);

                player.sendMessage(Utils.translate(getMain().getLanguageHandler().getPrefix()
                        + " &aYou removed reward &e" + rewardID + " &afrom rewards of cubelet type &e" + cubeletType.getId()));

                reloadMenu();

                playSound(SoundType.CLICK);

            }

        }

    }

    @Override
    public void OnMenuClosed() {

        String id = (String) getAttribute(AttrType.CUSTOM_ID_ATTR);

        getMain().getCubeletTypesHandler().getTypeBydId(id).saveType();

    }

}
