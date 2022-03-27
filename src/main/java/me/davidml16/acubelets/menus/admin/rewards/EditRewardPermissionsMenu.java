package me.davidml16.acubelets.menus.admin.rewards;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.rewards.EditPermissionObjectRewardConversation;
import me.davidml16.acubelets.conversation.rewards.PermissionObjectRewardConversation;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.objects.rewards.PermissionObject;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EditRewardPermissionsMenu extends Menu {

    public EditRewardPermissionsMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        Reward reward = (Reward) getAttribute(AttrType.REWARD_ATTR);

        List<PermissionObject> permissions = reward.getPermissions();

        if(page > 0 && permissions.size() < (page * 21) + 1) {
            openPage(getPage() - 1);
            return;
        }

        Inventory gui = createInventory(45, "%reward% | Permissions".replaceAll("%reward%", reward.getId()));

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack to config")).toItemStack();

        ItemStack newCommand = new ItemBuilder(XMaterial.SUNFLOWER.parseItem()).setName(Utils.translate("&aCreate new permission")).toItemStack();

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

        if (permissions.size() > (page + 1) * 21) {
            gui.setItem(26, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aNext page")).toItemStack());
        } else {
            gui.setItem(26, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        gui.setItem(39, newCommand);
        gui.setItem(41, back);

        if (permissions.size() > 21) permissions = permissions.subList(page * 21, ((page * 21) + 21) > permissions.size() ? permissions.size() : (page * 21) + 21);

        if(permissions.size() > 0) {

            for (PermissionObject permission : permissions) {

                gui.addItem(new ItemBuilder(XMaterial.PAPER.parseItem())
                        .setName(Utils.translate("&a" + permission.getId()))
                        .setLore(
                                "",
                                Utils.translate(" &7Permission: &6" + permission.getPermission()),
                                "",
                                Utils.translate("&eLeft-Click » &aRemove permission "),
                                Utils.translate("&eRight-Click » &aEdit permission ")
                        ).toItemStack());

            }

        } else {

            gui.setItem(22, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cAny permissions added")).setLore(
                    "",
                    Utils.translate(" &7You dont have any "),
                    Utils.translate(" &7permissions added. "),
                    ""
            ).toItemStack());

        }

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;

        int slot = event.getRawSlot();

        Reward reward = (Reward) getAttribute(AttrType.REWARD_ATTR);
        CubeletType cubeletType = reward.getParentCubelet();

        Player player = getOwner();

        if (slot == 18 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            previousPage();

        } else if (slot == 26 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            nextPage();

        } else if (slot == 39) {

            player.closeInventory();

            new PermissionObjectRewardConversation(getMain()).getConversation(player, cubeletType, reward).begin();

            playSound(SoundType.ANVIL_USE);

        } else if (slot == 41) {

            RewardsMenu rewardsMenu = new RewardsMenu(getMain(), player);
            rewardsMenu.setAttribute(AttrType.CUSTOM_ID_ATTR, cubeletType.getId());
            rewardsMenu.open();

        } else if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25) || (slot >= 28 && slot <= 34)) {

            if (reward.getPermissions().size() == 0) return;

            String permissionID = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            PermissionObject permissionObject = reward.getPermission(permissionID);

            if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {

                reward.getPermissions().remove(permissionObject);
                reward.recreatePermissions();

                player.sendMessage(Utils.translate(getMain().getLanguageHandler().getPrefix()
                        + " &aYou removed &e" + permissionObject.getId() + " &afrom permissions of reward &e" + reward.getId()));

                reloadMenu();

            } else if(event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {

                player.closeInventory();

                new EditPermissionObjectRewardConversation(getMain()).getConversation(player, cubeletType, reward, permissionObject).begin();

                playSound(SoundType.ANVIL_USE);

            }

        }

    }

    @Override
    public void OnMenuClosed() {

        Reward reward = (Reward) getAttribute(AttrType.REWARD_ATTR);

        reward.getParentCubelet().saveType();

    }

}
