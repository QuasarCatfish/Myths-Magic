package com.quas.mythsmagic.commands.player;

import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import com.quas.mythsmagic.commands.Command;
import com.quas.mythsmagic.commands.CommandInfo;
import com.quas.mythsmagic.database.DB;
import com.quas.mythsmagic.database.Player;
import com.quas.mythsmagic.util.Constants;
import com.quas.mythsmagic.util.Rand;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandInfo(name = "bonus", desc = "Claims your bonus money")
public class BonusCommand extends Command {

	@Override
	public void handle(SlashCommandEvent event) {
		Player player = Player.of(event.getUser());
		
		if (player.getLastBonus() + Constants.BONUS_WAIT_TIME <= System.currentTimeMillis()) {
			int bonus = Rand.nextElement(Constants.BONUS_AMOUNTS);
			if (player.isPremium()) bonus = Math.max(bonus, Rand.nextElement(Constants.BONUS_AMOUNTS));
			
			event.getHook().editOriginalFormat("%s, you earned %,d Jewels from your bonus!", event.getUser().getAsMention(), bonus).queue();
			DB.update("update `players` set `money` = `money` + ?, `lastBonus` = ? where `playerId` = ?;", bonus, System.currentTimeMillis(), player.getPlayerId());
		} else {
			long time = player.getLastBonus() + Constants.BONUS_WAIT_TIME - System.currentTimeMillis();
			StringJoiner sj = new StringJoiner(" and ");
			sj.setEmptyValue("1 minute");
			
			int hours = (int)(time / TimeUnit.HOURS.toMillis(1));
			time %= TimeUnit.HOURS.toMillis(1);
			if (hours > 0) sj.add(String.format("%,d hour%s", hours, hours > 1 ? "s" : ""));
			
			int minutes = (int)(time / TimeUnit.MINUTES.toMillis(1));
			if (minutes > 0) sj.add(String.format("%,d minute%s", minutes, minutes > 1 ? "s" : ""));
			
			event.getHook().editOriginalFormat("%s, it's not time for your bonus yet. Come back in %s.", event.getUser().getAsMention(), sj.toString()).queue();
		}
	}
}
