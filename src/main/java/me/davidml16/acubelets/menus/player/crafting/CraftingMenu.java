package me.davidml16.acubelets.menus.player.crafting;

import com.cryptomorin.xseries.XMaterial;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.enums.CraftType;
import me.davidml16.acubelets.menus.player.CubeletsMenu;
import me.davidml16.acubelets.objects.*;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CraftingMenu extends Menu {

    public CraftingMenu(Main main, Player player) {
        super(main, player);
        setSize(6); // 6 rows
    }

    @Override
    public void OnPageOpened(int page) {

        Player player = getOwner();

        player.updateInventory();

        GUILayout guiLayout = getMain().getLayoutHandler().getLayout("crafting");

        Inventory gui = createInventory(getSize(), guiLayout.getMessage("Title"));

        if(guiLayout.getBoolean("Items.PlayerInfo.Enabled")) {

            ItemStack info = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.PlayerInfo.Material")).get().parseItem()).toItemStack();

            if(XMaterial.PLAYER_HEAD.parseItem().equals(info))
                info = SkullCreator.itemFromUuid(player.getUniqueId());

            ItemStack finalItem = new ItemBuilder(info)
                    .setName(guiLayout.getMessage("Items.PlayerInfo.Name").replaceAll("%player%", player.getName()))
                    .setLore(guiLayout.getMessageListPlaceholders(player, "Items.PlayerInfo.Lore"))
                    .toItemStack();

            gui.setItem((getSize() - 10) + guiLayout.getSlot("PlayerInfo"), finalItem);

        }

        ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Back.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Back.Name"))
                .setLore(guiLayout.getMessageList("Items.Back.Lore"))
                .toItemStack();
        gui.setItem((getSize() - 10) + guiLayout.getSlot("Back"), back);

        if(guiLayout.getBoolean("Items.Points.Enabled")) {

            List<String> lorePoints = new ArrayList<>();
            for (String line : guiLayout.getMessageList("Items.Points.Lore")) {
                lorePoints.add(line.replaceAll("%points_available%", "" + getMain().getPlayerDataHandler().getData(player).getLootPoints()));
            }

            ItemStack points = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Points.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.Points.Name"))
                    .setLore(lorePoints)
                    .toItemStack();

            gui.setItem((getSize() - 10) + guiLayout.getSlot("Points"), points);

        }

        for(CraftParent craft : getMain().getCubeletCraftingHandler().getCrafts()) {

            CubeletType cubeletType = getMain().getCubeletTypesHandler().getTypeBydId(craft.getCubeletType());

            List<String> lore = new ArrayList<>();

            for (String line : guiLayout.getMessageList("Ingredients.Lore")) {

                if(line.contains("%ingredients%")) {

                    for(CraftIngredient ingredient : craft.getIngrediens()) {

                        String status;
                        if(getMain().getCubeletCraftingHandler().haveIngredient(player, ingredient))
                            status = guiLayout.getMessage("Ingredients.Status.Available");
                        else
                            status = guiLayout.getMessage("Ingredients.Status.NotAvailable");

                        if(ingredient.getCraftType() == CraftType.CUBELET)
                            lore.add(Utils.translate(guiLayout.getMessage("Ingredients.Ingredient.Cubelet")
                                    .replaceAll("%name%", Utils.removeColors(getMain().getCubeletTypesHandler().getTypeBydId(ingredient.getName()).getName()))
                                    .replaceAll("%amount%", ""+ingredient.getAmount())
                                    .replaceAll("%status%", status)
                            ));
                        else if(ingredient.getCraftType() == CraftType.MONEY)
                            lore.add(Utils.translate(guiLayout.getMessage("Ingredients.Ingredient.Money")
                                    .replaceAll("%amount%", ""+ingredient.getAmount())
                                    .replaceAll("%status%", status)
                            ));
                        else if(ingredient.getCraftType() == CraftType.POINTS)
                            lore.add(Utils.translate(guiLayout.getMessage("Ingredients.Ingredient.Points")
                                    .replaceAll("%amount%", ""+ingredient.getAmount())
                                    .replaceAll("%status%", status)
                            ));
                    }

                } else if(line.contains("%description%")) {

                    for(String desc_line : cubeletType.getDescription())
                        lore.add(Utils.translate(desc_line));

                } else {

                    lore.add(Utils.translate(line));

                }

            }

            ItemStack item = new ItemBuilder(cubeletType.getIcon()).setName(Utils.translate(cubeletType.getName())).setLore(lore).toItemStack();
            item = NBTEditor.set(item, craft.getCubeletType(), "cubeletType");
            item = NBTEditor.set(item, Boolean.toString(getMain().getCubeletCraftingHandler().haveIngredients(player, craft)), "haveIngredients");

            if(craft.getIngrediens().size() > 0)
                if(craft.getSlot() <= (getSize() - 10))
                    gui.setItem(craft.getSlot(), item);

        }

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;

        Player player = getOwner();

        int slot = event.getRawSlot();
        GUILayout guiLayout = getMain().getLayoutHandler().getLayout("crafting");

        if(event.getClick() == ClickType.DOUBLE_CLICK) return;

        if (slot >= 0 && slot <= (getSize() - 10)) {

            String haveIngredients = NBTEditor.getString(event.getCurrentItem(), "haveIngredients");

            if (haveIngredients.equalsIgnoreCase("false")) {

                player.sendMessage(getMain().getLayoutHandler().getLayout("crafting").getMessage("NoAfford"));

                return;

            }

            String cubeletType = NBTEditor.getString(event.getCurrentItem(), "cubeletType");

            CraftingConfirmationMenu craftingConfirmationMenu = new CraftingConfirmationMenu(getMain(), player);
            craftingConfirmationMenu.setAttribute(AttrType.CUSTOM_ID_ATTR, cubeletType);
            craftingConfirmationMenu.open();

        } else if (slot == ((getSize() - 10) + guiLayout.getSlot("Back"))) {

            new CubeletsMenu(getMain(), player).open();

        }

    }

    @Override
    public void OnMenuClosed() { }

}