package me.cerial.brawlkits.core.listeners;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.StatsDataManager;
import org.bukkit.OfflinePlayer;
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

            // Give the attacker some cash
            int money = Utils.randomNumber(5, 20);
            Core.getEcon().depositPlayer(attacker, money);
            Utils.message(attacker, "&7You have been given &a$"+money+" &7for killing &4"+victim.getName()+"&7.");
        }
    }
}
