package me.davidml16.acubelets.menus.player.rewards;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.player.CubeletsMenu;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.objects.*;
import me.davidml16.acubelets.utils.NBTEditor;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.regex.Matcher;

public class RewardsPreviewMenu extends Menu {

    public RewardsPreviewMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        String type = (String) getAttribute(AttrType.CUSTOM_ID_ATTR);
        Boolean openedExternally = (Boolean) getAttribute(AttrType.OPENED_EXTERNALLY_ATTR);
        Player player = getOwner();

        CubeletType cubeletType = getMain().getCubeletTypesHandler().getTypeBydId(type);
        List<Reward> rewards = cubeletType.getAllRewards();

        GUILayout guiLayout = getMain().getLayoutHandler().getLayout("preview");

        int pageSize = getPageSize(guiLayout);

        if(page > 0 && rewards.size() < (page * pageSize) + 1) {
            openPage(getPage() - 1);
            return;
        }

        if (rewards.size() > pageSize) rewards = rewards.subList(page * pageSize, Math.min(((page * pageSize) + pageSize), rewards.size()));

        int neededSize = getNeededSize(guiLayout, rewards.size());

        Inventory gui = createInventory(neededSize, guiLayout.getMessage("Title").replaceAll("%cubelet_type%", Utils.removeColors(cubeletType.getName())));

        if (page > 0) {

            int amount = guiLayout.getBoolean("Items.PreviousPage.ShowPageNumber") ? page : 1;

            ItemStack item = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.PreviousPage.Material")).get().parseMaterial(), amount)
                    .setName(guiLayout.getMessage("Items.PreviousPage.Name"))
                    .toItemStack();
            item = NBTEditor.set(item, "previous", "action");

            if(guiLayout.getSlot("PreviousPage") >= 0)
                gui.setItem(((neededSize - 10) + guiLayout.getSlot("PreviousPage")), item);

        }

        if (cubeletType.getAllRewards().size() > (page + 1) * pageSize) {

            int amount = guiLayout.getBoolean("Items.NextPage.ShowPageNumber") ? (page + 2) : 1;

            ItemStack item = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.NextPage.Material")).get().parseMaterial(), amount)
                    .setName(guiLayout.getMessage("Items.NextPage.Name"))
                    .toItemStack();
            item = NBTEditor.set(item, "next", "action");

            if(guiLayout.getSlot("NextPage") >= 0)
                gui.setItem((neededSize - 10) + guiLayout.getSlot("NextPage"), item);

        }

        if(!openedExternally) {

            ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Back.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.Back.Name"))
                    .setLore(guiLayout.getMessageList("Items.Back.Lore"))
                    .toItemStack();
            back = NBTEditor.set(back, "back", "action");

            gui.setItem((neededSize - 10) + guiLayout.getSlot("Back"), back);

        } else {

            ItemStack close = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Close.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.Close.Name"))
                    .setLore(guiLayout.getMessageList("Items.Close.Lore"))
                    .toItemStack();
            close = NBTEditor.set(close, "close", "action");

            gui.setItem((neededSize - 10) + guiLayout.getSlot("Close"), close);

        }

        for (int i = 0; i <= (neededSize-10); i++)
            gui.setItem(i, null);

        if(rewards.size() > 0) {

            for (Reward reward : rewards) {

                List<String> lore = new ArrayList<>();

                if(reward.getIcon().getItemMeta().hasLore()) {

                    lore.addAll(reward.getIcon().getItemMeta().getLore());
                    lore.add("");

                }

                for (String line : guiLayout.getMessageList("Items.Reward.Lore")) {

                    line = line.replaceAll("%reward_rarity%", reward.getRarity().getName());
                    line = line.replaceAll("%reward_chance%", Double.toString(reward.getRarity().getChance()));
                    line = line.replaceAll("%reward_name%", Matcher.quoteReplacement(reward.getName()));
                    line = line.replaceAll("%reward_id%", reward.getId());

                    lore.add(Utils.translate(line));

                }

                gui.addItem(new ItemBuilder(reward.getIcon().clone())
                        .setName(Utils.translate(guiLayout.getMessage("Items.Reward.Name").replaceAll("%reward_name%", Matcher.quoteReplacement(reward.getName()))))
                        .setLore(lore)
                        .hideAttributes()
                        .toItemStack());

            }

        } else {

            player.closeInventory();

        }

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;

        Player player = getOwner();
        int slot = event.getRawSlot();

        int size = player.getOpenInventory().getTopInventory().getSize();

        if (slot >= (size - 9) && slot <= size) {

            String action = NBTEditor.getString(event.getCurrentItem(), "action");

            if(event.getClick() == ClickType.DOUBLE_CLICK) return;

            if(action == null) return;

            switch (action) {

                case "previous":
                    previousPage();
                    break;

                case "next":
                    nextPage();
                    break;

                case "close":
                    player.closeInventory();
                    break;

                case "back":
                    new CubeletsMenu(getMain(), player).open();
                    break;

            }

        }

        player.updateInventory();

    }

    @Override
    public void OnMenuClosed() { }

    private int getNeededSize(GUILayout guiLayout, int cubelets) {

        int finalRows = 0;
        int rows = guiLayout.getInteger("Size.Max-Cubelets-Rows");

        if(rows < 1) rows = 1;
        else if(rows > 5) rows = 5;

        if(guiLayout.getBoolean("Size.Dynamic")) {

            if(rows == 1) {
                finalRows = 1;
            } else if(rows == 2) {
                if(cubelets <= 9) finalRows = 1;
                else finalRows = 2;
            } else if(rows == 3) {
                if(cubelets >= 0 && cubelets <= 9) finalRows = 1;
                else if(cubelets >= 9 && cubelets <= 18) finalRows = 2;
                else finalRows = 3;
            } else if(rows == 4) {
                if(cubelets >= 0 && cubelets <= 9) finalRows = 1;
                else if(cubelets >= 9 && cubelets <= 18) finalRows = 2;
                else if(cubelets >= 18 && cubelets <= 27) finalRows = 3;
                else finalRows = 4;
            } else {
                if(cubelets >= 0 && cubelets <= 9) finalRows = 1;
                else if(cubelets >= 9 && cubelets <= 18) finalRows = 2;
                else if(cubelets >= 18 && cubelets <= 27) finalRows = 3;
                else if(cubelets >= 27 && cubelets <= 36) finalRows = 4;
                else finalRows = 5;
            }

        } else {

            finalRows = rows;

        }

        return (finalRows + 1) * 9;
    }

    private int getPageSize(GUILayout guiLayout) {

        int rows = guiLayout.getInteger("Size.Max-Cubelets-Rows");

        if(rows < 1) rows = 1;
        else if(rows > 5) rows = 5;

        return rows * 9;

    }

}
