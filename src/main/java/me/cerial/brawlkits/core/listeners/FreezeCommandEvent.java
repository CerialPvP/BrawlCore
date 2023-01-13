package me.cerial.brawlkits.core.listeners;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.ServerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class FreezeCommandEvent implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        ServerDataManager data = new ServerDataManager(Core.getInstance());
        if (data.getConfig().getBoolean("players."+e.getPlayer().getUniqueId()+".frozen")) {
            if (!e.getMessage().contains("discord")) {
                e.setCancelled(true);
                Utils.message(e.getPlayer(), "&cYou may only run the /discord command while being frozen.");
            }
        }
    }
}
