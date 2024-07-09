package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.NamespacedKey;
import org.orangejuiceplz.opitemsplugin.OPItemsPlugin;

import java.util.ArrayList;
import java.util.List;

public class Hackerman implements Listener {

    private final OPItemsPlugin plugin;
    private final NamespacedKey statusKey;

    public Hackerman(OPItemsPlugin plugin) {
        this.plugin = plugin;
        this.statusKey = new NamespacedKey(plugin, "hackerman_status");
    }

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6Hackerman");
            List<String> lore = new ArrayList<>();
            lore.add("§7Right-click to toggle on/off");
            lore.add("§7When on, creates blocks beneath you");
            meta.setLore(lore);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(statusKey, PersistentDataType.INTEGER, 0);
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta()) return;
        if (!item.getItemMeta().getDisplayName().equals("§6Hackerman")) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            toggleHackerman(player, item);
        }
    }

    private void toggleHackerman(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        int status = container.getOrDefault(statusKey, PersistentDataType.INTEGER, 0);

        if (status == 0) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            container.set(statusKey, PersistentDataType.INTEGER, 1);
        } else {
            meta.removeEnchant(Enchantment.UNBREAKING);
            container.set(statusKey, PersistentDataType.INTEGER, 0);
        }

        item.setItemMeta(meta);
        player.playSound(player.getLocation(), Sound.ENTITY_PILLAGER_AMBIENT, 1, 2);
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (isHackerman(mainHand) || isHackerman(offHand)) {
            event.setCancelled(true);
        }
    }

    private boolean isHackerman(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().getDisplayName().equals("§6Hackerman");
    }

    public void activeEffect(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!isHackerman(item)) {
            item = player.getInventory().getItemInOffHand();
            if (!isHackerman(item)) return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        int status = container.getOrDefault(statusKey, PersistentDataType.INTEGER, 0);

        if (status == 1 && !player.isSneaking()) {
            Block block = player.getLocation().add(0, -1, 0).getBlock();
            if (!block.getType().isSolid()) {
                block.setType(Material.STONE);
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    if (block.getType() == Material.STONE) {
                        block.setType(Material.AIR);
                    }
                }, 40L); // 2 seconds
            }
        }
    }
}