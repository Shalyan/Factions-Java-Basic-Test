package main;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


import comandos.Faction;
import events.Join;
import events.OnAttack;


public class Principal extends JavaPlugin{
	
	private FileConfiguration factions = null;
	private File factionsFile = null;
	private File playersFile = null;
	private FileConfiguration players = null;
	PluginDescriptionFile pdffile = getDescription();
	
	public String configpath;
	
	public String name = pdffile.getName();
	public String version = pdffile.getVersion();
	
	public void onEnable(){
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c"+ name + "&f ON"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVersion &f " + version));
		Bukkit.getConsoleSender().sendMessage("");
		ConfigRegister();
		comandosreg();
		registerFactions();
		registerPlayers();
		eregister();
	}
	
	
	public void comandosreg(){
		this.getCommand("faction").setExecutor(new Faction(this));
		this.getCommand("f").setExecutor(new Faction(this));
	}
	
	public void eregister(){
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new OnAttack(this), (this));
		pm.registerEvents(new Join(this), (this));
	}
	
	public void ConfigRegister(){
		File config = new File(this.getDataFolder(), "config.yml");
		configpath = config.getPath();
		if(!config.exists()){
			saveDefaultConfig();
		}
	}
	
	public FileConfiguration getFactions(){
		if(factions == null){
			reloadFactions();
		}
		return factions;
	}
	
	public void reloadFactions(){
		if (factions == null){
			factionsFile = new File(getDataFolder(), "factions.yml");
			
		}
		factions = YamlConfiguration.loadConfiguration(factionsFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("factions.yml"), "UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				factions.setDefaults(defConfig);
			}
			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	public void saveFactions(){
		try{
			factions.save(factionsFile);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void registerFactions(){
		factionsFile = new File(this.getDataFolder(), "factions.yml");
		if(!factionsFile.exists()){
			this.getFactions().options().copyDefaults(true);
			saveFactions();
		}
	}
	public FileConfiguration getPlayers(){
		if(players == null){
			reloadPlayers();
		}
		return players;
	}
	
	public void reloadPlayers(){
		if (players == null){
			playersFile = new File(getDataFolder(), "players.yml");
			
		}
		players = YamlConfiguration.loadConfiguration(playersFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(this.getResource("players.yml"), "UTF8");
			if(defConfigStream != null){
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				players.setDefaults(defConfig);
			}
			
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	public void savePlayers(){
		try{
			players.save(playersFile);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void registerPlayers(){
		playersFile = new File(this.getDataFolder(), "players.yml");
		if(!playersFile.exists()){
			this.getPlayers().options().copyDefaults(true);
			savePlayers();
		}
	}
	
	public void onDisable(){
		Bukkit.getServer().savePlayers();
	}

}
