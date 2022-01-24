package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Card {

	public static Card of(int cardId) {
		try (ResultSet res = DB.query("select * from `cards` where `cardId` = ?;", cardId)) {
			if (res.next()) return new Card(res);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Card[] values() {
		ArrayList<Card> cards = new ArrayList<>();
		
		try (ResultSet res = DB.query("select * from `cards`;")) {
			while (res.next()) cards.add(new Card(res));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return cards.toArray(new Card[0]);
	}
	
	//////////////////////////////////////////////
	
	private int cardId;
	private String name;
	private String flavorText;
	private CardType cardType;
	private CardRarity rarity;
	
	private Card(ResultSet res) throws SQLException {
		cardId = res.getInt("cards.cardId");
		name = res.getString("cards.cardName");
		flavorText = res.getString("cards.flavorText");
		cardType = CardType.valueOf(res.getString("cards.cardType"));
		rarity = CardRarity.valueOf(res.getString("cards.rarity"));
	}
	
	public int getCardId() {
		return cardId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFlavorText() {
		return flavorText;
	}
	
	public CardType getCardType() {
		return cardType;
	}
	
	public CardRarity getCardRarity() {
		return rarity;
	}
}
