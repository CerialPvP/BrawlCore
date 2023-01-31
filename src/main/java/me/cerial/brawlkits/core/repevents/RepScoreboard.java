package me.cerial.brawlkits.core.repevents;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.StatsDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class RepScoreboard implements Runnable {
    private void applyBoard(Player p) {
        // Initialize data manager(s)
        StatsDataManager sdata = new StatsDataManager(Core.getInstance());

        // Initialize scoreboard variables
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("board-1", "dummy", Utils.color("&4&lBrawl&c&lKits &8| &cS1"));

        // Initialize other variables
        DecimalFormat df = new DecimalFormat("0.00");

        // Initialize HashMap of the scores
        HashMap<Integer, String> scores = new HashMap<>();

        scores.put(15, "");
        scores.put(14, "&c&lPlayer");
        scores.put(13, "&7&l\u27A5 &4&lUsername: &c"+p.getName());
        scores.put(12, "&7&l\u27A5 &4&lKills: &c"+sdata.getConfig().getInt(p.getUniqueId() + ".kills"));
        scores.put(11, "&7&l\u27A5 &4&lDeaths: &c"+sdata.getConfig().getInt(p.getUniqueId() + ".deaths"));
        scores.put(10, "&7&l\u27A5 &4&lKDR: &c"+sdata.getConfig().getInt(p.getUniqueId() + ".kills")/sdata.getConfig().getInt(p.getUniqueId() + ".deaths"));
        scores.put(9, "&7&l\u27A5 &4&lBalance: &c$"+Core.getEcon().getBalance(p));
        scores.put(8, "");
        scores.put(7, "&c&lServer");
        scores.put(6, "&7&l\u27A5 &4&lOnline: &c"+Bukkit.getServer().getOnlinePlayers().size()+"/"+Bukkit.getServer().getMaxPlayers());
        scores.put(5, "&7&l\u27A5 &4&lTPS: &c"+Float.parseFloat(df.format((float) ((TPSUtil.getTPS()*100D)/100D))));
        scores.put(4, "");
        scores.put(3, "&7&oBrawlKits.minehut.gg");

        // Put all the scores from the HashMap into the board and register that board for the player
        for (Map.Entry<Integer, String> entry: scores.entrySet()) {
            obj.getScore(Utils.color(entry.getValue())).setScore(entry.getKey());
        }

        p.setScoreboard(board);
    }

    @Override
    public void run() {
        // Run async task
        Bukkit.getServer().getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
            // Get all the players and apply the scoreboard
            for (Player pl: Bukkit.getServer().getOnlinePlayers()) {
                applyBoard(pl);
            }
        });
    }
}
