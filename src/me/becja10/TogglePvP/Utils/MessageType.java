package me.becja10.TogglePvP.Utils;

import org.bukkit.ChatColor;

public enum MessageType
{
  PREFIX("&8[&bTogglePvP&8] "), 
  NO_PERM(PREFIX.getMsg() + "&4You do not have permission to use that command!"), 
  CAN_TOGGLE(PREFIX.getMsg() + "&7You can now toggle PvP"), 
  PLAYER_ONLY(PREFIX.getMsg() + "&7You must be a player in order to use this command!"), 
  PVP_ENABLED(PREFIX.getMsg() + "&7You have PvP set to &ctrue"), 
  PVP_DISABLED(PREFIX.getMsg() + "&7You have PvP set to &afalse"),
  NO_PLAYER(PREFIX.getMsg() + "&7There is no player by that name online"), 
  RESET(PREFIX.getMsg() + "&7Reset toggle cooldown timer"),
  NO_LAVA(PREFIX.getMsg() + "&7Can't dump lava near players unless both are PvP"),
  PLAYER_NOT_FOUND(PREFIX.getMsg() + "&7Could not find any player by that name."), 
  TOGGLE_LOCKED(PREFIX.getMsg() + "&4You cannot toggle your pvp status because it has been locked.");

  String msg;

  private MessageType(String s) {
    this.msg = s;
  }

  public String getMsg() {
    return ChatColor.translateAlternateColorCodes('&', this.msg);
  }
}