package es.pyronixstudio.cinematicas;

import es.pyronixstudio.util.ficheros.FicheroBase;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class IdiomaManager {

	private final String IDIOMA;
	private final HashMap<String, String> diccionarioTraducciones = new HashMap<>();
	private final String RUTA;

    public IdiomaManager(String ruta) {
		this.RUTA = ruta;
		this.IDIOMA = CinematicasMain.main.config.getString("idioma");
		leerIdioma();
	}
	
	
	private void leerIdioma() {
		FicheroBase idioma = FicheroBase.of(Path.of(RUTA,IDIOMA+".yml"));
		if(idioma.existe()) {
			
			List<String> lineas = idioma.getAllLines();
			for(String linea : lineas) {
				if(linea.startsWith("#") || linea.isBlank())
					continue; //Codigo que permite que haya comentarios en el fichero.
                String CARACTER_SPLIT = ":";
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