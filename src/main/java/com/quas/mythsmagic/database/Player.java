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
			DB.update("update `players` set `playerName` = ? where `playerId` = ?;", user.getName(), p.getId());
		}
		
		return p;
	}
	
	public static Player of(long id) {
		try (ResultSet res = DB.query("select * from `players` where `playerId` = ?;", id)) {
			if (res.next()) {
				return new Player(res);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//////////////////////////////////////////////
	
	private long id;
	private String name;
	private long money;
	private long lastBonus;
	
	private Player(ResultSet res) throws SQLException {
		id = res.getLong("players.playerId");
		name = res.getString("players.playerName");
		money = res.getLong("players.money");
		lastBonus = res.getLong("players.lastBonus");
	}
	
	public String getId() {
		return Long.toString(id);
	}
	
	public long getIdLong() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public long getMoney() {
		return money;
	}
	
	public long getLastBonus() {
		return lastBonus;
	}
}
