package me.cerial.brawlkits.core.commands;

import me.cerial.brawlkits.core.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DiscordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // -- /discord --
        if (command.getName().equalsIgnoreCase("discord")) {
            TextComponent tcomponent = new TextComponent(Utils.color("&4&lBrawl&c&lKits &8> &aClick on this message to join our Discord."));
            tcomponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/Y8fHW2dshD"));
            sender.spigot().sendMessage(tcomponent);
        }

        return true;
    }
}
