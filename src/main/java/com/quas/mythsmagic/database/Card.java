package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Card {

	public static Card of(int cardId) {
		try (ResultSet res = DB.query("select * from `cards` where `cardId` = ?;", cardId)) {
			if (res.next()) return new Card(res);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//////////////////////////////////////////////
	
	private int cardId;
	private String name;
	
	private Card(ResultSet res) throws SQLException {
		cardId = res.getInt("cards.cardId");
		name = res.getString("cards.cardName");
	}
	
	public int getCardId() {
		return cardId;
	}
	
	public String getName() {
		return name;
	}
}
