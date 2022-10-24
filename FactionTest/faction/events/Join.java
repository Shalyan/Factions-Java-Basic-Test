package events;

import main.Principal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener{
	
	private Principal plugin;
	public Join(Principal plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoinGetFaction(PlayerJoinEvent event){
		Player jugador = event.getPlayer();
		if(plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")!=null){
			if(plugin.getFactions().getString("Factions."+plugin.getPlayers().getString("Players."+jugador.getName()+".Faction"))!= null){
				return;
			}else{
				jugador.sendMessage("La faction en la que estabas ha sido eliminada");
				plugin.getPlayers().set("Players."+jugador.getName()+".Faction", null);
				plugin.savePlayers();
				return;
			}
		}else{
			return;
		}
		
	}
	
	@EventHandler
	public void onJoinMessageFaction(PlayerJoinEvent event){
		Player jugador = event.getPlayer();
		for(Player players : Bukkit.getOnlinePlayers()){
			if(plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")!=null){
				if(plugin.getPlayers().getString("Players."+players.getName()+".Faction").equals(plugin.getPlayers().getString("Players."+jugador.getName()+".Faction"))){
					players.sendMessage("[Faction] "+jugador.getName()+" Se ha unido");
				}
			}
		}
	}

}
