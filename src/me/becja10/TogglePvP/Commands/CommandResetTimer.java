package me.becja10.TogglePvP.Commands;

import me.becja10.TogglePvP.Utils.MessageType;
import me.becja10.TogglePvP.Utils.Toggle;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandResetTimer extends CommandModel
{
	public CommandResetTimer() 
	{
		super("togglepvp.admin", "/togglepvpreset <player>");
	}
	
	public boolean onCmd(CommandSender sender, Command cmd, String cml, String[] args)
	{
		if (cml.equalsIgnoreCase("togglepvpreset")) 
		{
			//make sure they only type one name
			if (args.length == 1) 
			{
				@SuppressWarnings("deprecation") //probably a better way to do this with UUID, but too lazy
				Player p = Bukkit.getPlayer(args[0]);
				
				//make sure there is actually a player by that name online.
				if(p == null)
				{
					sender.sendMessage(MessageType.NO_PLAYER.getMsg());
					return true;
				}
				//check if they have a cooldown timer
				sender.sendMessage(Toggle.reset(p));				
			} 
			//they typed too many names, or something
			else {
				return false;
			}
		}
		return true;
	}
}
