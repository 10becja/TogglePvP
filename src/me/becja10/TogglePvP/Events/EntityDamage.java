package me.becja10.TogglePvP.Events;

import me.becja10.TogglePvP.TogglePvP;
import me.becja10.TogglePvP.Utils.Toggle;

import org.bukkit.World.Environment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener
{
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
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
		Boolean ignoreNether = TogglePvP.getInstance().config_nether;
		Boolean ignoreEnd = TogglePvP.getInstance().config_end;
		
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
		
		else if(damageSource instanceof Egg)
		{
			Egg egg = (Egg) damageSource;
			if(egg.getShooter() instanceof Player)
				attacker = (Player)egg.getShooter();
		}
		
		else if(damageSource instanceof Snowball)
		{
			Snowball ball = (Snowball) damageSource;
			if(ball.getShooter() instanceof Player)
				attacker = (Player)ball.getShooter();
		}
		
		else if(damageSource instanceof FishHook)
		{
			FishHook hook = (FishHook) damageSource;
			if(hook.getShooter() instanceof Player)
				attacker = (Player)hook.getShooter();
		}
		
		else if(damageSource instanceof EnderPearl)
		{
			EnderPearl pearl = (EnderPearl) damageSource;
			if(pearl.getShooter() instanceof Player)
				attacker = (Player)pearl.getShooter();
		}
		//if we decided that the attacker is a player
		if(attacker != null)
		{
			//admins can damage anyone, at any time. MUHAHAHA
			if(attacker.hasPermission("togglepvp.admin"))
				return;
			
			//if either party has PvE enabled cancel the event
			if((!Toggle.isPvPEnabled(attacker)) || (!Toggle.isPvPEnabled(attacked)))
			{
				event.setCancelled(true);
			}
		}
	}
}