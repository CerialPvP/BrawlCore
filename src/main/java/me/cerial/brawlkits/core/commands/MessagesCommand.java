package me.cerial.brawlkits.core.commands;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.ServerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MessagesCommand implements TabExecutor {

    private void modMessage(String message, CommandSender sender, String type) {
        ServerDataManager data = new ServerDataManager(Core.getInstance());
        // Loop through the current messages
        boolean exists = false;
        int msgIndex = -1;
        for (String msg : data.getConfig().getStringList("messages")) {
            msgIndex++;
            if (msg.equalsIgnoreCase(message)) {
                exists = true;
                break;
            }
        }

        List<String> messages = data.getConfig().getStringList("messages");


        if (type.equalsIgnoreCase("add")) {
            if (exists) {
                Utils.message(sender, "&cThis message already exists!");
            } else {
                messages.add(message);
                Utils.message(sender, "&aAdded message successfully:\n&r" +
                        message);
                data.getConfig().set("messages", messages);
                data.saveConfig();
            }

        } else if (type.equalsIgnoreCase("remove")) {
            if (!exists) {
                Utils.message(sender, "&cThis message doesn't exist!");
            } else {
                messages.remove(msgIndex);
                Utils.message(sender, "&aRemoved message successfully:\n&r" +
                        message);

                data.getConfig().set("messages", messages);
                data.saveConfig();
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
                        List<String> sepMessage = new ArrayList<>();
                        // Loop through the arguments
                        // Start in arg1
                        int index = 1;
                        for (String larg : args) {
                            index++;
                            sepMessage.add(larg);
                        }

                        // Join the message
                        String message = String.join(" ", sepMessage);

                        modMessage(message, sender, args[0]);
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    ServerDataManager data = new ServerDataManager(Core.getInstance());
                    Utils.message(sender, "&eList of messages:\n&8&m                                                       ");
                    for (String msg : data.getConfig().getStringList("messages")) {
                        Utils.message(sender, "&7- &r" + msg);
                    }
                } else {
                    Utils.message(sender, "&cUnknown paramater for first argument.");
                    Utils.message(sender, "&4Usage: &7&o/messages <add|remove|list> <message>");
                }
            } else {
                Utils.message(sender, "&cNo arguments are set.");
                Utils.message(sender, "&4Usage: &7&o/messages <add|remove|list> <message>");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        ServerDataManager data = new ServerDataManager(Core.getInstance());
        if (args.length == 1) {
            list.add("add");
            list.add("remove");
            list.add("list");
            return list;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("remove")) {
                list.addAll(data.getConfig().getStringList("messages"));
                return list;
            }

            return null;
        }

        return null;
    }
}
