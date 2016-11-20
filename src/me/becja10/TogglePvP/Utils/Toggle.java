package me.becja10.TogglePvP.Utils;

import java.util.HashMap;
import java.util.Map;

import me.becja10.TogglePvP.TogglePvP;
import me.becja10.TogglePvP.Events.PvpToggleEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Toggle
{
	//list of players who have recently toggled pvp
	private static Map<String, Long> toggled = new HashMap<String, Long>();
	
	/*
	 * Toggles the pvp status of a player
	 */
	public static void togglePVP(final Player player)
	{
		FileConfiguration c = FileManager.getPlayers();
		
		//if they have pvp enabled, change to pve, and update UUID.pvp in players.yml
		if (isPvPEnabled(player))
			c.set(player.getUniqueId().toString()+".pvp", false);
		else 
			c.set(player.getUniqueId().toString()+".pvp", true);
		
		//store the map the current time to the players name, so they know how much longer to wait
		toggled.put(player.getName(), System.currentTimeMillis());
		FileManager.savePlayers();
		
		//fire event so other plugins can pick it up
		PvpToggleEvent event = new PvpToggleEvent(player, isPvPEnabled(player));
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		//add the player to the right team, this way their name changes
		ColorName.changeColor(player);

		//schedule an event to remove the player from the list of players who have recently changed state
		//this way they can change again if they want
		Bukkit.getScheduler().scheduleSyncDelayedTask(TogglePvP.getInstance(), new Runnable()
		{
			public void run()
			{
				Toggle.toggled.remove(player.getName());
				player.sendMessage(MessageType.CAN_TOGGLE.getMsg());
			}
		}
		, 20L * TogglePvP.getInstance().config_toggle_delay);
	}
	
	/*
	 * Checks if the player has pvp enabled or not
	 */
	public static boolean isPvPEnabled(Player player) {
		return FileManager.getPlayers().getBoolean(player.getUniqueId().toString()+".pvp");
	}

	public static Map<String, Long> getList() {
		return toggled;
	}
	
	/*
	 * Get the time remaining for the player
	 */
	public static int getTimeRemaining(String name) {
		//compare time stored when player used command, and current time
		long time =  System.currentTimeMillis() - toggled.get(name);
		return (int)(TogglePvP.getInstance().config_toggle_delay - time/1000);
	}

	/*
	 * Resets a players cooldown timer
	 */
	public static String reset(Player p) {
		if (Toggle.getList().containsKey(p.getName()))
		{
			toggled.remove(p.getName());
			//let the player know they can toggle again
			p.sendMessage(MessageType.CAN_TOGGLE.getMsg());
			return MessageType.RESET.getMsg() + " for " + ChatColor.AQUA + p.getName();
		}
		return MessageType.PREFIX.getMsg() + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " has no cooldown timer";
	}
}