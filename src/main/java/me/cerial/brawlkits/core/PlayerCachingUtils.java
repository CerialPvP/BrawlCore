package me.cerial.brawlkits.core;

import me.cerial.brawlkits.core.datamanagers.PlayerCacheDataManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class PlayerCachingUtils {

    /**
     * <h1>Save to Cache</h1>
     * Saves a Player to the cache.
     * @param p The player which will be saved
     * @param byPlayer Check if the cache was saved via the command.
     */
    public static void saveToCache(Player p, boolean byPlayer) {
        // Get the data manager
        PlayerCacheDataManager data = new PlayerCacheDataManager(Core.getInstance());

        // Generate a random UUID
        UUID randomUUID = UUID.randomUUID();
        String key = "cached."+randomUUID;

        // Save all the data needed
        data.getConfig().set(key+".username", p.getName());
        data.getConfig().set(key+".uuid", p.getUniqueId());
        data.getConfig().set(key+".last-cached", System.currentTimeMillis());
    }

    /**
     * <h1>Get player via UUID</h1>
     * Simple, get a player from the cache via a UUID
     * @param uuid The UUID you want to find.
     * @return The OfflinePlayer from the cache.
     */
    @Nullable
    public static OfflinePlayer getPlayerViaUUID(@NotNull UUID uuid) {
        // Get the data manager
        PlayerCacheDataManager data = new PlayerCacheDataManager(Core.getInstance());

        // Loop over the values
        for (String entry: data.getConfig().getConfigurationSection("cached").getKeys(false)) {
            // Check if the UUID is the currently looped one and if the cache date is less or equal to 1 hour
            if (data.getConfig().getString("cached."+entry+".uuid").equalsIgnoreCase(uuid.toString()) &&
                    (System.currentTimeMillis() - data.getConfig().getDouble("cached."+entry+".last-cached")) >= 3600 * 1000)
                return Bukkit.getOfflinePlayer(UUID.fromString(data.getConfig().getString("cached."+entry+".uuid")));
        }

        return null;
    }

    /**
     * <h1>Get player via UUID</h1>
     * Simple, get a player from the cache via a UUID
     * @param name The username you want to find.
     * @return The OfflinePlayer from the cache.
     */
    @Nullable
    public static OfflinePlayer getPlayerViaName(@NotNull String name) {
        // Get the data manager
        PlayerCacheDataManager data = new PlayerCacheDataManager(Core.getInstance());

        // Loop over the values
        for (String entry: data.getConfig().getConfigurationSection("cached").getKeys(false)) {
            // Check if the UUID is the currently looped one and if the cache date is less or equal to 1 hour
            if (data.getConfig().getString("cached."+entry+".username").equalsIgnoreCase(name) &&
                    (System.currentTimeMillis() - data.getConfig().getDouble("cached."+entry+".last-cached")) >= 3600 * 1000)
                return Bukkit.getOfflinePlayer(UUID.fromString(data.getConfig().getString("cached."+entry+".uuid")));
        }

        return null;
    }

}
