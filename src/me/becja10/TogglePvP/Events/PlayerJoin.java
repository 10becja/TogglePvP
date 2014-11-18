package me.becja10.TogglePvP.Events;

import me.becja10.TogglePvP.Main;
import me.becja10.TogglePvP.Utils.FileManager;
import me.becja10.TogglePvP.Utils.PlayerBoard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener
{
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		//Set the main key as a player UUID
		//config should end up as
		/*
		 * UUID:
		 *   pvp:
		 *   name:
		 */
		String id = e.getPlayer().getUniqueId().toString();
		
		//get the default value for new players
		Boolean def = Main.getInstance().getConfig().getBoolean("PvP Enabled"); 
		
		//check if there is already an entry for this player, if not, add them to the file
		if (!FileManager.getPlayers().contains(id)) {
			//set default pvp status, as defined in config.yml
			FileManager.getPlayers().set(id+".pvp", def);
		}
		//update their name, in case they've changed it, or if they didn't have one for some reason
		FileManager.getPlayers().set(id+".name", e.getPlayer().getName());
		FileManager.savePlayers();
		
		//add them to the right team (color their name correctly
		PlayerBoard.addPlayerToTeam(e.getPlayer());
	}
}