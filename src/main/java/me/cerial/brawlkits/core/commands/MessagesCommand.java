package me.cerial.brawlkits.core.commands;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.MessagesDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class MessagesCommand implements TabExecutor {

    private void modMessage(String message, CommandSender sender, String type) {
        MessagesDataManager data = new MessagesDataManager(Core.getInstance());

        // Loop through the current messages
        boolean exists = false;
        for (String msg : data.getConfig().getStringList("messages")) {
            if (msg.equalsIgnoreCase(message)) {
                exists = true;
                break;
            }
        }

        if (exists) {
            Utils.message(sender, "&cThis message already exists. Try another one, or remove it with /messages remove <your message>.");
        } else {
            List<String> messages = data.getConfig().getStringList("messages");
            if (type.equalsIgnoreCase("add")) {
                messages.add(message);
                Utils.message(sender, "&aAdded message successfully:\n" +
                        message);
            } else if (type.equalsIgnoreCase("remove")) {
                messages.remove(message);
                Utils.message(sender, "&aRemoved message successfully:\n" +
                        message);
            }
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //-- /messages <add|remove|list> <message> --
        if (command.getName().equalsIgnoreCase("messages")) {
            // Check if player has permission
            if (!sender.hasPermission("core.manager")) {
                Utils.message(sender, "&cYou don't have permission to run this command.");
                return true;
            }

            // Check if first argument is set
            if (args.length >= 1) {
                if (
                        args[0].equalsIgnoreCase("add") ||
                        args[0].equalsIgnoreCase("remove")
                ) {
                    if (args.length >= 2) {
                        modMessage(args[1], sender, args[0]);
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    MessagesDataManager data = new MessagesDataManager(Core.getInstance());
                    Utils.message(sender, "&eList of messages:\n&8&m                                                       ");
                    for (String msg : data.getConfig().getStringList("messages")) {
                        Utils.message(sender, "&7- &r" + msg);
                    }
                } else {
                    Utils.message(sender, "&cUnknown paramater for first argument.");
                    Utils.message(sender, "&4Usage: &7&o/messages <add|remove|list> <message>");
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
