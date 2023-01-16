package me.cerial.brawlkits.core.commands;
import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.KitsDataManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitCommand implements TabExecutor {
    /** Takes a enchantment name by string and matches
         *  to an enchantment value using a little fuzzy
         *  matching (strip any space, underscore or dash
         *  and case doesn't matter)
         * 
         * @param enchString
         * @return
         */
    private static Enchantment getEnchantment(String enchString) {
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
        // Getting all enchants, that internal for loop might not be healthy but fuck it :P
        List<String> enchantmentList = new ArrayList<>(data.getConfig().getStringList("kits."+kit+".enchants"));
        Map<Enchantment, Integer> enchants = new HashMap<>();
        for (String loopEnchant : data.getConfig().getStringList("kits."+kit+".enchants")) {
            // Split the enchants and put them in the map we made
            String[] splitEnchant = loopEnchant.split(" ");
            int value = Integer.parseInt(splitEnchant[1]);

            enchants.put(getEnchantment(splitEnchant[0]), value);
        }

        // Get all the items of the kit and loop through them
        List<String> items = new ArrayList<>(data.getConfig().getConfigurationSection("kits."+kit).getKeys(true));
        for (String item : items) {
            // First, get the item as an ItemStack
            Material itemAsMaterial = Material.getMaterial(item);
            assert itemAsMaterial != null;
            ItemStack itemAsStack = new ItemStack(itemAsMaterial);
            ItemMeta itemAsMeta = itemAsStack.getItemMeta();

            // Second, set the item name
            assert itemAsMeta != null;
            itemAsMeta.setDisplayName(data.getConfig().getString(item+".name"));

            // Third, set the item lore
            itemAsMeta.setLore(data.getConfig().getStringList(item+".lore"));

            // Forth, set the enchants
            // I'm using unsafe since why the hell not :P
            itemAsStack.addUnsafeEnchantments(enchants);

            // Fifth, set the ItemMeta to the ItemStack
            itemAsStack.setItemMeta(itemAsMeta);

            // Sixth, give the player the item
            p.getInventory().addItem(itemAsStack);
        }

        Utils.message(p, "&7Given kit &c"+kit+"&7.");
    }
    
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
                giveKitItem(p, args[0]);
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
