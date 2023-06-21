package me.davidml16.acubelets.menus.player.crafting;

import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.enums.CraftType;
import me.davidml16.acubelets.menus.player.CubeletsMenu;
import me.davidml16.acubelets.objects.CraftIngredient;
import me.davidml16.acubelets.objects.CraftParent;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CraftingConfirmationMenu extends Menu {

    public CraftingConfirmationMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        Player player = getOwner();
        String id = (String) getAttribute(AttrType.CUSTOM_ID_ATTR);

        player.updateInventory();

        GUILayout guiLayout = getMain().getLayoutHandler().getLayout("craftingconfirmation");

        Inventory gui = createInventory(InventoryType.HOPPER, guiLayout.getMessage("Title"));
        CraftParent craftParent = getMain().getCubeletCraftingHandler().getCraftById(id);

        List<String> lore = new ArrayList<>();

        for (String line : guiLayout.getMessageList("Items.Craft.Lore")) {

            if(line.contains("%ingredients%")) {

                for(CraftIngredient ingredient : craftParent.getIngrediens()) {

                    if(ingredient.getCraftType() == CraftType.CUBELET)
                        lore.add(Utils.translate(guiLayout.getMessage("Ingredients.Ingredient.Cubelet")
                                .replaceAll("%name%", Utils.removeColors(getMain().getCubeletTypesHandler().getTypeBydId(ingredient.getName()).getName()))
                                .replaceAll("%amount%", ""+ingredient.getAmount())
                        ));
                    else if(ingredient.getCraftType() == CraftType.MONEY)
                        lore.add(Utils.translate(guiLayout.getMessage("Ingredients.Ingredient.Money")
                                .replaceAll("%amount%", ""+ingredient.getAmount())
                        ));
                    else if(ingredient.getCraftType() == CraftType.POINTS)
                        lore.add(Utils.translate(guiLayout.getMessage("Ingredients.Ingredient.Points")
                                .replaceAll("%amount%", ""+ingredient.getAmount())
                        ));

                }

            } else {

                lore.add(Utils.translate(line));

            }

        }

        ItemStack craft = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Craft.Material")).get().parseItem())
                .setName(Utils.translate(guiLayout.getMessage("Items.Craft.Name")))
                .setLore(lore)
                .toItemStack();

        ItemStack cancel = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Cancel.Material")).get().parseItem())
                .setName(Utils.translate(guiLayout.getMessage("Items.Cancel.Name")))
                .setLore(guiLayout.getMessageList("Items.Cancel.Lore"))
                .toItemStack();

        gui.setItem(1, craft);
        gui.setItem(3, cancel);

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        Player player = getOwner();
        int slot = event.getRawSlot();

        if(slot == 1) {

            String cubeletType = (String) getAttribute(AttrType.CUSTOM_ID_ATTR);
            CraftParent craft = getMain().getCubeletCraftingHandler().getCraftById(cubeletType);

            if(!getMain().getCubeletCraftingHandler().haveIngredients(player, craft)) {
                player.sendMessage(getMain().getLayoutHandler().getLayout("crafting").getMessage("NoAfford"));
                return;
            }

            getMain().getCubeletCraftingHandler().removeIngredients(player, craft);
            try {
                getMain().getTransactionHandler().giveCubelet(player.getUniqueId(), craft.getCubeletType(), 1);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            new CubeletsMenu(getMain(), player).open();

            playSound(SoundType.ANVIL_USE);

        } else if(slot == 3) {

            new CraftingMenu(getMain(), player).open();

        }

    }

    @Override
    public void OnMenuClosed() { }

}