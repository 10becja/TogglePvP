package me.becja10.TogglePvP.Commands;

import me.becja10.TogglePvP.Utils.MessageType;
import me.becja10.TogglePvP.Utils.Toggle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTogglePlayer extends CommandModel
{
	public CommandTogglePlayer()
	{
		super("togglepvp.player", "/pvptoggle");
	}
	
	/*
	 * Logic for pvptoggle command
	 */
	public boolean onCmd(CommandSender sender, Command cmd, String cml, String[] args)
	{
		//make sure command was not run from console or some such nonsense
		if(!(sender instanceof Player))
		{
			sender.sendMessage(MessageType.PLAYER_ONLY.getMsg());
			return true;
		}
		
		Player p = (Player)sender;
		if (cml.equalsIgnoreCase("pvptoggle")) {
			
			//make sure they don't type anything behind the command
			if (args.length == 0) {
				//check to see if they are in the list of people who used /pvptoggle recently
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
			//they screwed the command up
			else {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Helper function that returns either hours, minutes, or seconds.
	 */
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
}