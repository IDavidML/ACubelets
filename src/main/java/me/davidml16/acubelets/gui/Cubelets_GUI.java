package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.*;

public class Cubelets_GUI implements Listener {

    private HashMap<UUID, Integer> opened;

    private Inventory gui;

    private Main main;

    public Cubelets_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, Integer> getOpened() {
        return opened;
    }

    public void loadGUI() {
        Inventory gui = Bukkit.createInventory(null, 36, "Cubelets");

        ItemStack back = new ItemBuilder(Material.BOOK, 1).setName(ColorUtil.translate("&9Close")).toItemStack();

        gui.setItem(13, new ItemBuilder(Material.BARRIER, 1).setDurability((short) 14).setName(ColorUtil.translate("&4Loading...")).toItemStack());

        gui.setItem(31, back);

        this.gui = gui;
    }

    private void openPage(Player p, int page) {

        List<Cubelet> cubelets = main.getPlayerDataHandler().getData(p.getUniqueId()).getCubelets();
        cubelets.sort(Collections.reverseOrder());

        if(page > 0 && cubelets.size() < (page * 27) + 1) {
            openPage(p, page - 1);
            return;
        }

        int neededInventories = ((int) Math.ceil(cubelets.size() / 27));

        Inventory gui = Bukkit.createInventory(null, 36, main.getLanguageHandler().getMessage("GUIs.Open.title"));
        gui.setContents(this.gui.getContents());

        for (int i = 0; i <= 8; i++)
            gui.setItem(i, null);
        for (int i = 9; i <= 17; i++)
            gui.setItem(i, null);
        for (int i = 18; i <= 26; i++)
            gui.setItem(i, null);

        if (page > 0) {
            gui.setItem(27, new ItemBuilder(Material.ARROW, 1).setName(ColorUtil.translate("&aPrevious page (" + (page) + "/" + (neededInventories + 1) + ")")).toItemStack());
        }

        if (cubelets.size() > (page + 1) * 27) {
            gui.setItem(35, new ItemBuilder(Material.ARROW, 1).setName(ColorUtil.translate("&aNext page (" + (page + 2) + "/" + (neededInventories + 1) + ")")).toItemStack());
        }

        if (cubelets.size() > 27) cubelets = cubelets.subList(page * 27, ((page * 27) + 27) > cubelets.size() ? cubelets.size() : (page * 27) + 27);

        if(cubelets.size() > 0) {
            for (Cubelet cubelet : cubelets) {
                CubeletType type = main.getCubeletTypesHandler().getTypeBydId(cubelet.getType());

                List<String> lore = new ArrayList<>();
                for(String line : type.getLore()) {
                    lore.add(ColorUtil.translate(line.replaceAll("%received%", TimeUtils.millisToLongDHMS(System.currentTimeMillis() - cubelet.getDate()))));
                }

                ItemStack item = new ItemBuilder(type.getIcon()).setName(ColorUtil.translate(type.getName())).setLore(lore).toItemStack();
                item = NBTEditor.setItemTag(item, cubelet.getUuid().toString(), "cubeletUUID");
                item = NBTEditor.setItemTag(item, type.getId(), "typeID");

                gui.addItem(item);
            }
        } else {
            gui.setItem(13, new ItemBuilder(Material.BARRIER, 1).setDurability((short) 14).setName(ColorUtil.translate("&4You have no Cubelets")).setLore(
                    "",
                    ColorUtil.translate(" &7You can get Cubelets by playing games. "),
                    ColorUtil.translate(" &7You are also able to purchase them "),
                    ColorUtil.translate(" &7Yon our store. "),
                    ""
            ).toItemStack());
        }

        if (!opened.containsKey(p.getUniqueId())) {
            p.openInventory(gui);
        } else {
            p.getOpenInventory().getTopInventory().setContents(gui.getContents());
        }

        opened.put(p.getUniqueId(), page);
    }

    public void open(Player p) {
        p.updateInventory();
        openPage(p, 0);

        opened.put(p.getUniqueId(), 0);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == Material.AIR) return;

        if (opened.containsKey(p.getUniqueId())) {
            e.setCancelled(true);
            int slot = e.getRawSlot();
            if (slot == 27 && e.getCurrentItem().getType() == Material.ARROW) {
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                openPage(p, opened.get(p.getUniqueId()) - 1);
            } else if (slot == 35 && e.getCurrentItem().getType() == Material.ARROW) {
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                openPage(p, opened.get(p.getUniqueId()) + 1);
            } else if (slot == 31) {
                p.closeInventory();
            } else if (slot >= 0 && slot <= 26) {
                if(main.getPlayerDataHandler().getData(p.getUniqueId()).getCubelets().size() > 0) {
                    String cubeletUUID = NBTEditor.getString(e.getCurrentItem(), "cubeletUUID");
                    String typeID = NBTEditor.getString(e.getCurrentItem(), "typeID");

                    Profile profile = main.getPlayerDataHandler().getData(p);

                    main.getCubeletOpenHandler().openAnimation(p, profile.getBoxOpened(), main.getCubeletTypesHandler().getTypeBydId(typeID));

                    try {
                        main.getDatabaseHandler().removeCubelet(p.getUniqueId(), UUID.fromString(cubeletUUID));
                        profile.getCubelets().removeIf(cubelet -> {
                            return cubelet.getUuid().toString().equals(cubeletUUID);
                        });
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    p.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if(opened.containsKey(p.getUniqueId())) {
            opened.remove(p.getUniqueId());
            main.getPlayerDataHandler().getData(p).setBoxOpened(null);
        }
    }

}
