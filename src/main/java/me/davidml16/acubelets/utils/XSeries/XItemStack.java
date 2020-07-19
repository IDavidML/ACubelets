/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Crypto Morin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.davidml16.acubelets.utils.XSeries;

import com.google.common.base.Enums;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * <b>XItemStack</b> - YAML Item Serializer<br>
 * Using ConfigurationSection Example:
 * <pre>
 *     ConfigurationSection section = plugin.getConfig().getConfigurationSection("staffs.dragon-staff");
 *     ItemStack item = XItemStack.deserialize(section);
 * </pre>
 * ItemStack: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html
 *
 * @author Crypto Morin
 * @version 1.1.1
 * @see XMaterial
 * @see XPotion
 * @see SkullUtils
 * @see XEnchantment
 * @see ItemStack
 */
public class XItemStack {

    /**
     * Writes an ItemStack object into a config.
     * The config file will not save after the object is written.
     *
     * @param item   the ItemStack to serialize.
     * @param config the config section to write this item to.
     * @since 1.0.0
     */
    public static void serialize(ItemStack item, FileConfiguration config, String path) {
        ItemMeta meta = item.getItemMeta();

        config.set(path + "." + "material", item.getType().name());
        if (item.getAmount() > 1) config.set(path + "." + "amount", item.getAmount());

        if (XMaterial.isNewVersion()) {
            if (meta instanceof Damageable) {
                Damageable damageable = (Damageable) meta;
                if (damageable.hasDamage()) config.set(path + "." + "damage", damageable.getDamage());
            }
        } else {
            int damage = item.getDurability();
            if (damage > 0) config.set(path + "." + "damage", damage);
        }

        if (XMaterial.supports(11)) if (meta.isUnbreakable()) config.set(path + "." + "unbreakable", true);

        if (meta.getEnchants().size() > 0) config.set(path + "." + "enchanted", true);
        else config.set(path + "." + "enchanted", false);

        if (meta instanceof SkullMeta) {
            if(XMaterial.supports(12)) config.set(path + "." + "skull", ((SkullMeta) meta).getOwningPlayer().getUniqueId().toString());
            else config.set(path + "." + "skull", ((SkullMeta) meta).getOwner());
        } else if (meta instanceof BannerMeta) {
            BannerMeta banner = (BannerMeta) meta;
            List<String> patterns = new ArrayList<>();
            for (Pattern pattern : banner.getPatterns()) {
                patterns.add(pattern.getColor() + " " + pattern.getPattern().getIdentifier());
            }
            config.set(path + "." + "patterns", patterns);
        } else if (meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leather = (LeatherArmorMeta) meta;
            Color color = leather.getColor();
            config.set(path + "." + "color", color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
        } else if (meta instanceof PotionMeta) {
            PotionMeta potion = (PotionMeta) meta;
            List<String> effects = new ArrayList<>();
            for (PotionEffect effect : potion.getCustomEffects())
                effects.add(effect.getType().getName() + " " + effect.getDuration() + " " + effect.getAmplifier());

            config.set(path + "." + "effects", effects);
        }
    }

    /**
     * Deserialize an ItemStack from the config.
     *
     * @param config the config section to deserialize the ItemStack object from.
     * @return a deserialized ItemStack.
     * @since 1.0.0
     */
    @SuppressWarnings("deprecation")
    public static ItemStack deserialize(FileConfiguration config, String path) {
        // Material
        String material = config.getString(path + "." + "material");
        if (material == null) return null;
        Optional<XMaterial> matOpt = XMaterial.matchXMaterial(material);
        if (!matOpt.isPresent()) return null;

        // Build
        ItemStack item = matOpt.get().parseItem();
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();

        // Amount
        int amount = config.getInt(path + "." + "amount");
        if (amount > 1) item.setAmount(amount);

        // Durability - Damage
        if (XMaterial.isNewVersion()) {
            if (meta instanceof Damageable) {
                int damage = config.getInt(path + "." + "damage");
                if (damage > 0) ((Damageable) meta).setDamage(damage);
            }
        } else {
            int damage = config.getInt(path + "." + "damage");
            if (damage > 0) item.setDurability((short) damage);
        }

        // Special Items
        if (item.getType() == XMaterial.PLAYER_HEAD.parseMaterial()) {
            String skull = config.getString(path + "." + "skull");
            if (skull != null) SkullUtils.applySkin(meta, skull);
        } else if (meta instanceof BannerMeta) {
            BannerMeta banner = (BannerMeta) meta;

            for (String pattern : config.getStringList(path + "." + "patterns")) {
                String[] split = pattern.split("  +");
                if (split.length == 0) continue;
                DyeColor color = Enums.getIfPresent(DyeColor.class, split[0]).or(DyeColor.WHITE);
                PatternType type;

                if (split.length > 1) {
                    type = PatternType.getByIdentifier(split[1]);
                    if (type == null) type = Enums.getIfPresent(PatternType.class, split[1]).or(PatternType.BASE);
                } else {
                    type = PatternType.BASE;
                }

                banner.addPattern(new Pattern(color, type));
            }
        } else if (meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leather = (LeatherArmorMeta) meta;
            String colorStr = config.getString(path + "." + "color");
            if (colorStr != null) {
                leather.setColor(parseColor(colorStr));
            }
        } else if (meta instanceof PotionMeta) {
            PotionMeta potion = (PotionMeta) meta;
            for (String effects : config.getStringList(path + "." + "effects")) {
                PotionEffect effect = XPotion.parsePotionEffectFromString(effects);
                potion.addCustomEffect(effect, true);
            }
        }

        // Unbreakable
        if (XMaterial.supports(11)) meta.setUnbreakable(config.getBoolean(path + "." + "unbreakable"));

        // Enchantments
        if(config.getBoolean(path + "." + "enchanted") == true) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Parses RGB color codes from a string.
     * This only works for 1.13 and above.
     *
     * @param str the RGB string.
     * @return a color based on the RGB.
     * @since 1.1.0
     */
    public static Color parseColor(String str) {
        if (Strings.isNullOrEmpty(str)) return Color.BLACK;
        String[] rgb = StringUtils.split(StringUtils.deleteWhitespace(str), ',');
        if (rgb.length < 3) return Color.WHITE;
        return Color.fromRGB(NumberUtils.toInt(rgb[0], 0), NumberUtils.toInt(rgb[1], 0), NumberUtils.toInt(rgb[1], 0));
    }
}
