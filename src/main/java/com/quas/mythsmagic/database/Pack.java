package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Pack {

	public static Pack of(int packId) {
		try (ResultSet res = DB.query("select * from `packs` where `packId` = ?;", packId)) {
			if (res.next()) return new Pack(res);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Pack[] values() {
		ArrayList<Pack> packs = new ArrayList<>();
		
		try (ResultSet res = DB.query("select * from `packs`;")) {
			while (res.next()) packs.add(new Pack(res));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return packs.toArray(new Pack[0]);
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
