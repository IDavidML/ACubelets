package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.RarityMenu;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Pair;
import me.davidml16.acubelets.objects.Rarity;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class Rarities_GUI implements Listener {

    private HashMap<UUID, Pair> opened;
    private HashMap<String, Inventory> guis;
    private List<Integer> borders;

    private Main main;

    public Rarities_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<UUID, Pair>();
        this.guis = new HashMap<String, Inventory>();
        this.borders = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 40, 42, 43, 44);
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, Pair> getOpened() {
        return opened;
    }

    public HashMap<String, Inventory> getGuis() {
        return guis;
    }

    public void loadGUI() {
        for (File file : Objects.requireNonNull(new File(main.getDataFolder(), "types").listFiles())) {
            loadGUI(file.getName().toLowerCase().replace(".yml", ""));
        }
    }

    public void loadGUI(String id) {
        if (guis.containsKey(id)) return;

        Inventory gui = Bukkit.createInventory(null, 45, "%cubelet_type% | Rarities".replaceAll("%cubelet_type%", id));

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack newRarity = new ItemBuilder(XMaterial.SUNFLOWER.parseItem()).setName(ColorUtil.translate("&aCreate new rarity")).toItemStack();
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(ColorUtil.translate("&aBack to config")).toItemStack();

        for (Integer i : borders) {
            gui.setItem(i, edge);
        }

        gui.setItem(39, newRarity);
        gui.setItem(41, back);

        guis.put(id, gui);
    }

    public void reloadAllGUI() {
        for(String id : main.getCubeletTypesHandler().getTypes().keySet()) {
            reloadGUI(id);
        }
    }

    public void reloadGUI(String id) {
        for(UUID uuid : opened.keySet()) {
            if(opened.get(uuid).getCubeletType().equals(id)) {
                Player p = Bukkit.getPlayer(uuid);
                openPage(p, id, opened.get(uuid).getPage());
            }
        }
    }

    private void openPage(Player p, String id, int page) {
        List<Rarity> rarities = new ArrayList<>(main.getCubeletTypesHandler().getTypeBydId(id).getRarities().values());
        Collections.sort(rarities, Collections.reverseOrder());

        if(page > 0 && rarities.size() < (page * 21) + 1) {
            openPage(p, id, page - 1);
            return;
        }

        Inventory gui = Bukkit.createInventory(null, 45, "%cubelet_type% | Rarities".replaceAll("%cubelet_type%", id));

        if(!guis.containsKey(id)) loadGUI(id);

        gui.setContents(guis.get(id).getContents());

        for (int i = 10; i <= 16; i++)
            gui.setItem(i, null);
        for (int i = 19; i <= 25; i++)
            gui.setItem(i, null);
        for (int i = 28; i <= 34; i++)
            gui.setItem(i, null);

        if (page > 0) {
            gui.setItem(18, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(ColorUtil.translate("&aPrevious page")).toItemStack());
        } else {
            gui.setItem(18, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (rarities.size() > (page + 1) * 21) {
            gui.setItem(26, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(ColorUtil.translate("&aNext page")).toItemStack());
        } else {
            gui.setItem(26, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (rarities.size() > 21) rarities = rarities.subList(page * 21, ((page * 21) + 21) > rarities.size() ? rarities.size() : (page * 21) + 21);

        if(rarities.size() > 0) {
            for (Rarity rarity : rarities) {
                gui.addItem(new ItemBuilder(XMaterial.ITEM_FRAME.parseItem())
                        .setName(ColorUtil.translate("&a" + rarity.getId()))
                        .setLore(
                                "",
                                ColorUtil.translate(" &7Name: &6" + rarity.getName() + " "),
                                ColorUtil.translate(" &7Chance: &6" + rarity.getChance() + "% "),
                                ColorUtil.translate(" &7Duplicate Points: &6" + rarity.getDuplicatePointsRange() + " "),
                                "",
                                ColorUtil.translate("&eClick to remove! ")).toItemStack());
            }
        } else {
            gui.setItem(22, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cAny rarity created")).setLore(
                    "",
                    ColorUtil.translate(" &7You dont have any "),
                    ColorUtil.translate(" &7rarity created. "),
                    ""
            ).toItemStack());
        }

        if (!opened.containsKey(p.getUniqueId())) {
            p.openInventory(gui);
        } else {
            p.getOpenInventory().getTopInventory().setContents(gui.getContents());
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), new Pair(id, page)), 1L);
    }

    public void open(Player p, String id) {
        p.updateInventory();
        openPage(p, id, 0);

        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), new Pair(id, 0)), 1L);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;

        if (opened.containsKey(p.getUniqueId())) {
            e.setCancelled(true);
            int slot = e.getRawSlot();
            String id = opened.get(p.getUniqueId()).getCubeletType();
            CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(opened.get(p.getUniqueId()).getCubeletType());
            if (slot == 18 && e.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                openPage(p, id, opened.get(p.getUniqueId()).getPage() - 1);
            } else if (slot == 26 && e.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                openPage(p, id, opened.get(p.getUniqueId()).getPage() + 1);
            } else if (slot == 39) {
                p.closeInventory();
                new RarityMenu(main).getConversation(p, cubeletType).begin();
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 100, 3);
            } else if (slot == 41) {
                main.getTypeConfigGUI().open(p, cubeletType.getId());
            } else if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25) || (slot >= 28 && slot <= 34)) {
                if (e.getCurrentItem().getType() == Material.AIR) return;

                if (cubeletType.getAllRewards().size() == 0) return;

                String rarityID = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                for(Rarity rarity : cubeletType.getRarities().values()) {
                    if(rarity.getId().equalsIgnoreCase(rarityID)) {
                        cubeletType.getRarities().remove(rarityID);

                        p.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix()
                                + " &aYou removed rarity &e" + rarityID + " &afrom rarities of cubelet type &e" + cubeletType.getId()));
                        reloadGUI(cubeletType.getId());
                        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (opened.containsKey(p.getUniqueId())) {
            main.getCubeletTypesHandler().getTypeBydId(opened.get(p.getUniqueId()).getCubeletType()).saveType();
            opened.remove(p.getUniqueId());
        }
    }

}
