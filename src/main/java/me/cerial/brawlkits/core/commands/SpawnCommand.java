package me.cerial.brawlkits.core.commands;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.ServerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("spawn")) {
            // Check if first argument is set
            if (args.length >= 1) {
                // Check if the player has permission
                if (!sender.hasPermission("core.smod")) {
                    Utils.message(sender, "&cYou don't have permission to teleport others to spawn.");
                    return true;
                }

                // Get that player
                Player pl = null;
                for (Player looppl : Bukkit.getServer().getOnlinePlayers()) {
                    if (args[0].equalsIgnoreCase(looppl.getName())) {
                        pl = looppl;
                        break;
                    }
                }

                // Check if the player is actually set
                if (pl != null) {
                    // Teleport that player to spawn
                    ServerDataManager data = new ServerDataManager(Core.getInstance());
                    Location location = new Location(
                            Bukkit.getServer().getWorld(data.getConfig().getString("spawn.world")),
                            data.getConfig().getDouble("spawn.x"),
                            data.getConfig().getDouble("spawn.y"),
                            data.getConfig().getDouble("spawn.z")
                    );
                    location.setPitch((float) data.getConfig().getDouble("spawn.pitch"));
                    location.setYaw((float) data.getConfig().getDouble("spawn.yaw"));

                    pl.teleport(location);

                    // Send a message to the player and to all staff
                    Utils.message(pl, "&eYou have been teleported to spawn by a staff member.");
                    Bukkit.broadcast(Utils.color("&4&lBrawl&c&lKits &8> &4"+pl.getName()+" &7has been teleported to spawn by &c"+sender.getName()+"."), "core.trainee");

                } else {
                    Utils.message(sender, "&cThat player is not online.");
                    Utils.message(sender, "&cUsage: &7&o/spawn [player]");
                }
            } else {
                // Check if the sender is a player
                if (sender instanceof Player p) {
                    // Teleport that player to spawn
                    ServerDataManager data = new ServerDataManager(Core.getInstance());
                    Location location = new Location(
                            Bukkit.getServer().getWorld(data.getConfig().getString("spawn.world")),
                            data.getConfig().getDouble("spawn.x"),
                            data.getConfig().getDouble("spawn.y"),
                            data.getConfig().getDouble("spawn.z")
                    );
                    location.setPitch((float) data.getConfig().getDouble("spawn.pitch"));
                    location.setYaw((float) data.getConfig().getDouble("spawn.yaw"));

                    p.teleport(location);
                    Utils.message(p, "&aYou have been teleported to spawn.");
                } else {
                    Utils.message(sender, "&cYou must be a player to execute this command without arguments.");
                }
            }
        }


        return true;
    }
}
