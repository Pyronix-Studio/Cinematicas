package com.pyronixstudios.cinematicas;

import java.util.ArrayList;
import java.util.HashMap;

public class IdiomaManager {

	private final String IDIOMA;
	private HashMap<String, String> diccionarioTraducciones = new HashMap<String, String>();
	private final String RUTA;
	private final String CARACTER_SPLIT = ":";
	public IdiomaManager(String ruta) {
		this.RUTA = ruta;
		this.IDIOMA = CinematicasMain.main.config.getString("idioma");
		leerIdioma();
	}
	
	
	private void leerIdioma() {
		FicheroManager idioma = new FicheroManager(RUTA+IDIOMA+".yml");
		if(idioma.existe()) {
			
			ArrayList<String> lineas = idioma.leerTodo();
			for(String linea : lineas) {
				if(linea.startsWith("#") || linea.isEmpty() || linea.isBlank())
					continue; //Codigo que permite que haya comentarios en el fichero.
				String[] lineaSplit = linea.split(CARACTER_SPLIT);
				String key = lineaSplit[0];
				String traduccion = lineaSplit[1];
				diccionarioTraducciones.put(key.toLowerCase(), traduccion);
			}
		}else CinematicasMain.main.getLogger().info("Idioma " + IDIOMA + " no valido.");
		
	}
	
	public String leerTraduccion(String key) {
		 key = key.toLowerCase();
		return diccionarioTraducciones.containsKey(key) ? diccionarioTraducciones.get(key) : "Traduccion no valida para clave: " + key;
	}
	
	
}