package com.brandonmabey.goldshop;


import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin{

	@Override
	public void onEnable() {
		this.getLogger().info("GoldShop loaded");
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info("GoldShop unloaded");
	}


	
	
}
