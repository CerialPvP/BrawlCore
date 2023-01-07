package me.cerial.brawlkits.core.listeners;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.datamanagers.StatsDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        StatsDataManager data = new StatsDataManager(Core.getInstance());

        // Victim stuff
        Player victim = e.getEntity();
        int deaths = data.getConfig().getInt(victim.getUniqueId() + ".deaths");
        data.getConfig().set(victim.getUniqueId() + ".deaths", deaths + 1);

        // Attacker stuff
        Player attacker = e.getEntity().getKiller();
        if (attacker != null) {
            int kills = data.getConfig().getInt(attacker.getUniqueId() + ".kills");
            data.getConfig().set(victim.getUniqueId() + ".kills", kills + 1);
            data.saveConfig();
        }



    }
}
