package me.becja10.TogglePvP.Listeners;

import java.util.Arrays;
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
	
	private PotionEffectType[] list = new PotionEffectType[]
			{
			PotionEffectType.POISON,
			PotionEffectType.BLINDNESS,
			PotionEffectType.CONFUSION,
			PotionEffectType.HARM,
			PotionEffectType.HUNGER,
			PotionEffectType.WEAKNESS,
			PotionEffectType.SLOW
			};
	
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
				if(thing instanceof Player)
				{
					Player hit = (Player) thing;
					if((!Toggle.isPvPEnabled(hit)) || (!Toggle.isPvPEnabled(thrower)))
					{						
						//loop through all effects and see if Poison is one of them
						for(PotionEffect effect : e.getPotion().getEffects())
						{
							if(Arrays.asList(list).contains(effect.getType()))
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
}
