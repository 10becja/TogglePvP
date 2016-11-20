package me.becja10.TogglePvP.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PvpToggleEvent extends Event{
	
	private static final HandlerList handlers = new HandlerList();

	private Player player;
	private boolean pvpState;
	
	public PvpToggleEvent(Player p, boolean state){
		player = p;
		pvpState = state;
	}
	
	/**
	 * Gets the player involved in the event
	 * @return The player who had their pvp toggled.
	 */
	public Player getPlayer(){
		return player;
	}
	
	/**
	 * Gets the state of the player
	 * @return True if PvP has been toggled on, false if toggled off
	 */
	public boolean isPvpOn(){
		return pvpState;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}

}
