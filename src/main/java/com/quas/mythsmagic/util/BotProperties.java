package com.quas.mythsmagic.util;

import java.io.File;
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
	
	// Databse username
	private String username = "username";
	public String getUsername() {
		return username;
	}
	
	// Database password
	private String password = "password";
	public String getPassword() {
		return password;
	}
	
	// Create properties file if it does not exist
	public static void createDefault() {
		File f = new File(Constants.FILE_PROPERTIES);
		if (!f.exists()) {
			try (FileWriter out = new FileWriter(Constants.FILE_PROPERTIES)) {
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				out.write(gson.toJson(new BotProperties(), BotProperties.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
