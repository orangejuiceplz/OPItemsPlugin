package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Location;
import org.bukkit.Material;
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

public class AspectOfTheEnd implements Listener {

    private final JavaPlugin plugin;

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
                Location playerLoc = player.getLocation();
                Vector direction = playerLoc.getDirection().normalize();

                Location targetLoc = playerLoc.clone().add(direction.multiply(8));

                Location safeLoc = findSafeLocation(playerLoc, targetLoc);

                if (safeLoc != null) {
                    player.teleport(safeLoc);
                    float originalSpeed = player.getWalkSpeed();
                    player.setWalkSpeed(originalSpeed * 1.5f);
                    player.sendMessage("§aYou used Instant Transmission!");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.setWalkSpeed(originalSpeed);
                            player.sendMessage("§cInstant Transmission speed boost has worn off.");
                        }
                    }.runTaskLater(plugin, 60L);
                } else {
                    player.sendMessage("§cNo safe location found for teleportation.");
                }
            }
        }
    }

    private Location findSafeLocation(Location start, Location end) {
        Vector direction = end.toVector().subtract(start.toVector()).normalize();
        double distance = start.distance(end);

        for (double d = 0; d <= distance; d += 0.5) {
            Location checkLoc = start.clone().add(direction.clone().multiply(d));
            if (checkLoc.getBlock().getType().isAir() &&
                    checkLoc.clone().add(0, 1, 0).getBlock().getType().isAir()) {
                Block blockBelow = checkLoc.clone().add(0, -1, 0).getBlock();
                if (blockBelow.getType().isSolid()) {
                    return checkLoc.clone().add(0, 0.5, 0);
                }
            }
        }
        return null;
    }
}
