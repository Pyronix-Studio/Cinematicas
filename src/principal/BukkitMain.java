package principal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import principal.comandos.ComandosManager;

public class BukkitMain extends JavaPlugin implements Listener {

	public FileConfiguration config;
	
	public static BukkitMain main;
	public static String nombrePlugin = "&6[&7Cinematicas&6] ";
	@Override
	public void onLoad() {
		main = this;
	}

	@Override
	public void onEnable() {
		config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
		new ComandosManager(this);
	
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
	
	public static String format(String texto) {
		return ChatColor.translateAlternateColorCodes('&', texto);
	}

}