package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SmiteStick implements Listener {

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eSmite Stick");
        meta.setLore(Arrays.asList(
                "§7A powerful stick imbued with the",
                "§7power of lightning.",
                "",
                "§6Item Ability: Smite §e§lRIGHT CLICK",
                "§7Right-click on an entity to strike",
                "§7it with lightning.",
                "",
                "§e§lLEGENDARY WEAPON"
        ));
        meta.addEnchant(Enchantment.UNBREAKING, 10, true);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.STICK && item.getItemMeta().getDisplayName().equals("§eSmite Stick")) {
            event.getRightClicked().getWorld().strikeLightning(event.getRightClicked().getLocation());
            player.sendMessage("§aYou smited the entity with lightning!");
        }
    }
}