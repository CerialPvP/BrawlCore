package me.cerial.brawlkits.core.commands;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.KitsDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class DeleteKitCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("deletekit")) {
            // -- /delkit <kit> --

            // Check if the sender doesn't have permission
            if (!sender.hasPermission("core.manager")) {
                Utils.message(sender, "&cYou don't have permission to use this command.");
                return true;
            }

            // Get the kit list
            KitsDataManager data = new KitsDataManager(Core.getInstance());
            List<String> kits = new ArrayList<>(data.getConfig().getConfigurationSection("kits").getKeys(false));

            // Check if there are no kits
            if (kits == null) {
                Utils.message(sender, "&cNo kits have been found in the kit system. Please contact a manager immediately.");
                return true;
            }

            if (args.length >= 1) {
                // Check if the argument is one of the kits in the YML
                boolean exists = false;
                for (String kit : kits) {
                    if (args[0].equalsIgnoreCase(kit)) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    data.getConfig().set("kits." + args[0], null);
                    data.saveConfig();
                    Utils.message(sender, "&aDeleted kit " + args[0] + " successfully.");
                } else {
                    Utils.message(sender, "&cThe kit \"" + args[0] + "\" doesn't exist. \n" +
                            "&6Usage: &7&o/kits <" + (String.join(", ", kits) + ">"));
                }
            } else {
                Utils.message(sender, "&cYou did not specify a kit. \n" +
                        "&6Usage: &7&o/deletekit <" + (String.join(", ", kits) + ">"));
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            KitsDataManager data = new KitsDataManager(Core.getInstance());
            return new ArrayList<>(data.getConfig().getConfigurationSection("kits").getKeys(false));
        }

        return null;
    }
}
