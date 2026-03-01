package es.pyronixstudio.cinematicas.comandos;

import es.pyronixstudio.cinematicas.CinematicasMain;

public class ComandosManager {

	public CinematicasMain plugin;

	public ComandosManager(CinematicasMain mainPlugin) {

		this.plugin = mainPlugin;
		registrar();

	}

	public void registrar() {

		this.plugin.getServer().getPluginCommand("cinematicas").setExecutor(new CmdCinematicas());
		
	}
	
	
	

}