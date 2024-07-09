package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Random;

public class Stonk implements Listener {

    private static final Random random = new Random();

    public static ItemStack createItem() {
        ItemStack item = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Stonk");
        meta.setLore(Arrays.asList(
                "§7Efficiency VI",
                "§7Unbreakable",
                "",
                "§6Ability: End Stone Bonus",
                "§75% chance to drop an extra",
                "§7End Stone when mining End Stone.",
                "",
                "§6§lLEGENDARY PICKAXE"
        ));
        meta.addEnchant(Enchantment.EFFICIENCY, 6, true);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null &&
                event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§6Stonk")) {
            if (event.getBlock().getType() == Material.END_STONE) {
                if (random.nextDouble() < 0.05) {
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.END_STONE));
                    event.getPlayer().giveExp(1);
                }
            }
        }
    }
}
