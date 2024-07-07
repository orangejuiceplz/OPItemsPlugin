package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class StormArmor implements Listener {
    private final JavaPlugin plugin;

    public StormArmor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack createArmorPiece(Material material, String piece) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        if (meta != null) {
            meta.setColor(Color.AQUA);
            meta.addItemFlags(ItemFlag.HIDE_DYE);
            meta.setDisplayName("§6Storm's " + piece);
            meta.setLore(Arrays.asList(
                    "§7Health: §a+100",
                    "§7Defense: §a+60",
                    "§7Intelligence: §a+100",
                    "",
                    "§6Full Set Bonus: Witherborn",
                    "§7Spawns a wither minion every",
                    "§a30§7 seconds up to a",
                    "§7maximum of §e1§7 wither. Your withers will",
                    "§7travel to and explode on nearby",
                    "§7enemies",
                    "",
                    "§7Reduces the damage you take from withers by §c10%§7.",
                    "",
                    "§6§lLEGENDARY ARMOR PIECE"
            ));

            meta.addEnchant(Enchantment.PROTECTION, 10, true);
            meta.addEnchant(Enchantment.FIRE_PROTECTION, 10, true);
            meta.addEnchant(Enchantment.BLAST_PROTECTION, 10, true);
            meta.addEnchant(Enchantment.PROJECTILE_PROTECTION, 10, true);
            meta.addEnchant(Enchantment.THORNS, 10, true);
            meta.addEnchant(Enchantment.MENDING, 10, true);

            if (material == Material.LEATHER_HELMET) {
                meta.addEnchant(Enchantment.RESPIRATION, 10, true);
                meta.addEnchant(Enchantment.AQUA_AFFINITY, 1, true);
            } else if (material == Material.LEATHER_BOOTS) {
                meta.addEnchant(Enchantment.FEATHER_FALLING, 10, true);
                meta.addEnchant(Enchantment.DEPTH_STRIDER, 10, true);
                meta.addEnchant(Enchantment.SOUL_SPEED, 3, true);
            } else if (material == Material.LEATHER_LEGGINGS) {
                meta.addEnchant(Enchantment.SWIFT_SNEAK, 5, true);
            }

            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(meta);
        }
        return item;
    }

    public boolean isWearingFullSet(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        return helmet != null && helmet.getType() == Material.LEATHER_HELMET &&
                chestplate != null && chestplate.getType() == Material.LEATHER_CHESTPLATE &&
                leggings != null && leggings.getType() == Material.LEATHER_LEGGINGS &&
                boots != null && boots.getType() == Material.LEATHER_BOOTS &&
                helmet.getItemMeta().getDisplayName().contains("Storm's") &&
                chestplate.getItemMeta().getDisplayName().contains("Storm's") &&
                leggings.getItemMeta().getDisplayName().contains("Storm's") &&
                boots.getItemMeta().getDisplayName().contains("Storm's");
    }

    private void applySetBonus(Player player) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(40);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isWearingFullSet(player)) {
                    WitherSkull witherSkull = player.launchProjectile(WitherSkull.class);
                    witherSkull.setYield(0); // No environmental damage
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 600L); // 30 seconds
    }

    private void removeSetBonus(Player player) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20); // Reset to default
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0); // Reset to default
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (isWearingFullSet(player)) {
                applySetBonus(player);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (isWearingFullSet(player)) {
                    applySetBonus(player);
                } else {
                    removeSetBonus(player);
                }
            }, 1L);
        }
    }
}
