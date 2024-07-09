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
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Treecapitator implements Listener {

    private static final Set<Material> LOG_TYPES = new HashSet<>(Arrays.asList(
            Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG,
            Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.DARK_OAK_LOG
    ));

    public static ItemStack createItem() {
        ItemStack item = new ItemStack(Material.GOLDEN_AXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§5Treecapitator");
        meta.setLore(Arrays.asList(
                "§7A forceful golden axe which can",
                "break a large amount of logs in",
                "a single hit!",
                "§8Cooldown: ",
                "",
                "§5§lEPIC AXE"
        ));
        meta.addEnchant(Enchantment.EFFICIENCY, 5, true);
        meta.addEnchant(Enchantment.MENDING,1,true);

        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getItemMeta() != null &&
                item.getItemMeta().getDisplayName().equals("§5Treecapitator") &&
                LOG_TYPES.contains(event.getBlock().getType())) {
            chopTree(event.getBlock());
        }
    }

    private void chopTree(Block startBlock) {
        Stack<Block> toCheck = new Stack<>();
        toCheck.push(startBlock);

        while (!toCheck.isEmpty()) {
            Block current = toCheck.pop();
            if (LOG_TYPES.contains(current.getType())) {
                current.breakNaturally();
                for (int x = -1; x <= 1; x++) {
                    for (int y = 0; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            if (x != 0 || y != 0 || z != 0) {
                                Block relative = current.getRelative(x, y, z);
                                if (LOG_TYPES.contains(relative.getType())) {
                                    toCheck.push(relative);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
