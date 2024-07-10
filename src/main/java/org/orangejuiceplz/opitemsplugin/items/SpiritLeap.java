package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class SpiritLeap implements Listener {

    public static ItemStack createItem() {
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§9Spirit Leap");
        meta.setLore(Arrays.asList(
                "§6Ability: Spirit Teleport §e§lRIGHT CLICK",
                "§7Allows you to teleport to any",
                "§7person when below a certain",
                "§7threshold!",
                "§8Cooldown: §a5s",
                "",
                "§9§lRARE ITEM"
        ));
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                item != null && item.getItemMeta() != null &&
                item.getItemMeta().getDisplayName().equals("§1Spirit Leap")) {

            openSpiritLeapMenu(player);
            event.setCancelled(true);
        }
    }

    private void openSpiritLeapMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§1Spirit Leap");

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)) {
                ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
                skullMeta.setOwningPlayer(onlinePlayer);
                skullMeta.setDisplayName("§e" + onlinePlayer.getName());
                skull.setItemMeta(skullMeta);
                inventory.addItem(skull);
            }
        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("§1Spirit Leap")) {
            event.setCancelled(true);

            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                Player player = (Player) event.getWhoClicked();
                String targetPlayerName = event.getCurrentItem().getItemMeta().getDisplayName().substring(2);
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

                if (targetPlayer != null && targetPlayer.isOnline()) {
                    player.teleport(targetPlayer.getLocation());
                    player.sendMessage("§aTeleported to " + targetPlayer.getName());
                } else {
                    player.sendMessage("§cPlayer is no longer online.");
                }

                player.closeInventory();
            }
        }
    }
}
