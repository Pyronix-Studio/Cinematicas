package principal.comandos;

import principal.BukkitMain;

public class ComandosManager {

	public BukkitMain plugin;

	public ComandosManager(BukkitMain mainPlugin) {

		this.plugin = mainPlugin;
		registrar();

	}

	public void registrar() {

		this.plugin.getServer().getPluginCommand("cinematicas").setExecutor(new CmdCinematicas());
		
	}
	
	
	

}