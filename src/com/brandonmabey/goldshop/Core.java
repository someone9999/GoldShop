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
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Core extends JavaPlugin implements Listener{

	public static Material CURRENCY = Material.GOLD_INGOT;
	
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
				
				int itemID;
				try {
					itemID = Integer.valueOf(itemLine[1]);
				} catch (Exception ex) {
					this.getLogger().warning("Line 2 has bad syntax for purcahse sign at " + xLoc + ";" + yLoc + ";" + zLoc);
					return;
				}
				this.getLogger().info("Item Name: " + itemLine[0] + " Item ID: " + itemLine[1] + " is selected at " + xLoc + "," + yLoc + "," + zLoc);
				
				
				
				
				String buyLine[] = lines[2].split(";");
				if (buyLine.length != 3) {
					this.getLogger().warning("Purchase sign has incorrect number of arguments on line 3 at " + xLoc + "," + yLoc + "," + zLoc);
					return;
				}
				
				int amountBuy;
				int priceBuy;
				try {
					amountBuy = Integer.valueOf(buyLine[1]);
					priceBuy = Integer.valueOf(buyLine[2].substring(0, buyLine[2].length() - 1));
				} catch (Exception ex) {
					this.getLogger().warning("Purchase sign has bad syntax on line 3 at " + xLoc + "," + yLoc + "," + zLoc);
					return;
				}
				
				if (amountBuy == 0 && priceBuy != 0) {
					this.getLogger().warning("Purchase sign is buying 0 items for a price at " + xLoc + "," + yLoc + "," + zLoc);
					return;
				}
				this.getLogger().info("Purchase sign is " + amountBuy + " for " + priceBuy + "gold");
				
				
				String sellLine[] = lines[3].split(";");
				if (sellLine.length != 3) {
					this.getLogger().warning("Purchase sign has incorrect number of arguments on line 4 at " + xLoc + "," + yLoc + "," + zLoc);
					return;
				}
				
				int amountSell;
				int priceSell;
				try {
					amountSell = Integer.valueOf(sellLine[1]);
					priceSell = Integer.valueOf(sellLine[2].substring(0, sellLine[2].length() - 1));
				} catch (Exception ex) {
					this.getLogger().warning("Purchase sign has bad syntax on line 4 at " + xLoc + "," + yLoc + "," + zLoc);
					return;
				}
				
				if (amountBuy == 0 && priceBuy != 0) {
					this.getLogger().warning("Purchase sign is selling 0 items for a price at " + xLoc + "," + yLoc + "," + zLoc);
					return;
				}
				this.getLogger().info("Purchase sign is selling " + amountSell + " for " + priceSell + "gold");
				
				
				
				boolean leftClick = (e.getAction() == Action.LEFT_CLICK_BLOCK); //buy
				Player p = e.getPlayer();
				if (leftClick) {
					ItemStack heldStack = p.getInventory().getItemInHand();
					
					if (heldStack.getType() != CURRENCY) {
						p.sendMessage("Wrong item in hand to purchase");
						this.getLogger().info("Player " + p.getDisplayName() + "tried to purchase with wrong item in hand" + itemLine[0] + " at " + xLoc + "," + yLoc + "," + zLoc);
						return;
					}
					
					if (heldStack.getAmount() < priceBuy) {
						p.sendMessage("Not enough gold in hand to purchase");
						this.getLogger().info("Player " + p.getDisplayName() + "tried to purchase with wrong amount of currency in hand" + itemLine[0] + " at " + xLoc + "," + yLoc + "," + zLoc);
						return;
					}
					
					if (heldStack.getAmount() == priceBuy) {
						p.getInventory().setItemInHand(null);
					} else {
						heldStack.setAmount(heldStack.getAmount() - priceBuy);
					}
					p.getInventory().addItem(new ItemStack(itemID, amountBuy));
					p.sendMessage("Item purchased!");
					return;
					
				} else {
					
				}
			}
			
			
		}
	}


	
	
}
