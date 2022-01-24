package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class StarterDeckContent implements Comparable<StarterDeckContent> {

	public static StarterDeckContent[] of(int starterDeckId) {
		ArrayList<StarterDeckContent> contents = new ArrayList<>();
		
		try (ResultSet res = DB.query("select * from `starterdeckcontents` where `starterDeckId` = ?;", starterDeckId)) {
			while (res.next()) contents.add(new StarterDeckContent(res));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Collections.sort(contents);
		return contents.toArray(new StarterDeckContent[0]);
	}
	
	//////////////////////////////////////////////
	
	private int deckContentsId;
	private int deckId;
	private StarterDeck deck;
	private int cardId;
	private Card card;
	private int quantity;
	private int sortIndex;
	
	private StarterDeckContent(ResultSet res) throws SQLException {
		deckContentsId = res.getInt("starterdeckcontents.deckContentsId");
		deckId = res.getInt("starterdeckcontents.starterDeckId");
		cardId = res.getInt("starterdeckcontents.cardId");
		quantity = res.getInt("starterdeckcontents.quantity");
		sortIndex = res.getInt("packcontents.sortIndex");
	}
	
	public int getDeckContentId() {
		return deckContentsId;
	}
	
	public int getStarterDeckId() {
		return deckId;
	}
	
	public StarterDeck getStarterDeck() {
		if (deck == null) deck = StarterDeck.of(deckId);
		return deck;
	}
	
	public int getCardId() {
		return cardId;
	}
	
	public Card getCard() {
		if (card == null) card = Card.of(cardId);
		return card;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	@Override
	public int compareTo(StarterDeckContent that) {
		int x = Integer.compare(this.sortIndex, that.sortIndex);
		if (x != 0) return x;
		return Integer.compare(this.deckContentsId, that.deckContentsId);
	}
}
