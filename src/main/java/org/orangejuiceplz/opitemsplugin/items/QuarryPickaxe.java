package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class QuarryPickaxe implements Listener {

    public static ItemStack createItem() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Quarry Pickaxe");
        meta.setLore(Arrays.asList(
                "§7Mines a 3x3x3 area at once.",
                "",
                "§6Ability: Quarry §e§lWHILE MINING",
                "§7Automatically mines a 3x3x1 area",
                "§7in the direction you're facing.",
                "",
                "§6§lLEGENDARY PICKAXE"
        ));
        meta.addEnchant(Enchantment.EFFICIENCY, 5, true);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getItemMeta() != null && item.getItemMeta().getDisplayName().equals("§bQuarry Pickaxe")) {
            Block centerBlock = event.getBlock();
            mineArea(player, centerBlock);
            event.setCancelled(true);
        }
    }

    private void mineArea(Player player, Block centerBlock) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Block relativeBlock = centerBlock.getRelative(x, y, 0);
                if (relativeBlock.getType().isBlock() && !relativeBlock.getType().isAir()) {
                    relativeBlock.breakNaturally(player.getInventory().getItemInMainHand());
                }
            }
        }
    }
}
