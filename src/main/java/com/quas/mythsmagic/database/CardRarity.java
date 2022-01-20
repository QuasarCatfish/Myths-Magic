package com.quas.mythsmagic.database;

public enum CardRarity {

	Unobtainable("Unobtainable"),
	Common("Common"),
	Uncommon("Uncommon"),
	Rare("Rare"),
	DivineRare("Divine Rare"),
	;
	
	private String name;
	private CardRarity(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
