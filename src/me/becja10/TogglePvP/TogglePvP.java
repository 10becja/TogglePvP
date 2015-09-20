package me.becja10.TogglePvP;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import me.becja10.TogglePvP.Events.EntityDamage;
import me.becja10.TogglePvP.Events.PlayerJoin;
import me.becja10.TogglePvP.Events.PlayerMove;
import me.becja10.TogglePvP.Events.SplashPotion;
import me.becja10.TogglePvP.Utils.ColorName;
import me.becja10.TogglePvP.Utils.CommandHelper;
import me.becja10.TogglePvP.Utils.FileManager;
import me.becja10.TogglePvP.Utils.MessageType;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class TogglePvP extends JavaPlugin
{
	private static TogglePvP plugin;
	public final Logger log = Logger.getLogger("Minecraft");
	
	private String configPath;
	private FileConfiguration config;
	private static FileConfiguration outConfig;
	
	public long config_toggle_delay;				//how long before toggle is turned off
	public boolean config_default_pvp;				//pvp turned on by default or not
	public boolean config_nether;					//ignore nether?
	public boolean config_end;						//ignore end?
	public ChatColor config_pvpColor;				//the color for pvp
	public ChatColor config_pveColor;				//color for pve
	public boolean config_changeColor;				//change the color of names?
	public boolean config_show_TAB;					//show dots on tablis
	
	private void loadConfig()
	{
		config_toggle_delay = config.getLong("Toggle Delay", 180);
		config_default_pvp = config.getBoolean("PvP Enabled", true);
		config_nether = config.getBoolean("Ignore Nether", true);
		config_end = config.getBoolean("Ignore End", true);
		config_show_TAB = config.getBoolean("Display on TAB list", true);
		config_changeColor = config.getBoolean("Change name color", true);
				
		String pvpColor = ChatColor.RED.name();
		pvpColor = config.getString("PvP Color", pvpColor);
		config_pvpColor = ChatColor.getByChar(pvpColor);
		if(config_pvpColor == null)
		{
			log.info("ERROR: Color " + pvpColor + " not found. Using default");
			config_pvpColor = ChatColor.RED;
		}

		String pveColor = ChatColor.GREEN.name();
		pveColor = config.getString("PvE Color", pveColor);
		config_pveColor = ChatColor.getByChar(pveColor);
		if(config_pveColor == null)
		{
			log.info("ERROR: Color " + pveColor + " not found. Using default");
			config_pveColor = ChatColor.GREEN;
		}
				
		outConfig.set("Toggle Delay", config_toggle_delay);
		outConfig.set("PvP Enabled", config_default_pvp);
		outConfig.set("Ignore Nether", config_nether);
		outConfig.set("Ignore End", config_end);
		outConfig.set("PvP Color", config_pvpColor.name());
		outConfig.set("PvE Color", config_pveColor.name());
		outConfig.set("Display on TAB list", config_show_TAB);
		outConfig.set("Change name color", config_changeColor);
		
		save();
	}

	public void onEnable()
	{
		plugin = this;
    
		//let the console know that the plugin has been enabled
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " Version "+ pdfFile.getVersion() + " Has Been Enabled!");
		
		//load config
		configPath = plugin.getDataFolder() + File.separator + "config.yml"; //here if breaks
		config = YamlConfiguration.loadConfiguration(new File(configPath));
		outConfig = new YamlConfiguration();
		
		loadConfig();
		FileManager.saveDefaultPlayers();
		new ColorName();

		//register listeners 
		getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
		getServer().getPluginManager().registerEvents(new EntityDamage(), this);
		getServer().getPluginManager().registerEvents(new SplashPotion(), this); 
		getServer().getPluginManager().registerEvents(new PlayerMove(), this);
	}

	public void onDisable() 
	{ 
		//let the console know the plugin has stopped
		PluginDescriptionFile pdfFile = this.getDescription();
		this.log.info(pdfFile.getName() + " Has Been Disabled!");
		plugin = null; 
		
		save();
		FileManager.savePlayers();
	}
  
	public boolean onCommand(CommandSender sender, Command cmd, String cml, String[] args)
	{
		String command = cmd.getName().toLowerCase();
		switch (command)
		{
		case "togglepvpreload":
			if(sender instanceof Player && !sender.hasPermission("togglepvp.admin"))
				sender.sendMessage(MessageType.NO_PERM.getMsg());
			else
			{
				FileManager.reloadPlayers();
				loadConfig();
				System.out.println("[TogglePvP] Reload successful.");
			}
			break;
			
		case "togglepvptime":
			return CommandHelper.togglepvptime(sender, args);
			
		case "togglepvpcheck":
			return CommandHelper.togglepvpcheck(sender, args);
			
		case "togglepvp":
			return CommandHelper.togglepvp(sender, args);
		
		case "togglepvpreset":
			if(sender instanceof Player && !sender.hasPermission("togglepvp.admin"))
				sender.sendMessage(MessageType.NO_PERM.getMsg());
			else
				return CommandHelper.togglepvpreset(sender, args);
			break;
		}
		return true;
	}
	
	private void save()
	{
        try
        {
            outConfig.save(configPath);
        }
        catch(IOException exception)
        {
            log.info("Unable to write to the configuration file at \"" + configPath + "\"");
        }
	}
  
	public static TogglePvP getInstance()
	{
		return plugin;
	}
}