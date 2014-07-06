package pl.mavepp.mvptools.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
 
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Bossbar implements Listener {
	
	private static Object packet;
	
	private static Player player;
	
	private static Map<String, Object> dragons = new HashMap<String, Object>();
	
	public static class SpawnBar extends Bossbar {
		
		public SpawnBar(Player p, String text) {
			
			player = (Player) p;
			
			if(!dragons.containsKey(p.getName())) {
				
				try {
					
					Object ender = getClass("{net}:EntityEnderDragon").getConstructor(getClass("{net}:World")).newInstance(getClass("{org}:CraftWorld").getMethod("getHandle").invoke(p.getWorld()));
					
					ender.getClass().getMethod("setLocation", double.class, double.class, double.class, float.class, float.class).invoke(ender, p.getLocation().getX(), -500, p.getLocation().getZ(), 0F, 0F);
					
					ender.getClass().getMethod("setCustomName", String.class).invoke(ender, text);
					
					ender.getClass().getMethod("setCustomNameVisible", boolean.class).invoke(ender, false);
					
					ender.getClass().getMethod("setHealth", float.class).invoke(ender, 200F + 0.1F);
					
					dragons.put(p.getName(), ender);
					
					packet = getClass("{net}:PacketPlayOutSpawnEntityLiving").getConstructor(getClass("{net}:EntityLiving")).newInstance(ender);
					
				} catch (Exception e) { e.printStackTrace(); }
				
			} else {
				
				new ChangeBar(p, text);
			}
			
		}
		
		public SpawnBar(Player p, float hp, String text) {
			
			player = (Player) p;
			
			if(!dragons.containsKey(p.getName())) {
				
				try {
					
					Object ender = getClass("{net}:EntityEnderDragon").getConstructor(getClass("{net}:World")).newInstance(getClass("{org}:CraftWorld").getMethod("getHandle").invoke(p.getWorld()));
					
					ender.getClass().getMethod("setLocation", double.class, double.class, double.class, float.class, float.class).invoke(ender, p.getLocation().getX(), -500, p.getLocation().getZ(), 0F, 0F);
					
					ender.getClass().getMethod("setCustomName", String.class).invoke(ender, text);
					
					ender.getClass().getMethod("setCustomNameVisible", boolean.class).invoke(ender, false);
					
					ender.getClass().getMethod("setHealth", float.class).invoke(ender, hp + 0.1F);
					
					dragons.put(p.getName(), ender);
					
					packet = getClass("{net}:PacketPlayOutSpawnEntityLiving").getConstructor(getClass("{net}:EntityLiving")).newInstance(ender);
					
				} catch (Exception e) { e.printStackTrace(); }
				
			} else {
				
				new ChangeBar(p, hp, text);
				
			}
			
		}
		
	}
	
	public static class DestroyBar extends Bossbar {
		
		public DestroyBar(Player p)  {
			
			player = (Player) p;
			
			if(dragons.containsKey(p.getName())){
				
				try {
					
					Object ender = dragons.get(p.getName());
					
					int id = (Integer) ender.getClass().getMethod("getId").invoke(ender, new Object[0]);
					
                                        packet = getClass("{net}:PacketPlayOutEntityDestroy").getConstructor(int[].class).newInstance(new int[] { id });
						
					dragons.remove(p.getName());
				
				} catch (Exception e) { e.printStackTrace(); }
				
			}
			
		}
		
	}
	
	public static class DestroyAllBar extends Bossbar {
		public DestroyAllBar()  {
			for(Player players : Bukkit.getOnlinePlayers()){
				new DestroyBar(players).send();
				dragons.clear();
			}
		}
	}
	
	private static class ChangeBar extends Bossbar {
		
		private ChangeBar(Player p, String text)  {
			
			player = (Player) p;
			
			try {
				
				Object ender = dragons.get(p.getName());
				
				int id = (Integer) ender.getClass().getMethod("getId").invoke(ender, new Object[0]);
				
				Object watcher = getClass("{net}:DataWatcher").getConstructor(getClass("{net}:Entity")).newInstance(ender);
				
				Method a = watcher.getClass().getMethod("a", int.class, Object.class );
				
				
				a.invoke(watcher, 0, (byte) 0x20);
				
				a.invoke(watcher, 6, (float) 200.0 + 0.1F);
				
				a.invoke(watcher, 8, (byte) 0);
			  	
				a.invoke(watcher, 10, text);
			  	
				a.invoke(watcher, 11, (byte) 0);
			  	
				packet = getClass("{net}:PacketPlayOutEntityMetadata").getConstructor(int.class, getClass("{net}:DataWatcher"), boolean.class).newInstance(id, watcher, true);
			
			} catch (Exception e) { e.printStackTrace(); }
			
		}
		
		public ChangeBar(Player p, float hp, String text)  {
			
			player = (Player) p;
			
			if(dragons.containsKey(p.getName())){
			
				try {
					
					Object ender = dragons.get(p.getName());
					
					int id = (Integer) ender.getClass().getMethod("getId").invoke(ender, new Object[0]);
					
					Object watcher = getClass("{net}:DataWatcher").getConstructor(getClass("{net}:Entity")).newInstance(ender);
					
					Method a = watcher.getClass().getMethod("a", int.class, Object.class );
					
					a.invoke(watcher, 0, (byte) 0x20);
					
					a.invoke(watcher, 6, (float) hp + 0.1F);
					
					a.invoke(watcher, 8, (byte) 0);
				  	
					a.invoke(watcher, 10, text);
				  	
				  	a.invoke(watcher, 11, (byte) 0);
				  	
					packet = getClass("{net}:PacketPlayOutEntityMetadata").getConstructor(int.class, getClass("{net}:DataWatcher"), boolean.class).newInstance(id, watcher, true);
				
				} catch (Exception e) { e.printStackTrace(); }
				
			}
			
		}
		
	}
	
	public static ArrayList<String> players() {
		
		ArrayList<String> players = new ArrayList<>();
		
		for(String pwd : dragons.keySet()){
			
			players.add(pwd);
			
		}
		
		return players;
		
	}
	
	public void send() {
		
		try {
			
			Player p = (Player) player;
			
			Object craftplayer = getClass("{org}:entity.CraftPlayer").getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
			
			Object connection = getClass("{net}:EntityPlayer").getField("playerConnection").get(craftplayer);
			
			Method sendpacket = getClass("{net}:PlayerConnection").getMethod("sendPacket", new Class[] { getClass("{net}:Packet")});
			
			sendpacket.invoke(connection, packet);
			
		} catch (Exception e) { e.printStackTrace(); }
		
	}
	
	private static Class<?> getClass(String name) throws ClassNotFoundException {
        
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        
		String className = null;
        
        if(name.split("\\:")[0].equals("{net}"))
            
        	className = "net.minecraft.server." + version + name.split(":")[1];
        
        if(name.split("\\:")[0].equals("{org}"))
        
        	className = "org.bukkit.craftbukkit." + version + name.split(":")[1];
        
        return Class.forName(className);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		Player p = (Player) e.getPlayer();
		dragons.remove(p.getName());
	}
}