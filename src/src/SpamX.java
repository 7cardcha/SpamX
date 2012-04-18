package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SpamX extends JavaPlugin
{
	public static File file;
	
	public static FileWriter log;
	
	public static BufferedWriter logFile;
	
	public static FileConfiguration config;
	
	public static HashMap<String, Integer> spamHash = new HashMap<String, Integer>();
	
	public void onEnable()
	{
		loadConfig();
		addDefaults();
		getServer().getPluginManager().registerEvents(new EventManager(), this);
	}
	
	public void onDisable()
	{
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		if(cmd.getName().equalsIgnoreCase("xReload"))
		{
			loadConfig();
			
			sender.sendMessage("Config reloaded.");
			
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("xOffense"))
		{
			if(args.length >= 1)
			{
				if(!config.contains(args[0]))
				{
					sender.sendMessage("Player hasn't logged in or he doesn't have any offenses.");
				}
				else
				{
					sender.sendMessage("Player has " + config.getInt(args[0]) + " offenses.");
				}
			}
			else
			{
				sender.sendMessage("Not enough arguments.");
			}
			
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("xSetOffense"))
		{
			if(args.length >= 2)
			{
				config.set(args[0], Integer.parseInt(args[1]));
				try 
				{
					config.save(file);
				} 
				catch (IOException e) 
				{
					sender.sendMessage("Failed to write to config file.");
					e.printStackTrace();
					return false;
				}
				
				sender.sendMessage("Player offense level set to " + args[1]);
			}
			else
			{
				sender.sendMessage("Not enough arguments.");
			}
			
			return true;
		}
		
		return false;
	}
	
	public static void loadConfig()
	{
		file = new File("SpamX.yml");
		if(!file.exists())
		{
			try 
			{
				file.createNewFile();
			} catch (IOException e) 
			{
				System.out.println("!!!!Failed to load config for SpamX!!!!");
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
		
		try {
			log = new FileWriter("spamx.log");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logFile = new BufferedWriter(log);
		
	}
	
	public static void addDefaults()
	{
		if(!config.contains("spamlimit"))
		{
			config.set("limit", 3);
		}
		if(!config.contains("banlimit"))
		{
			config.set("banlimit", 3);
		}
		try 
		{
			config.save(file);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
