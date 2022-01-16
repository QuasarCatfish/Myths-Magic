package com.quas.mythsmagic.commands.info;

import com.quas.mythsmagic.commands.Command;
import com.quas.mythsmagic.commands.CommandInfo;
import com.quas.mythsmagic.database.Player;
import com.quas.mythsmagic.util.Constants;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandInfo(name = "profile", desc = "Displays your player information")
public class ProfileCommand extends Command {

	@Override
	public void handle(SlashCommandEvent event) {
		Player player = Player.of(event.getUser().getIdLong());
		if (player == null) player = Player.create(event.getUser().getIdLong(), event.getUser().getName());
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Constants.COLOR);
		
		eb.setTitle(player.getName() + "'s Profile");
		eb.setThumbnail(event.getUser().getEffectiveAvatarUrl());
		
		eb.addField("Money", String.format("%,d", player.getMoney()), true);
		
		event.reply(event.getUser().getAsMention()).addEmbeds(eb.build()).setEphemeral(isEphemeral(event)).queue();
	}
}
