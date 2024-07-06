package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class AspectOfTheJerry implements Listener {

    private final JavaPlugin plugin;

    public AspectOfTheJerry(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§fAspect of the Jerry");
        List<String> lore = new ArrayList<>();
        lore.add("§7Damage: §c+1");
        lore.add("");
        lore.add("§6Item Ability: Parley §e§lRIGHT CLICK");
        lore.add("§7Channel your inner §eJerry§7.");
        lore.add("§8Cooldown: §a5s");
        lore.add("");
        lore.add("§f§lCOMMON SWORD");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item.getType() == Material.WOODEN_SWORD && item.getItemMeta().getDisplayName().equals("§fAspect of the Jerry")) {

                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);

                player.sendMessage("§aJerry.");

                player.setCooldown(Material.WOODEN_SWORD, 100); // 5 seconds = 100 ticks
            }
        }
    }
}
