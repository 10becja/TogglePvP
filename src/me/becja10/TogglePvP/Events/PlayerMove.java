package me.becja10.TogglePvP.Events;

import me.becja10.TogglePvP.TogglePvP;
import me.becja10.TogglePvP.Utils.ColorName;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerMove implements Listener
{
	private Scoreboard scoreboard;
	
	public PlayerMove()
	{
		scoreboard = TogglePvP.getInstance().getServer().getScoreboardManager().getMainScoreboard();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent event)
	{
		Player p = event.getPlayer();		
		for(PotionEffect effect : p.getActivePotionEffects())
		{
			if(effect.getType().equals(PotionEffectType.INVISIBILITY))
			{
				if(!scoreboard.getTeam(ColorName.invisible).hasPlayer(p))
					scoreboard.getTeam(ColorName.invisible).addPlayer(p);
				return;
			}
		}
		if(scoreboard.getPlayerTeam(p) == null) return;
		if(scoreboard.getPlayerTeam(p).getName().equals(ColorName.invisible))
		{
			//re-add them to the correct team
			ColorName.updateColor(p);
		}
	}
}
