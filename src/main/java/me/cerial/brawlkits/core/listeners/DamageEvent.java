package me.cerial.brawlkits.core.listeners;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.ServerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEvent implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        ServerDataManager data = new ServerDataManager(Core.getInstance());

        // Attacker -> Frozen Person
        if (e.getEntity() instanceof Player p) {
            if (data.getConfig().getBoolean("players."+p.getUniqueId()+".frozen")) {
                e.setCancelled(true);
                Utils.message((Player) e.getDamager(), "&cThat player is frozen.");
            }
        }

        // Frozen Person -> Attacker
        if (e.getDamager() instanceof Player p) {
            if (data.getConfig().getBoolean("players."+p.getUniqueId()+".frozen")) {
                e.setCancelled(true);
                Utils.message(p, "&cYou are currently frozen. Please follow the instruction sent in chat.");
            }
        }
    }
}
