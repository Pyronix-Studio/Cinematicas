package principal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class Cinematica implements Listener {

	private FicheroManager fichero;
	private final String CARACTER_SPLIT = "&";
	private ArrayList<Location> cacheCinematica = new ArrayList<Location>();
	private String nombre;
	private BukkitTask taskGrabar;
	public Cinematica(String nombre) {
		Bukkit.getPluginManager().registerEvents(this, BukkitMain.main);
		this.fichero = new FicheroManager(BukkitMain.main.getDataFolder()+"/"+nombre);
		this.nombre = nombre;
	}
	

	
	public void grabar(Player player, boolean continuar) {
		if(!continuar)
			cacheCinematica.clear();
		taskGrabar = new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!isCancelled()) 
					cacheCinematica.add(player.getLocation().clone());
				
			}
		}.runTaskTimer(BukkitMain.main, 0, 1L);
	}
	
	public void pararGrabar() {
		taskGrabar.cancel();
	}
	
	public void guardar() {
		//world,x,y,z,pitch,yaw,dx,dy,dz
		ArrayList<String> lineas = new ArrayList<String>();
		for(Location location : cacheCinematica) {
			
			String parsed = location.getWorld().getName()+CARACTER_SPLIT+location.getX()+CARACTER_SPLIT+location.getY()+CARACTER_SPLIT+location.getZ()+CARACTER_SPLIT
			+location.getPitch()+CARACTER_SPLIT+location.getYaw()
			+CARACTER_SPLIT+location.getDirection().getX()+CARACTER_SPLIT+location.getDirection().getY()+CARACTER_SPLIT+location.getDirection().getZ();
			lineas.add(parsed);
		}
		
		fichero.escribir(lineas, false);
	}

	
	public void cargar() {
		//world,x,y,z,pitch,yaw,dx,dy,dz
		cacheCinematica.clear();
		ArrayList<String> ficheroSinParsear = fichero.leerTodo();
		ficheroSinParsear.forEach((localizacion) -> {
			
			String[] localizacionSplit = localizacion.split(CARACTER_SPLIT);
			
			World world = Bukkit.getWorld(localizacionSplit[0]);
			double x = Double.parseDouble(localizacionSplit[1]);
			double y = Double.parseDouble(localizacionSplit[2]);
			double z = Double.parseDouble(localizacionSplit[3]);
			float pitch = Float.parseFloat(localizacionSplit[4]);
			float yaw = Float.parseFloat(localizacionSplit[5]);
			double direccionX = Double.parseDouble(localizacionSplit[6]);
			double direccionY = Double.parseDouble(localizacionSplit[7]);
			double direccionZ = Double.parseDouble(localizacionSplit[8]);
			Location location = new Location(world, x, y, z, pitch, yaw);
			location.setDirection(new Vector(direccionX, direccionY, direccionZ));
			cacheCinematica.add(location);
		});
		
	}
	
	public void ejecutar(List<UUID> jugadores, CinematicaTermina funcionTerminar) {
		new BukkitRunnable() {
			int indiceEjecucion = 0;
			@Override
			public void run() {
				
				if(indiceEjecucion >= cacheCinematica.size()) {
					if(funcionTerminar != null)
						funcionTerminar.run();
					cacheCinematica.clear();
					cancel();
				}
				if(!isCancelled())
					for(UUID uuid : jugadores) {
						Player player = Bukkit.getPlayer(uuid);
						if(player != null) {
							if(player.isOnline()) {
								player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 2, false, false, false));
								player.teleport(cacheCinematica.get(indiceEjecucion));
							}
						}
					}
					
				indiceEjecucion++;
				
			}
		}.runTaskTimer(BukkitMain.main, 0, 1L);
		
	}
	
	public String getNombre() {
		return nombre;
	}
	
}
