package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Material;
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
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item.getType() == Material.DIAMOND_SWORD && item.getItemMeta().getDisplayName().equals("§5Aspect of the End")) {
                player.teleport(player.getLocation().add(player.getLocation().getDirection().multiply(8)));
                float originalSpeed = player.getWalkSpeed();
                player.setWalkSpeed(originalSpeed * 1.5f);
                player.sendMessage("§aYou used Instant Transmission!");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.setWalkSpeed(originalSpeed);
                        player.sendMessage("§cInstant Transmission speed boost has worn off.");
                    }
                }.runTaskLater(plugin, 60L); // 60 ticks = 3 seconds
            }
        }
    }
}