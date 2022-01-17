package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerPack {

	public static PlayerPack of(long playerId, int packId) {
		try (ResultSet res = DB.query("select * from `playerpacks` where `playerId` = ? and `packId` = ?;", playerId, packId)) {
			if (res.next()) return new PlayerPack(res);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static PlayerPack[] of(long playerId) {
		ArrayList<PlayerPack> packs = new ArrayList<>();
		try (ResultSet res = DB.query("select * from `playerpacks` where `playerId` = ? and `quantity` > 0;", playerId)) {
			while (res.next()) packs.add(new PlayerPack(res));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return packs.toArray(new PlayerPack[0]);
	}
	
	//////////////////////////////////////////////
	
	private long playerId;
	private Player player;
	private int packId;
	private Pack pack;
	private int quantity;
	
	private PlayerPack(ResultSet res) throws SQLException {
		playerId = res.getLong("playerpacks.playerId");
		packId = res.getInt("playerpacks.packId");
		quantity = res.getInt("playerpacks.quantity");
	}
	
	public long getPlayerId() {
		return playerId;
	}
	
	public Player getPlayer() {
		if (player == null) player = Player.of(playerId);
		return player;
	}
	
	public int getPackId() {
		return packId;
	}
	
	public Pack getPack() {
		if (pack == null) pack = Pack.of(packId);
		return pack;
	}
	
	public int getQuantity() {
		return quantity;
	}
}
