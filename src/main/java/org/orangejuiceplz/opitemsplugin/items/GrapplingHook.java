package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.ChatColor;
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
import java.util.HashMap;
import java.util.UUID;

public class GrapplingHook implements Listener {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 2000; // 2 seconds in milliseconds

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Grappling Hook");
        meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Travel in style with this cool",
                ChatColor.GRAY + "grappling hook!",
                "",
                ChatColor.GOLD + "Item Ability: Grapple " + ChatColor.YELLOW + ChatColor.BOLD + "RIGHT CLICK",
                ChatColor.GRAY + "Pulls you towards the hook's landing",
                ChatColor.GRAY + "location.",
                "",
                ChatColor.GRAY + "Cooldown: 2 seconds",
                "",
                ChatColor.YELLOW + "§lLEGENDARY TOOL"
        ));
        meta.addEnchant(Enchantment.UNBREAKING, 50, true);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.FISHING_ROD && item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Grappling Hook")) {
            if (event.getState() == PlayerFishEvent.State.REEL_IN || event.getState() == PlayerFishEvent.State.IN_GROUND) {
                if (isOnCooldown(player)) {
                    player.sendMessage(ChatColor.RED + "Grappling hook is on cooldown!");
                    event.setCancelled(true);
                    return;
                }

                Vector hookVector = event.getHook().getLocation().toVector().subtract(player.getLocation().toVector());
                player.setVelocity(hookVector.normalize().multiply(1.5));
                player.sendMessage(ChatColor.GREEN + "Grappling hook activated!");

                setCooldown(player);
            }
        }
    }

    private boolean isOnCooldown(Player player) {
        if (cooldowns.containsKey(player.getUniqueId())) {
            long timeElapsed = System.currentTimeMillis() - cooldowns.get(player.getUniqueId());
            return timeElapsed < COOLDOWN_TIME;
        }
        return false;
    }

    private void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
