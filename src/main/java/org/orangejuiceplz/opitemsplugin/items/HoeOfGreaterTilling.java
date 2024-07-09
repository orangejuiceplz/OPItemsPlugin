package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class HoeOfGreaterTilling implements Listener {

    public static ItemStack createItem() {
        ItemStack item = new ItemStack(Material.NETHERITE_HOE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cHoe of Greater Tilling");
        meta.setLore(Arrays.asList(
                "§7Tills a 5x5 area at once.",
                "",
                "§6Ability: Greater Tilling §e§lRIGHT CLICK",
                "§7Tills a 5x5 area of grass or dirt",
                "§7centered on the target block.",
                "",
                "§c§lUNREAL HOE"
        ));
        meta.addEnchant(Enchantment.EFFICIENCY, 5, true);
        meta.addEnchant(Enchantment.FORTUNE, 5, true);
        meta.addEnchant(Enchantment.MENDING, 5, true);
        meta.addEnchant(Enchantment.LOYALTY, 0, true);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                item.getItemMeta() != null &&
                item.getItemMeta().getDisplayName().equals("§cHoe of Greater Tilling")) {

            Block centerBlock = event.getClickedBlock();
            if (centerBlock != null && (centerBlock.getType() == Material.GRASS_BLOCK || centerBlock.getType() == Material.DIRT)) {
                tillArea(centerBlock);
                event.setCancelled(true);
            }
        }
    }

    private void tillArea(Block centerBlock) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                Block relativeBlock = centerBlock.getRelative(x, 0, z);
                if (relativeBlock.getType() == Material.GRASS_BLOCK || relativeBlock.getType() == Material.DIRT) {
                    relativeBlock.setType(Material.FARMLAND);
                    // Check if there's a block above and break it (e.g., grass, flowers)
                    Block aboveBlock = relativeBlock.getRelative(BlockFace.UP);
                    if (aboveBlock.getType() != Material.AIR) {
                        aboveBlock.breakNaturally();
                    }
                }
            }
        }
    }
}
