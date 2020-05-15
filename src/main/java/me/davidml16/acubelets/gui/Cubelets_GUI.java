package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.interfaces.CubeletDateComparator;
import me.davidml16.acubelets.interfaces.CubeletTypeComparator;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.TimeAPI.TimeUtils;
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

    public Cubelets_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, Integer> getOpened() {
        return opened;
    }

    private void openPage(Player p, int page) {

        Profile profile = main.getPlayerDataHandler().getData(p.getUniqueId());
        List<Cubelet> cubelets = profile.getCubelets();

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

        Inventory gui = Bukkit.createInventory(null, neededSize, main.getLanguageHandler().getMessage("GUI.Opening.Title"));

        if (page > 0) {
            gui.setItem((neededSize - 9), new ItemBuilder(ACMaterial.SUGAR_CANE.parseItem())
                    .setName(main.getLanguageHandler().getMessage("GUI.Opening.Items.PreviousPage.Name"))
                    .toItemStack());
        }

        if (main.getPlayerDataHandler().getData(p.getUniqueId()).getCubelets().size() > (page + 1) * 27) {
            gui.setItem((neededSize - 1), new ItemBuilder(ACMaterial.SUGAR_CANE.parseItem())
                    .setName(main.getLanguageHandler().getMessage("GUI.Opening.Items.NextPage.Name"))
                    .toItemStack());
        }

        ItemStack back = new ItemBuilder(ACMaterial.BOOK.parseItem())
                .setName(main.getLanguageHandler().getMessage("GUI.Opening.Items.Close.Name"))
                .setLore(main.getLanguageHandler().getMessageList("GUI.Opening.Items.Close.Lore"))
                .toItemStack();
        gui.setItem(neededSize - 5, back);

        for (int i = 0; i <= (neededSize-10); i++)
            gui.setItem(i, null);

        if(cubelets.size() > 0) {

            if(profile.getOrderBy().equalsIgnoreCase("date")) {
                ItemStack orderByDate = new ItemBuilder(ACMaterial.CLOCK.parseItem())
                        .setName(main.getLanguageHandler().getMessage("GUI.Opening.Items.Ordered.Date.Name"))
                        .setLore(main.getLanguageHandler().getMessageList("GUI.Opening.Items.Ordered.Date.Lore"))
                        .toItemStack();
                gui.setItem(neededSize - 3, orderByDate);
            } else if(profile.getOrderBy().equalsIgnoreCase("type")) {
                ItemStack orderByType = new ItemBuilder(ACMaterial.NAME_TAG.parseItem())
                        .setName(main.getLanguageHandler().getMessage("GUI.Opening.Items.Ordered.Type.Name"))
                        .setLore(main.getLanguageHandler().getMessageList("GUI.Opening.Items.Ordered.Type.Lore"))
                        .toItemStack();
                gui.setItem(neededSize - 3, orderByType);
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
            gui.setItem(0, new ItemBuilder(ACMaterial.BARRIER.parseItem())
                    .setName(main.getLanguageHandler().getMessage("GUI.Opening.Items.NoCubelets.Name"))
                    .setLore(main.getLanguageHandler().getMessageList("GUI.Opening.Items.NoCubelets.Lore")
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
            if(e.getClick() != ClickType.DOUBLE_CLICK) {
                if (slot == (p.getOpenInventory().getTopInventory().getSize() - 9) && e.getCurrentItem().getType() == ACMaterial.SUGAR_CANE.parseMaterial()) {
                    openPage(p, opened.get(p.getUniqueId()) - 1);
                } else if (slot == (p.getOpenInventory().getTopInventory().getSize() - 1) && e.getCurrentItem().getType() == ACMaterial.SUGAR_CANE.parseMaterial()) {
                    openPage(p, opened.get(p.getUniqueId()) + 1);
                } else if (slot == (p.getOpenInventory().getTopInventory().getSize() - 5)) {
                    p.closeInventory();
                } else if (slot == (p.getOpenInventory().getTopInventory().getSize() - 3)) {
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

                        Profile profile = main.getPlayerDataHandler().getData(p);
                        Optional<Cubelet> cubelet = profile.getCubelets().stream().filter(cbl -> cbl.getUuid().toString().equalsIgnoreCase(cubeletUUID)).findFirst();

                        if(cubelet.isPresent()) {
                            if (cubelet.get().getExpire() > System.currentTimeMillis()) {
                                main.getCubeletOpenHandler().openAnimation(p, profile.getBoxOpened(), main.getCubeletTypesHandler().getTypeBydId(typeID));

                                profile.getCubelets().removeIf(cblt -> cblt.getUuid().toString().equals(cubeletUUID));

                                main.getDatabaseHandler().removeCubelet(p.getUniqueId(), UUID.fromString(Objects.requireNonNull(cubeletUUID)));
                                profile.getCubelets().remove(cubelet);

                                p.closeInventory();
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

}
