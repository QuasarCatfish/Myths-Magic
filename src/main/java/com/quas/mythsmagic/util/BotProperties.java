package com.quas.mythsmagic.util;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BotProperties {

	// Token for the Discord Bot
	private String token = "PASTE TOKEN HERE";
	public String getToken() {
		return token;
	}
	
	// Version of the program
	private String version = "0.0.1";
	public String getVersion() {
		return version;
	}
	
	private DatabaseInfo databaseInfo = new DatabaseInfo();
	public DatabaseInfo getDatabaseInfo() {
		return databaseInfo;
	}
	
	private BotInfo botInfo = new BotInfo();
	public BotInfo getBotInfo() {
		return botInfo;
	}
	
	// Save properties file
	public void save() {
		try (FileWriter out = new FileWriter(Constants.FILE_PROPERTIES)) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			out.write(gson.toJson(this, BotProperties.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Database information
	public static class DatabaseInfo {
		
		private String url = "localhost:3306";
		public String getUrl() {
			return url;
		}
		
		private String username = "username";
		public String getUsername() {
			return username;
		}
		
		private String password = "password";
		public String getPassword() {
			return password;
		}
	}
	
	public static class BotInfo {
		
		private String title = "Myths & Magic";
		public String getTitle() {
			return title;
		}
		
		private String description = "";
		public String getDescription() {
			return description;
		}
		
		private String author = "Quas";
		public String getAuthor() {
			return author;
		}
		
		private String[] links = new String[0];
		public String[] getLinks() {
			return links;
		}
	}
}
