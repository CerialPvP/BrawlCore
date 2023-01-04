package me.cerial.brawlkits.core.listeners;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.ServerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinMessageEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        // Check if the player has played before
        if (p.hasPlayedBefore()) {
            e.setJoinMessage(Utils.color("&7[&a&7] &a"+p.getName()+" &7has joined the server."));
        } else {
            ServerDataManager sdm = new ServerDataManager(Core.getInstance());
            int joins = sdm.getConfig().getInt("joins");
            sdm.getConfig().set("joins", joins + 1);
            sdm.saveConfig();
            e.setJoinMessage("&7[&b+&7] &b"+p.getName()+" &7has joined the server for the first time. (#"+(joins+1)+")");
        }
    }
}
