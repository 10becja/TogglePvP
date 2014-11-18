package me.becja10.TogglePvP.Utils;

import me.becja10.TogglePvP.Main;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PlayerBoard
{
	private static Scoreboard scoreboard;
	private static final String pvp = "TogglePVP-PVP";
	private static final String pve = "TogglePVP-PVE";

	public PlayerBoard()
	{
		scoreboard = Main.getInstance().getServer().getScoreboardManager().getMainScoreboard();
		
		//set up 2 teams for pvp and pve
		initializeTeam(pvp, ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Enabled Color")));
		initializeTeam(pve, ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Disabled Color")));
	}
	
	/*
	 * Sets up the teams if they haven't been set up already
	 */
	public static void initializeTeam(String teamName, String prefix) 
	{
		if (scoreboard.getTeam(teamName) == null)
			scoreboard.registerNewTeam(teamName).setPrefix(prefix);
	}
	
	/*
	 * Adds the player to the correct team, to properly color their nameplate
	 */
	public static void addPlayerToTeam(Player player) 
	{
		//pvp
		if (Toggle.isPvPEnabled(player)) {
			if (Main.getInstance().getConfig().getBoolean("Do Color Change"))
				((Team)scoreboard.getTeam(pvp)).addPlayer(player);
			player.sendMessage(MessageType.PVP_ENABLED.getMsg());
		} 
		//pve
		else 
		{
			if (Main.getInstance().getConfig().getBoolean("Do Color Change"))
				((Team)scoreboard.getTeam(pve)).addPlayer(player);
			player.sendMessage(MessageType.PVP_DISABLED.getMsg());
		}
	}
}