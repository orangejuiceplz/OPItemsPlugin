package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class GrapplingHook implements Listener {

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Grappling Hook");
        meta.setLore(Arrays.asList(
                "§7Travel in style with this cool",
                "§7grappling hook!",
                "",
                "§6Item Ability: Grapple §e§lRIGHT CLICK",
                "§7Pulls you towards the hook's landing",
                "§7location.",
                "",
                "§6§lLEGENDARY TOOL"
        ));
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.FISHING_ROD && item.getItemMeta().getDisplayName().equals("§6Grappling Hook")) {
            if (event.getState() == PlayerFishEvent.State.REEL_IN || event.getState() == PlayerFishEvent.State.IN_GROUND) {
                Vector hookVector = event.getHook().getLocation().toVector().subtract(player.getLocation().toVector());
                player.setVelocity(hookVector.normalize().multiply(1.5));
                player.sendMessage("§aGrappling hook activated!");
            }
        }
    }
}