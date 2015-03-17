package me.becja10.TogglePvP.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHelper {
	
	public static boolean togglepvptime(CommandSender sender, String[] args)
	{
		boolean isPlayer = sender instanceof Player;
		switch (args.length)
		{
			case 0: //player is using it on self
				if(isPlayer && sender.hasPermission("togglepvp.player"))
				{
					Player p = (Player)sender;	
					//if they aren't on the list
					if(!Toggle.getList().containsKey(p.getName()))
						p.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "You have no cooldown timer.");
					else
						p.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "You have " 
																  + ChatColor.AQUA + getTime(p)
																  + ChatColor.GRAY + getEnding(p) + " left.");
				}
				else //they aren't a player
					sender.sendMessage(MessageType.PLAYER_ONLY.getMsg());				
				break;
					
			case 1: //trying to do it on someone else
				if(isPlayer && !sender.hasPermission("togglepvp.admin"))
					sender.sendMessage(MessageType.NO_PERM.getMsg());
				else
				{
					switch (args[0])
					{
					case "list": //this is used to get a list of everyone that is on the togglelist
						for(String name : Toggle.getList().keySet())
						{
							sender.sendMessage(name + " " + Toggle.getTimeRemaining(name));
						}
						break;
						
					default: //want to check a player
						Player t = Bukkit.getPlayer(args[0]);
						
						//make sure there is actually a player by that name online.
						if(t == null)
						{
							sender.sendMessage(MessageType.NO_PLAYER.getMsg());
							return true;
						}
						if(!Toggle.getList().containsKey(t.getName()))
							sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + t.getName() + ChatColor.GRAY + " has no cooldown timer.");
						else
							sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + t.getName() + " has " 
																	  + ChatColor.AQUA + getTime(t)
																	  + ChatColor.GRAY + getEnding(t) + " left.");
					}
				}
				break;
					
			default: //ya done screwed up (too many arguments)
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.DARK_RED + "Incorrect Usage.");
		}
		return true;
	}
	
	public static boolean togglepvpcheck(CommandSender sender, String[] args)
	{
		boolean isPlayer = sender instanceof Player;
		switch (args.length)
		{
			case 0: //player is using it on self
				if(isPlayer && sender.hasPermission("togglepvp.player"))
				{
					Player p = (Player)sender;
					//see if they have pvp enabled or disabled
					if(Toggle.isPvPEnabled(p))
						p.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "You are in " + ChatColor.RED + "pvp");
					else
						p.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "You are in " + ChatColor.GREEN + "pve");
				}
				else //they aren't a player
					sender.sendMessage(MessageType.PLAYER_ONLY.getMsg());				
				break;
					
			case 1: //trying to do it on someone else
				if(isPlayer && !sender.hasPermission("togglepvp.check"))
					sender.sendMessage(MessageType.NO_PERM.getMsg());
				else
				{
					Player t = Bukkit.getPlayer(args[0]);
					
					//make sure there is actually a player by that name online.
					if(t == null)
					{
						sender.sendMessage(MessageType.NO_PLAYER.getMsg());
						return true;
					}
					//check target
					if(Toggle.isPvPEnabled(t))
						sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + t.getName() + " is in " + ChatColor.RED + "pvp");
					else
						sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + t.getName() + " is in " + ChatColor.GREEN + "pve");
				} 
				break;
					
			default: //ya done screwed up (too many arguments)
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.DARK_RED + "Incorrect Usage.");
		}
		return true;
	}
	
	public static boolean togglepvpreset(CommandSender sender, String[] args)
	{
		//make sure they only type one name
		if (args.length == 1) 
		{
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
	
	public static boolean togglepvp(CommandSender sender, String[] args)
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
						//for some reason, every once in a while a player will get a negative time
						if(time <= 0)
						{
							Toggle.reset(p); //make sure they aren't in the list
							Toggle.togglePVP(p);
						}
						else //warn them
							p.sendMessage(MessageType.PREFIX.getMsg() + 
								ChatColor.GRAY + "You can't use that command for " + 
								ChatColor.AQUA + time + ChatColor.GRAY + getEnding(p) + "!");
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
	
	private static String getEnding(Player p) {
		String time = null;
		int t = Toggle.getTimeRemaining(p.getName());
		if (t >= 3600)
		{
			int hours = t / 3600; //how many hours
			Integer min = (t - (hours * 3600))/60;// left over minutes
			time = " hour(s) " + ChatColor.AQUA + min.toString() + ChatColor.GRAY + " minutes";
		}
		else if (t >= 60)
			time = " minute(s)";
		else
			time = " seconds";
		return time;
	}
	
	private static int getTime(Player p) {
		int time = Toggle.getTimeRemaining(p.getName());
		if (time >= 3600) //if there are hour(s) left, convert from seconds to hours
			time = time / 3600;
		else if (time >= 60) //minutes
			time = time / 60;
		return time;
	}
}
