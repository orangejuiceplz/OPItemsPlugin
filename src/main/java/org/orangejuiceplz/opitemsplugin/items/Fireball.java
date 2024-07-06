package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Fireball implements Listener {

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cMagic Fireball");
        meta.setLore(Arrays.asList(
                "§7A powerful fireball that explodes",
                "§7on impact, dealing massive damage.",
                "",
                "§6Item Ability: Launch §e§lRIGHT CLICK",
                "§7Launch a devastating fireball",
                "§7in the direction you're facing.",
                "",
                "§c§lSPECIAL ITEM"
        ));
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item.getType() == Material.FIRE_CHARGE && item.getItemMeta().getDisplayName().equals("§cMagic Fireball")) {
                player.launchProjectile(org.bukkit.entity.Fireball.class);
                item.setAmount(item.getAmount() - 1);
                player.sendMessage("§aYou launched a magic fireball!");
            }
        }
    }
}