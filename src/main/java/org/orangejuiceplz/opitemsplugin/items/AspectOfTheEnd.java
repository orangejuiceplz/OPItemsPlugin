package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class AspectOfTheEnd implements Listener {

    private final JavaPlugin plugin;
    private final HashMap<UUID, Float> originalSpeeds = new HashMap<>();

    public AspectOfTheEnd(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§5Aspect of the End");
        meta.setLore(Arrays.asList(
                "§7Damage: §c+100",
                "§7Strength: §c+100",
                "",
                "§6Item Ability: Instant Transmission §e§lRIGHT CLICK",
                "§7Teleport §a8 blocks §7ahead of",
                "§7you and gain §a+50% §f✦ Speed",
                "§7for §a3 seconds§7.",
                "",
                "§5§lEPIC SWORD"
        ));
        meta.addEnchant(Enchantment.SHARPNESS, 10, true);
        meta.addEnchant(Enchantment.UNBREAKING, 5, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addEnchant(Enchantment.LOOTING, 3, true);
        meta.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item.getType() == Material.DIAMOND_SWORD && item.getItemMeta().getDisplayName().equals("§5Aspect of the End")) {
                Location start = player.getEyeLocation().clone();
                Location end = start.clone().add(player.getEyeLocation().getDirection().multiply(8));
                safeTeleport(player, start, end);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

                if (!originalSpeeds.containsKey(player.getUniqueId())) {
                    originalSpeeds.put(player.getUniqueId(), player.getWalkSpeed());
                }
                float originalSpeed = originalSpeeds.get(player.getUniqueId());
                player.setWalkSpeed(originalSpeed * 1.5f);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.setWalkSpeed(originalSpeed);
                        player.sendMessage("§cInstant Transmission speed boost has worn off.");
                    }
                }.runTaskLater(plugin, 60L);
            }
        }
    }

    private void safeTeleport(Player player, Location start, Location end) {
        Vector direction = end.toVector().subtract(start.toVector()).normalize();
        double distance = start.distance(end);
        double maxDistance = Math.min(distance, 8);

        Location lastSafeLocation = start.clone();

        for (double d = 0.5; d <= maxDistance; d += 0.5) {
            Location checkLoc = start.clone().add(direction.clone().multiply(d));

            if (isSafeLocation(checkLoc)) {
                lastSafeLocation = checkLoc.clone().add(0, 0.5, 0);
            } else if (isAirLocation(checkLoc)) {
                lastSafeLocation = checkLoc.clone();
            } else {
                break;
            }
        }

        player.teleport(lastSafeLocation);
    }

    private boolean isSafeLocation(Location location) {
        return location.getBlock().getType().isAir() &&
                location.clone().add(0, 1, 0).getBlock().getType().isAir() &&
                location.clone().add(0, -1, 0).getBlock().getType().isSolid();
    }

    private boolean isAirLocation(Location location) {
        return location.getBlock().getType().isAir() &&
                location.clone().add(0, 1, 0).getBlock().getType().isAir();
    }
}
