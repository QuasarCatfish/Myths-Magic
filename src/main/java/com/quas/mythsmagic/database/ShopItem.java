package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringJoiner;

public class ShopItem {

	public static ShopItem of(int shopId) {
		try (ResultSet res = DB.query("select * from `shop` where `shopId` = ?;", shopId)) {
			if (res.next()) return new ShopItem(res);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static ShopItem[] getActive() {
		ArrayList<ShopItem> shopItems = new ArrayList<>();
		try (ResultSet res = DB.query("select * from `shop` where `active`;")) {
			while (res.next()) shopItems.add(new ShopItem(res));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return shopItems.toArray(new ShopItem[0]);
	}
	
	//////////////////////////////////////////////
	
	private int shopId;
	private String shopName;
	private int starterDeckId;
	private StarterDeck starterDeck;
	private int packId1;
	private int packQuantity1;
	private int packId2;
	private int packQuantity2;
	private int packId3;
	private int packQuantity3;
	private boolean active;
	
	private int[] packIds;
	private Pack[] packs;
	private int[] packQuantities;
	
	private ShopItem(ResultSet res) throws SQLException {
		shopId = res.getInt("shop.shopId");
		shopName = res.getString("shop.shopName");
		starterDeckId = res.getInt("shop.starterDeckId");
		packId1 = res.getInt("shop.packId1");
		packQuantity1 = res.getInt("shop.packQuantity1");
		packId2 = res.getInt("shop.packId2");
		packQuantity2 = res.getInt("shop.packQuantity2");
		packId3 = res.getInt("shop.packId3");
		packQuantity3 = res.getInt("shop.packQuantity3");
		active = res.getBoolean("shop.active");
		
		if (packId1 == 0) {
			packIds = new int[] {};
			packQuantities = new int[] {};
		} else if (packId2 == 0) {
			packIds = new int[] { packId1 };
			packQuantities = new int[] { packQuantity1 };
		} else if (packId3 == 0) {
			packIds = new int[] { packId1, packId2 };
			packQuantities = new int[] { packQuantity1, packQuantity2 };
		} else {
			packIds = new int[] { packId1, packId2, packId3 };
			packQuantities = new int[] { packQuantity1, packQuantity2, packQuantity3 };
		}
	}
	
	public int getShopId() {
		return shopId;
	}
	
	public String getShopName() {
		return shopName;
	}
	
	public String getShopDescription() {
		StringJoiner sj = new StringJoiner("\n");
		if (hasStarterDeck()) sj.add(String.format("-- 1x %s", getStarterDeck().getName()));
		
		if (hasPacks()) {
			for (int q = 0; q < getPacks().length; q++) {
				sj.add(String.format("-- %,dx %s", getPackQuantities()[q], getPacks()[q].getName()));
			}
		}
		
		return sj.toString();
	}
	
	public boolean hasStarterDeck() {
		return starterDeckId > 0;
	}
	
	public int getStarterDeckId() {
		return starterDeckId;
	}
	
	public StarterDeck getStarterDeck() {
		if (starterDeck == null) starterDeck = StarterDeck.of(starterDeckId);
		return starterDeck;
	}
	
	public boolean hasPacks() {
		return packIds.length > 0;
	}
	
	public int[] getPackIds() {
		return packIds;
	}
	
	public Pack[] getPacks() {
		if (packs == null) {
			packs = new Pack[packIds.length];
			for (int q = 0; q < packIds.length; q++) packs[q] = Pack.of(packIds[q]);
		}
		
		return packs;
	}
	
	public int[] getPackQuantities() {
		return packQuantities;
	}
	
	public boolean isActive() {
		return active;
	}
}
