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
		
		try (FileReader in = new FileReader(Constants.FILE_PROPERTIES)) {
			
			// Read in properties
			properties = new Gson().fromJson(in, BotProperties.class);
			
			// Create bot
			JDA bot = JDABuilder.createDefault(properties.getToken()).build();
			bot.awaitReady();
			
			// Load commands
			CommandManager.load(bot, false);
			
		} catch (Exception e) {
			e.printStackTrace();
			new BotProperties().save();
			System.exit(0);
		}
	}
}
