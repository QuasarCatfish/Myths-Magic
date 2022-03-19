package com.quas.mythsmagic.util;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

public class Constants {

	// Myths and Magic Setup
	public static final String FILE_PROPERTIES = "properties.json";
	
	// Game constants
	public static final Color COLOR = new Color(0xFFFFFF);
	
	// Command constants
	public static final long DUEL_COMMAND_TIMEOUT_TIME = TimeUnit.MINUTES.toMillis(5);
	
	// Bonus
	public static final long BONUS_WAIT_TIME = TimeUnit.HOURS.toMillis(6);
	public static final int[] BONUS_AMOUNTS = new int[] {
			100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
			250, 250, 250, 250, 250, 250, 500, 500, 500, 1000
	};
	
	// Player IDs
	public static final long QUAS_ID = 563661703124877322L;
}
