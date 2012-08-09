package com.brandonmabey.goldshop;


import org.bukkit.Material;
import org.bukkit.block.Sign;
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
			Sign sign = (Sign) e.getClickedBlock().getState();
			String lines[] = sign.getLines();
			
			this.getLogger().info(lines[0]);
			
			if (lines[0].equalsIgnoreCase("[gold]")) {
				this.getLogger().info("Recognized as gold shop.");
				int xLoc = e.getClickedBlock().getX();
				int yLoc = e.getClickedBlock().getY();
				int zLoc = e.getClickedBlock().getZ();
				
				String itemLine[] = lines[1].split(";");
				if (itemLine.length != 2) {
					this.getLogger().warning("Purchase sign has incorrect number of arguments on line 2 at " + xLoc + "," + yLoc + "," + zLoc);
					return;
				}
				this.getLogger().info("Item Name: " + itemLine[0] + " Item ID: " + itemLine[1] + " is selected at " + xLoc + "," + yLoc + "," + zLoc);
				
				
				
			}
			
			
		}
	}


	
	
}
