package src;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class EventManager implements Listener 
{
	public static String dateF = "yyyy/MM/dd HH:mm:ss";
	
	public static SimpleDateFormat sdf = new SimpleDateFormat(dateF);
	
	public static Calendar cal = Calendar.getInstance();
	
	public static Date date = new Date(0);
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(PlayerChatEvent event)
	{
		System.out.println(event.getPlayer().getAddress());
		
		final String name = event.getPlayer().getName();
		
		if(!SpamX.spamHash.containsKey(name))
		{
			SpamX.spamHash.put(name, 1);
		}
		else
		{
			SpamX.spamHash.put(name, SpamX.spamHash.get(name) + 1);
		}
		
		if(SpamX.spamHash.get(name) >= SpamX.config.getInt("limit"))
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "kick " + name);
			
			try 
			{
				SpamX.logFile.write("Kicked player " + name + " at ip address " + event.getPlayer().getAddress().getHostString() + " at time " + sdf.format(cal.getTime()) + "\n");;
				SpamX.logFile.flush();
			} 
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			
			if(!SpamX.config.contains(name))
			{
				SpamX.config.set(name, 1);
			}
			else
			{
				SpamX.config.set(name, SpamX.config.getInt(name) + 1);
			}
			try 
			{
				SpamX.config.save(SpamX.file);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			if(SpamX.config.getInt(name) >= SpamX.config.getInt("banlimit"))
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ban " + name);
				
				try 
				{
					SpamX.logFile.write("Banned player " + name + " at ip address " + event.getPlayer().getAddress().getHostString() + " at time " + sdf.format(cal.getTime()) + "\n");
					SpamX.logFile.flush();
				} 
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
				
			}
			
			SpamX.spamHash.remove(name);
			
			return;
		}
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("SpamX"), new Runnable() 
		{

			public void run() 
			{
				SpamX.spamHash.put(name, SpamX.spamHash.get(name) + - 1);
		    }
		}, 20L);
	}
}
