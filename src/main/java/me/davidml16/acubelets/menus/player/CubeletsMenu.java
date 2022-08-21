package me.davidml16.acubelets.menus.player;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.interfaces.CubeletDateComparator;
import me.davidml16.acubelets.interfaces.CubeletTypeComparator;
import me.davidml16.acubelets.menus.player.crafting.CraftingMenu;
import me.davidml16.acubelets.menus.player.gifts.GiftPlayerMenu;
import me.davidml16.acubelets.menus.player.rewards.RewardsPreviewMenu;
import me.davidml16.acubelets.objects.*;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.TimeAPI.TimeUtils;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CubeletsMenu extends Menu {

    public CubeletsMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        Player player = getOwner();

        Profile profile = getMain().getPlayerDataHandler().getData(player.getUniqueId());
        List<Cubelet> cubelets = profile.getCubelets();

        GUILayout guiLayout = getMain().getLayoutHandler().getLayout("opencubelet");

        if(profile.getOrderBy().equalsIgnoreCase("date"))
            cubelets.sort(new CubeletDateComparator());
        else if(profile.getOrderBy().equalsIgnoreCase("type"))
            cubelets.sort(new CubeletTypeComparator());

        int pageSize = getPageSize(guiLayout);

        if(page < 0) {
            openPage(0);
            return;
        }

        if(page > 0 && cubelets.size() < (page * pageSize) + 1) {
            openPage(getPage() - 1);
            return;
        }

        if (cubelets.size() > pageSize) cubelets = cubelets.subList(page * pageSize, Math.min(((page * pageSize) + pageSize), cubelets.size()));

        int neededSize = getNeededSize(guiLayout, cubelets.size());

        Inventory inventory = createInventory(neededSize, guiLayout.getMessage("Title"));

        if (page > 0) {

            int amount = guiLayout.getBoolean("Items.PreviousPage.ShowPageNumber") ? page : 1;

            ItemStack item = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.PreviousPage.Material")).get().parseMaterial(), amount)
                    .setName(guiLayout.getMessage("Items.PreviousPage.Name"))
                    .toItemStack();
            item = NBTEditor.set(item, "previous", "action");

            if(guiLayout.getSlot("PreviousPage") >= 0)
                inventory.setItem(((neededSize - 10) + guiLayout.getSlot("PreviousPage")), item);

        }

        if (getMain().getPlayerDataHandler().getData(player.getUniqueId()).getCubelets().size() > (page + 1) * pageSize) {

            int amount = guiLayout.getBoolean("Items.NextPage.ShowPageNumber") ? (page + 2) : 1;

            ItemStack item = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.NextPage.Material")).get().parseMaterial(), amount)
                    .setName(guiLayout.getMessage("Items.NextPage.Name"))
                    .toItemStack();
            item = NBTEditor.set(item, "next", "action");

            if(guiLayout.getSlot("NextPage") >= 0)
                inventory.setItem((neededSize - 10) + guiLayout.getSlot("NextPage"), item);

        }

        if(getMain().isCraftingEnabled()) {

            ItemStack crafting = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Crafting.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.Crafting.Name"))
                    .setLore(guiLayout.getMessageList("Items.Crafting.Lore"))
                    .toItemStack();
            crafting = NBTEditor.set(crafting, "crafting", "action");

            if(guiLayout.getBoolean("Items.Crafting.Enabled") && guiLayout.getSlot("Crafting") >= 0)
                inventory.setItem((neededSize - 10) + guiLayout.getSlot("Crafting"), crafting);

        }

        if(getMain().isAnimationByPlayer()) {

            ItemStack animation = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Animations.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.Animations.Name"))
                    .setLore(guiLayout.getMessageList("Items.Animations.Lore"))
                    .toItemStack();
            animation = NBTEditor.set(animation, "animations", "action");

            if(guiLayout.getBoolean("Items.Animations.Enabled") && guiLayout.getSlot("Animations") >= 0)
                inventory.setItem((neededSize - 10) + guiLayout.getSlot("Animations"), animation);

        }

        ItemStack close = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Close.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Close.Name"))
                .setLore(guiLayout.getMessageList("Items.Close.Lore"))
                .toItemStack();
        close = NBTEditor.set(close, "close", "action");

        if(guiLayout.getBoolean("Items.Close.Enabled") && guiLayout.getSlot("Close") >= 0)
            inventory.setItem((neededSize - 10) + guiLayout.getSlot("Close"), close);

        ItemStack history = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.LootHistory.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.LootHistory.Name"))
                .setLore(guiLayout.getMessageList("Items.LootHistory.Lore"))
                .toItemStack();
        history = NBTEditor.set(history, "loothistory", "action");

        if(guiLayout.getBoolean("Items.LootHistory.Enabled") && guiLayout.getSlot("LootHistory") >= 0)
            inventory.setItem((neededSize - 10) + guiLayout.getSlot("LootHistory"), history);


        if(getMain().isGiftCubelets()) {

            ItemStack gift = new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlZjlhYTE0ZTg4NDc3M2VhYzEzNGE0ZWU4OTcyMDYzZjQ2NmRlNjc4MzYzY2Y3YjFhMjFhODViNyJ9fX0="))
                    .setName(guiLayout.getMessage("Items.Gift.Name"))
                    .setLore(guiLayout.getMessageList("Items.Gift.Lore"))
                    .toItemStack();
            gift = NBTEditor.set(gift, "gift", "action");

            if(guiLayout.getBoolean("Items.Gift.Enabled") && guiLayout.getSlot("Gift") >= 0)
                inventory.setItem((neededSize - 10) + guiLayout.getSlot("Gift"), gift);

        }

        for (int i = 0; i <= (neededSize-10); i++)
            inventory.setItem(i, null);

        if(getMain().getCubeletTypesHandler().getTypes().size() > 1) {

            if(guiLayout.getBoolean("Items.Ordered.Enabled")) {

                if (profile.getOrderBy().equalsIgnoreCase("date")) {

                    ItemStack orderByDate = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Ordered.Date.Material")).get().parseItem())
                            .setName(guiLayout.getMessage("Items.Ordered.Date.Name"))
                            .setLore(guiLayout.getMessageList("Items.Ordered.Date.Lore"))
                            .toItemStack();

                    orderByDate = NBTEditor.set(orderByDate, "ordered", "action");

                    inventory.setItem((neededSize - 10) + guiLayout.getSlot("Ordered"), orderByDate);

                } else if (profile.getOrderBy().equalsIgnoreCase("type")) {

                    ItemStack orderByType = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Ordered.Type.Material")).get().parseItem())
                            .setName(guiLayout.getMessage("Items.Ordered.Type.Name"))
                            .setLore(guiLayout.getMessageList("Items.Ordered.Type.Lore"))
                            .toItemStack();

                    orderByType = NBTEditor.set(orderByType, "ordered", "action");

                    inventory.setItem((neededSize - 10) + guiLayout.getSlot("Ordered"), orderByType);

                }

            } else {

                cubelets.sort(new CubeletDateComparator());

            }

        }

        List<ItemStack> items = new ArrayList<>();

        if(cubelets.size() > 0) {

            for (Cubelet cubelet : cubelets) {

                CubeletType type = getMain().getCubeletTypesHandler().getTypeBydId(cubelet.getType());

                List<String> lore = new ArrayList<>();

                if (cubelet.getExpire() > System.currentTimeMillis()) {

                    for (String line : type.getLoreAvailable()) {

                        lore.add(Utils.translate(line
                                .replaceAll("%received%", TimeUtils.millisToLongDHMS(System.currentTimeMillis() - cubelet.getReceived())))
                                .replaceAll("%expires%", TimeUtils.millisToLongDHMS(cubelet.getExpire() - System.currentTimeMillis())));

                    }

                } else {

                    for (String line : type.getLoreExpired()) {

                        lore.add(Utils.translate(line
                                .replaceAll("%received%", TimeUtils.millisToLongDHMS(System.currentTimeMillis() - cubelet.getReceived()))));

                    }

                }

                ItemStack item = new ItemBuilder(type.getIcon()).setName(Utils.translate(type.getName())).setLore(lore).toItemStack();
                item = NBTEditor.set(item, cubelet.getUuid().toString(), "cubeletUUID");
                item = NBTEditor.set(item, type.getId(), "typeID");

                inventory.addItem(item);
                items.add(item);

            }

        } else {

            int slot = 0;

            if(!guiLayout.getBoolean("Size.Dynamic")) {

                if(guiLayout.getSlot("NoCubelets") <= (neededSize - 10))
                    slot = guiLayout.getSlot("NoCubelets");

            }

            inventory.setItem(slot, new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.NoCubelets.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.NoCubelets.Name"))
                    .setLore(guiLayout.getMessageList("Items.NoCubelets.Lore")
                    ).toItemStack());

        }

        openInventory();

        setAttribute(AttrType.CUBELET_DISPLAYED_LIST_ATTR, cubelets);
        setAttribute(AttrType.CUBELET_DISPLAYED_ITEMS_ATTR, items);

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;

        Player player = getOwner();

        int slot = event.getRawSlot();
        int size = player.getOpenInventory().getTopInventory().getSize();
        GUILayout guiLayout = getMain().getLayoutHandler().getLayout("opencubelet");

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

                case "ordered":
                    Profile profile = getMain().getPlayerDataHandler().getData(player.getUniqueId());
                    if(profile.getOrderBy().equalsIgnoreCase("date"))
                        profile.setOrderBy("type");
                    else if(profile.getOrderBy().equalsIgnoreCase("type"))
                        profile.setOrderBy("date");
                    reloadMyMenu();
                    break;

                case "crafting":
                    new CraftingMenu(getMain(), player).open();
                    break;

                case "loothistory":
                    LootHistoryMenu lootHistoryMenu = new LootHistoryMenu(getMain(), player);
                    lootHistoryMenu.setAttribute(AttrType.OPENED_EXTERNALLY_ATTR, new Boolean(false));
                    lootHistoryMenu.open();
                    break;

                case "animations":
                    new PlayerAnimationMenu(getMain(), player).open();
                    break;

                case "gift":
                    new GiftPlayerMenu(getMain(), player).open();
                    break;
            }

        } else if (slot >= 0 && slot <= (player.getOpenInventory().getTopInventory().getSize() - 10)) {

            if (getMain().getPlayerDataHandler().getData(player.getUniqueId()).getCubelets().size() > 0) {

                String cubeletUUID = NBTEditor.getString(event.getCurrentItem(), "cubeletUUID");
                String typeID = NBTEditor.getString(event.getCurrentItem(), "typeID");
                CubeletType type = getMain().getCubeletTypesHandler().getTypeBydId(typeID);

                if(event.getClick() != getMain().getMenuHandler().getRewardPreviewClickType()) {

                    Profile profile = getMain().getPlayerDataHandler().getData(player);

                    if(profile.getBoxOpened().isWaiting()) {

                        Optional<Cubelet> cubelet = profile.getCubelets().stream().filter(cbl -> cbl.getUuid().toString().equalsIgnoreCase(cubeletUUID)).findFirst();

                        if (cubelet.isPresent()) {

                            if (cubelet.get().getExpire() > System.currentTimeMillis()) {

                                if (type.getAllRewards().size() > 0) {

                                    getMain().getCubeletOpenHandler().openAnimation(player, profile.getBoxOpened(), type, false);

                                    profile.getCubelets().removeIf(cblt -> cblt.getUuid().toString().equals(cubeletUUID));

                                    getMain().getDatabaseHandler().removeCubelet(player.getUniqueId(), UUID.fromString(Objects.requireNonNull(cubeletUUID)));

                                    getMain().getHologramImplementation().reloadHolograms(player);

                                    player.closeInventory();

                                }

                            }

                        }

                    } else {

                        if(profile.getBoxOpened().getPlayerOpening().getUuid() == player.getUniqueId()) {

                            player.sendMessage(getMain().getLanguageHandler().getMessage("Cubelet.BoxInUse.Me"));

                        } else {

                            player.sendMessage(getMain().getLanguageHandler().getMessage("Cubelet.BoxInUse.Other").replaceAll("%player%", profile.getBoxOpened().getPlayerOpening().getName()));

                        }

                    }

                } else if (event.getClick() == getMain().getMenuHandler().getRewardPreviewClickType()) {

                    if(getMain().isPreviewEnabled()) {
                        RewardsPreviewMenu rewardsPreviewMenu = new RewardsPreviewMenu(Main.get(), player);
                        rewardsPreviewMenu.setAttribute(Menu.AttrType.CUSTOM_ID_ATTR, typeID);
                        rewardsPreviewMenu.setAttribute(Menu.AttrType.OPENED_EXTERNALLY_ATTR, new Boolean(false));
                        rewardsPreviewMenu.open();
                        return;
                    }

                }

            } else {

                int noCubeletsSlot = 0;

                if(!guiLayout.getBoolean("Size.Dynamic"))

                    if(guiLayout.getSlot("NoCubelets") <= (getPageSize(guiLayout)))
                        noCubeletsSlot = guiLayout.getSlot("NoCubelets");

                if (slot == noCubeletsSlot) {

                    player.closeInventory();
                    MessageUtils.sendShopMessage(player);

                }

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

            if(cubelets > 36)
                finalRows = 5;
            else
                finalRows = (cubelets / 9) + (cubelets == 0 ? 1 : cubelets % 9 != 0 ? 1 : 0);

            finalRows = Math.min(finalRows, rows);

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
