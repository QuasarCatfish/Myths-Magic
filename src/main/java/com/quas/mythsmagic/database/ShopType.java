package com.quas.mythsmagic.database;

public enum ShopType {

	None("None"),
	Packs("Booster Packs"),
	StarterDecks("Starter Decks"),
	StarterBundles("Starter Bundles");
	
	private String name;
	private ShopType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
