
package comandos;

import main.Principal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Faction implements CommandExecutor{

	private Principal plugin;
	public Faction(Principal plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command comando, String label,
			String[] args) {
		if(!(sender instanceof Player)){
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "Only Player Command"));
			return false;
		}else{
			Player jugador = (Player) sender;
			
			if(args.length >= 1){
				if(args[0].equalsIgnoreCase("create")){
					if(args.length == 1 || args.length >= 3){
						jugador.sendMessage("Usage: /faction create <faction>");
					}else{
						if(plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")!=null){
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYa estas en una faction"));
						}else if(plugin.getFactions().getString("Factions."+args[1])!=null){
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYa existe una faction con el nombre de "+args[1]));
						}else{
							jugador.sendMessage("creaste la faction "+args[1]);
							plugin.getPlayers().set("Players."+jugador.getName()+".Faction", args[1]);
							plugin.getFactions().set("Factions."+args[1]+".Leader", jugador.getName());
							plugin.getFactions().set("Factions."+args[1]+".PvP", "false");
							if(plugin.getPlayers().getString("Players."+jugador.getName()+".Invitacion") != null){
								plugin.getPlayers().set("Players."+jugador.getName()+".Invitacion", null);
							}
							plugin.savePlayers();
							plugin.saveFactions();
						}
					}
						
					
				}else if(args[0].equalsIgnoreCase("leave")){
					if(plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")!=null){
						if(plugin.getFactions().getString("Factions."+plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")+".Leader").equals(jugador.getName())){
							jugador.sendMessage("Eres el lider usa /f disband para eliminar la faction");
						}else{
							for (Player players : Bukkit.getOnlinePlayers()){
								if(plugin.getPlayers().getString("Players."+players.getName()+".Faction").equals(plugin.getPlayers().getString("Players."+jugador.getName()+".Faction"))){
									players.sendMessage(jugador.getName()+" ha salido de la faction");
								}
							}
							plugin.getPlayers().set("Players."+jugador.getName()+".Faction", null);
							plugin.savePlayers();
						}
						
					}else{
						jugador.sendMessage("No tienes faction unete a una o creala");
					}
				}else if(args[0].equalsIgnoreCase("disband")){
					if(plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")!=null){
						String getfaction = "Players."+jugador.getName()+".Faction";
						if(plugin.getFactions().getString("Factions."+plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")+".Leader").equals(jugador.getName())){
							plugin.getFactions().set("Factions."+plugin.getPlayers().getString(getfaction), null);
							jugador.sendMessage("Eliminaste tu faction");
							for(Player players : Bukkit.getOnlinePlayers()){
								if(plugin.getFactions().getString("Factions."+plugin.getPlayers().getString("Players."+players.getName()+".Faction")) == null){
									plugin.getPlayers().set("Players."+players.getName()+".Faction", null);
									plugin.savePlayers();
									plugin.saveFactions();
								}
							}
						}else{
							jugador.sendMessage("No eres el lider");
						}
					}else{
						jugador.sendMessage("No tienes una faction");
					}
				}else if(args[0].equalsIgnoreCase("invite")){
					if(args.length == 1 || args.length >= 3){
						jugador.sendMessage("Usage: /faction invite <user>");
					}else{
						if(plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")!=null){
							if(plugin.getFactions().getString("Factions."+plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")+".Leader").equals(jugador.getName())){
								Player jugadorinvitado = Bukkit.getPlayer(args[1]);
								if(jugadorinvitado == null){
									jugador.sendMessage("Este jugador no esta en linea");
								}else{
									if(plugin.getPlayers().getString("Players."+jugadorinvitado.getName()+".Faction")!=null){
										jugador.sendMessage("Est jugador ya tiene una faction");
									}else{
										if(plugin.getPlayers().getString("Players."+jugadorinvitado.getName()+".Invitacion")!=null){
											jugador.sendMessage("Este jugador ya tiene una invitacion");
										}else{
											jugador.sendMessage("Haz invitado al jugador "+jugadorinvitado.getName());
											jugadorinvitado.sendMessage("Haz sido invitado a la faction "+plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")+"usa '/f accept' para unirte");
											String faction = "Players."+jugador.getName()+".Faction";
											plugin.getPlayers().set("Players."+jugadorinvitado.getName()+".Invitacion", plugin.getPlayers().getString(faction));
											plugin.savePlayers();
										}
									}
								}
							}else{
								jugador.sendMessage("No eres el lider de esta faction");
							}
						}else{
							jugador.sendMessage("No tienes una faction");
						}
					}
				}else if(args[0].equalsIgnoreCase("accept")){
					if(args.length == 1){
						if(plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")!=null){
							jugador.sendMessage("Ya tienes una faction");
						}else{
							if(plugin.getPlayers().getString("Players."+jugador.getName()+".Invitacion")!=null){
								if(plugin.getFactions().getString("Factions."+plugin.getPlayers().getString("Players."+jugador.getName()+".Invitacion"))!= null){
									plugin.getPlayers().set("Players."+jugador.getName()+".Faction", plugin.getPlayers().getString("Players."+jugador.getName()+".Invitacion"));
									plugin.getPlayers().set("Players."+jugador.getName()+".Invitacion", null);
									plugin.savePlayers();
									jugador.sendMessage("Te uniste a la faction "+plugin.getPlayers().getString("Players."+jugador.getName()+".Faction"));
								}else{
									jugador.sendMessage("Esta faction ya no existe");
									plugin.getPlayers().set("Players."+jugador.getName()+".Invitacion", null);
								}
							}else{
								jugador.sendMessage("No tienes ninguna invitacion");
							}
						}
					}else{
						jugador.sendMessage("Usage: /f accept");
					}
				}else if(args[0].equalsIgnoreCase("show")){
					if(args.length == 1){
						String getfaction = "Players."+jugador.getName()+".Faction";
						if(plugin.getPlayers().getString(getfaction)!=null){
							jugador.sendMessage("tu faction es "+plugin.getPlayers().getString(getfaction));
						}else{
							jugador.sendMessage("No tienes faction");
						}
					}else{
						jugador.sendMessage("Usage: /faction show");
					}
				}else if(args[0].equalsIgnoreCase("pvp")){
					if(args.length == 1 || args.length >= 3){
						jugador.sendMessage("Usage: /faction pvp <enable>|<disable>");
					}else{
						if(plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")!=null){
							if(plugin.getFactions().getString("Factions."+plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")+".Leader").equals(jugador.getName())){
								if(args.length == 2){
									if(args[1].equalsIgnoreCase("enable")){
										plugin.getFactions().set("Factions."+plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")+".PvP", "true");
										plugin.saveFactions();
										jugador.sendMessage("Faction pvp has enabled");
									}else if(args[1].equalsIgnoreCase("disable")){
										plugin.getFactions().set("Factions."+plugin.getPlayers().getString("Players."+jugador.getName()+".Faction")+".PvP", "false");
										plugin.saveFactions();
										jugador.sendMessage("Faction pvp has disabled");
									}else{
										jugador.sendMessage("Invalid argument!");
									}
								}
							}else{
								jugador.sendMessage("No eres el lider");
							}
						}else{
							jugador.sendMessage("No tienes faction");
						}
					}
				}else if(args[0].equalsIgnoreCase("help")){
					jugador.sendMessage("");
					jugador.sendMessage("");
					jugador.sendMessage("");
					jugador.sendMessage("");
					jugador.sendMessage("");
					jugador.sendMessage("");
					jugador.sendMessage("");
					jugador.sendMessage("");
					jugador.sendMessage("");
					jugador.sendMessage("");
					jugador.sendMessage("Faction Test Plugin");
					jugador.sendMessage("--------------------");
					jugador.sendMessage("/faction create <faction>");
					jugador.sendMessage("/faction invite <player>");
					jugador.sendMessage("/faction accept");
					jugador.sendMessage("/faction show");
					jugador.sendMessage("/faction pvp <enable>|<disable>");
					jugador.sendMessage("/faction leave");
					jugador.sendMessage("/faction disband");
					jugador.sendMessage("--------------------");
				}else{
					jugador.sendMessage("/faction help");
				}
		}else{
			jugador.sendMessage("/faction help");
		}
	}
		return true;
	}
}
