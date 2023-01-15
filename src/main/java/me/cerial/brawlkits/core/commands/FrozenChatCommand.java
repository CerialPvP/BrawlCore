package me.cerial.brawlkits.core.commands;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.ServerDataManager;
import me.cerial.brawlkits.core.listeners.ChatEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FrozenChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("frozenchat")) {
            // Check if the sender has the permissions
            if (!sender.hasPermission("core.trainee")) {
                Utils.message(sender, "&cYou don't have permission to use this command.");
                return true;
            }

            // Check if any arguments are set
            if (args.length >= 1) {
                // Make a message from the arguments provided
                String messageFinal = String.join(" ", args);
                // Send the message
                ChatEvent.sendFrozen(sender, messageFinal);
            } else {
                // Check if the sender is a player
                if (sender instanceof Player p) {
                    // Toggle the frozen chat on or off
                    // Path: players.uuid.togglefc
                    ServerDataManager data = new ServerDataManager(Core.getInstance());

                    if (data.getConfig().getBoolean("players."+p.getUniqueId()+".togglefc")) {
                        // Toggle the frozen chat on
                        data.getConfig().set("players."+p.getUniqueId()+".togglefc", true);
                        data.saveConfig();
                        Utils.message(sender, "&7Frozen chat is now toggled &aon&7.");
                    } else {
                        // Toggle the frozen chat off
                        data.getConfig().set("players."+p.getUniqueId()+".togglefc", false);
                        data.saveConfig();
                        Utils.message(sender, "&7Frozen chat is now toggled &coff&7.");
                    }
                } else {
                    Utils.message(sender, "&cYou must be a player to toggle frozen chat on.");
                }
            }
        }

        return true;
    }
}
