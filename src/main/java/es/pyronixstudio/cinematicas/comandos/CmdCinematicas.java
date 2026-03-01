package es.pyronixstudio.cinematicas.comandos;


import es.pyronixstudio.cinematicas.Cinematica;
import es.pyronixstudio.cinematicas.CinematicasMain;
import es.pyronixstudio.util.ficheros.FicheroBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CmdCinematicas implements TabExecutor, CommandExecutor {

	private static final HashMap<UUID, Cinematica> cinematicas = new HashMap<>();
	private static final String RUTA_CINEMATICAS = CinematicasMain.main.getDataFolder()+"/cinematicas/";

	@Override
	public List<String> onTabComplete(CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
		if(!sender.isOp()) return null;
		
		
		if(args.length == 1) return Arrays.asList(Objects.requireNonNull(new File(CinematicasMain.main.getDataFolder() + "/cinematicas/").list()));
		
		if(args.length == 2) return Arrays.asList("grabar","continuar","guardar","parar","ejecutar","borrar");
		
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
		if(!sender.isOp()) { sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("sender.noPermisos")));	 return true;}
		if(args.length < 2) { sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.cinematicas.argumentoInvalido")));	 return true;}
		
		String subComando = args[1];

        switch (subComando) {
            case "grabar" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.noJugador")));
                    return true;
                }
                String nombre = args[0];
                if (FicheroBase.of(Path.of(RUTA_CINEMATICAS, nombre)).existe()) {
                    sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.cinematicas.ficheroYaExiste")));
                    return true;
                }
                Cinematica cinematica = new Cinematica(nombre);
                cinematicas.put(player.getUniqueId(), cinematica);
                cinematica.grabar(player, false);
                sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.cinematicas.grabando").replace("%nombre%", nombre)));
            }
            case "continuar" -> {

                if (!(sender instanceof Player player)) {
                    sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.noJugador")));
                    return true;
                }

                if (!cinematicas.containsKey(player.getUniqueId())) {
                    sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.cinematicas.jugadorNoEncontrado")));
                    return true;
                }

                Cinematica cinematica = cinematicas.get(player.getUniqueId());
                cinematica.grabar(player, true);
                sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.cinematicas.continuando").replace("%nombre%", cinematica.getNombre())));


            }
            case "guardar" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.noJugador")));
                    return true;
                }

                if (!cinematicas.containsKey(player.getUniqueId())) {
                    sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.cinematicas.jugadorNoEncontrado")));
                    return true;
                }

                Cinematica cinematica = cinematicas.get(player.getUniqueId());
                cinematica.guardar();
                cinematicas.remove(player.getUniqueId());
                sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.cinematicas.guardado").replace("%nombre%", cinematica.getNombre())));

            }
            case "parar" -> {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.noJugador")));
                    return true;
                }

                if (!cinematicas.containsKey(player.getUniqueId())) {
                    sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.cinematicas.jugadorNoEncontrado")));
                    return true;
                }

                Cinematica cinematica = cinematicas.get(player.getUniqueId());
                cinematica.pararGrabar();
                sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.cinematicas.parado").replace("%nombre%", cinematica.getNombre())));

            }
            case "ejecutar" -> {
                Cinematica cinematica = new Cinematica(args[0]);
                cinematica.cargar();
                cinematica.ejecutar(new ArrayList<>(Bukkit.getOnlinePlayers()), () -> {
                });
                sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.cinematicas.ejecutando").replace("%nombre%", cinematica.getNombre())));

            }
            case "borrar" -> {
                String nombreCinematica = args[0];
                FicheroBase ficheroCinematica = FicheroBase.of(Path.of(RUTA_CINEMATICAS, nombreCinematica));
                if (!ficheroCinematica.existe()) {
                    sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.cinematicas.noExiste").replace("%nombre%", nombreCinematica)));
                    return true;
                }
                ficheroCinematica.borrar();
                sender.sendMessage(CinematicasMain.format(CinematicasMain.main.getTraduccion("comando.cinematicas.borrar").replace("%nombre%", nombreCinematica)));
            }
        }
		
		return true;
	}

}
