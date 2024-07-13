package principal.comandos;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import principal.BukkitMain;
import principal.Cinematica;
import principal.FicheroManager;

public class CmdCinematicas implements TabExecutor, CommandExecutor {

	private static HashMap<UUID, Cinematica> cinematicas = new HashMap<UUID, Cinematica>();
	private static final String RUTA_CINEMATICAS = BukkitMain.main.getDataFolder()+"/cinematicas/";
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.isOp()) return null;
		
		
		if(args.length == 1) return Arrays.asList(new File(BukkitMain.main.getDataFolder()+"/cinematicas/").list());
		
		if(args.length == 2) return Arrays.asList(new String[] {"grabar","continuar","guardar","parar","ejecutar","borrar"});
		
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.isOp()) { sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("sender.noPermisos")));	 return true;}
		if(args.length < 2) { sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.cinematicas.argumentoInvalido")));	 return true;}
		
		String subComando = args[1];
		
		if(subComando.equals("grabar")) {
			if(!(sender instanceof Player)) { sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.noJugador")));		 return true;}
			Player player = (Player)sender;
			String nombre = args[0];
			if(new FicheroManager(RUTA_CINEMATICAS+nombre).existe()) {sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.cinematicas.ficheroYaExiste")));	 return true;}
			Cinematica cinematica = new Cinematica(nombre);
			cinematicas.put(player.getUniqueId(), cinematica);
			cinematica.grabar(player, false);
			sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.cinematicas.grabando").replace("%nombre%", nombre)));
		}else if(subComando.equals("continuar")) {
		
			if(!(sender instanceof Player)) { sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.noJugador")));	 return true;}
			Player player = (Player)sender;

			if(!cinematicas.containsKey(player.getUniqueId())) {sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.cinematicas.jugadorNoEncontrado"))); return true;}
			
			Cinematica cinematica = cinematicas.get(player.getUniqueId());
			cinematica.grabar(player, true);
			sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.cinematicas.continuando").replace("%nombre%", cinematica.getNombre())));

			
		}else if(subComando.equals("guardar")) {
			if(!(sender instanceof Player)) {  sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.noJugador")));	 return true;}
			Player player = (Player)sender;

			if(!cinematicas.containsKey(player.getUniqueId())) {sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.cinematicas.jugadorNoEncontrado")));return true;}
			
			Cinematica cinematica = cinematicas.get(player.getUniqueId());
			cinematica.guardar();
			cinematicas.remove(player.getUniqueId());
			sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.cinematicas.guardado").replace("%nombre%",cinematica.getNombre())));

		}else if(subComando.equals("parar")) {
			if(!(sender instanceof Player)) { sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.noJugador")));	 return true;}
			Player player = (Player)sender;

			if(!cinematicas.containsKey(player.getUniqueId())) {sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.cinematicas.jugadorNoEncontrado")));return true;}
			
			Cinematica cinematica = cinematicas.get(player.getUniqueId());
			cinematica.pararGrabar();
			sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.cinematicas.parado").replace("%nombre%", cinematica.getNombre())));

		}else if(subComando.equals("ejecutar")) {
			Cinematica cinematica = new Cinematica(args[0]);
			cinematica.cargar();
			cinematica.ejecutar(new ArrayList<Player>(Bukkit.getOnlinePlayers()), null);
			sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.cinematicas.ejecutando").replace("%nombre%", cinematica.getNombre())));

		}else if(subComando.equals("borrar")) {
			String nombreCinematica = args[0];
			FicheroManager ficheroCinematica = new FicheroManager(RUTA_CINEMATICAS+nombreCinematica);
			if(!ficheroCinematica.existe()) {sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.cinematicas.noExiste").replace("%nombre%", nombreCinematica)));	 return true;}
			ficheroCinematica.borrar();
			sender.sendMessage(BukkitMain.format(BukkitMain.main.getTraduccion("comando.cinematicas.borrar").replace("%nombre%", nombreCinematica)));
			
		}
		
		return true;
	}

}
