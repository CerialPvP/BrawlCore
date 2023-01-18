package me.cerial.brawlkits.core.commands;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.KitsDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateKitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("createkit")) {
            // -- /createkit <name> <cooldown> --
            // Check if executor is not a player
            if (!(sender instanceof Player p)) {
                Utils.message(sender, "&cYou must be a player to use this command.");
                return true;
            } else if (!p.hasPermission("core.manager")) {
                Utils.message(p, "&cYou don't have permission to use this command.");
                return true;
            } else {
                // Check if first argument is set
                if (args.length >= 1) {
                    // Check if second argument is set
                    if (args.length >= 2) {
                        long n1 = System.currentTimeMillis();
                        // Create a kit with the name and data
                        KitsDataManager data = new KitsDataManager(Core.getInstance());
                        List<String> itemsList = new ArrayList<>();

                        // Set the delay
                        data.getConfig().set("kits."+args[0]+".cooldown", Integer.parseInt(args[1]));
                        // Loop through the player's inventory
                        for (ItemStack item : p.getInventory().getContents()) {
                            // -- Example: item_name 1 name:This_is_a_name protection:1 unbreaking:1 --
                            if (item == null) {
                                continue;
                            } else {
                                Bukkit.broadcastMessage(item.getAmount() + "");
                                // Get display name
                                String display = item.getItemMeta().getDisplayName()
                                        .replaceAll(" ", "_");

                                List<String> enchants = new ArrayList<>();
                                // Loop over enchant map
                                Map<Enchantment, Integer> enchantMap = item.getEnchantments();

                                for (Map.Entry<Enchantment, Integer> entry : enchantMap.entrySet()) {
                                    enchants.add(" " + entry.getKey().getName()+":"+entry.getValue());
                                }
                                itemsList.add(item.getType().name() + " " + item.getAmount() + " name:"+display + String.join("", enchants));
                            }
                        }

                        // Set the list and save it
                        data.getConfig().set("kits."+args[0]+".items", itemsList);
                        data.saveConfig();

                        // Send a message to the player
                        Utils.message(p, "&aSaved kit successfully with the following settings: \n" +
                                "&7- &4Name: &c"+args[0]+"\n" +
                                "&7- &4Delay: &c"+args[1]+"\n" +
                                "&7- &4Items: &c"+itemsList.toArray().length+" items \n" +
                                "&7- &4Time took: &c"+(System.currentTimeMillis()-n1)+"ms");
                    } else {
                        Utils.message(p, "&cYou must specify a cooldown time for the kit. \n" +
                                "&6Usage: &7&o/createkit <name> <cooldown>");
                    }
                } else {
                    Utils.message(p, "&cYou must specify a name for the kit. \n" +
                            "&6Usage: &7&o/createkit <name> <cooldown>");
                }
            }
        }

        return true;
    }
}
