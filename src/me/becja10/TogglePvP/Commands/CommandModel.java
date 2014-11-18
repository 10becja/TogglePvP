package me.becja10.TogglePvP.Commands;

import me.becja10.TogglePvP.Utils.MessageType;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class CommandModel implements CommandExecutor
{
	private String perms;
	private String usage;

	public CommandModel(String permission, String usage)
	{
		this.perms = permission;
		this.usage = usage;
	}
	
	public abstract boolean onCmd(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString);

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		//checks for permission
		if (!sender.hasPermission(this.perms)) {
			sender.sendMessage(MessageType.NO_PERM.getMsg());
			return false;
		}
		
		//Checks if correct usage or not
		if ((!onCmd(sender, cmd, commandLabel, args)) && 
				(this.usage != null)) {
			sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.DARK_RED + "Incorrect Usage. Try " + this.usage);
		}
		return true;
	}
}