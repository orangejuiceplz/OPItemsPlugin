package org.orangejuiceplz.opitemsplugin.items;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MaxedSpiritSceptre implements Listener {

    private final JavaPlugin plugin;

    public MaxedSpiritSceptre(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.ALLIUM);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§d§lSpirit Sceptre");
        List<String> lore = new ArrayList<>();
        lore.add("§7Damage: §c+310");
        lore.add("§7Intelligence: §a+400");
        lore.add("");
        lore.add("§6Item Ability: Spirit Ballz §e§lRIGHT CLICK");
        lore.add("§7Shoots a spirit ball that");
        lore.add("§7deals damage to nearby enemies.");
        lore.add("§8Mana Cost: §3200");
        lore.add("");
        lore.add("§d§lMYTHIC DUNGEON SCEPTRE");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                item.getType() == Material.ALLIUM && item.getItemMeta().getDisplayName().equals("§d§lSpirit Sceptre")) {

            event.setCancelled(true);

            Fireball spiritBall = player.launchProjectile(Fireball.class);
            spiritBall.setYield(0f);
            spiritBall.setIsIncendiary(false);
            spiritBall.setVelocity(player.getLocation().getDirection().multiply(1.5));
            spiritBall.setCustomName("SpiritBall");
            spiritBall.setCustomNameVisible(false);

            player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals("SpiritBall")) {
            event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 0f, false, false);
            event.getEntity().getWorld().getNearbyEntities(event.getEntity().getLocation(), 3, 3, 3).forEach(entity -> {
                if (entity != event.getEntity().getShooter() && entity instanceof org.bukkit.entity.Damageable) {
                    ((org.bukkit.entity.Damageable) entity).damage(310, (Player) event.getEntity().getShooter());
                }
            });
        }
    }
}
