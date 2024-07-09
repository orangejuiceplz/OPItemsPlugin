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

public class BuildersWand implements Listener {

    public static ItemStack createItem() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§4§l§kL§r §4§lBuilder's Dream §4§l§kL");
        meta.setLore(Arrays.asList(
                "§7Right-click to extend blocks",
                "§7in the direction you're facing.",
                "",
                "§6Ability: Extend §e§lRIGHT CLICK",
                "§7Extends up to 5 blocks in a line.",
                "",
                "§4§lADMINISTRATOR TOOL"
        ));
        meta.addEnchant(Enchantment.EFFICIENCY, 10, true);
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
                item.getItemMeta().getDisplayName().equals("§4§l§kL§r §4§lBuilder's Dream §4§l§kL")) {

            Block clickedBlock = event.getClickedBlock();
            BlockFace face = event.getBlockFace();

            if (clickedBlock != null) {
                extendBlocks(player, clickedBlock, face);
                event.setCancelled(true);
            }
        }
    }

    private void extendBlocks(Player player, Block startBlock, BlockFace face) {
        Material material = startBlock.getType();
        for (int i = 1; i <= 5; i++) {
            Block nextBlock = startBlock.getRelative(face, i);
            if (nextBlock.getType() == Material.AIR) {
                nextBlock.setType(material);
            } else {
                break;
            }
        }
    }
}
