package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.interfaces.CubeletDateComparator;
import me.davidml16.acubelets.interfaces.CubeletTypeComparator;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.TimeAPI.TimeUtils;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.*;

public class Cubelets_GUI implements Listener {

    private HashMap<UUID, Integer> opened;
    private Main main;

    private ClickType clickType;

    public Cubelets_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
        this.clickType = ClickType.SHIFT_LEFT;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public void setClickType(String clickType) {
        if(Arrays.asList("LEFT", "RIGHT", "MIDDLE", "SHIFT_LEFT", "SHIFT_RIGHT").contains(clickType))
            this.clickType = ClickType.valueOf(clickType.toUpperCase());
        else
            this.clickType = ClickType.SHIFT_LEFT;
    }

    public HashMap<UUID, Integer> getOpened() {
        return opened;
    }

    public void reloadPage(Player p) {
        openPage(p, opened.get(p.getUniqueId()));
    }

    private void openPage(Player p, int page) {

        Profile profile = main.getPlayerDataHandler().getData(p.getUniqueId());
        List<Cubelet> cubelets = profile.getCubelets();

        GUILayout guiLayout = main.getLayoutHandler().getLayout("opencubelet");

        if(profile.getOrderBy().equalsIgnoreCase("date"))
            cubelets.sort(new CubeletDateComparator());
        else if(profile.getOrderBy().equalsIgnoreCase("type"))
            cubelets.sort(new CubeletTypeComparator());

        if(page > 0 && cubelets.size() < (page * 27) + 1) {
            openPage(p, page - 1);
            return;
        }

        if (cubelets.size() > 27) cubelets = cubelets.subList(page * 27, Math.min(((page * 27) + 27), cubelets.size()));

        int neededSize = getNeededSize(cubelets.size());

        Inventory gui = Bukkit.createInventory(null, neededSize, guiLayout.getMessage("Title"));

        if (page > 0) {
            gui.setItem(((neededSize - 10) + guiLayout.getSlot("PreviousPage")), new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.PreviousPage.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.PreviousPage.Name"))
                    .toItemStack());
        }

        if (main.getPlayerDataHandler().getData(p.getUniqueId()).getCubelets().size() > (page + 1) * 27) {
            gui.setItem((neededSize - 10) + guiLayout.getSlot("NextPage"), new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.NextPage.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.NextPage.Name"))
                    .toItemStack());
        }

        ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Close.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Close.Name"))
                .setLore(guiLayout.getMessageList("Items.Close.Lore"))
                .toItemStack();
        gui.setItem((neededSize - 10) + guiLayout.getSlot("Close"), back);


        if(main.isCraftingEnabled()) {
            ItemStack crafting = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Crafting.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.Crafting.Name"))
                    .setLore(guiLayout.getMessageList("Items.Crafting.Lore"))
                    .toItemStack();
            gui.setItem((neededSize - 10) + guiLayout.getSlot("Crafting"), crafting);
        }

        for (int i = 0; i <= (neededSize-10); i++)
            gui.setItem(i, null);

        if(cubelets.size() > 0) {

            if(main.getCubeletTypesHandler().getTypes().size() > 1) {
                if (profile.getOrderBy().equalsIgnoreCase("date")) {
                    ItemStack orderByDate = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Ordered.Date.Material")).get().parseItem())
                            .setName(guiLayout.getMessage("Items.Ordered.Date.Name"))
                            .setLore(guiLayout.getMessageList("Items.Ordered.Date.Lore"))
                            .toItemStack();
                    gui.setItem((neededSize - 10) + guiLayout.getSlot("Ordered"), orderByDate);
                } else if (profile.getOrderBy().equalsIgnoreCase("type")) {
                    ItemStack orderByType = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Ordered.Type.Material")).get().parseItem())
                            .setName(guiLayout.getMessage("Items.Ordered.Type.Name"))
                            .setLore(guiLayout.getMessageList("Items.Ordered.Type.Lore"))
                            .toItemStack();
                    gui.setItem((neededSize - 10) + guiLayout.getSlot("Ordered"), orderByType);
                }
            }

            for (Cubelet cubelet : cubelets) {
                CubeletType type = main.getCubeletTypesHandler().getTypeBydId(cubelet.getType());

                List<String> lore = new ArrayList<>();

                if (cubelet.getExpire() > System.currentTimeMillis()) {
                    for (String line : type.getLoreAvailable()) {
                        lore.add(ColorUtil.translate(line
                                .replaceAll("%received%", TimeUtils.millisToLongDHMS(System.currentTimeMillis() - cubelet.getReceived())))
                                .replaceAll("%expires%", TimeUtils.millisToLongDHMS(cubelet.getExpire() - System.currentTimeMillis())));
                    }
                } else {
                    for (String line : type.getLoreExpired()) {
                        lore.add(ColorUtil.translate(line
                                .replaceAll("%received%", TimeUtils.millisToLongDHMS(System.currentTimeMillis() - cubelet.getReceived()))));
                    }
                }

                ItemStack item = new ItemBuilder(type.getIcon()).setName(ColorUtil.translate(type.getName())).setLore(lore).toItemStack();
                item = NBTEditor.setItemTag(item, cubelet.getUuid().toString(), "cubeletUUID");
                item = NBTEditor.setItemTag(item, type.getId(), "typeID");

                gui.addItem(item);
            }
        } else {
            gui.setItem(0, new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.NoCubelets.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.NoCubelets.Name"))
                    .setLore(guiLayout.getMessageList("Items.NoCubelets.Lore")
            ).toItemStack());
        }

        p.openInventory(gui);

        opened.put(p.getUniqueId(), page);
    }

    public void open(Player p) {
        p.updateInventory();
        openPage(p, 0);

        opened.put(p.getUniqueId(), 0);
    }

    public int getNeededSize(int cubelets) {
        if(cubelets <= 9) return 18;
        else if(cubelets <= 18) return 27;
        else if(cubelets <= 27) return 36;
        return 36;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) throws SQLException {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == Material.AIR) return;

        if (opened.containsKey(p.getUniqueId())) {
            e.setCancelled(true);

            int slot = e.getRawSlot();
            int size = p.getOpenInventory().getTopInventory().getSize();
            GUILayout guiLayout = main.getLayoutHandler().getLayout("opencubelet");

            if (slot == ((size - 10) + guiLayout.getSlot("PreviousPage"))) {
                if(e.getClick() != ClickType.DOUBLE_CLICK)
                    openPage(p, opened.get(p.getUniqueId()) - 1);
            } else if (slot == ((size - 10) + guiLayout.getSlot("NextPage"))) {
                if(e.getClick() != ClickType.DOUBLE_CLICK)
                    openPage(p, opened.get(p.getUniqueId()) + 1);
            } else if (slot == ((size - 10) + guiLayout.getSlot("Close"))) {
                p.closeInventory();
            } else if (slot == ((size - 10) + guiLayout.getSlot("Crafting")) && main.isCraftingEnabled()) {
                main.getCraftingGUI().open(p);
            } else if (slot == ((size - 10) + guiLayout.getSlot("Ordered")) && main.getCubeletTypesHandler().getTypes().size() > 1) {
                Profile profile = main.getPlayerDataHandler().getData(p.getUniqueId());
                if(profile.getOrderBy().equalsIgnoreCase("date"))
                    profile.setOrderBy("type");
                else if(profile.getOrderBy().equalsIgnoreCase("type"))
                    profile.setOrderBy("date");
                openPage(p, opened.get(p.getUniqueId()));
            } else if (slot >= 0 && slot <= (p.getOpenInventory().getTopInventory().getSize() - 10)) {
                if (main.getPlayerDataHandler().getData(p.getUniqueId()).getCubelets().size() > 0) {
                    String cubeletUUID = NBTEditor.getString(e.getCurrentItem(), "cubeletUUID");
                    String typeID = NBTEditor.getString(e.getCurrentItem(), "typeID");
                    CubeletType type = main.getCubeletTypesHandler().getTypeBydId(typeID);

                    if(e.getClick() != clickType) {

                        Profile profile = main.getPlayerDataHandler().getData(p);
                        Optional<Cubelet> cubelet = profile.getCubelets().stream().filter(cbl -> cbl.getUuid().toString().equalsIgnoreCase(cubeletUUID)).findFirst();

                        if (cubelet.isPresent()) {
                            if (cubelet.get().getExpire() > System.currentTimeMillis()) {

                                if (type.getAllRewards().size() > 0) {
                                    main.getCubeletOpenHandler().openAnimation(p, profile.getBoxOpened(), type);

                                    profile.getCubelets().removeIf(cblt -> cblt.getUuid().toString().equals(cubeletUUID));

                                    main.getDatabaseHandler().removeCubelet(p.getUniqueId(), UUID.fromString(Objects.requireNonNull(cubeletUUID)));
                                    profile.getCubelets().remove(cubelet);

                                    p.closeInventory();
                                }
                            }
                        }

                    } else if (e.getClick() == clickType) {
                        if(main.isPreviewEnabled()) main.getRewardsPreviewGUI().open(p, typeID);
                    }
                } else {
                    if(slot == 0) {
                        p.closeInventory();
                        MessageUtils.sendShopMessage(p);
                    }
                }
            }

            p.updateInventory();
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

}
