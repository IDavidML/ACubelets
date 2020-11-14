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
import me.davidml16.acubelets.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

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
    public static void serializeIcon(ItemStack item, FileConfiguration config, String path, boolean lore) {
        Objects.requireNonNull(item, "Cannot serialize a null item");
        Objects.requireNonNull(config, "Cannot serialize item from a null configuration section.");
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

        if(lore) {
            if (meta.hasLore()) {
                List<String> lines = new ArrayList<>();
                for (String line : meta.getLore()) lines.add(line.replace('§', '&'));
                config.set(path + "." + "lore", lines);
            }
        }

        if (XMaterial.supports(11)) config.set(path + "." + "unbreakable", meta.isUnbreakable());
        if (XMaterial.supports(14)) if (meta.hasCustomModelData()) config.set(path + "." + "custom-model", meta.getCustomModelData());

        if (meta.getEnchants().size() > 0) config.set(path + "." + "enchanted", true);
        else config.set(path + "." + "enchanted", false);

        // Flags
        if (meta.getItemFlags().size() > 0) {
            List<String> flags = new ArrayList<>();
            for (ItemFlag flag : meta.getItemFlags()) flags.add(flag.name());
            config.set(path + "." + "flags", flags);
        }

        if (meta instanceof SkullMeta) {
            String texture = SkullUtils.getSkinValue(item);
            if(texture == null) {
                if (XMaterial.supports(12)) {
                    config.set(path + "." + "skull", ((SkullMeta) meta).getOwningPlayer().getUniqueId().toString());
                } else {
                    config.set(path + "." + "skull", ((SkullMeta) meta).getOwner());
                }
            } else {
                config.set(path + "." + "skull", texture);
            }
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
    public static ItemStack deserializeIcon(FileConfiguration config, String path, boolean lore) {
        Objects.requireNonNull(config, "Cannot deserialize item to a null configuration section.");

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
        if (XMaterial.supports(11)) {
            if(config.contains(path + "." + "unbreakable")) {
                meta.setUnbreakable(config.getBoolean(path + "." + "unbreakable", false));
            }
        }

        // Custom Model Data
        if (XMaterial.supports(14)) {
            int modelData = config.getInt(path + "." + "model-data");
            if (modelData != 0) meta.setCustomModelData(modelData);
        }

        // Flags
        List<String> flags = config.getStringList("flags");
        for (String flag : flags) {
            flag = flag.toUpperCase(Locale.ENGLISH);
            if (flag.equals("ALL")) {
                meta.addItemFlags(ItemFlag.values());
                break;
            }

            ItemFlag itemFlag = Enums.getIfPresent(ItemFlag.class, flag).orNull();
            if (itemFlag != null) meta.addItemFlags(itemFlag);
        }

        // Enchantments
        if(config.contains(path + "." + "enchanted")) {
            if (config.getBoolean(path + "." + "enchanted")) {
                meta.addEnchant(Enchantment.DURABILITY, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
        }

        if(lore) {
            if(config.contains(path + "." + "lore")) {
                List<String> lores = config.getStringList(path + "." + "lore");
                if (!lores.isEmpty()) {
                    List<String> translatedLore = new ArrayList<>();

                    for (String line : lores) {
                        if (line.isEmpty()) {
                            translatedLore.add(" ");
                            continue;
                        }

                        for (String singleLore : StringUtils.splitPreserveAllTokens(line, '\n')) {
                            if (singleLore.isEmpty()) {
                                translatedLore.add(" ");
                                continue;
                            }
                            singleLore = ChatColor.translateAlternateColorCodes('&', singleLore);
                            translatedLore.add(singleLore);
                        }
                    }

                    meta.setLore(translatedLore);
                } else {
                    String line = config.getString(path + "." + "lore");
                    if (!Strings.isNullOrEmpty(line)) {
                        List<String> translatedLore = new ArrayList<>();

                        for (String singleLore : StringUtils.splitPreserveAllTokens(line, '\n')) {
                            if (singleLore.isEmpty()) {
                                translatedLore.add(" ");
                                continue;
                            }
                            singleLore = ChatColor.translateAlternateColorCodes('&', singleLore);
                            translatedLore.add(singleLore);
                        }

                        meta.setLore(translatedLore);
                    }
                }
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    @SuppressWarnings("deprecation")
    public static void serializeItem(ItemStack item, FileConfiguration config, String path) {
        Objects.requireNonNull(item, "Cannot serialize a null item");
        Objects.requireNonNull(config, "Cannot serialize item from a null configuration section.");
        ItemMeta meta = item.getItemMeta();

        if (meta.hasDisplayName()) config.set(path + "." + "name", meta.getDisplayName().replace('§', '&'));
        if (meta.hasLore()) {
            List<String> lines = new ArrayList<>();
            for (String lore : meta.getLore()) lines.add(lore.replace('§', '&'));
            config.set(path + "." + "lore", lines);
        }
        if (item.getAmount() > 1) config.set(path + "." + "amount", item.getAmount());
        if (XMaterial.isNewVersion()) {
            if (meta instanceof Damageable) {
                Damageable damageable = (Damageable) meta;
                if (damageable.hasDamage()) config.set(path + "." + "damage", damageable.getDamage());
            }
        } else if (XMaterial.isDamageable(item.getType().name())) {
            config.set(path + "." + "damage", item.getDurability());
        }
        config.set(path + "." + "material", item.getType().name());
        if (XMaterial.supports(11)) if (meta.isUnbreakable()) config.set(path + "." + "unbreakable", true);
        if (XMaterial.supports(14)) if (meta.hasCustomModelData()) config.set(path + "." + "custom-model", meta.getCustomModelData());

        // Enchantments
        for (Map.Entry<Enchantment, Integer> enchant : meta.getEnchants().entrySet()) {
            String entry = "enchants." + XEnchantment.matchXEnchantment(enchant.getKey()).name();
            config.set(path + "." + entry, enchant.getValue());
        }

        // Flags
        if (meta.getItemFlags().size() != 0) {
            List<String> flags = new ArrayList<>();
            for (ItemFlag flag : meta.getItemFlags()) flags.add(flag.name());
            config.set(path + "." + "flags", flags);
        }

        // Attributes - https://minecraft.gamepedia.com/Attribute
        if (XMaterial.supports(9) && meta.hasAttributeModifiers()) {
            for (Map.Entry<Attribute, AttributeModifier> attribute : meta.getAttributeModifiers().entries()) {
                String path2 = path + "." + "attributes." + attribute.getKey().name() + '.';
                AttributeModifier modifier = attribute.getValue();

                config.set(path2 + "id", modifier.getUniqueId().toString());
                config.set(path2 + "name", modifier.getName());
                config.set(path2 + "amount", modifier.getAmount());
                config.set(path2 + "operation", modifier.getOperation().name());
                config.set(path2 + "slot", modifier.getSlot().name());
            }
        }

        if (meta instanceof SkullMeta) {
            String texture = SkullUtils.getSkinValue(item);
            if(texture == null) {
                if (XMaterial.supports(12)) {
                    config.set(path + "." + "skull", ((SkullMeta) meta).getOwningPlayer().getUniqueId().toString());
                } else {
                    config.set(path + "." + "skull", ((SkullMeta) meta).getOwner());
                }
            } else {
                config.set(path + "." + "skull", texture);
            }
        } else if (meta instanceof BannerMeta) {
            BannerMeta banner = (BannerMeta) meta;
            ConfigurationSection patterns = config.createSection(path + "." + "patterns");

            for (Pattern pattern : banner.getPatterns()) {
                patterns.set(pattern.getPattern().name(), pattern.getColor().name());
            }
        } else if (meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leather = (LeatherArmorMeta) meta;
            Color color = leather.getColor();
            config.set(path + "." + "color", color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
        } else if (meta instanceof PotionMeta) {
            PotionMeta potion = (PotionMeta) meta;
            List<String> effects = new ArrayList<>();
            for (PotionEffect effect : potion.getCustomEffects())
                effects.add(effect.getType().getName() + ", " + effect.getDuration() + ", " + effect.getAmplifier());

            config.set(path + "." + "effects", effects);
            PotionData potionData = potion.getBasePotionData();
            config.set(path + "." + "base-effect", potionData.getType().name() + ", " + potionData.isExtended() + ", " + potionData.isUpgraded());

            if (potion.hasColor()) {
                config.set(path + "." + "color", potion.getColor().asRGB());
            }
        } else if (meta instanceof FireworkMeta) {
            FireworkMeta firework = (FireworkMeta) meta;
            config.set("power", firework.getPower());
            int i = 0;

            for (FireworkEffect fw : firework.getEffects()) {
                ConfigurationSection fwc = config.getConfigurationSection(path + "." + "firework." + i);
                fwc.set("type", fw.getType().name());

                List<String> colors = new ArrayList<>();
                for (Color color : fw.getColors()) colors.add(color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
                fwc.set("colors", colors);

                colors.clear();
                for (Color color : fw.getFadeColors()) colors.add(color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
                fwc.set("fade-colors", colors);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static ItemStack deserializeItem(FileConfiguration config, String path) {
        Objects.requireNonNull(config, "Cannot deserialize item to a null configuration section.");

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
        if (matOpt.get() == XMaterial.PLAYER_HEAD) {
            String skull = config.getString(path + "." + "skull");
            if (skull != null) SkullUtils.applySkin(meta, skull);
        } else if (meta instanceof BannerMeta) {
            BannerMeta banner = (BannerMeta) meta;
            ConfigurationSection patterns = config.getConfigurationSection(path + "." + "patterns");

            if (patterns != null) {
                for (String pattern : patterns.getKeys(false)) {
                    PatternType type = PatternType.getByIdentifier(pattern);
                    if (type == null) type = Enums.getIfPresent(PatternType.class, pattern.toUpperCase(Locale.ENGLISH)).or(PatternType.BASE);
                    DyeColor color = Enums.getIfPresent(DyeColor.class, patterns.getString(pattern).toUpperCase(Locale.ENGLISH)).or(DyeColor.WHITE);

                    banner.addPattern(new Pattern(color, type));
                }
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

            String baseEffect = config.getString(path + "." + "base-effect");
            if (!Strings.isNullOrEmpty(baseEffect)) {
                String[] split = StringUtils.split(baseEffect, ',');
                PotionType type = Enums.getIfPresent(PotionType.class, split[0].trim().toUpperCase(Locale.ENGLISH)).or(PotionType.UNCRAFTABLE);
                boolean extended = split.length != 1 && Boolean.parseBoolean(split[1].trim());
                boolean upgraded = split.length > 2 && Boolean.parseBoolean(split[2].trim());

                PotionData potionData = new PotionData(type, extended, upgraded);
                potion.setBasePotionData(potionData);
            }

            if (config.contains(path + "." + "color")) {
                potion.setColor(Color.fromRGB(config.getInt(path + "." + "color")));
            }
        } else if (meta instanceof BlockStateMeta) {
            BlockStateMeta bsm = (BlockStateMeta) meta;
            BlockState state = bsm.getBlockState();
            if (state instanceof CreatureSpawner) {
                CreatureSpawner spawner = (CreatureSpawner) state;
                spawner.setSpawnedType(Enums.getIfPresent(EntityType.class, config.getString(path + "." + "spawner").toUpperCase(Locale.ENGLISH)).orNull());
                bsm.setBlockState(spawner);
            }
        } else if (meta instanceof FireworkMeta) {
            FireworkMeta firework = (FireworkMeta) meta;
            firework.setPower(config.getInt(path + "." + "power"));

            ConfigurationSection fireworkSection = config.getConfigurationSection(path + "." + "firework");
            if (fireworkSection != null) {
                FireworkEffect.Builder builder = FireworkEffect.builder();
                for (String fws : fireworkSection.getKeys(false)) {
                    ConfigurationSection fw = config.getConfigurationSection("firework." + fws);

                    builder.flicker(fw.getBoolean("flicker"));
                    builder.trail(fw.getBoolean("trail"));
                    builder.with(Enums.getIfPresent(FireworkEffect.Type.class, fw.getString("type").toUpperCase(Locale.ENGLISH)).or(FireworkEffect.Type.STAR));

                    List<Color> colors = new ArrayList<>();
                    for (String colorStr : fw.getStringList("colors")) {
                        colors.add(parseColor(colorStr));
                    }
                    builder.withColor(colors);

                    colors.clear();
                    for (String colorStr : fw.getStringList("fade-colors")) {
                        colors.add(parseColor(colorStr));
                    }
                    builder.withFade(colors);

                    firework.addEffect(builder.build());
                }
            }
        }

        // Display Name
        String name = config.getString(path + "." + "name");
        if (!Strings.isNullOrEmpty(name)) {
            String translated = ChatColor.translateAlternateColorCodes('&', name);
            meta.setDisplayName(translated);
        }

        // Unbreakable
        if (XMaterial.supports(11)) {
            if(config.contains(path + "." + "unbreakable")) {
                meta.setUnbreakable(config.getBoolean(path + "." + "unbreakable", false));
            }
        }

        // Custom Model Data
        if (XMaterial.supports(14)) {
            int modelData = config.getInt(path + "." + "model-data");
            if (modelData != 0) meta.setCustomModelData(modelData);
        }

        // Lore
        List<String> lores = config.getStringList(path + "." + "lore");
        if (!lores.isEmpty()) {
            List<String> translatedLore = new ArrayList<>();

            for (String lore : lores) {
                if (lore.isEmpty()) {
                    translatedLore.add(" ");
                    continue;
                }

                for (String singleLore : StringUtils.splitPreserveAllTokens(lore, '\n')) {
                    if (singleLore.isEmpty()) {
                        translatedLore.add(" ");
                        continue;
                    }
                    singleLore = ChatColor.translateAlternateColorCodes('&', singleLore);
                    translatedLore.add(singleLore);
                }
            }

            meta.setLore(translatedLore);
        } else {
            String lore = config.getString(path + "." + "lore");
            if (!Strings.isNullOrEmpty(lore)) {
                List<String> translatedLore = new ArrayList<>();

                for (String singleLore : StringUtils.splitPreserveAllTokens(lore, '\n')) {
                    if (singleLore.isEmpty()) {
                        translatedLore.add(" ");
                        continue;
                    }
                    singleLore = ChatColor.translateAlternateColorCodes('&', singleLore);
                    translatedLore.add(singleLore);
                }

                meta.setLore(translatedLore);
            }
        }

        // Enchantments
        ConfigurationSection enchants = config.getConfigurationSection(path + "." + "enchants");
        if (enchants != null) {
            for (String ench : enchants.getKeys(false)) {
                Optional<XEnchantment> enchant = XEnchantment.matchXEnchantment(ench);
                enchant.ifPresent(xEnchantment -> meta.addEnchant(xEnchantment.parseEnchantment(), enchants.getInt(ench), true));
            }
        }

        // Flags
        List<String> flags = config.getStringList(path + "." + "flags");
        for (String flag : flags) {
            flag = flag.toUpperCase(Locale.ENGLISH);
            if (flag.equals("ALL")) {
                meta.addItemFlags(ItemFlag.values());
                break;
            }

            ItemFlag itemFlag = Enums.getIfPresent(ItemFlag.class, flag).orNull();
            if (itemFlag != null) meta.addItemFlags(itemFlag);
        }

        // Atrributes - https://minecraft.gamepedia.com/Attribute
        if (XMaterial.supports(9)) {
            ConfigurationSection attributes = config.getConfigurationSection(path + "." + "attributes");
            if (attributes != null) {
                for (String attribute : attributes.getKeys(false)) {
                    Attribute attributeInst = Enums.getIfPresent(Attribute.class, attribute.toUpperCase(Locale.ENGLISH)).orNull();
                    if (attributeInst == null) continue;

                    String attribId = attributes.getString("id");
                    UUID id = attribId != null ? UUID.fromString(attribId) : UUID.randomUUID();

                    AttributeModifier modifier = new AttributeModifier(
                            id,
                            attributes.getString("name"),
                            attributes.getInt("amount"),
                            Enums.getIfPresent(AttributeModifier.Operation.class, attributes.getString("operation"))
                                    .or(AttributeModifier.Operation.ADD_NUMBER),
                            Enums.getIfPresent(EquipmentSlot.class, attributes.getString("slot")).or(EquipmentSlot.HAND));

                    meta.addAttributeModifier(attributeInst, modifier);
                }
            }
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
        return Color.fromRGB(NumberUtils.toInt(rgb[0], 0), NumberUtils.toInt(rgb[1], 0), NumberUtils.toInt(rgb[2], 0));
    }


    public static String itemStackToBase64(ItemStack item) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(item);

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static ItemStack itemStackFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack item = (ItemStack) dataInput.readObject();
            dataInput.close();

            return item;
        } catch (ClassNotFoundException | IOException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }


}
