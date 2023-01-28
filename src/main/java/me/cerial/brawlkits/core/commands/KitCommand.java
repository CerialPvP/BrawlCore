package me.cerial.brawlkits.core.commands;
import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.KitsDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.*;

public class KitCommand implements TabExecutor {
    /** Takes a enchantment name by string and matches
         *  to an enchantment value using a little fuzzy
         *  matching (strip any space, underscore or dash
         *  and case doesn't matter)
         * 
         * @param enchString
         * @return
         */
    protected static Enchantment getEnchantment(String enchString) {
        // Clean up string - make lowercase and strip space/dash/underscore
        enchString = enchString.toLowerCase().replaceAll("[ _-]", "");
 
        // Set up aliases (this could probably be done outside the function so
        // we only do it once (eg. in a support class init or read from a file)
        Map<String, String> aliases = new HashMap<String, String>();
        aliases.put("aspectfire", "fireaspect");
        aliases.put("sharpness", "damageall");
        aliases.put("smite", "damageundead");
        aliases.put("punch", "arrowknockback");
        aliases.put("looting", "lootbonusmobs");
        aliases.put("fortune", "lootbonusblocks");
        aliases.put("baneofarthropods", "damageundead");
        aliases.put("power", "arrowdamage");
        aliases.put("flame", "arrowfire");
        aliases.put("infinity", "arrowinfinite");
        aliases.put("unbreaking", "durability");
        aliases.put("efficiency", "digspeed");
 
        // If an alias exists, use it
        String alias = aliases.get(enchString);
        if (alias != null)
            enchString = alias;
 
        // Loop through all enchantments and match (case insensitive and ignoring space,
        // underscore and dashes
        for (Enchantment value : Enchantment.values()) {
            if (enchString.equalsIgnoreCase(value.getName().replaceAll("[ _-]", ""))) {
                return value;
            }
        }
       
        return null; // nothing found.
    }

    private void giveKitItem(Player p, String kit) {
        KitsDataManager data = new KitsDataManager(Core.getInstance());

        // -- Example: item_name 1 name:This_is_a_name protection:1 unbreaking:1 --

        // Initialized failed item hashmap
        HashMap<Integer, String> failed = new HashMap<>();
        int index = -1;

        // We loop through all the kit items
        for (String item : data.getConfig().getStringList("kits."+kit+".items")) {
            index++;

            // Split the item string
            String[] split = item.split(" ");

            // Get the item as an ItemStack
            Material mprsd = Material.getMaterial(split[0]);
            if (mprsd == null) {
                // If the material is null, put the failed item in a map.
                failed.put(index, split[0]+";Failed to get Material.");
                continue;
            }

            ItemStack prsd = new ItemStack(mprsd);
            ItemMeta meprsd = prsd.getItemMeta();
            if (meprsd == null) {
                // If the meta is null, put the failed item in a map.
                failed.put(index, split[0]+";Failed to get ItemMeta.");
                continue;
            }

            // Do the stuff I need to do with meta
            String display = split[2]
                    .replace("name:", "")
                    .replaceAll("_", " ");

            meprsd.setDisplayName(display);
            prsd.setAmount(Integer.parseInt(split[1]));

            // Clone the split array and remove the first 3 keys
            List<String> cloneSplit = new ArrayList<>(Arrays.stream(split).toList());

            for (int i = 0; i < 3; i++) {
                cloneSplit.remove(0);
            }

            // Loop through this list
            for (String ench : cloneSplit) {
                // Split the enchant key
                String[] enchSplit = ench.split(":");

                Enchantment enchantment = getEnchantment(enchSplit[0]);
                int power = Integer.parseInt(enchSplit[1]);
                assert enchantment != null;
                meprsd.addEnchant(enchantment, power, false);
            }
            // Set the modified item meta to the ItemStack
            prsd.setItemMeta(meprsd);

            // Give the item to the player
            p.getInventory().addItem(prsd);
        }

        // Send a message to the player
        Utils.message(p, "&7Given kit &4"+kit+".");

        // Set the cooldown of the kit
        // -- Example: players.uuid.kit --
        double cooldown = (data.getConfig().getDouble("kits."+kit+".cooldown") * 1000);
        Bukkit.broadcastMessage(cooldown + "");

        cooldowns.put(p.getName() + ";" + kit, System.currentTimeMillis());
    }

    private final HashMap<String, Long> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kit")) {
            // Check if the executor is not a player
            if (!(sender instanceof Player p)) {
                Utils.message(sender, "&cYou must be a player to execute this command!");
                return true;
            }

            // Get the kit list
            KitsDataManager data = new KitsDataManager(Core.getInstance());
            List<String> kits = new ArrayList<>(data.getConfig().getConfigurationSection("kits").getKeys(false));

            // Check if there are no kits
            if (kits.isEmpty()) {
                Utils.message(p, "&cNo kits have been found in the kit system. Please contact a manager immediately.");
                return true;
            }

            // Check if first argument is set
            if (args.length >= 1) {
                // Check if the argument is one of the kits in the YML
                boolean exists = false;
                for (String kit : kits) {
                    if (args[0].equalsIgnoreCase(kit)) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    Utils.message(p, "&cThe kit \""+args[0]+"\" does not exist.");
                    Utils.message(p, "&6Usage: &7&o/kits <"+(String.join(", ", kits)+">"));
                } else {
                    // -- HashMap: Key = name;kit, Value = Current timestamp in MS --

                    // Check if the user has their key saved in the hashmap
                    String key = p.getName() + ";" + args[0];
                    if (cooldowns.containsKey(key)) {
                        long remaining = System.currentTimeMillis() - cooldowns.get(key);
                        if (remaining < (data.getConfig().getDouble("kits."+args[0]+".cooldown") * 1000)) {
                            Utils.message(p, "&cYou cannot use kit \""+args[0]+"\" for another "+Utils.formatTime((long) (((data.getConfig().getDouble("kits."+args[0]+".cooldown") * 1000)-remaining) / 1000))+".");
                        } else {
                            giveKitItem(p, args[0]);
                        }
                    } else {
                        giveKitItem(p, args[0]);
                    }
                }
            } else {
                Utils.message(sender, "&cYou did not specify a kit. \n" +
                        "&6Usage: &7&o/kit <" + (String.join(", ", kits) + ">"));
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
