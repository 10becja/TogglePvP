package me.becja10.TogglePvP.Events;

import me.becja10.TogglePvP.Utils.Toggle;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
//import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SplashPotion implements Listener 
{
	/*
	 * This nullifies nasty potions
	 */
	@EventHandler
	public void onSplash(PotionSplashEvent e)
	{
		//only care if a player threw something
		if(e.getEntity().getShooter() instanceof Player)
		{
			Player thrower = (Player) e.getEntity().getShooter(); 

			//loop through all entities affected by potion
			for(LivingEntity thing : e.getAffectedEntities())
			{	
				//only care if entity is Player, and one of the players has PVE
				if((thing instanceof Player) & ((!Toggle.isPvPEnabled((Player) thing)) || (!Toggle.isPvPEnabled(thrower))))
				{
					Player hit = (Player) thing;
											
					//loop through all effects and see if Poison is one of them
					for(PotionEffect effect : e.getPotion().getEffects())
					{
						if(effect.getType().equals(PotionEffectType.POISON))
						{							
							//now set the intensity for the hit player to be negative
							e.setIntensity(hit, -1);									
						}
					}
				}
			}
		}
	}
}
