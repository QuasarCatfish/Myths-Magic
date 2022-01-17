package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Pack {

	public static Pack of(int packId) {
		try (ResultSet res = DB.query("select * from `packs` where `packId` = ?;", packId)) {
			if (res.next()) return new Pack(res);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//////////////////////////////////////////////
	
	private int packId;
	private String name;
	
	private Pack(ResultSet res) throws SQLException {
		packId = res.getInt("packs.packId");
		name = res.getString("packs.packName");
	}
	
	public int getPackId() {
		return packId;
	}
	
	public String getName() {
		return name;
	}
}
