package src;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class EventManager implements Listener 
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(PlayerChatEvent event)
	{
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
			}
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
