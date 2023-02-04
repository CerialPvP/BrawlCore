package me.cerial.brawlkits.core.commands;

import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.utils.ReloadLB;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReloadLBCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("reloadlb")) {
            // -- /reloadlb <kills|deaths|balance|bounty|killstreak> --
            // Check if the sender doesn't have permission
            if (!(sender.hasPermission("core.admin"))) {
                // Send a message
                Utils.message(sender, "&cYou don't have permission to use this command.");
                return true;
            }

            // Check if first argument is set
            if (args.length >= 1) {
                // Array time
                String[] validargs = {
                        "kills",
                        "deaths",
                        "balance",
                        "bounty",
                        "killstreak"
                };

                // Check if the argument is equal to one of those
                boolean isValid = false;
                for (String loopArg: validargs) {
                    if (args[0].equalsIgnoreCase(loopArg)) {
                        isValid = true;
                        break;
                    }
                }

                // Check if the argument is valid
                if (isValid) {
                    // Call the function
                    Bukkit.broadcastMessage(Utils.color("&4&lBrawl&c&lKits &8> &c"+sender.getName()+" &7has reloaded the leaderboard &4"+args[0]+"&7."));
                    ReloadLB.reload(args[0]);
                } else {
                    // Send a message
                    Utils.message(sender, "&cInvalid leaderboard provided. \n" +
                            "&6Usage: &7&o/reloadlb <kills|deaths|balance|bounty|killstreak>");
                    return true;
                }
            } else {
                // Send a message
                Utils.message(sender, "&c");
            }
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> returnl = new ArrayList<>();
        if (args.length == 1) {
            returnl.add("kills");
            returnl.add("deaths");
            returnl.add("balance");
            returnl.add("bounty");
            returnl.add("killstreak");
            return returnl;
        }

        return null;
    }
}
