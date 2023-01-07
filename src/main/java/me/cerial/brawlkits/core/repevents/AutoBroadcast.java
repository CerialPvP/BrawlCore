package me.cerial.brawlkits.core.repevents;

import me.cerial.brawlkits.core.Core;
import me.cerial.brawlkits.core.Utils;
import me.cerial.brawlkits.core.datamanagers.ServerDataManager;
import org.bukkit.Bukkit;

import java.util.List;

public class AutoBroadcast implements Runnable {

    private static int currentBC = -1;

    @Override
    public void run() {
        ServerDataManager data = new ServerDataManager(Core.getInstance());

        // Add 1 to currentBC
        currentBC++;

        // Get all broadcasts and check if the currentBC is more than the amount
        List<String> broadcasts = data.getConfig().getStringList("messages");
        if (currentBC > broadcasts.size()-1) {
            currentBC = 0;
        }

        Bukkit.broadcastMessage(Utils.color("&6&lAlerts &8> &r" + broadcasts.get(currentBC)));
    }
}
