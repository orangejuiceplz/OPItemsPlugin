package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Fireball implements Listener {

    private final JavaPlugin plugin;
    private final NamespacedKey envDamageKey;

    public Fireball(JavaPlugin plugin) {
        this.plugin = plugin;
        this.envDamageKey = new NamespacedKey(plugin, "environmental_damage");
    }

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Magic Fireball");
        meta.getPersistentDataContainer().set(envDamageKey, PersistentDataType.BOOLEAN, false);
        updateLore(meta, false);
        item.setItemMeta(meta);
        return item;
    }

    private void updateLore(ItemMeta meta, boolean envDamage) {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "A powerful fireball that explodes");
        lore.add(ChatColor.GRAY + "on impact, dealing massive damage.");
        lore.add("");
        lore.add(ChatColor.GOLD + "Item Ability: Launch " + ChatColor.YELLOW + ChatColor.BOLD + "RIGHT CLICK");
        lore.add(ChatColor.GRAY + "Launch a devastating fireball");
        lore.add(ChatColor.GRAY + "in the direction you're facing.");
        lore.add("");
        lore.add(ChatColor.GOLD + "Toggle Environmental Damage: " + ChatColor.YELLOW + ChatColor.BOLD + "SHIFT LEFT CLICK");
        lore.add(ChatColor.GRAY + "Current state: " + (envDamage ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
        lore.add("");
        lore.add("§4§lSPECIAL ITEM");
        meta.setLore(lore);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.FIRE_CHARGE && item.hasItemMeta() &&
                ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals("Magic Fireball")) {

            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                launchFireball(player, item);
            } else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (player.isSneaking()) {
                    event.setCancelled(true);
                    toggleEnvironmentalDamage(item);
                    player.sendMessage(ChatColor.GREEN + "Environmental damage toggled!");
                }
            }
        }
    }

    private void launchFireball(Player player, ItemStack item) {
        org.bukkit.entity.Fireball fireball = player.launchProjectile(org.bukkit.entity.Fireball.class);
        fireball.setYield(5.0F); // Increased yield for more damage
        fireball.setIsIncendiary(true);

        boolean envDamage = item.getItemMeta().getPersistentDataContainer().getOrDefault(envDamageKey, PersistentDataType.BOOLEAN, false);
        if (!envDamage) {
            fireball.setMetadata("no_environmental_damage", new org.bukkit.metadata.FixedMetadataValue(plugin, true));
        }

        player.sendMessage(ChatColor.GREEN + "You launched a magic fireball!");
    }

    private void toggleEnvironmentalDamage(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        boolean currentState = meta.getPersistentDataContainer().getOrDefault(envDamageKey, PersistentDataType.BOOLEAN, false);
        boolean newState = !currentState;
        meta.getPersistentDataContainer().set(envDamageKey, PersistentDataType.BOOLEAN, newState);
        updateLore(meta, newState);
        item.setItemMeta(meta);
    }
}
