package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Random;

public class SorrowMiningArmor implements Listener {

    private static final Random random = new Random();

    public static ItemStack createArmorPiece(Material material, String piece) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Sorrow " + piece);
        meta.setLore(Arrays.asList(
                "§7Mining Speed: §a+100",
                "§7Mining Fortune: §a+10",
                "",
                "§6Full Set Bonus: Sorrow",
                "§7Grants §a+20% §7chance to find",
                "§7rare drops while mining.",
                "",
                "§6§lLEGENDARY ARMOR PIECE"
        ));

        meta.addEnchant(Enchantment.PROTECTION, 10, true);
        meta.addEnchant(Enchantment.FIRE_PROTECTION, 10, true);
        meta.addEnchant(Enchantment.BLAST_PROTECTION, 10, true);
        meta.addEnchant(Enchantment.PROJECTILE_PROTECTION, 10, true);
        meta.addEnchant(Enchantment.THORNS, 10, true);
        meta.addEnchant(Enchantment.MENDING, 10, true);
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);

        if (material == Material.CHAINMAIL_HELMET) {
            meta.addEnchant(Enchantment.AQUA_AFFINITY, 1, true);
            meta.addEnchant(Enchantment.RESPIRATION, 10, true);
        } else if (material == Material.CHAINMAIL_BOOTS) {
            meta.addEnchant(Enchantment.FEATHER_FALLING, 10, true);
            meta.addEnchant(Enchantment.DEPTH_STRIDER, 10, true);
            meta.addEnchant(Enchantment.SOUL_SPEED, 3, true);
        } else if (material == Material.CHAINMAIL_LEGGINGS) {
            meta.addEnchant(Enchantment.SWIFT_SNEAK, 5, true);
        }

        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }

    public boolean isWearingFullSet(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        return helmet != null && helmet.getItemMeta().getDisplayName().contains("Sorrow") &&
                chestplate != null && chestplate.getItemMeta().getDisplayName().contains("Sorrow") &&
                leggings != null && leggings.getItemMeta().getDisplayName().contains("Sorrow") &&
                boots != null && boots.getItemMeta().getDisplayName().contains("Sorrow");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (isWearingFullSet(player)) {
            // Increase mining speed
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 20,4));

            // Increase chance for better drops
            if (random.nextDouble() < 0.2) { // 20% chance for better drops
                Material blockType = event.getBlock().getType();
                Location dropLocation = event.getBlock().getLocation();

                switch (blockType) {
                    case DIAMOND_ORE:
                    case DEEPSLATE_DIAMOND_ORE:
                        dropLocation.getWorld().dropItemNaturally(dropLocation, new ItemStack(Material.DIAMOND, 2));
                        break;
                    case GOLD_ORE:
                    case DEEPSLATE_GOLD_ORE:
                        dropLocation.getWorld().dropItemNaturally(dropLocation, new ItemStack(Material.GOLD_INGOT, 2));
                        break;
                    case IRON_ORE:
                    case DEEPSLATE_IRON_ORE:
                        dropLocation.getWorld().dropItemNaturally(dropLocation, new ItemStack(Material.IRON_INGOT, 2));
                        break;
                    case ANCIENT_DEBRIS:
                        dropLocation.getWorld().dropItemNaturally(dropLocation, new ItemStack(Material.NETHERITE_SCRAP, 2));
                        break;
                    // Add more cases for other ores or blocks as needed
                }
            }
        }
    }
}
