package me.cerial.brawlkits.core.utils;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.datamanagers.StatsDataManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.*;

public class ReloadLB {
    
    private static String getPlaceColor(int place) {
        if (place == 1)
            return "&e#1";
        else if (place == 2)
            return "2";
        else if (place == 3)
            return "&6#3";
        else
            return "&8#"+place;
    }
    
    public static void reload(@NotNull String lb) {
        StatsDataManager data = new StatsDataManager(Core.getInstance());
        if (lb.equalsIgnoreCase("kills")) {
            // Note the time
            Long t1 = System.currentTimeMillis();

            // Make the leaderboard "reload" mode
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                "hd setline stats_kills 2 &7Refreshing leaderboards, make take a while..."
            );
            for (int i = 3; i < 10; i++) {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                        "hd setline stats_kills "+i+" &7..."
                );
            }

            // Loop through the YML file
            Map<Integer, String> kills = new TreeMap<>(Collections.reverseOrder());
            for (String entry: data.getConfig().getKeys(true)) {
                // Check current looped value
                String checked = ".kills";
                if (entry.contains(checked)) {
                    String replace = entry.replace(checked, "");
                    kills.put(data.getConfig().getInt(entry), replace);
                }
            }

            // Loop through the map
            int mi = 1;
            for (Map.Entry<Integer, String> entry: kills.entrySet()) {
                if (mi < 11) {
                    // Execute the setline command
                    String name = Bukkit.getServer().getOfflinePlayer(UUID.fromString(entry.getValue())).getName();
                    if (name == null) continue;

                    int upmi = mi+1;
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "hd setline" +
                            " stats_kills "+upmi+" "+getPlaceColor(mi)+" &4"+name+" &8- &c"+ entry.getKey()+" kills");
                    mi++;

                } else {
                    break;
                }
            }

            // Set the final lines
            long t2 = System.currentTimeMillis();
            Date d = new Timestamp(t2);
            long diff = t2-t1;
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "hd setline" +
                    " stats_kills 12 &7Refreshed at &c"+d+" &7("+diff+"ms)");

            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "hd setline" +
                    " stats_kills 13 &eThe leaderboards refresh every 10 minutes.");
        } else if (lb.equalsIgnoreCase("deaths")) {
            // Note the time
            Long t1 = System.currentTimeMillis();

            // Make the leaderboard "reload" mode
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                    "hd setline stats_deaths 2 &7Refreshing leaderboards, make take a while..."
            );
            for (int i = 3; i < 10; i++) {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                        "hd setline stats_deaths "+i+" &7..."
                );
            }

            // Loop through the YML file
            Map<Integer, String> deaths = new TreeMap<>(Collections.reverseOrder());
            for (String entry: data.getConfig().getKeys(true)) {
                // Check current looped value
                String checked = ".deaths";
                if (entry.contains(checked)) {
                    String replace = entry.replace(checked, "");
                    deaths.put(data.getConfig().getInt(entry), replace);
                }
            }

            // Loop through the map
            int mi = 1;
            for (Map.Entry<Integer, String> entry: deaths.entrySet()) {
                if (mi < 11) {
                    // Execute the setline command
                    String name = Bukkit.getServer().getOfflinePlayer(UUID.fromString(entry.getValue())).getName();
                    if (name == null) continue;

                    int upmi = mi+1;
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "hd setline" +
                            " stats_deaths "+upmi+" "+getPlaceColor(mi)+" &4"+name+" &8- &c"+ entry.getKey()+" deaths");
                    mi++;

                } else {
                    break;
                }
            }

            // Set the final lines
            long t2 = System.currentTimeMillis();
            Date d = new Timestamp(t2);
            long diff = t2-t1;
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "hd setline" +
                    " stats_deaths 12 &7Refreshed at &c"+d+" &7("+diff+"ms)");

            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "hd setline" +
                    " stats_deaths 13 &eThe leaderboards refresh every 10 minutes.");
        } else if (lb.equalsIgnoreCase("balance")) {
            // Note the time
            Long t1 = System.currentTimeMillis();

            // Make the leaderboard "reload" mode
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                    "hd setline stats_balance 2 &7Refreshing leaderboards, make take a while..."
            );
            for (int i = 3; i < 10; i++) {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                        "hd setline stats_balance "+i+" &7..."
                );
            }

            // Loop through the offline players
            Map<Double, String> balance = new TreeMap<>(Collections.reverseOrder());
            for (OfflinePlayer pl: Bukkit.getServer().getOfflinePlayers()) {
                // Check if the balance of that player is 0, and if it is just continue
                if (Core.getEcon().getBalance(pl) == 0 || pl.getName() == null) continue;

                // Add the balance to the map
                balance.put(Core.getEcon().getBalance(pl), pl.getUniqueId().toString());
            }

            // Loop through the map
            int mi = 1;
            for (Map.Entry<Double, String> entry: balance.entrySet()) {
                if (mi < 11) {
                    // Execute the setline command
                    int upmi = mi+1;
                    String name = Bukkit.getServer().getOfflinePlayer(UUID.fromString(entry.getValue())).getName();
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "hd setline" +
                            " stats_balance "+upmi+" "+getPlaceColor(mi)+" &4"+name+" &8- &c$"+ entry.getKey());
                    mi++;

                } else {
                    break;
                }
            }

            // Set the final lines
            long t2 = System.currentTimeMillis();
            Date d = new Timestamp(t2);
            long diff = t2-t1;
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "hd setline" +
                    " stats_balance 12 &7Refreshed at &c"+d+" &7("+diff+"ms)");

            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "hd setline" +
                    " stats_balance 13 &eThe leaderboards refresh every 10 minutes.");
        } else if (lb.equalsIgnoreCase("killstreak")) {
            // Note the time
            Long t1 = System.currentTimeMillis();

            // Make the leaderboard "reload" mode
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                    "hd setline stats_killstreak 2 &7Refreshing leaderboards, make take a while..."
            );
            for (int i = 3; i < 10; i++) {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
                        "hd setline stats_killstreak "+i+" &7..."
                );
            }

            // Loop through the YML file
            Map<Integer, String> killstreak = new TreeMap<>(Collections.reverseOrder());
            for (String entry: data.getConfig().getKeys(true)) {
                // Check current looped value
                String checked = ".killstreak";
                if (entry.contains(checked)) {
                    String replace = entry.replace(checked, "");
                    killstreak.put(data.getConfig().getInt(entry), replace);
                }
            }

            // Loop through the map
            int mi = 1;
            for (Map.Entry<Integer, String> entry: killstreak.entrySet()) {
                if (mi < 11) {
                    // Execute the setline command
                    String name = Bukkit.getServer().getOfflinePlayer(UUID.fromString(entry.getValue())).getName();
                    if (name == null) continue;

                    int upmi = mi+1;
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "hd setline" +
                            " stats_killstreak "+upmi+" "+getPlaceColor(mi)+" &4"+name+" &8- &c"+ entry.getKey()+" killstreak");
                    mi++;

                } else {
                    break;
                }
            }

            // Set the final lines
            long t2 = System.currentTimeMillis();
            Date d = new Timestamp(t2);
            long diff = t2-t1;
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "hd setline" +
                    " stats_killstreak 12 &7Refreshed at &c"+d+" &7("+diff+"ms)");

            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "hd setline" +
                    " stats_killstreak 13 &eThe leaderboards refresh every 10 minutes.");
        }
    }
}
