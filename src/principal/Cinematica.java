package principal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
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
	private static ArrayList<Player> jugadoresEnCinematica;
	public Cinematica(String nombre) {
		this.fichero = new FicheroManager(BukkitMain.main.getDataFolder()+"/cinematicas/"+nombre);
		this.nombre = nombre;
		if(jugadoresEnCinematica == null) {
			Cinematica.jugadoresEnCinematica = new ArrayList<Player>();
			Bukkit.getPluginManager().registerEvents(this, BukkitMain.main);
		}
		
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
	@EventHandler
	private static void onPlayerDamageEvent(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			if(Cinematica.jugadoresEnCinematica.contains(player)) {
				event.setCancelled(true);
			}
		}
	}
	
	public boolean ejecutar(List<Player> jugadores, CinematicaTermina funcionTerminar) {
		if(cacheCinematica.size() > 0) {
			Cinematica.jugadoresEnCinematica.addAll(jugadores);
			jugadores.forEach((pla) -> {
				pla.getInventory().setArmorContents(new ItemStack[]{null,null,null,new ItemStack(Material.CARVED_PUMPKIN)});
				pla.setGameMode(GameMode.ADVENTURE);
				pla.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2, false, false, false));
			});
			new BukkitRunnable() {
				int indiceEjecucion = 0;
				@Override
				public void run() {
					
					if(indiceEjecucion >= cacheCinematica.size()) {
						if(funcionTerminar != null) {
							jugadores.forEach((pla) -> {
								pla.getInventory().setArmorContents(new ItemStack[]{null,null,null,null});
								pla.removePotionEffect(PotionEffectType.INVISIBILITY);
								
								});
							Cinematica.jugadoresEnCinematica.removeAll(jugadores);
							funcionTerminar.run();
						}
						cacheCinematica.clear();
						cancel();
					}
					if(!isCancelled())
						for(Player player : jugadores) {
							if(player != null) {
								if(player.isOnline()) {
									player.teleport(cacheCinematica.get(indiceEjecucion));
									
								}
							}
						}
						
					indiceEjecucion++;
					
				}
			}.runTaskTimer(BukkitMain.main, 0, 1L);
			return true;
		}
		return false;
	}
	
	public String getNombre() {
		return nombre;
	}
	
}
