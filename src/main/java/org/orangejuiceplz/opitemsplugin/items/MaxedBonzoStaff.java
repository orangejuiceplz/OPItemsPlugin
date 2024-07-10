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

public class MaxedBonzoStaff implements Listener {

    private final JavaPlugin plugin;

    public MaxedBonzoStaff(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§1§lBonzo Staff");
        List<String> lore = new ArrayList<>();
        lore.add("§7Damage: §c+210");
        lore.add("§7Intelligence: §a+350");
        lore.add("");
        lore.add("§6Item Ability: Showtime! §e§lRIGHT CLICK");
        lore.add("§7Shoots a bouncy ball that deals");
        lore.add("§7damage to nearby enemies.");
        lore.add("§8Mana Cost: §390");
        lore.add("");
        lore.add("§1§lRARE DUNGEON STAFF");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                item.getType() == Material.BLAZE_ROD && item.getItemMeta().getDisplayName().equals("§1§lBonzo Staff")) {

            event.setCancelled(true);

            Fireball fireball = player.launchProjectile(Fireball.class);
            fireball.setYield(0f);
            fireball.setIsIncendiary(false);
            fireball.setVelocity(player.getLocation().getDirection().multiply(2));
            fireball.setCustomName("BonzoBall");
            fireball.setCustomNameVisible(false);

            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals("BonzoBall")) {
            event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 0f, false, false);
            event.getEntity().getWorld().getNearbyEntities(event.getEntity().getLocation(), 3, 3, 3).forEach(entity -> {
                if (entity != event.getEntity().getShooter() && entity instanceof org.bukkit.entity.Damageable) {
                    ((org.bukkit.entity.Damageable) entity).damage(210, (Player) event.getEntity().getShooter());
                }
            });
        }
    }
}