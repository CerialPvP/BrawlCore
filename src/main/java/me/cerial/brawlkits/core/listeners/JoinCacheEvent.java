package me.cerial.brawlkits.core.listeners;

import me.cerial.brawlkits.core.utils.PlayerCachingUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * <h1>Join Cache Event</h1>
 * A class which has an event which adds a player to the cache.
 */
public class JoinCacheEvent implements Listener {
    @EventHandler
    public void join(PlayerLoginEvent e) {
        // Add the player to the cache
        PlayerCachingUtils.saveToCache(e.getPlayer(), false);
    }
}
