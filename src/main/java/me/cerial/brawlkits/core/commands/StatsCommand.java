package me.cerial.brawlkits.core.commands;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.StatsDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.HashMap;
import java.util.Map;

public class StatsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("stats")) {
            StatsDataManager data = new StatsDataManager(Core.getInstance());
            // Check if sender is not a player
            if (!(sender instanceof Player p)) {
                Utils.message(sender, "&cYou must be a player to run this command.");
                return true;
            }

            Player target = null;
            // -- /stats [player] --
            // Check if first argument is set
            if (args.length >= 1) {
                Bukkit.broadcastMessage("argument specified, " + args[0]);
                // Loop through the offline players
                for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                    // Check if the name is the same as ours
                    if (pl.getName().equalsIgnoreCase(args[0])) {
                        target = pl;
                        break;
                    }
                }

                if (target == null) {
                    target = p;
                    Utils.message(p, "&6That player wasn't online, checking your own stats...");
                }
            }

            // Make a new GUI
            InventoryGUI gui = new InventoryGUI(Bukkit.createInventory(null, 9, "Stats of " + target.getName()));
            Map<Integer, ItemBuilder> builderMap = new HashMap<>();

            // Check if the target has a bounty
            if (data.getConfig().getDouble("") >= 1) {
                // 123 567
                gui.addButton(1,
                        ItemButton.create(new ItemBuilder(Material.DIAMOND_SWORD)
                            .setName(Utils.color("&4Kills: &c"+Math.round(data.getConfig().getDouble(target.getUniqueId() + ".kills")))), e -> e.setCancelled(true))
                );

                gui.addButton(2,
                        ItemButton.create(new ItemBuilder(Material.DANDELION)
                                .setName(Utils.color("&4Deaths: &c"+Math.round(data.getConfig().getDouble(target.getUniqueId() + ".deaths")))), e -> e.setCancelled(true))
                );

                gui.addButton(3,
                        ItemButton.create(new ItemBuilder(Material.GOLDEN_SWORD)
                                .setName(Utils.color("&4KDR: &c"+data.getConfig().getDouble(target.getUniqueId() + ".kills")/data.getConfig().getDouble(target.getUniqueId() + ".deaths"))), e -> e.setCancelled(true))
                );

                gui.addButton(5,
                        ItemButton.create(new ItemBuilder(Material.IRON_SWORD)
                                .setName(Utils.color("&4Killstreak: &c"+Math.round(data.getConfig().getDouble(target.getUniqueId() + ".killstreak")))), e -> e.setCancelled(true))
                );

                gui.addButton(6,
                        ItemButton.create(new ItemBuilder(Material.SUNFLOWER)
                                .setName(Utils.color("&4Balance: &c"+Core.getEcon().getBalance(target))), e -> e.setCancelled(true))
                );

                gui.addButton(7,
                        ItemButton.create(new ItemBuilder(Material.LAVA_BUCKET)
                                .setName(Utils.color("&4Bounty: &c$"+Math.round(data.getConfig().getDouble(target.getUniqueId() + ".bounty")))), e -> e.setCancelled(true))
                );

            } else {
                // 23456
                gui.addButton(2,
                        ItemButton.create(new ItemBuilder(Material.DIAMOND_SWORD)
                                .setName(Utils.color("&4Kills: &c"+Math.round(data.getConfig().getDouble(target.getUniqueId() + ".kills")))), e -> e.setCancelled(true))
                );

                gui.addButton(3,
                        ItemButton.create(new ItemBuilder(Material.DANDELION)
                                .setName(Utils.color("&4Deaths: &c"+Math.round(data.getConfig().getDouble(target.getUniqueId() + ".deaths")))), e -> e.setCancelled(true))
                );

                gui.addButton(4,
                        ItemButton.create(new ItemBuilder(Material.GOLDEN_SWORD)
                                .setName(Utils.color("&4KDR: &c"+data.getConfig().getDouble(target.getUniqueId() + ".kills")/data.getConfig().getDouble(target.getUniqueId() + ".deaths"))), e -> e.setCancelled(true))
                );

                gui.addButton(5,
                        ItemButton.create(new ItemBuilder(Material.IRON_SWORD)
                                .setName(Utils.color("&4Killstreak: &c"+Math.round(data.getConfig().getDouble(target.getUniqueId() + ".killstreak")))), e -> e.setCancelled(true))
                );

                gui.addButton(6,
                        ItemButton.create(new ItemBuilder(Material.SUNFLOWER)
                                .setName(Utils.color("&4Balance: &c"+Core.getEcon().getBalance(target))), e -> e.setCancelled(true))
                );
            }

            gui.open(p);
        }

        return true;
    }
}
