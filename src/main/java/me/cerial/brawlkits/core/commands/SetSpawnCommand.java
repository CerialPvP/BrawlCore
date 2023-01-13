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

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setspawn")) {
            if (sender instanceof Player p) {
                // Check if player has permission
                if (sender.hasPermission("core.manager")) {
                    // Get the server config and set the x, y, z, pitch and yaw
                    ServerDataManager data = new ServerDataManager(Core.getInstance());
                    Location loc = p.getLocation();

                    data.getConfig().set("spawn.x", loc.getX());
                    data.getConfig().set("spawn.y", loc.getY());
                    data.getConfig().set("spawn.z", loc.getZ());
                    data.getConfig().set("spawn.pitch", loc.getPitch());
                    data.getConfig().set("spawn.yaw", loc.getYaw());
                    data.getConfig().set("spawn.world", loc.getWorld().getName());
                    data.saveConfig();

                    Bukkit.broadcast(Utils.color("&4&lBrawl&c&lKits &8> &4"+p.getName()+" &7has set the spawn location to &c("+loc.getBlockX()+" "+loc.getBlockY()+" "+loc.getBlockZ()+")&7."), "op");
                    return true;
                } else {
                    Utils.message(p, "&cYou don't have permission to run this command.");
                }
            } else {
                Utils.message(sender, "&cYou must be a player to execute this.");
            }
        }


        return true;
    }
}
