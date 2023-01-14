package me.cerial.brawlkits.core.commands;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.ServerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("freeze")) {
            // -- /freeze <player> --
            if (args.length >= 1) {
                Player p = null;
                for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                    if (pl.getName().equalsIgnoreCase(args[0])) {
                        p = pl;
                        break;
                    }
                }

                if (p != null) {
                    ServerDataManager data = new ServerDataManager(Core.getInstance());
                    boolean isFrozen = data.getConfig().getBoolean("players." + p.getUniqueId() + ".frozen");
                    if (!isFrozen) {
                        data.getConfig().set("players." + p.getUniqueId() + ".frozen", true);
                        data.saveConfig();

                        Bukkit.broadcast(Utils.color("&4&lBrawl&c&lKits &8> &4" + sender.getName() + " &7has frozen &c" + p.getName() + "&7."), "core.trainee");

                        // Send a message to the player
                        Utils.message(p, "&cYou have been frozen!");
                        p.sendMessage(Utils.color("&7Please join the Discord server via /discord."));
                        p.sendMessage(Utils.color("&4Refusing to join in 3 minutes will result in a ban."));
                    } else {
                        data.getConfig().set("players." + p.getUniqueId() + ".frozen", false);
                        data.saveConfig();

                        Bukkit.broadcast(Utils.color("&4&lBrawl&c&lKits &8> &4" + sender.getName() + " &7has un-frozen &c" + p.getName() + "&7."), "core.trainee");

                        // Send a message to the player
                        Utils.message(p, "&aYou have been unfrozen. Enjoy playing!");
                        return true;
                    }
                } else {
                    Utils.message(sender, "&cThat player is not online.");
                    Utils.message(sender, "&cUsage: &7&o/freeze <player>");
                }
            }
        }
        return true;
    }
}