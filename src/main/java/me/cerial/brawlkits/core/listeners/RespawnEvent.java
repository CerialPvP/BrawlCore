package me.cerial.brawlkits.core.listeners;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.datamanagers.ServerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnEvent implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Bukkit.broadcastMessage("event executed");
        // Teleport that player to spawn
        ServerDataManager data = new ServerDataManager(Core.getInstance());
        Location location = new Location(
                Bukkit.getServer().getWorld(data.getConfig().getString("spawn.world")),
                data.getConfig().getDouble("spawn.x"),
                data.getConfig().getDouble("spawn.y"),
                data.getConfig().getDouble("spawn.z")
        );
        location.setPitch((float) data.getConfig().getDouble("spawn.pitch"));
        location.setYaw((float) data.getConfig().getDouble("spawn.yaw"));

        e.getPlayer().teleport(location);
    }
}
