package me.cerial.brawlkits.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftEvent implements Listener {
    @EventHandler
    public void onCraft(CraftItemEvent e) {
        e.setCancelled(true);
    }
}
