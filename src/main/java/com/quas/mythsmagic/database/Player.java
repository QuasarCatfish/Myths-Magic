package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.dv8tion.jda.api.entities.User;

public class Player {

	public static Player of(User user) {
		Player p = of(user.getIdLong());
		
		// If player does not exist
		if (p == null) {
			DB.update("insert into `players`(`playerId`, `playerName`) values(?, ?);", user.getIdLong(), user.getName());
			p = of(user.getIdLong());
		}
		
		// If player has updated their name
		if (!user.getName().equals(p.getName())) {
			p.name = user.getName();
			DB.update("update `players` set `playerName` = ? where `playerId` = ?;", user.getName(), p.getPlayerId());
		}
		
		return p;
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
	private long money;
	private int starterBundleTickets;
	
	private Player(ResultSet res) throws SQLException {
		playerId = res.getLong("players.playerId");
		name = res.getString("players.playerName");
		lastBonus = res.getLong("players.lastBonus");
		money = res.getLong("players.money");
		starterBundleTickets = res.getInt("players.starterBundleTickets");
	}
	
	public long getPlayerId() {
		return playerId;
	}
	
	public String getName() {
		return name;
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
}
