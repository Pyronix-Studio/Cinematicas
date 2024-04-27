package principal.comandos;

import java.io.File;
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

public class CmdCinematicas implements TabExecutor, CommandExecutor {

	private static HashMap<UUID, Cinematica> cinematicas = new HashMap<UUID, Cinematica>();
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.isOp()) return null;
		
		
		if(args.length == 1) return Arrays.asList(new File(BukkitMain.main.getDataFolder().toString()).list());
		
		if(args.length == 2) return Arrays.asList(new String[] {"grabar","continuar","guardar","parar","ejecutar"});
		
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.isOp()) { /*mensaje*/	 return true;}
		
		if(args.length < 2) { /*mensaje*/ return true;}
		
		
		String subComando = args[1];
		
		if(subComando.equals("grabar")) {
			if(!(sender instanceof Player)) { /*mensaje*/	 return true;}
			
			
			Player player = (Player)sender;
			String nombre = args[0];
			Cinematica cinematica = new Cinematica(nombre);
			cinematicas.put(player.getUniqueId(), cinematica);
			cinematica.grabar(player, false);
			sender.sendMessage("empezo a grabar");
		}else if(subComando.equals("continuar")) {
		
			if(!(sender instanceof Player)) { /*mensaje*/	 return true;}
			Player player = (Player)sender;

			if(!cinematicas.containsKey(player.getUniqueId())) {/*mensaje*/return true;}
			
			Cinematica cinematica = cinematicas.get(player.getUniqueId());
			cinematica.grabar(player, true);
			
			
		}else if(subComando.equals("guardar")) {
			if(!(sender instanceof Player)) { /*mensaje*/	 return true;}
			Player player = (Player)sender;

			if(!cinematicas.containsKey(player.getUniqueId())) {/*mensaje*/return true;}
			
			Cinematica cinematica = cinematicas.get(player.getUniqueId());
			cinematica.guardar();
			cinematicas.remove(player.getUniqueId());
			
		}else if(subComando.equals("parar")) {
			if(!(sender instanceof Player)) { /*mensaje*/	 return true;}
			Player player = (Player)sender;

			if(!cinematicas.containsKey(player.getUniqueId())) {/*mensaje*/return true;}
			
			Cinematica cinematica = cinematicas.get(player.getUniqueId());
			cinematica.pararGrabar();
			
		}else if(subComando.equals("ejecutar")) {
			Cinematica cinematica = new Cinematica(args[0]);
			cinematica.cargar();
			cinematica.ejecutar(Bukkit.getOnlinePlayers().stream().map((pl) -> pl.getUniqueId()).toList(), null);
		}
		
		return true;
	}

}
