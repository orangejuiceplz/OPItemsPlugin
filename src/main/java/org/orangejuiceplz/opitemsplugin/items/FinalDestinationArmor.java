package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class FinalDestinationArmor implements Listener {
    private final JavaPlugin plugin;
    private final HashMap<UUID, BukkitRunnable> activeEffects = new HashMap<>();
    private final HashMap<UUID, Double> originalSpeeds = new HashMap<>();

    public FinalDestinationArmor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack createArmorPiece(Material material, String piece) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        if (meta != null) {
            meta.setColor(Color.BLACK);
            meta.addItemFlags(ItemFlag.HIDE_DYE);
            meta.setDisplayName("§6Final Destination " + piece);
            meta.setLore(Arrays.asList(
                    "§7Health: §a+120",
                    "§7Defense: §a+80",
                    "§7Intelligence: §a+80",
                    "",
                    "§6Full Set Bonus: Final Destination - Vivacious Darkness",
                    "§7Costs §32 Iron §7per 5s in combat",
                    "§7while §asneaking§7: ",
                    "§3⁍ §c+30❁ Strength",
                    "§3⁍ §e+20 ⚔ Bonus Attack Speed",
                    "§3⁍ §f+10✦ Speed",
                    "§3⁍ §bMultiply✎ Intelligence by 1.25x",
                    "§3⁍ §c+200 ⫽ Ferocity against Endermen",
                    "§3⁍ §a100% Damage against Endermen",
                    "",
                    "§6Piece Bonus: Enderman Bulwark",
                    "",
                    "§7Kill Enderman to accumulate defense",
                    "§7against them.",
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
                meta.addEnchant(Enchantment.AQUA_AFFINITY, 1, true);
                meta.addEnchant(Enchantment.RESPIRATION, 10, true);
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
                helmet.getItemMeta().getDisplayName().contains("Final Destination") &&
                chestplate.getItemMeta().getDisplayName().contains("Final Destination") &&
                leggings.getItemMeta().getDisplayName().contains("Final Destination") &&
                boots.getItemMeta().getDisplayName().contains("Final Destination");
    }

    private void applySetBonus(Player player) {
        if (!originalSpeeds.containsKey(player.getUniqueId())) {
            originalSpeeds.put(player.getUniqueId(), (double) player.getWalkSpeed());
        }

        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).addModifier(new AttributeModifier("strength", 30, AttributeModifier.Operation.ADD_NUMBER));
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(new AttributeModifier("attack_speed", 1, AttributeModifier.Operation.ADD_NUMBER));
        player.setWalkSpeed((float) (originalSpeeds.get(player.getUniqueId()) * 1.1));
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(60);
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(24);

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isSneaking() && isWearingFullSet(player)) {
                    ItemStack ironIngots = new ItemStack(Material.IRON_INGOT, 2);
                    if (player.getInventory().containsAtLeast(ironIngots, 2)) {
                        player.getInventory().removeItem(ironIngots);
                    } else {
                        player.sendMessage("§cNot enough iron ingots to maintain the set bonus!");
                        removeSetBonus(player);
                        cancel();
                    }
                } else {
                    removeSetBonus(player);
                    cancel();
                }
            }
        };

        runnable.runTaskTimer(plugin, 0L, 100L); // 5 seconds
        activeEffects.put(player.getUniqueId(), runnable);
    }

    private void removeSetBonus(Player player) {
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getModifiers().removeIf(modifier -> modifier.getName().equals("strength"));
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getModifiers().removeIf(modifier -> modifier.getName().equals("attack_speed"));
        player.setWalkSpeed(originalSpeeds.getOrDefault(player.getUniqueId(), 0.2).floatValue());
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20); // Reset to default
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0); // Reset to default

        BukkitRunnable runnable = activeEffects.remove(player.getUniqueId());
        if (runnable != null) {
            runnable.cancel();
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (isWearingFullSet(player) && event.isSneaking()) {
            applySetBonus(player);
        } else {
            removeSetBonus(player);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (isWearingFullSet(player) && player.isSneaking()) {
                event.setDamage(event.getDamage() * 2);
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