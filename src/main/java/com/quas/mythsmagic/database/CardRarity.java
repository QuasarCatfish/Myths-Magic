package com.quas.mythsmagic.database;

public enum CardRarity {

	Unobtainable("Unobtainable"),
	DivineRare("Divine Rare"),
	EpicRare("Epic Rare"),
	Rare("Rare"),
	Uncommon("Uncommon"),
	Common("Common"),
	;
	
	private String name;
	private CardRarity(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
