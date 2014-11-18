package me.becja10.TogglePvP.Events;

import me.becja10.TogglePvP.Main;
import me.becja10.TogglePvP.Utils.Toggle;

import org.bukkit.World.Environment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener
{
	@EventHandler
	public void onDmg(EntityDamageEvent event)
	{
		//only concerned when an entity damages another entity
		if(!(event instanceof EntityDamageByEntityEvent)) return;
		
		//also only concerned if the thing being damaged is a Player
		if(!(event.getEntity() instanceof Player)) return;
		
		Player attacked = (Player)event.getEntity(); //the player who is getting damaged
		
		//see if they are in a nether or end
		Boolean nether = attacked.getWorld().getEnvironment().equals(Environment.NETHER);
		Boolean end = attacked.getWorld().getEnvironment().equals(Environment.THE_END);

		//get the config options
		Boolean ignoreNether = Main.getInstance().getConfig().getBoolean("Ignore Nether");
		Boolean ignoreEnd = Main.getInstance().getConfig().getBoolean("Ignore End");
		
		//if player is in nether/end and we are ignoring those world types, forget about the event
		if((nether & ignoreNether) || (end & ignoreEnd)) return;
		
		//where the damage came from. Need to cast to EntityDamgeByEntity to get the damager
		Entity damageSource = ((EntityDamageByEntityEvent)event).getDamager();
		
		//create a null Player in case it turns out the attacker wasn't a player
		Player attacker = null;
		
	  
		//if the damaged is caused by a player
		if(damageSource instanceof Player)
			attacker = (Player) damageSource;
	  
		//if the damage came from an Arrow
		else if (damageSource instanceof Arrow)
		{
			Arrow arrow = (Arrow) damageSource;
			//and the shooter is a player
			if(arrow.getShooter() instanceof Player)
				attacker = (Player)arrow.getShooter();
		}
		//check if the damage was caused by a thrown potion
		else if(damageSource instanceof ThrownPotion)
		{
			ThrownPotion potion = (ThrownPotion)damageSource;
			//and the thrower is a player
			if(potion.getShooter() instanceof Player)
				attacker = (Player)potion.getShooter();
		}
		//if we decided that the attacker is a player
		if(attacker != null)
			//make sure both attacker and attacked have pvp enabled, if not, cancel the event
			if((!Toggle.isPvPEnabled(attacker)) || (!Toggle.isPvPEnabled(attacked)))
				event.setCancelled(true);
	}
}