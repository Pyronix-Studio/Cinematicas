package com.pyronixstudios.cinematicas.comandos;

import com.pyronixstudios.cinematicas.CinematicasMain;

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