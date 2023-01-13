package me.cerial.brawlkits.core.listeners;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.datamanagers.ServerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        ServerDataManager data = new ServerDataManager(Core.getInstance());
        Player p = e.getPlayer();


        if (data.getConfig().getBoolean("players."+p.getUniqueId()+".frozen")) {
            e.setCancelled(true);
        }
    }
}
