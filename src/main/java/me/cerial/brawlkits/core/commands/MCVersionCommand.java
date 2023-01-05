package me.cerial.brawlkits.core.commands;

import com.viaversion.viaversion.api.Via;
import me.cerial.brawlkits.core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MCVersionCommand implements CommandExecutor {
    private String getVersion(int protocol) {
        // Versions wanted: 1.8, 1.16-Latest

        if (protocol == 761) {
            return "1.19.3";
        } else if (protocol == 760) {
            return "1.19.1/2";
        } else if (protocol == 759) {
            return "1.19";
        } else if (protocol == 758) {
            return "1.18.2";
        } else if (protocol == 757) {
            return "1.18.0/1";
        } else if (protocol == 756) {
            return "1.17.1";
        } else if (protocol == 755) {
            return "1.17";
        } else if (protocol == 754) {
            return "1.16.5";
        } else if (protocol == 47) {
            return "1.8";
        } else {
            return "unk";
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mcversion")) {
            // Check if sender has permission
            if (!sender.hasPermission("core.trainee")) {
                Utils.message(sender, "&cYou don't have permission to run this command.");
                return true;
            }


            // Check if first argument is set
            if (args.length >= 1) {
                boolean isOnline = false;
                Player p = null;
                for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                    if (args[0].equalsIgnoreCase(pl.getName())) {
                        isOnline = true;
                        p = pl;
                        break;
                    }
                }

                if (isOnline) {
                    // Check ViaVersion version of player
                    int version = Via.getAPI().getPlayerVersion(p.getUniqueId());
                    String pversion = getVersion(version);
                    if (pversion.equalsIgnoreCase("unk")) {
                        Utils.message(sender, "&4"+p.getName()+"&7's version is undocumented ("+ version +"). &cPlease refer to the documentation here: https://wiki.vg/Protocol_version_numbers#Versions_after_the_Netty_rewrite");
                        return true;
                    }

                    Utils.message(sender, "&4"+p.getName()+"&7's version is &c"+getVersion(version)+" &7("+version+")");
                } else {
                    Utils.message(sender, "&cThis player is not online.");
                    Utils.message(sender, "&cUsage: &7&o/mcversion <player>");
                }
            } else {
                Utils.message(sender, "&cSpecify a player name.");
                Utils.message(sender, "&cUsage: &7&o/mcversion <player>");
            }
        }

        return true;
    }
}
