package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerStarterDeck {

	public static PlayerStarterDeck of(long playerId, int starterDeckId) {
		try (ResultSet res = DB.query("select * from `playerstarterdecks` where `playerId` = ? and `starterDeckId` = ?;", playerId, starterDeckId)) {
			if (res.next()) return new PlayerStarterDeck(res);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static PlayerStarterDeck[] of(long playerId) {
		ArrayList<PlayerStarterDeck> packs = new ArrayList<>();
		try (ResultSet res = DB.query("select * from `playerstarterdecks` where `playerId` = ? and `quantity` > 0;", playerId)) {
			while (res.next()) packs.add(new PlayerStarterDeck(res));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return packs.toArray(new PlayerStarterDeck[0]);
	}
	
	//////////////////////////////////////////////
	
	private long playerId;
	private Player player;
	private int starterDeckId;
	private StarterDeck starterDeck;
	private int quantity;
	
	private PlayerStarterDeck(ResultSet res) throws SQLException {
		playerId = res.getLong("playerstarterdecks.playerId");
		starterDeckId = res.getInt("playerstarterdecks.starterDeckId");
		quantity = res.getInt("playerstarterdecks.quantity");
	}
	
	public long getPlayerId() {
		return playerId;
	}
	
	public Player getPlayer() {
		if (player == null) player = Player.of(playerId);
		return player;
	}
	
	public int getStarterDeckId() {
		return starterDeckId;
	}
	
	public StarterDeck getStarterDeck() {
		if (starterDeck == null) starterDeck = StarterDeck.of(starterDeckId);
		return starterDeck;
	}
	
	public int getQuantity() {
		return quantity;
	}
}
