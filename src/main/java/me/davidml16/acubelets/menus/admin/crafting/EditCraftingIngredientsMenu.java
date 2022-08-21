package me.davidml16.acubelets.menus.admin.crafting;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.crafting.*;
import me.davidml16.acubelets.enums.CraftType;
import me.davidml16.acubelets.objects.*;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.ItemBuilder;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EditCraftingIngredientsMenu extends Menu {

    public EditCraftingIngredientsMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        CraftParent craftParent = (CraftParent) getAttribute(AttrType.CRAFT_PARENT_ATTR);

        List<CraftIngredient> ingredients = craftParent.getIngrediens();

        if(page > 0 && ingredients.size() < (page * 21) + 1) {
            openPage(getPage() - 1);
            return;
        }

        Inventory gui = createInventory(36, "Crafting editor | Ingredients");

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack newReward = new ItemBuilder(XMaterial.SUNFLOWER.parseItem()).setName(Utils.translate("&aCreate new ingredient")).setLore(
                "",
                Utils.translate("&eLeft-Click » &aCubelet ingredient "),
                Utils.translate("&eMiddle-Click » &aLoot Points ingredient "),
                Utils.translate("&eRight-Click » &aMoney ingredient ")
        ).toItemStack();
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack")).toItemStack();

        fillBorders(edge);

        gui.setItem(30, newReward);
        gui.setItem(32, back);

        for (int i = 10; i <= 16; i++)
            gui.setItem(i, null);
        for (int i = 19; i <= 25; i++)
            gui.setItem(i, null);

        if (page > 0) {
            gui.setItem(27, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aPrevious page")).toItemStack());
        } else {
            gui.setItem(27, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (ingredients.size() > (page + 1) * 14) {
            gui.setItem(35, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aNext page")).toItemStack());
        } else {
            gui.setItem(35, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (ingredients.size() > 14) ingredients = ingredients.subList(page * 14, ((page * 14) + 14) > ingredients.size() ? ingredients.size() : (page * 14) + 14);

        if(ingredients.size() > 0) {

            for (CraftIngredient craftIngredient : ingredients) {

                List<String> lore;

                if(craftIngredient.getCraftType() == CraftType.CUBELET) {

                    lore = Arrays.asList("",
                            Utils.translate(" &7Parent: &6" + craftIngredient.getParentType() + " "),
                            Utils.translate(" &7Type: &6" + craftIngredient.getCraftType().toString() + " "),
                            Utils.translate(" &7Name: &6" + craftIngredient.getName() + " "),
                            Utils.translate(" &7Amount: &6" + craftIngredient.getAmount() + " "),
                            "",
                            Utils.translate("&eLeft-Click » &aRemove ingredient "),
                            Utils.translate("&eRight-Click » &aEdit ingredient "));

                } else {

                    lore = Arrays.asList("",
                            Utils.translate(" &7Parent: &6" + craftIngredient.getParentType() + " "),
                            Utils.translate(" &7Type: &6" + craftIngredient.getCraftType().toString() + " "),
                            Utils.translate(" &7Amount: &6" + craftIngredient.getAmount() + " "),
                            "",
                            Utils.translate("&eLeft-Click » &aEdit ingredient "),
                            Utils.translate("&eRight-Click » &aRemove ingredient "));

                }

                ItemStack ingredient = new ItemBuilder(getIconByIngredient(craftIngredient))
                        .setName(Utils.translate("&a" + craftIngredient.getName() + " x" + craftIngredient.getAmount()))
                        .setLore(lore)
                        .toItemStack();
                ingredient = NBTEditor.set(ingredient, craftIngredient.getUuid().toString(), "ingredientUUID");

                gui.addItem(ingredient);

            }

        } else {

            ItemStack noCreated = new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cAny ingredients created")).setLore(
                    "",
                    Utils.translate(" &7You dont have any "),
                    Utils.translate(" &7ingredients created. "),
                    ""
            ).toItemStack();

            for (int i = 10; i <= 16; i++)
                gui.setItem(i, noCreated);
            for (int i = 19; i <= 25; i++)
                gui.setItem(i, noCreated);

        }

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        Player player = getOwner();
        int slot = event.getRawSlot();

        CraftParent craftParent = (CraftParent) getAttribute(AttrType.CRAFT_PARENT_ATTR);

        if (slot == 27 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            previousPage();

            playSound(SoundType.CLICK);

        } else if (slot == 35 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            nextPage();

            playSound(SoundType.CLICK);

        } else if (slot == 30) {

            player.closeInventory();

            if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT)
                new CraftIngredientCubeletConversation(getMain()).getConversation(player, craftParent, CraftType.CUBELET).begin();
            else if(event.getClick() == ClickType.MIDDLE)
                new CraftIngredientPointsConversation(getMain()).getConversation(player, craftParent, CraftType.POINTS).begin();
            else if (event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT)
                new CraftIngredientMoneyConversation(getMain()).getConversation(player, craftParent, CraftType.MONEY).begin();

            playSound(SoundType.ANVIL_USE);

        } else if (slot == 32) {

            new EditCraftingCraftsMenu(getMain(), player).open();

        } else if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25)) {

            if (event.getCurrentItem().getType() == Material.AIR) return;

            if (getMain().getCubeletCraftingHandler().getCrafts().size() == 0) return;

            String ingredientUUID = NBTEditor.getString(event.getCurrentItem(), "ingredientUUID");
            Optional<CraftIngredient> craftIngredient = craftParent.getIngrediens().stream().filter(ing -> ing.getUuid().toString().equalsIgnoreCase(ingredientUUID)).findFirst();

            if (craftIngredient.isPresent()) {

                if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {

                    player.closeInventory();

                    CraftIngredient finalIngredient = craftIngredient.get();

                    if(finalIngredient.getCraftType() == CraftType.CUBELET)
                        new EditCraftIngredientCubeletConversation(getMain()).getConversation(player, craftParent, finalIngredient).begin();
                    else if(finalIngredient.getCraftType() == CraftType.POINTS)
                        new EditCraftIngredientPointsConversation(getMain()).getConversation(player, craftParent, finalIngredient).begin();
                    else if(finalIngredient.getCraftType() == CraftType.MONEY)
                        new EditCraftIngredientMoneyConversation(getMain()).getConversation(player, craftParent, finalIngredient).begin();

                    playSound(SoundType.ANVIL_USE);

                } else if (event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {

                    craftParent.getIngrediens().remove(craftIngredient.get());
                    getMain().getCubeletCraftingHandler().saveCrafting();

                    reloadMenu();

                    playSound(SoundType.CLICK);

                }

            }

        }

    }

    @Override
    public void OnMenuClosed() { }

    private ItemStack getIconByIngredient(CraftIngredient craftIngredient) {
        if(craftIngredient.getCraftType() == CraftType.CUBELET)
            return getMain().getCubeletTypesHandler().getTypeBydId(craftIngredient.getName()).getIcon();
        else if(craftIngredient.getCraftType() == CraftType.POINTS)
            return XMaterial.POPPY.parseItem();
        else if(craftIngredient.getCraftType() == CraftType.MONEY)
            return XMaterial.GOLD_INGOT.parseItem();
        else
            return XMaterial.BARRIER.parseItem();
    }

}
