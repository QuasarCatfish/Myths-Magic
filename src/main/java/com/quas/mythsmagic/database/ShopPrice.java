package com.quas.mythsmagic.database;

import com.quas.mythsmagic.util.Util;

public enum ShopPrice {

	None(0, 0),
	StarterBundle(0, 1),
	;
	
	private int jewels;
	private int starterBundleTickets;
	private ShopPrice(int jewels, int starterBundleTickets) {
		this.jewels = jewels;
		this.starterBundleTickets = starterBundleTickets;
	}
	
	public boolean isBuyableWithJewels() {
		return jewels > 0;
	}
	
	public int getJewelsPrice() {
		return jewels;
	}
	
	public boolean isBuyableWithStarterBundleTickets() {
		return starterBundleTickets > 0;
	}
	
	public int getStarterBundleTicketsPrice() {
		return starterBundleTickets;
	}
	
	@Override
	public String toString() {
		if (starterBundleTickets > 0) return Util.quantity(starterBundleTickets, "Starter Bundle Ticket", "s");
		if (jewels > 0) return Util.quantity(jewels, "Jewel", "s");
		return "Not Purchasable";
	}
}
