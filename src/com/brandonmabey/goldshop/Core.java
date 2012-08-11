package com.brandonmabey.goldshop;


import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Core extends JavaPlugin implements Listener{

	public static Material CURRENCY = Material.GOLD_INGOT;
	public static final String CURRENCY_NAME = "Gold Ingot";
	
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
		sender.sendMessage(getSyntaxMessage());
		return true;
	}

	public static void main(String args[]) {
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent e) {
		if (e.getClickedBlock() == null) {
			return;
		}
		if (e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN) {
			Sign sign = (Sign) e.getClickedBlock().getState();
			String lines[] = stripColors(sign.getLines());
			
			
			if (lines[0].equalsIgnoreCase("[gold]")) {
				this.getLogger().info("Recognized as gold shop.");
				int xLoc = e.getClickedBlock().getX();
				int yLoc = e.getClickedBlock().getY();
				int zLoc = e.getClickedBlock().getZ();
				
				String itemLine[] = lines[1].split(";");
				if (itemLine.length != 2 && itemLine.length != 3) {
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
				int metadata = 0;
				if (itemLine.length == 3) {
					try {
						metadata = Integer.valueOf(itemLine[2]);
					} catch (Exception ex) {
						this.getLogger().warning("Line 2 had bad syntax for purchase sign at " + getLocationString(xLoc, yLoc, zLoc));
					}
				}
				
				
				
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
				this.getLogger().fine("Purchase sign is selling " + amountSell + " for " + priceSell + "gold");
				
				
				
				boolean leftClick = (e.getAction() == Action.LEFT_CLICK_BLOCK); //buy
				Player p = e.getPlayer();
				if (leftClick) {
					ItemStack heldStack = p.getInventory().getItemInHand();
					if (priceBuy != 0 && heldStack == null) {
						p.sendMessage(ChatColor.GRAY + "Wrong item in hand to purchase. Use " + ChatColor.WHITE + priceBuy + " " + ChatColor.YELLOW + CURRENCY_NAME + "(s)" + ChatColor.GRAY + ".");
						this.getLogger().fine("Player " + p.getDisplayName() + "tried to purchase itemLine[0] with nothing in hand at " + xLoc + "," + yLoc + "," + zLoc);
						return;
					}
					
					if (priceBuy != 0 && heldStack.getType() != CURRENCY) {
						p.sendMessage(ChatColor.GRAY + "Wrong item in hand to purchase. Use " + ChatColor.WHITE + priceBuy + " " + ChatColor.YELLOW + CURRENCY_NAME + "(s)" + ChatColor.GRAY + ".");
						this.getLogger().fine("Player " + p.getDisplayName() + "tried to purchase with wrong item in hand" + itemLine[0] + " at " + xLoc + "," + yLoc + "," + zLoc);
						return;
					}
					
					if (priceBuy != 0 && heldStack.getAmount() < priceBuy) {
						p.sendMessage(ChatColor.GRAY + "Not enough " + ChatColor.YELLOW + CURRENCY_NAME + "s " + ChatColor.GRAY + "to buy. You need " + ChatColor.WHITE + priceBuy + ChatColor.GRAY + ".");
						this.getLogger().fine("Player " + p.getDisplayName() + "tried to purchase with wrong amount of currency in hand" + itemLine[0] + " at " + xLoc + "," + yLoc + "," + zLoc);
						return;
					}
					
					if (priceBuy != 0 && heldStack.getAmount() == priceBuy) {
						p.getInventory().setItemInHand(null);
					} else {
						heldStack.setAmount(heldStack.getAmount() - priceBuy);
						p.getInventory().setItemInHand(heldStack);
					}
					p.getInventory().addItem(new ItemStack(itemID, amountBuy, (short) metadata));
					p.sendMessage(ChatColor.GREEN + "Purchased " + ChatColor.YELLOW + itemLine[0] + ChatColor.GREEN + "!");
					p.updateInventory();
					this.getLogger().fine("Player " + p.getDisplayName() + "bought " + itemLine[0]);
					
					return;
					
				} else {
					ItemStack heldStack = p.getItemInHand();
					
					if (heldStack != null && heldStack.getTypeId() != 0 && heldStack.getType() != CURRENCY) {
						p.sendMessage(ChatColor.GRAY + "Wrong item in hand to Sell. Use a " + ChatColor.YELLOW + CURRENCY_NAME + ChatColor.GRAY + " or your " + ChatColor.YELLOW + "fists" + ChatColor.GRAY + ".");
						this.getLogger().fine("Player " + p.getDisplayName() + "tried to sell with wrong item in hand" + itemLine[0] + " at " + xLoc + "," + yLoc + "," + zLoc);
						return;
					}
						
					if (getItemIDWithAmount(itemID, amountSell, metadata, (itemLine.length == 3), p)) {
						p.sendMessage(ChatColor.RED + "Sold " + ChatColor.YELLOW + itemLine[0] + ChatColor.RED + "!");
						p.getInventory().addItem(new ItemStack(CURRENCY, priceSell));
						p.updateInventory();
						this.getLogger().fine("Player " + p.getDisplayName() + " sold " + itemLine[0]);
						return;
					}
					
					p.sendMessage(ChatColor.GRAY + "Not enough " + ChatColor.YELLOW +  itemLine[0] + "(s)" + ChatColor.GRAY + " in inventory to sell. You need " + ChatColor.WHITE + amountSell + ChatColor.GRAY + ".");
					this.getLogger().fine("Player " + p.getDisplayName() + " tried to sell with not enough items in inventory at " + xLoc + "," + yLoc + "," + zLoc);
					return;
				}
			}
			
			
		}
	}
	
	public boolean verifySignText(String[] lines, int xLoc, int yLoc, int zLoc) {
		lines = stripColors(lines);
		if (lines[0].equalsIgnoreCase("[gold]")) {
			this.getLogger().info("Recognized as gold shop.");
			
			String itemLine[] = lines[1].split(";");
			if (itemLine.length != 2 && itemLine.length != 3) {
				this.getLogger().warning("Purchase sign has incorrect number of arguments on line 2 at " + xLoc + "," + yLoc + "," + zLoc);
				return false;
			}
			
			try {
				Integer.valueOf(itemLine[1]);
			} catch (Exception ex) {
				this.getLogger().warning("Line 2 has bad syntax for purcahse sign at " + xLoc + ";" + yLoc + ";" + zLoc);
				return false;
			}
			this.getLogger().info("Item Name: " + itemLine[0] + " Item ID: " + itemLine[1] + " is selected at " + xLoc + "," + yLoc + "," + zLoc);
			if (itemLine.length == 3) {
				try {
					Integer.valueOf(itemLine[2]);
				} catch (Exception ex) {
					this.getLogger().warning("Line 2 had bad syntax for purchase sign at " + getLocationString(xLoc, yLoc, zLoc));
					return false;
				}
			}
			
			
			
			
			String buyLine[] = lines[2].split(";");
			if (buyLine.length != 3) {
				this.getLogger().warning("Purchase sign has incorrect number of arguments on line 3 at " + xLoc + "," + yLoc + "," + zLoc);
				return false;
			}
			
			int amountBuy;
			int priceBuy;
			try {
				amountBuy = Integer.valueOf(buyLine[1]);
				priceBuy = Integer.valueOf(buyLine[2].substring(0, buyLine[2].length() - 1));
			} catch (Exception ex) {
				this.getLogger().warning("Purchase sign has bad syntax on line 3 at " + xLoc + "," + yLoc + "," + zLoc);
				return false;
			}
			
			if (amountBuy == 0 && priceBuy != 0) {
				this.getLogger().warning("Purchase sign is buying 0 items for a price at " + xLoc + "," + yLoc + "," + zLoc);
				return false;
			}
			this.getLogger().info("Purchase sign is " + amountBuy + " for " + priceBuy + "gold");
			
			
			String sellLine[] = lines[3].split(";");
			if (sellLine.length != 3) {
				this.getLogger().warning("Purchase sign has incorrect number of arguments on line 4 at " + xLoc + "," + yLoc + "," + zLoc);
				return false;
			}
			
			int amountSell;
			int priceSell;
			try {
				amountSell = Integer.valueOf(sellLine[1]);
				priceSell = Integer.valueOf(sellLine[2].substring(0, sellLine[2].length() - 1));
			} catch (Exception ex) {
				this.getLogger().warning("Purchase sign has bad syntax on line 4 at " + xLoc + "," + yLoc + "," + zLoc);
				return false;
			}
			
			if (amountBuy == 0 && priceBuy != 0) {
				this.getLogger().warning("Purchase sign is selling 0 items for a price at " + xLoc + "," + yLoc + "," + zLoc);
				return false;
			}
			this.getLogger().fine("Purchase sign is selling " + amountSell + " for " + priceSell + "gold");
			return true;
			
		} else {
			return false;
		}
	}
	
	public boolean getItemIDWithAmount(int ID, int amount, int metadata, boolean metadataMatters, Player p) {
		ArrayList<Integer> spots = new ArrayList<Integer>();
		
		HashMap<Integer, ? extends ItemStack> hm = p.getInventory().all(ID);
		for (int slotID : hm.keySet()) {
			if (metadataMatters && hm.get(slotID).getData().getData() != metadata) {
				continue;
			}
			
			if (hm.get(slotID).getAmount() < amount) {
				amount -= hm.get(slotID).getAmount();
				spots.add(slotID);
				continue;
			} else {
				for (int i = 0; i < spots.size(); i++) {
					p.getInventory().setItem(spots.get(i), null);
				}
				if (hm.get(slotID).getAmount() == amount) {
					p.getInventory().setItem(slotID, null);
				} else {
					p.getInventory().getItem(slotID).setAmount(p.getInventory().getItem(slotID).getAmount() - amount);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		Player p = e.getPlayer();
		
		String lines[] = stripColors(e.getLines());
		
		if (lines[0].equalsIgnoreCase("[gold]")) {
			int x = e.getBlock().getX();
			int y = e.getBlock().getY();
			int z = e.getBlock().getZ();
			
			if (!p.isOp()) {
				p.sendMessage(ChatColor.RED + "You do not have permission to create a gold shop sign.");
				this.getLogger().info(p.getDisplayName() + " tried to create a gold shop without permission at " + getLocationString(x,y,z));
				e.setLine(0, "");
			} else {
				if (verifySignText(lines, x, y, z)) {
					
					e.setLine(0, ChatColor.GOLD + lines[0]);
					return;
				} else {
					p.sendMessage(ChatColor.RED + "Incorrect gold shop syntax.");
					sendSyntaxMessage(p);
					e.setLine(0,"");
					e.setLine(1, "");
					e.setLine(2, "");
					e.setLine(3, "");
				}
			}
		}
	}
	
	private void sendSyntaxMessage(Player p) {
		
		p.sendMessage(getSyntaxMessage());
	}
	
	private String[] getSyntaxMessage() {
		return new String[] {
				ChatColor.GRAY + "Gold shop syntax must be as follows: ",
				ChatColor.GRAY + "Anything in red [] brackets is optional.",
				ChatColor.GRAY + "Anything in white is non-variable text.",
				ChatColor.GRAY + "Anything in yellow is variable text.",
				ChatColor.WHITE + "[gold]",
				ChatColor.YELLOW + "<Block Name>" + ChatColor.WHITE + ";" + ChatColor.YELLOW + "<Block ID>" + ChatColor.RED + "[" + ChatColor.WHITE + ";" + ChatColor.YELLOW + "<metadata>" + ChatColor.RED + "]",
				ChatColor.WHITE + "buy;" + ChatColor.YELLOW + "<amount>" + ChatColor.WHITE + ";" + ChatColor.YELLOW + "<price>" + ChatColor.WHITE + "g",
				ChatColor.WHITE + "sell;" + ChatColor.YELLOW + "<amount>" + ChatColor.WHITE + ";" + ChatColor.YELLOW + "<payment>" + ChatColor.WHITE + "g"
		};
	}
	
	private String getLocationString(int x, int y, int z) {
		return x + "," + y + "," + z;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.getBlock().getType() != Material.SIGN_POST && e.getBlock().getType() != Material.WALL_SIGN) {
//			this.getLogger().info("A block that is not a sign was destroyed at " + e.getBlock().getX() + "," + e.getBlock().getY() + "," + e.getBlock().getZ());
			return;
		}
		
		Sign sign = (Sign) e.getBlock().getState();
		String lines[] = stripColors(sign.getLines());
		
		if (lines[0].equalsIgnoreCase("[gold]")) {
			if (!e.getPlayer().isOp()) {
				e.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to destroy a gold shop sign");
				e.setCancelled(true);
				} else {
				e.getPlayer().sendMessage(ChatColor.GRAY + "Destroyed gold shop sign.");
			}
		}
	}

	private String[] stripColors(String[] lines) {
		for (int i = 0; i < 4; i++) {
			lines[i] = ChatColor.stripColor(lines[i]);
		}
		
		return lines;
	}


	
	
}
