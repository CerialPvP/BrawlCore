package me.cerial.brawlkits.core.repevents;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.StatsDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class RepScoreboard implements Runnable {
    private void applyBoard(Player p) {
        // Initialize data manager(s)
        StatsDataManager sdata = new StatsDataManager(Core.getInstance());

        // Initialize scoreboard variables
        Scoreboard board = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        Objective obj = board.registerNewObjective("board-1", "dummy", Utils.color("&4&lBrawl&c&lKits &8| &cS1"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Initialize other variables
        DecimalFormat df = new DecimalFormat("0.00");

        // Initialize HashMap of the scores
        Map<Integer, String> scores = new TreeMap<>(Collections.reverseOrder());

        scores.put(15, "&1");
        scores.put(14, "&c&lPlayer");
        scores.put(13, "&7&l\u279C &4&lUsername: &c"+p.getName());
        scores.put(12, "&7&l\u279C &4&lKills: &c"+sdata.getConfig().getInt(p.getUniqueId() + ".kills"));
        scores.put(11, "&7&l\u279C &4&lDeaths: &c"+sdata.getConfig().getInt(p.getUniqueId() + ".deaths"));
        scores.put(10, "&7&l\u279C &4&lKDR: &c"+sdata.getConfig().getInt(p.getUniqueId() + ".kills")/sdata.getConfig().getInt(p.getUniqueId() + ".deaths"));
        scores.put(9, "&7&l\u279C &4&lBalance: &c$"+Core.getEcon().getBalance(p));
        scores.put(8, "&2");
        scores.put(7, "&c&lServer");
        scores.put(6, "&7&l\u279C &4&lOnline: &c"+Bukkit.getServer().getOnlinePlayers().size()+"/"+Bukkit.getServer().getMaxPlayers());
        scores.put(5, "&7&l\u279C &4&lTPS: &c"+Float.parseFloat(df.format((float) ((TPSUtil.getTPS()*100D)/100D))));
        scores.put(4, "&3");
        scores.put(3, "&7&oBrawlKits.minehut.gg");

        // Put all the scores from the HashMap into the board and register that board for the player
        for (Map.Entry<Integer, String> entry: scores.entrySet()) {
            Score ls = obj.getScore(Utils.color(entry.getValue()));
            ls.setScore(entry.getKey());
        }

        p.setScoreboard(board);
    }

    @Override
    public void run() {
        // Get all the players and apply the scoreboard (sadly not async since yeah)
        for (Player pl: Bukkit.getServer().getOnlinePlayers()) {
            applyBoard(pl);
        }
    }
}
