package me.davidml16.acubelets.gui.rewards;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.rewards.CommandObjectRewardMenu;
import me.davidml16.acubelets.conversation.rewards.EditCommandObjectRewardMenu;
import me.davidml16.acubelets.objects.rewards.CommandObject;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EditRewardCommands_GUI implements Listener {

    private HashMap<UUID, GUISession> opened;
    private List<Integer> borders;

    private Main main;

    public EditRewardCommands_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<UUID, GUISession>();
        this.borders = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 40, 42, 43, 44);
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, GUISession> getOpened() {
        return opened;
    }

    public void reloadGUI(Reward reward) {
        for(UUID uuid : opened.keySet()) {
            if(opened.get(uuid).getReward().getId().equalsIgnoreCase(reward.getId())) {
                Player p = Bukkit.getPlayer(uuid);
                openPage(p, reward, opened.get(uuid).getPage());
            }
        }
    }

    private void openPage(Player p, Reward reward, int page) {

        List<CommandObject> commands = reward.getCommands();

        if(page > 0 && commands.size() < (page * 21) + 1) {
            openPage(p, reward, page - 1);
            return;
        }

        Inventory gui = Bukkit.createInventory(null, 45, "%reward% | Commands".replaceAll("%reward%", reward.getId()));

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack to config")).toItemStack();

        ItemStack newCommand = new ItemBuilder(XMaterial.SUNFLOWER.parseItem()).setName(Utils.translate("&aCreate new command")).toItemStack();

        for (Integer i : borders)
            gui.setItem(i, edge);

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

        if (commands.size() > (page + 1) * 21) {
            gui.setItem(26, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aNext page")).toItemStack());
        } else {
            gui.setItem(26, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        gui.setItem(39, newCommand);
        gui.setItem(41, back);

        if (commands.size() > 21) commands = commands.subList(page * 21, ((page * 21) + 21) > commands.size() ? commands.size() : (page * 21) + 21);

        if(commands.size() > 0) {
            for (CommandObject command : commands) {
                gui.addItem(new ItemBuilder(XMaterial.PAPER.parseItem())
                        .setName(Utils.translate("&a" + command.getId()))
                        .setLore(
                                "",
                                Utils.translate(" &7Command: &6" + command.getCommand()),
                                "",
                                Utils.translate("&eLeft-Click » &aRemove command "),
                                Utils.translate("&eRight-Click » &aEdit command ")
                        ).toItemStack());
            }
        } else {
            gui.setItem(22, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cAny commands added")).setLore(
                    "",
                    Utils.translate(" &7You dont have any "),
                    Utils.translate(" &7commands added. "),
                    ""
            ).toItemStack());
        }

        if (!opened.containsKey(p.getUniqueId())) {
            p.openInventory(gui);
        } else {
            p.getOpenInventory().getTopInventory().setContents(gui.getContents());
        }

        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), new GUISession(reward, page)), 1L);

    }

    public void open(Player p, Reward reward) {
        p.updateInventory();
        openPage(p, reward, 0);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (opened.containsKey(p.getUniqueId())) {

            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;
            if (e.getCurrentItem().getType() == Material.AIR) return;

            int slot = e.getRawSlot();

            Reward reward = opened.get(p.getUniqueId()).getReward();
            CubeletType cubeletType = reward.getParentCubelet();

            if (slot == 18 && e.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {
                openPage(p, reward, opened.get(p.getUniqueId()).getPage() - 1);
            } else if (slot == 26 && e.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {
                openPage(p, reward, opened.get(p.getUniqueId()).getPage() + 1);
            } else if (slot == 39) {
                p.closeInventory();
                new CommandObjectRewardMenu(main).getConversation(p, cubeletType, reward).begin();
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 100, 3);
            } else if (slot == 41) {
                main.getRewardsGUI().open(p, cubeletType.getId());
            } else if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25) || (slot >= 28 && slot <= 34)) {

                if (reward.getCommands().size() == 0) return;

                String commandID = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                CommandObject commandObject = reward.getCommand(commandID);

                if(e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {

                    reward.getCommands().remove(commandObject);
                    reward.recreateCommands();

                    p.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix()
                            + " &aYou removed &e" + commandObject.getId() + " &afrom commands of reward &e" + reward.getId()));
                    reloadGUI(reward);

                } else if(e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.SHIFT_RIGHT) {
                    p.closeInventory();
                    new EditCommandObjectRewardMenu(main).getConversation(p, cubeletType, reward, commandObject).begin();
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 100, 3);
                }

            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (opened.containsKey(p.getUniqueId())) {
            opened.get(p.getUniqueId()).getReward().getParentCubelet().saveType();
            opened.remove(p.getUniqueId());
        }
    }

    class GUISession {

        private Reward reward;
        private int page;

        public GUISession(Reward reward, int page) {
            this.reward = reward;
            this.page = page;
        }

        public Reward getReward() {
            return reward;
        }

        public void setReward(Reward reward) {
            this.reward = reward;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

    }

}
