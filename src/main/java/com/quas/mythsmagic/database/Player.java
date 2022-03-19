package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.dv8tion.jda.api.entities.User;

public class Player {

	public static Player of(User user) {
		DB.update("insert into `players`(`playerId`, `playerName`) values(?, ?) on duplicate key update `playerName` = ?;", user.getIdLong(), user.getName(), user.getName());
		return of(user.getIdLong());
	}
	
	public static Player of(long playerId) {
		try (ResultSet res = DB.query("select * from `players` where `playerId` = ?;", playerId)) {
			if (res.next()) return new Player(res);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//////////////////////////////////////////////
	
	private long playerId;
	private String name;
	private long lastBonus;
	private long premium;
	private long money;
	private int starterBundleTickets;
	private int gamesWon;
	private int gamesTied;
	private int gamesLost;
	private int gamesPlayed;
	private double rating;
	
	private Player(ResultSet res) throws SQLException {
		playerId = res.getLong("players.playerId");
		name = res.getString("players.playerName");
		lastBonus = res.getLong("players.lastBonus");
		premium = res.getLong("players.premium");
		money = res.getLong("players.money");
		starterBundleTickets = res.getInt("players.starterBundleTickets");
		
		gamesWon = res.getInt("players.gamesWon");
		gamesTied = res.getInt("players.gamesTied");
		gamesLost = res.getInt("players.gamesLost");
		gamesPlayed = gamesWon + gamesTied + gamesLost;
		rating = res.getDouble("players.rating");
	}
	
	public long getPlayerId() {
		return playerId;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isPremium() {
		return premium >= System.currentTimeMillis();
	}
	
	public long getPremium() {
		return premium;
	}
	
	public long getLastBonus() {
		return lastBonus;
	}
	
	public long getMoney() {
		return money;
	}
	
	public int getStarterBundleTickets() {
		return starterBundleTickets;
	}
	
	public int getGamesWon() {
		return gamesWon;
	}
	
	public int getGamesTied() {
		return gamesTied;
	}
	
	public int getGamesLost() {
		return gamesLost;
	}
	
	public int getGamesPlayed() {
		return gamesPlayed;
	}
	
	public double getRating() {
		return rating;
	}
	
	public boolean hasValidDeck() {
		return true;
	}
}
