package com.quas.mythsmagic;

import java.io.FileReader;

import com.google.gson.Gson;
import com.quas.mythsmagic.util.BotProperties;
import com.quas.mythsmagic.util.Constants;

public class UpdateProperties {
	
	public static void main(String[] args) {
		
		try (FileReader in = new FileReader(Constants.FILE_PROPERTIES)) {
			// Read in properties
			BotProperties prop = new Gson().fromJson(in, BotProperties.class);
			prop.save();
		} catch (Exception e) {
			e.printStackTrace();
			new BotProperties().save();
		}
	}
}
