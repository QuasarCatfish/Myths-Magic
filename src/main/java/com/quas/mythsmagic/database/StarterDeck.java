package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StarterDeck {

	public static StarterDeck of(int starterDeckId) {
		try (ResultSet res = DB.query("select * from `starterdecks` where `starterDeckId` = ?;", starterDeckId)) {
			if (res.next()) return new StarterDeck(res);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//////////////////////////////////////////////
	
	private int starterDeckId;
	private String name;
	
	private StarterDeck(ResultSet res) throws SQLException {
		starterDeckId = res.getInt("starterdecks.starterDeckId");
		name = res.getString("starterdecks.starterDeckName");
	}
	
	public int getStarterDeckId() {
		return starterDeckId;
	}
	
	public String getName() {
		return name;
	}
}
