package me.becja10.TogglePvP;

import java.util.logging.Logger;

import me.becja10.TogglePvP.Commands.CommandResetTimer;
import me.becja10.TogglePvP.Commands.CommandToggleAdmin;
import me.becja10.TogglePvP.Commands.CommandTogglePlayer;
import me.becja10.TogglePvP.Events.EntityDamage;
import me.becja10.TogglePvP.Events.PlayerJoin;
import me.becja10.TogglePvP.Events.SplashPotion;
import me.becja10.TogglePvP.Utils.FileManager;
import me.becja10.TogglePvP.Utils.PlayerBoard;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
  private static Main plugin;
  public final Logger log = Logger.getLogger("Minecraft");

  public void onEnable()
  {
    plugin = this;
    
    //let the console know that the plugin has been enabled
    PluginDescriptionFile pdfFile = this.getDescription();
	log.info(pdfFile.getName() + " Version "+ pdfFile.getVersion() + " Has Been Enabled!");
	
    //set up some teams so that we can color nameplates
    new PlayerBoard();

    //register listeners 
    getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
    getServer().getPluginManager().registerEvents(new EntityDamage(), this);
    getServer().getPluginManager().registerEvents(new SplashPotion(), this);    

    //set command executors
    getCommand("pvptoggle").setExecutor(new CommandTogglePlayer());
    getCommand("pvpadmin").setExecutor(new CommandToggleAdmin());
    getCommand("pvpreset").setExecutor(new CommandResetTimer());

    //yeah...
    saveDefaultConfig();

    FileManager.saveDefaultPlayers();
  }

  public void onDisable() 
  { 
	  //let the console know the plugin has stopped
	  PluginDescriptionFile pdfFile = this.getDescription();
	  this.log.info(pdfFile.getName() + " Has Been Disabled!");
	  plugin = null; 
  }

  public static Main getInstance()
  {
    return plugin;
  }
}