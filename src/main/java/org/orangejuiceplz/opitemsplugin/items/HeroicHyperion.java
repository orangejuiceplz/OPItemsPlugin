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
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeroicHyperion implements Listener {

    private final JavaPlugin plugin;

    public HeroicHyperion(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§d§l§kL§r §d§lHeroic Hyperion §d§l§kL");
        List<String> lore = new ArrayList<>(Arrays.asList(
                "§7Gear Score: §d5000",
                "§7Damage: §c+1,500.3",
                "§7Strength: §c+940.1 §e(+30) §9(Heroic +50)",
                "§7Crit Damage: §c+300.6%",
                "§7Bonus Attack Speed: §c10.5% §9(Heroic +7%)",
                "§7Ability Damage: §c+108.533%",
                "",
                "§7Intelligence: §a+2,600.917 §9(Heroic +125)",
                "§7Ferocity: §a52.5",
                "",
                "§7Deals §c+50% §7damage to Withers,",
                "§7Grants §c+1 Damage §7and §a+2 §b§lIntelligence",
                "§7per §cCatacombs §7level.",
                "",
                "§7Catacombs Level: §c50",
                "",
                "§6Item Ability: Wither Impact §e§lRIGHT CLICK",
                "§7Teleport §a10 blocks §7ahead of",
                "§7you. Then implode, dealing",
                "§c15,000 §7damage to nearby",
                "§7enemies. Also applies the wither",
                "§7shield scroll ability, reducing",
                "§7damage taken and granting an",
                "§7absorption shield for §e5 §7seconds.",
                ""

        ));

        meta.addEnchant(Enchantment.SHARPNESS, 35, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 20, true);
        meta.addEnchant(Enchantment.LOOTING, 10, true);
        meta.addEnchant(Enchantment.UNBREAKING, 50, true);
        meta.addEnchant(Enchantment.MENDING, 10, true);
        meta.addEnchant(Enchantment.SWEEPING_EDGE,10,true);

        lore.add("§dChimera V");
        lore.add("§9Cleave VI");
        lore.add("§9Critical VII");
        lore.add("§9Cubism VI");
        lore.add("§9Dragon Hunter V");
        lore.add("§9Ender Slayer VII");
        lore.add("§9Execute VI");
        lore.add("§9Experience IV");
        lore.add("§9Giant Killer VII");
        lore.add("§9Impaling III");
        lore.add("§9Lethality VI");
        lore.add("§9Looting V");
        lore.add("§9Luck VII");
        lore.add("§9Scavenger V");
        lore.add("§9Smite VII");
        lore.add("§9Syphon V");
        lore.add("§9Telekinesis I");
        lore.add("§9Thunderlord VI");
        lore.add("§9Triple-Strike V");
        lore.add("§9Vampirism VI");
        lore.add("§9Venomous VI");
        lore.add("§9Vicious V");
        lore.add("");
        lore.add("§d§lMYTHIC SWORD");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item.getType() == Material.IRON_SWORD && item.getItemMeta().getDisplayName().equals("§d§l§kL§r §d§lHeroic Hyperion §d§l§kL")) {
                Location start = player.getEyeLocation().clone();
                Location end = start.clone().add(player.getEyeLocation().getDirection().multiply(10));
                safeTeleport(player, start, end);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

                performHyperionEffect(player, player.getLocation());
            }
        }
    }

    private void safeTeleport(Player player, Location start, Location end) {
        Vector direction = end.toVector().subtract(start.toVector()).normalize();
        double distance = start.distance(end);
        double maxDistance = Math.min(distance, 10);

        Location lastSafeLocation = start.clone();

        for (double d = 0.5; d <= maxDistance; d += 0.5) {
            Location checkLoc = start.clone().add(direction.clone().multiply(d));

            if (isSafeLocation(checkLoc)) {
                lastSafeLocation = checkLoc.clone().add(0, 0.5, 0);
            } else if (isAirLocation(checkLoc)) {
                lastSafeLocation = checkLoc.clone();
            } else {
                break;
            }
        }

        player.teleport(lastSafeLocation);
    }

    private boolean isSafeLocation(Location location) {
        return location.getBlock().getType().isAir() &&
                location.clone().add(0, 1, 0).getBlock().getType().isAir() &&
                location.clone().add(0, -1, 0).getBlock().getType().isSolid();
    }

    private boolean isAirLocation(Location location) {
        return location.getBlock().getType().isAir() &&
                location.clone().add(0, 1, 0).getBlock().getType().isAir();
    }

    private void performHyperionEffect(Player player, Location location) {
        player.getWorld().createExplosion(location, 0, false, false);

        List<Entity> nearbyEntities = player.getNearbyEntities(5, 5, 5);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).damage(15000, player);
            }
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 4)); // 5 seconds, Absorption V
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 2)); // 5 seconds, Resistance III

        player.sendMessage("§aYou used Wither Impact!");

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.removePotionEffect(PotionEffectType.ABSORPTION);
            player.removePotionEffect(PotionEffectType.RESISTANCE);
            player.sendMessage("§cWither shield has worn off.");
        }, 100L);
    }
}