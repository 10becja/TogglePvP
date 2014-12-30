package me.becja10.TogglePvP;

import java.util.logging.Logger;

import me.becja10.TogglePvP.Events.ColorName;
import me.becja10.TogglePvP.Events.EntityDamage;
import me.becja10.TogglePvP.Events.PlayerJoin;
import me.becja10.TogglePvP.Events.SplashPotion;
import me.becja10.TogglePvP.Utils.FileManager;
import me.becja10.TogglePvP.Utils.MessageType;
import me.becja10.TogglePvP.Utils.Toggle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
		//TOGGLEPVP
		if(cmd.getName().equalsIgnoreCase("togglepvp"))
		{
			return togglepvpcommand(sender, args);
		}

		//TOGGLEPVPRESET
		if(cmd.getName().equalsIgnoreCase("togglepvpreset"))
		{		
			if(sender instanceof Player && !sender.hasPermission("togglepvp.admin"))
				sender.sendMessage(MessageType.NO_PERM.getMsg());
			else
				return togglepvpresetcommand(sender, args);
		}
		return true;
	}
	
	private boolean togglepvpcommand(CommandSender sender, String[] args)
	{
		
		
		boolean isPlayer = sender instanceof Player;
		switch (args.length)
		{
			case 0: //player is using it on self
				if(isPlayer && sender.hasPermission("togglepvp.player"))
				{
					Player p = (Player)sender;						
					//check to see if they are in the list of people who used /togglepvp recently
					if (!Toggle.getList().containsKey(p.getName()))
						Toggle.togglePVP(p);
					else
					{
						//if they are, alert them
						int time = getTime(p);
						p.sendMessage(MessageType.PREFIX.getMsg() + 
								ChatColor.GRAY + "You can't use that command for " + 
								ChatColor.AQUA + time + ChatColor.GRAY + getEnding(p));
					}
				}
				else //they aren't a player
					sender.sendMessage(MessageType.PLAYER_ONLY.getMsg());				
				break;
					
			case 1: //trying to do it on someone else
				if(isPlayer && !sender.hasPermission("togglepvp.admin"))
					sender.sendMessage(MessageType.NO_PERM.getMsg());
				else
				{
					@SuppressWarnings("deprecation") //probably a better way to do this with UUID, but too lazy
					Player t = Bukkit.getPlayer(args[0]);
					
					//make sure there is actually a player by that name online.
					if(t == null)
					{
						sender.sendMessage(MessageType.NO_PLAYER.getMsg());
						return true;
					}
					Toggle.togglePVP(t);
					sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Set PvP to " + 
								ChatColor.AQUA + Toggle.isPvPEnabled(t) + ChatColor.GRAY + " for " + ChatColor.AQUA + t.getName());
				} 
				break;
					
			default: //ya done screwed up (too many arguments)
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.DARK_RED + "Incorrect Usage.");
		}
		return true;
	}
	
	private boolean togglepvpresetcommand(CommandSender sender, String[] args)
	{
		//make sure they only type one name
		if (args.length == 1) 
		{
			@SuppressWarnings("deprecation") //probably a better way to do this with UUID, but too lazy
			Player p = Bukkit.getPlayer(args[0]);
			
			//make sure there is actually a player by that name online.
			if(p == null)
				sender.sendMessage(MessageType.NO_PLAYER.getMsg());
			else
				sender.sendMessage(Toggle.reset(p));				
		} 
		//they typed too many names, or something
		else 
		{
			sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.DARK_RED + "Incorrect Usage. Try /togglepvpreset <player>");
		}
		return true;
	}
	
	private String getEnding(Player p) {
		String time = null;
		int t = Toggle.getTimeRemaining(p);
		if (t >= 3600)
			time = " hour(s)!";
		else if (t >= 60)
			time = " minute(s)!";
		else
			time = " seconds!";
		return time;
	}
	
	private int getTime(Player p) {
		int time = Toggle.getTimeRemaining(p);
		if (time >= 3600) //if there are hour(s) left, convert from seconds to hours
			time = time / 3600;
		else if (time >= 60) //minutes
			time = time / 60;
		return time;
	}
  
	public static Main getInstance()
	{
		return plugin;
	}
}