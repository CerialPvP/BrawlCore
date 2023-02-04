package me.cerial.brawlkits.core.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class StepWaterEvent implements Listener {
    @EventHandler
    public void step(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        // 133
        if (e
                .getTo()
                .getBlock()
                .getRelative(BlockFace.DOWN)
                .getType() == Material.WATER ||
                    p.getLocation().getBlockY() <= 133) {

            // Set the hunger level of the player to 3
            p.setFoodLevel(3);
        } else {
            p.setFoodLevel(20);
        }
    }
}
