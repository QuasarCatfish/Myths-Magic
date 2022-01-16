package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Player {

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
	
	public static Player create(long id, String name) {
		DB.update("insert into `players`(`playerId`, `playerName`) values(?, ?);", id, name);
		return of(id);
	}
	
	//////////////////////////////////////////////
	
	private long id;
	private String name;
	private long money;
	
	private Player(ResultSet res) throws SQLException {
		id = res.getLong("players.playerId");
		name = res.getString("players.playerName");
		money = res.getLong("players.money");
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
}
