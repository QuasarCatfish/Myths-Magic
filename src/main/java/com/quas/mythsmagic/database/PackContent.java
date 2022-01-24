package com.quas.mythsmagic.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class PackContent implements Comparable<PackContent> {

	public static PackContent[] of(int packId) {
		ArrayList<PackContent> contents = new ArrayList<>();
		
		try (ResultSet res = DB.query("select * from `packcontents` where `packId` = ?;", packId)) {
			while (res.next()) contents.add(new PackContent(res));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Collections.sort(contents);
		return contents.toArray(new PackContent[0]);
	}
	
	//////////////////////////////////////////////
	
	private int packContentsId;
	private int packId;
	private Pack pack;
	private int cardId;
	private Card card;
	private int sortIndex;
	
	private PackContent(ResultSet res) throws SQLException {
		packContentsId = res.getInt("packcontents.packContentsId");
		packId = res.getInt("packcontents.packId");
		cardId = res.getInt("packcontents.cardId");
		sortIndex = res.getInt("packcontents.sortIndex");
	}
	
	public int getPackContentId() {
		return packContentsId;
	}
	
	public int getPackId() {
		return packId;
	}
	
	public Pack getPack() {
		if (pack == null) pack = Pack.of(packId);
		return pack;
	}
	
	public int getCardId() {
		return cardId;
	}
	
	public Card getCard() {
		if (card == null) card = Card.of(cardId);
		return card;
	}
	
	@Override
	public int compareTo(PackContent that) {
		int x = Integer.compare(this.sortIndex, that.sortIndex);
		if (x != 0) return x;
		return Integer.compare(this.packContentsId, that.packContentsId);
	}
}
