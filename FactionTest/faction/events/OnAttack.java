package events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import main.Principal;

public class OnAttack implements Listener{

	private Principal plugin;
	public OnAttack(Principal plugin){
		this.plugin = plugin;
	}
	@EventHandler
	public void onAttackGetFaction(EntityDamageByEntityEvent event){
		if(((event.getDamager() instanceof Player)) && ((event.getEntity() instanceof Player))){
			final Player player = (Player)event.getEntity();
		    final Player target = (Player)event.getDamager();
		    if(plugin.getPlayers().getString("Players."+target.getName()+".Faction")!=null){
		    	if(plugin.getFactions().getString("Factions."+plugin.getPlayers().getString("Players."+target.getName()+".Faction")+".PvP").equals("false")){
		    		if(plugin.getPlayers().getString("Players."+player.getName()+".Faction") == plugin.getPlayers().getString("Players."+target.getName()+".Faction")){
				    	event.setCancelled(true);
				    	target.sendMessage("No puedes da–ar a este jugador porque es de tu faction");
				    	return;
				    }
		    	}
		    }
		    
		}
	}
}
