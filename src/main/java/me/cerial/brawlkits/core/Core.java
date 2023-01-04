package me.cerial.brawlkits.core;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public final class Core extends JavaPlugin {

    public static Logger logger = getInstance().getLogger();
    public static Core instance = null;

    public static Core getInstance() {
        return instance;
    }

    public static Economy econ;

    public boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
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
        logger.info("Registering commands...");

        // Commands go here

        logger.info("Registered all commands successfully.");


        // Register looping events
        logger.info("Registering looping events...");
        logger.info("Registered all looping events successfully.");

        logger.info("Plugin finished loading.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }
}
