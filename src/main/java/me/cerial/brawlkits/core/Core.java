package me.cerial.brawlkits.core;

import me.cerial.brawlkits.core.commands.*;
import me.cerial.brawlkits.core.repevents.AutoBroadcast;
import me.cerial.brawlkits.core.repevents.RepScoreboard;
import me.cerial.brawlkits.core.repevents.TPSUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public final class Core extends JavaPlugin {

    public static Logger logger = null;
    public static Core instance = null;

    public static Core getInstance() {
        return instance;
    }

    public static Economy econ;

    public static boolean papi_installed = false;

    public static boolean getPapiInstalled() {
        return papi_installed;
    }

    public boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEcon() {
        return econ;
    }

    // In-plugin configuration
    private boolean whitelistonstartup = true;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logger = this.getLogger();
        logger.info("Starting plugin...");
        instance = this;

        // Register Vault economy
        logger.info("Registering Vault economy...");
        if (!setupEconomy()) {
            logger.severe("Couldn't register Vault economy! Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            logger.info("Registered Vault economy successfully.");
        }

        // Register events
        logger.info("Registering listeners...");
        String packageName = getClass().getPackage().getName();

        for (Class<?> clazz : new Reflections(packageName + ".listeners")
                .getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                Bukkit.getServer().getPluginManager().registerEvents(listener, this);
                logger.info("Registering listener " + listener.getClass().getName() + "...");
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                logger.warning("An error occurred whilst registering a listener:");
                e.printStackTrace();
            }
        }

        logger.info("Listener registration finished.");

        // Register commands
        logger.info("Registering commands (without tab completion)...");

        // my attempt at automatic command registration

        for (Class<?> clazz : new Reflections(packageName + ".commands").getSubTypesOf(CommandExecutor.class)) {
            try {
                CommandExecutor listener = (CommandExecutor) clazz.getDeclaredConstructor().newInstance();
                // Get the name
                String cmdName = ((listener.getClass().getSimpleName()).replace("Command", "")).toLowerCase();

                Utils.addCommand(listener, cmdName);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                logger.warning("An error occurred whilst registering a command:");
                e.printStackTrace();
            }
        }

        logger.info("Registered all commands (without tab completion) successfully.");

        // Register commands with tab completion
        logger.info("Registering commands (with tab completion)...");

        for (Class<?> clazz : new Reflections(packageName + ".commands").getSubTypesOf(TabExecutor.class)) {
            try {
                TabExecutor listener = (TabExecutor) clazz.getDeclaredConstructor().newInstance();
                // Get the name
                String cmdName = ((listener.getClass().getSimpleName()).replace("Command", "")).toLowerCase();

                Utils.addCommand(listener, cmdName);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                logger.warning("An error occurred whilst registering a command:");
                e.printStackTrace();
            }
        }

        logger.info("Registered all commands (with tab completion) successfully.");


        // Disable Minehut Cosmetics (might be against TOS, if MH is against it, I'll remove it!)
        logger.info("Disabling MinehutCosmetics...");
        PluginManager pm = Bukkit.getPluginManager();
        if (pm.isPluginEnabled("MinehutCosmetics")) {
            pm.disablePlugin(pm.getPlugin("MinehutCosmetics"));
            logger.info("Disabled MinehutCosmetics successfully.");
        } else {
            logger.warning("Couldn't find MinehutCosmetics!");
        }

        // Check if PlaceholderAPI is installed
        logger.info("Checking PlaceholderAPI status...");
        if (pm.isPluginEnabled("PlaceholderAPI")) {
            papi_installed = true;
            logger.info("PlaceholderAPI installed and enabled.");
        } else {
            logger.warning("PlaceholderAPI not installed or not enabled! Disabling plugin...");
            pm.disablePlugin(this);
        }

        // Enable whitelist if in-plugin config says to
        if (whitelistonstartup) {
            logger.info("Enabling whitelist...");
            Bukkit.getServer().setWhitelist(true);
        } else {
            logger.info("Disabling whitelist...");
            Bukkit.getServer().setWhitelist(false);
        }

        // Register looping events
        logger.info("Registering looping events...");
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new AutoBroadcast(), 0L, 6000L);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TPSUtil(), 0L, 20L);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new RepScoreboard(), 0L, 10L);
        logger.info("Registered all looping events successfully.");

        logger.info("Plugin finished loading.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
        logger = null;
        econ = null;
    }
}
