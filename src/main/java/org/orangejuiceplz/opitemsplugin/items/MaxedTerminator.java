package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class MaxedTerminator implements Listener {

    private final JavaPlugin plugin;

    public MaxedTerminator(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§d§lTerminator");
        List<String> lore = new ArrayList<>();
        lore.add("§7Damage: §c+380");
        lore.add("§7Strength: §c+50");
        lore.add("§7Crit Damage: §c+250%");
        lore.add("§7Attack Speed: §c+40%");
        lore.add("");
        lore.add("§6Item Ability: Shortbow §e§lRIGHT CLICK");
        lore.add("§7Shoots 3 arrows at once!");
        lore.add("§7Instantly shoots!");
        lore.add("");
        lore.add("§d§lMYTHIC BOW");
        meta.setLore(lore);

        meta.addEnchant(Enchantment.POWER, 25, true);
        meta.addEnchant(Enchantment.INFINITY, 1, true);
        meta.addEnchant(Enchantment.UNBREAKING, 15, true);
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addEnchant(Enchantment.FLAME, 1, true);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                item.getType() == Material.BOW && item.getItemMeta().getDisplayName().equals("§d§lTerminator")) {

            event.setCancelled(true);

            Vector playerDirection = player.getLocation().getDirection();
            double spread = 0.2;

            Arrow arrow1 = player.launchProjectile(Arrow.class);
            arrow1.setVelocity(playerDirection.multiply(4));

            Arrow arrow2 = player.launchProjectile(Arrow.class);
            arrow2.setVelocity(playerDirection.rotateAroundY(Math.toRadians(spread)).multiply(4));

            Arrow arrow3 = player.launchProjectile(Arrow.class);
            arrow3.setVelocity(playerDirection.rotateAroundY(Math.toRadians(-spread)).multiply(4));

            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 1.0f);
        }
    }
}
