package com.brandonmabey.goldshop;

public class GoldShopSignData {
	public String blockName;
	public int blockID;
	public int blockMetadata;
	public boolean blockMetadataMatters;
	public int buyAmount;
	public int buyPrice;
	public int sellAmount;
	public int sellPrice;
	
	public GoldShopSignData(String blockName, int blockID, int blockMetadata, boolean blockMetadataMatters, int buyAmount, int buyPrice, int sellAmount, int sellPrice) {
		super();
		this.blockName = blockName;
		this.blockID = blockID;
		this.blockMetadata = blockMetadata;
		this.blockMetadataMatters = blockMetadataMatters;
		this.buyAmount = buyAmount;
		this.buyPrice = buyPrice;
		this.sellAmount = sellAmount;
		this.sellPrice = sellPrice;
	}
	
	public GoldShopSignData(String blockName, int blockID, int buyAmount, int buyPrice, int sellAmount, int sellPrice) {
		super();
		this.blockName = blockName;
		this.blockID = blockID;
		this.buyAmount = buyAmount;
		this.buyPrice = buyPrice;
		this.blockMetadata = 0;
		this.blockMetadataMatters = false;
		this.sellAmount = sellAmount;
		this.sellPrice = sellPrice;
	}
	
	
}
