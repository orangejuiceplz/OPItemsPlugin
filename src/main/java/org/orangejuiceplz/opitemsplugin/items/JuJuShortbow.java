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

import java.util.ArrayList;
import java.util.List;

public class JuJuShortbow implements Listener {

    private final JavaPlugin plugin;

    public JuJuShortbow(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.BOW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§d§lMaxed JuJu Shortbow");
        List<String> lore = new ArrayList<>();
        lore.add("§7Gear Score: §d5000");
        lore.add("§7Damage: §c+310");
        lore.add("§7Strength: §c+40");
        lore.add("§7Crit Damage: §c+110%");
        lore.add("§7Bonus Attack Speed: §c10.5% §9(Heroic +7%)");
        lore.add("");
        lore.add("§7Ability: Shortbow");
        lore.add("§7Instantly shoots!");
        lore.add("");
        lore.add("§7When Arrow hits a mob, it");
        lore.add("§7ricochets to up to §c2 §7more");
        lore.add("§7mobs within §c3 §7blocks.");
        lore.add("");
        lore.add("§7Can damage Endermen.");
        lore.add("");
        lore.add("§7Catacombs Level: §c50");
        lore.add("");
        lore.add("§6Item Ability: Instant Shot §e§lRIGHT CLICK");
        lore.add("§7Instantly shoots an arrow.");
        lore.add("§8Cooldown: §a0.5s");
        lore.add("");
        lore.add("§d§lMYTHIC BOW");
        meta.setLore(lore);

        meta.addEnchant(Enchantment.POWER, 25, true);
        meta.addEnchant(Enchantment.INFINITY, 1, true);
        meta.addEnchant(Enchantment.UNBREAKING,20, true);
        meta.addEnchant(Enchantment.MENDING, 10, true);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                item.getType() == Material.BOW && item.getItemMeta().getDisplayName().equals("§d§lMaxed JuJu Shortbow")) {

            event.setCancelled(true);

            if (player.getCooldown(Material.BOW) > 0) {
                return;
            }


            Arrow arrow = player.launchProjectile(Arrow.class);
            arrow.setVelocity(player.getLocation().getDirection().multiply(3));

            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 1.0f);

            player.setCooldown(Material.BOW, 0);
        }
    }
}