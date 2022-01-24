package com.quas.mythsmagic;

import java.io.FileReader;

import com.google.gson.Gson;
import com.quas.mythsmagic.commands.CommandManager;
import com.quas.mythsmagic.util.BotProperties;
import com.quas.mythsmagic.util.Constants;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class MythsMagicBot {
	
	private static BotProperties properties;
	public static BotProperties getProperties() {
		return properties;
	}
	
	public static void main(String[] args) {
		
		initializeProperties();
		
		try {
			// Create bot
			JDA bot = JDABuilder.createDefault(properties.getToken()).build();
			bot.awaitReady();
			
			// Load commands
			CommandManager.load(bot, false);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// Read in properties file
	public static void initializeProperties() {
		try (FileReader in = new FileReader(Constants.FILE_PROPERTIES)) {
			properties = new Gson().fromJson(in, BotProperties.class);
		} catch (Exception e) {
			e.printStackTrace();
			new BotProperties().save();
			System.exit(0);
		}
	}
}
