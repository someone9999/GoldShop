package com.brandonmabey.goldshop;


import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerInteractEvent;

public class Core extends JavaPlugin implements Listener{

	@Override
	public void onEnable() {
		this.getLogger().info("GoldShop loaded");
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info("GoldShop unloaded");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
			if (command.getName().equalsIgnoreCase("lol")) {
				player.getInventory().addItem(new ItemStack(50,1));
				return true;
			}
		}
		return false;
	}

	public static void main(String args[]) {
		
	}
	
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent e) {
		if (e.getClickedBlock().getType() == Material.SIGN_POST) {
			this.getLogger().warning("You have clicked a sign");
		}
	}


	
	
}
