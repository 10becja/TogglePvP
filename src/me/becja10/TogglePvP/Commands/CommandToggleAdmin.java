package me.becja10.TogglePvP.Commands;

import me.becja10.TogglePvP.Utils.MessageType;
import me.becja10.TogglePvP.Utils.Toggle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandToggleAdmin extends CommandModel
{
	public CommandToggleAdmin()
	{
		super("togglepvp.admin", "/pvpadmin <player>");
	}

	public boolean onCmd(CommandSender sender, Command cmd, String cml, String[] args)
	{
		if (cml.equalsIgnoreCase("pvpadmin")) 
		{
			//make sure they only type one name
			if (args.length == 1) 
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
			else {
				return false;
			}
		}
		return true;
	}
}