package principal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FicheroManager {

	private String ruta;

	public FicheroManager(String ruta) {
		this.ruta = ruta;
	}

	public boolean existe() {
		File archivo = new File(ruta);
		return archivo.exists();
	}

	public void crearSiNoExiste() {

		try {
			File archivo = new File(ruta);
			archivo.getParentFile().mkdirs();
			if (!archivo.exists()) {
				archivo.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> leerTodo() {
		ArrayList<String> lineas = new ArrayList<String>();
		try {
			Scanner in = new Scanner(new File(ruta));
			while (in.hasNextLine()) {
				String linea = in.nextLine().trim();
				if(linea.startsWith("#"))
					continue;
				if (!linea.isEmpty() || linea.isBlank()) {
					lineas.add(linea);
				}
			}
			in.close();
			return lineas;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public void escribir(ArrayList<String> lineas, boolean append) {
		if (lineas.size() > 0) {
			crearSiNoExiste();
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(ruta, append));
				for (String linea : lineas) {
					out.write(linea);
					out.newLine();
				}
				out.flush();
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public void borrar() {
		File archivo = new File(ruta);
		if (archivo.exists()) {
			archivo.delete();
		}
	}
	
	public File getFichero() {
		return new File(ruta);
	}

}