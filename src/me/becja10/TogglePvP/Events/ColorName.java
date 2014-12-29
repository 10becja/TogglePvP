package me.becja10.TogglePvP.Events;

import me.becja10.TogglePvP.Utils.MessageType;
import me.becja10.TogglePvP.Utils.Toggle;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;
import org.kitteh.tag.TagAPI;

public class ColorName implements Listener
{
	private static ChatColor pvpColor;
	private static ChatColor pveColor;
	
	public ColorName()
	{
		pvpColor = ChatColor.RED; 
		pveColor = ChatColor.GREEN;
		
	}
	@EventHandler
	public void onNameTag(AsyncPlayerReceiveNameTagEvent event) 
	{
		Player player = event.getNamedPlayer();
		if (Toggle.isPvPEnabled(player)) //if they are pvp
			event.setTag(pvpColor + player.getName());
		else //they are pve
			event.setTag(pveColor + player.getName());
	}
	
	
	/*
	 * Takes care of refreshing the player's name, as well as in Tablist
	 */
	public static void changeColor(Player p)
	{
		//refresh their tag
		TagAPI.refreshPlayer(p);
		
		//change their TAB list color
		if (Toggle.isPvPEnabled(p)) //if they are pvp
		{
			p.setPlayerListName(pvpColor + p.getName());
			p.sendMessage(MessageType.PVP_ENABLED.getMsg());
		}
		else //they are pve
		{
			p.setPlayerListName(pveColor + p.getName());
			p.sendMessage(MessageType.PVP_DISABLED.getMsg());
		}
	}
}
