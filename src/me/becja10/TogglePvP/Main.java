package me.becja10.TogglePvP;

import java.util.logging.Logger;

import me.becja10.TogglePvP.Events.ColorName;
import me.becja10.TogglePvP.Events.EntityDamage;
import me.becja10.TogglePvP.Events.PlayerJoin;
import me.becja10.TogglePvP.Events.SplashPotion;
import me.becja10.TogglePvP.Utils.CommandHelper;
import me.becja10.TogglePvP.Utils.FileManager;
import me.becja10.TogglePvP.Utils.MessageType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

		//register listeners 
		getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
		getServer().getPluginManager().registerEvents(new EntityDamage(), this);
		getServer().getPluginManager().registerEvents(new SplashPotion(), this); 
		getServer().getPluginManager().registerEvents(new ColorName(), this);
		
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
  
	public boolean onCommand(CommandSender sender, Command cmd, String cml, String[] args)
	{
		String command = cmd.getName().toLowerCase();
		switch (command)
		{
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
  
	public static Main getInstance()
	{
		return plugin;
	}
}