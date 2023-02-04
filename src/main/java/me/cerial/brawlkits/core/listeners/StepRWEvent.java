package me.cerial.brawlkits.core.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class StepRWEvent implements Listener {
    @EventHandler
    public void step(PlayerMoveEvent e) {
        if (e
                .getTo()
                .getBlock()
                .getRelative(BlockFace.DOWN)
                .getType() == Material.RED_WOOL) {

            // Push the player
            Player p = e.getPlayer();
            p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 100.0f, 1.4f);
            p.setVelocity(p.getLocation().getDirection().multiply(3).multiply(1.5));
        }
    }
}
