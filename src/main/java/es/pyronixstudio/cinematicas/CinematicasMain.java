package es.pyronixstudio.cinematicas;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.pyronixstudio.cinematicas.comandos.ComandosManager;
import es.pyronixstudio.util.ficheros.FicheroBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
		File carpeta = new File(CinematicasMain.main.getDataFolder()+"/cinematicas/");
		if(!carpeta.exists())
			carpeta.mkdir();
		
	}

    public void copiarFicheros(String ruta) {
		copiarFicheros(ruta,getDataFolder()+ruta);
	}
	
	public void copiarFicheros(String ruta ,String rutaACopiar) {
		FicheroBase fichero = FicheroBase.of(Path.of(rutaACopiar));
		if(!fichero.existe())
			try(Scanner in = new Scanner(CinematicasMain.class.getClassLoader().getResourceAsStream("resources"+ruta))) {
			
				List<String> contenido = new ArrayList<>();
				while(in.hasNextLine())
					contenido.add(in.nextLine());
				
				fichero.escribirLineas(contenido);
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