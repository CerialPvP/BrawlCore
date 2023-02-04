package me.cerial.brawlkits.core.repevents;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.StatsDataManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class RepScoreboard implements Runnable {

    private void setBoard(Player p) {
        // Get everything needed
        StatsDataManager sdata = new StatsDataManager(Core.getInstance());
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("board-1", "dummy", Utils.color("&4&lBrawlKits &8| &cS1"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Make the TreeMap
        Map<Integer, String> scores = new TreeMap<>(Collections.reverseOrder());
        scores.put(15, "&1");
        scores.put(14, "&c&lPlayer");
        scores.put(13, "&7&l\u279C &4&lUsername: &c"+p.getName());
        scores.put(12, "&7&l\u279C &4&lKills: &c"+sdata.getConfig().getInt(p.getUniqueId() + ".kills"));
        scores.put(11, "&7&l\u279C &4&lDeaths: &c"+sdata.getConfig().getInt(p.getUniqueId() + ".deaths"));
        scores.put(10, "&7&l\u279C &4&lStreak: &c"+sdata.getConfig().getInt(p.getUniqueId() + ".killstreak"));
        scores.put(9, "&7&l\u279C &4&lBounty: &c$"+sdata.getConfig().getInt(p.getUniqueId() + ".bounty"));
        scores.put(8, "&7&l\u279C &4&lKDR: &c"+sdata.getConfig().getInt(p.getUniqueId() + ".kills")/sdata.getConfig().getInt(p.getUniqueId() + ".deaths"));
        scores.put(7, "&7&l\u279C &4&lBalance: &c$"+Core.getEcon().getBalance(p));
        scores.put(6, "&2");
        scores.put(5, "&c&lServer");
        scores.put(4, "&7&l\u279C &4&lOnline: &c"+Bukkit.getServer().getOnlinePlayers().size()+"/"+Bukkit.getServer().getMaxPlayers());
        scores.put(3, "&7&l\u279C &4&lTPS: &c"+ PlaceholderAPI.setPlaceholders(null, "%server_tps_1%"));
        scores.put(2, "&3");
        scores.put(1, "&7&oBrawlKits.minehut.gg");

        // Make the TreeMap into real scores
        for (Map.Entry<Integer, String> entry: scores.entrySet()) {
            // Add that as a score
            obj.getScore(Utils.color(entry.getValue())).setScore(entry.getKey());
        }

        // Set the scoreboard
        p.setScoreboard(board);
    }

    @Override
    public void run() {
        // Loop through all the online players and set the scoreboard
        for (Player pl: Bukkit.getServer().getOnlinePlayers()) {
            setBoard(pl);
        }
    }
}
