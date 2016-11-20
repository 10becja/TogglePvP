package me.becja10.TogglePvP.Listeners;

import me.becja10.TogglePvP.TogglePvP;
import me.becja10.TogglePvP.Utils.ColorName;
import me.becja10.TogglePvP.Utils.FileManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener
{
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		final Player p = e.getPlayer();
		//Set the main key as a player UUID
		//config should end up as
		/*
		 * UUID:
		 *   pvp:
		 *   name:
		 */
		String id = e.getPlayer().getUniqueId().toString();
		
		//get the default value for new players
		Boolean def = TogglePvP.getInstance().config_default_pvp; 
		
		//check if there is already an entry for this player, if not, add them to the file
		if (!FileManager.getPlayers().contains(id)) {
			//set default pvp status, as defined in config.yml
			FileManager.getPlayers().set(id+".pvp", def);
		}
		//update their name, in case they've changed it, or if they didn't have one for some reason
		FileManager.getPlayers().set(id+".name", e.getPlayer().getName());
		FileManager.savePlayers();
		
		//add them to the right team (color their name correctly
		Bukkit.getScheduler().scheduleSyncDelayedTask(TogglePvP.getInstance(), new Runnable()
		{public void run()
			{
				//wait a second then color their name
				ColorName.changeColor(p);
				//run through all players on tablist, updating them
				for(Player on : Bukkit.getOnlinePlayers())
				{
					ColorName.updateColor(on);
					ColorName.updateList(on);
				}
			}
		}, 20L);
	}
}