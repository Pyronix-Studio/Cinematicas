package com.pyronixstudios.cinematicas;

import java.util.ArrayList;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.pyronixstudios.cinematicas.comandos.ComandosManager;

public class CinematicasMain extends JavaPlugin implements Listener {

	public FileConfiguration config;
	
	public static CinematicasMain main;
	public static String nombrePlugin = "&6[&7Cinematicas&6] ";
	private IdiomaManager idioma;
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
		
		copiarFicheros("/idiomas/es_es.yml");
		
		if(idioma == null)
			idioma = new IdiomaManager(getDataFolder()+"/idiomas/");
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void copiarFicheros(String ruta) {
		copiarFicheros(ruta,getDataFolder()+ruta);
	}
	
	public void copiarFicheros(String ruta ,String rutaACopiar) {
		FicheroManager fichero = new FicheroManager(rutaACopiar);
		if(!fichero.existe())
			try(Scanner in = new Scanner(CinematicasMain.class.getClassLoader().getResourceAsStream("resources"+ruta))) {
			
				ArrayList<String> contenido = new ArrayList<String>();
				while(in.hasNextLine())
					contenido.add(in.nextLine());
				
				fichero.escribir(contenido, false);
			}catch (Exception e) {
				e.printStackTrace();
			}

		
	}
	
	public String getTraduccion(String key) {
		return idioma.leerTraduccion(key);
	}
	
	public static String format(String texto) {
		return ChatColor.translateAlternateColorCodes('&', nombrePlugin+texto);
	}

}