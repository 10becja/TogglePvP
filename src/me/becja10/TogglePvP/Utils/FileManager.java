package me.becja10.TogglePvP.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import me.becja10.TogglePvP.TogglePvP;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager
{
	private static FileConfiguration config = null;
	private static File players = null;

	/*
	 * Get information about the players stored in players.yml
	 */
	public static FileConfiguration getPlayers() {
		/*
		 * <id>:
		 *   name: blah
		 *   pvp: true|false
		 *   lock: true|false
		 */
		if (config == null)
			reloadPlayers();
		return config;
	}
	
	/*
	 * Reloads the player.yml file
	 */
	public static void reloadPlayers() {
		if (players == null)
			players = new File(TogglePvP.getInstance().getDataFolder(), "players.yml");
		config = YamlConfiguration.loadConfiguration(players);

		InputStream defConfigStream = TogglePvP.getInstance().getResource("players.yml");
		if (defConfigStream != null) {
			@SuppressWarnings("deprecation")
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}
	
	/*
	 * saves information to player.yml
	 */
	public static void savePlayers() {
		if ((config == null) || (players == null))
			return;
		try {
			getPlayers().save(players);
		} catch (IOException ex) {
			System.out.println(MessageType.PREFIX.getMsg() + "Could not save config to " + players);
		}
	}

	/*
	 * Creates the default, empty player.yml file
	 */
	public static void saveDefaultPlayers() {
		if (players == null)
			players = new File(TogglePvP.getInstance().getDataFolder(), "players.yml");
		if (!players.exists())
			TogglePvP.getInstance().saveResource("players.yml", false);
	}
}