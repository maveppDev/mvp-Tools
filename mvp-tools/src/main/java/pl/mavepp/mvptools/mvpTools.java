package pl.mavepp.mvptools;

import org.bukkit.plugin.java.JavaPlugin;
import pl.mavepp.mvptools.managers.ConfigManager;

/**
 *
 * @author maveppDev Copyright 2014 | maveppDev.net/Bukkit
 *
 */
public class mvpTools extends JavaPlugin {

    @Override
    public void onEnable() {
        //------------------------------
        /*
         // All moduls for start on enable:
         */
        ConfigManager.loadAll();
        ConfigManager.registerConfig("config", "config.yml", this);
        ConfigManager.registerConfig("players", "players.yml", this);
        ConfigManager.registerConfig("mysql", "mysql.yml", this);
        //------------------------------
    }

    @Override
    public void onDisable() {
        //------------------------------
        /*
         // All moduls for start on disable:
         */
        ConfigManager.saveAll();
        //------------------------------
    }

}
