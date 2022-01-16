package com.quas.mythsmagic.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.quas.mythsmagic.MythsMagicBot;
import com.quas.mythsmagic.util.BotProperties.DatabaseInfo;

public class DB {

	private static DB db = null;
	private Connection con = null;
	
	private DB() {}
	
	private static synchronized Connection getConnection() throws SQLException {
		// If DB singleton has not yet been initialized
		if (db == null) db = new DB();
		
		// If the Connection has not yet been initialized or it is closed
		if (db.con == null || !db.con.isValid(0)) {
			DatabaseInfo dbInfo = MythsMagicBot.getProperties().getDatabaseInfo();
			db.con = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s", dbInfo.getUrl(), dbInfo.getSchema()), dbInfo.getUsername(), dbInfo.getPassword());
		}
		
		// Return connection
		return db.con;
	}
	
	////////////////////////////////////////////////////////
	
	public static synchronized ResultSet query(String query, Object...args) throws SQLException {
		PreparedStatement ps = getConnection().prepareStatement(query);
		ps.closeOnCompletion();
		for (int q = 0; q < args.length; q++) ps.setObject(q + 1, args[q]);
		return ps.executeQuery();
	}
	
	public static synchronized boolean update(String update, Object...args) {
		try (PreparedStatement ps = getConnection().prepareStatement(update)) {
			for (int q = 0; q < args.length; q++) ps.setObject(q + 1, args[q]);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
