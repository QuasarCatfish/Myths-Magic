package com.quas.mythsmagic;

import java.io.FileReader;

import com.google.gson.Gson;
import com.quas.mythsmagic.util.BotProperties;
import com.quas.mythsmagic.util.Constants;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class MythsMagicBot {
	
	public static void main(String[] args) {
		
		try (FileReader in = new FileReader(Constants.FILE_PROPERTIES)) {
			
			// Read in properties
			BotProperties prop = new Gson().fromJson(in, BotProperties.class);
			
			// Create bot
			JDA bot = JDABuilder.createDefault(prop.getToken()).build();
			bot.awaitReady();
			
			// Load commands
			
		} catch (Exception e) {
			e.printStackTrace();
			BotProperties.createDefault();
			System.exit(0);
		}
	}
}
