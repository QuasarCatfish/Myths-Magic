package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerCard {

	public static PlayerCard of(long playerId, int cardId) {
		try (ResultSet res = DB.query("select * from `playercards` where `playerId` = ? and `cardId` = ?;", playerId, cardId)) {
			if (res.next()) return new PlayerCard(res);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static PlayerCard[] of(long playerId) {
		ArrayList<PlayerCard> packs = new ArrayList<>();
		try (ResultSet res = DB.query("select * from `playercards` where `playerId` = ? and `quantity` > 0;", playerId)) {
			while (res.next()) packs.add(new PlayerCard(res));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return packs.toArray(new PlayerCard[0]);
	}
	
	//////////////////////////////////////////////
	
	private long playerId;
	private Player player;
	private int cardId;
	private Card card;
	private int quantity;
	
	private PlayerCard(ResultSet res) throws SQLException {
		playerId = res.getLong("playercards.playerId");
		cardId = res.getInt("playercards.cardId");
		quantity = res.getInt("playercards.quantity");
	}
	
	public long getPlayerId() {
		return playerId;
	}
	
	public Player getPlayer() {
		if (player == null) player = Player.of(playerId);
		return player;
	}
	
	public int getCardId() {
		return cardId;
	}
	
	public Card getPack() {
		if (card == null) card = Card.of(cardId);
		return card;
	}
	
	public int getQuantity() {
		return quantity;
	}
}
