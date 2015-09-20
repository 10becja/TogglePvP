package me.becja10.TogglePvP.Utils;

import me.becja10.TogglePvP.TogglePvP;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ColorName implements Listener
{	
	private static Scoreboard scoreboard;
	public static final String pvp = "TogglePVP-PVP";
	public static final String pve = "TogglePVP-PVE";
	public static final String invisible = "TogglePVP-INV";
	public final static String dot = "\u2588";

	public ColorName()
	{
		scoreboard = TogglePvP.getInstance().getServer().getScoreboardManager().getMainScoreboard();

		//clear teams
		for(Team team : scoreboard.getTeams())
		{
			if(team.getName() == pvp || team.getName() == pve)
				team.unregister();
		}
		//set up 2 teams for pvp and pve
		initializeTeam(pvp, TogglePvP.getInstance().config_pvpColor+"");
		initializeTeam(pve, TogglePvP.getInstance().config_pveColor+"");
		initializeTeam(invisible, "");
	}

	/*
	 * Sets up the teams if they haven't been set up already
	 */
	public static void initializeTeam(String teamName, String prefix) 
	{
		if (scoreboard.getTeam(teamName) == null)
		{
			scoreboard.registerNewTeam(teamName).setPrefix(prefix);
			scoreboard.getTeam(teamName).setCanSeeFriendlyInvisibles(false);
			scoreboard.getTeam(teamName).setAllowFriendlyFire(true);
			if(teamName == invisible)
				scoreboard.getTeam(teamName).setNameTagVisibility(NameTagVisibility.NEVER);
		}
	}
	
	
	/*
	 * Takes care of refreshing the player's name, as well as in Tablist
	 */
	@SuppressWarnings("deprecation")
	public static void changeColor(Player p)
	{
		//refresh their tag
		String name = p.getDisplayName();
		
		//change their TAB list color and add them to the correct team
		if (Toggle.isPvPEnabled(p)) //if they are pvp
		{
			if(TogglePvP.getInstance().config_changeColor)
			{
				scoreboard.getTeam(pvp).addPlayer(p);
				name = TogglePvP.getInstance().config_pvpColor + dot + " " + ChatColor.RESET + p.getDisplayName();
			}
			p.sendMessage(MessageType.PVP_ENABLED.getMsg());
		}
		else //they are pve
		{
			if(TogglePvP.getInstance().config_changeColor)
			{
				scoreboard.getTeam(pve).addPlayer(p);
				name = TogglePvP.getInstance().config_pveColor + dot + " " + ChatColor.RESET + p.getDisplayName();
			}
			p.sendMessage(MessageType.PVP_DISABLED.getMsg());
		}
		p.setPlayerListName(name);
	}
	
	/*
	 * Update the players name, mostly used from other classes
	 */
	@SuppressWarnings("deprecation")
	public static void updateColor(Player p)
	{
		if (Toggle.isPvPEnabled(p)) //if they are pvp
		{
			if(TogglePvP.getInstance().config_changeColor)
			{
				scoreboard.getTeam(pvp).addPlayer(p);
			}
		}
		else //they are pve
		{
			if(TogglePvP.getInstance().config_changeColor)
			{
				scoreboard.getTeam(pve).addPlayer(p);
			}
		}
	}
	
	public static void updateList(Player p)
	{
		String name = p.getDisplayName();
		if (Toggle.isPvPEnabled(p)) //if they are pvp
		{
			if(TogglePvP.getInstance().config_changeColor)
			{
				name = TogglePvP.getInstance().config_pvpColor + dot + " " + ChatColor.RESET + p.getDisplayName();
			}
		}
		
		else //they are pve
		{
			if(TogglePvP.getInstance().config_changeColor)
			{
				name = TogglePvP.getInstance().config_pveColor + dot + " " + ChatColor.RESET + p.getDisplayName();
			}
		}
		p.setPlayerListName(name);
		
	}
}
