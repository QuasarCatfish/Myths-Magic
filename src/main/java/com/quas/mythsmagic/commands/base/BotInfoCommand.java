package com.quas.mythsmagic.commands.base;

import java.util.StringJoiner;

import com.quas.mythsmagic.MythsMagicBot;
import com.quas.mythsmagic.commands.Command;
import com.quas.mythsmagic.commands.CommandInfo;
import com.quas.mythsmagic.util.Constants;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandInfo(name = "botinfo", desc = "Lists information about the bot")
public class BotInfoCommand extends Command {

	@Override
	public void handle(SlashCommandEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Constants.COLOR);
		
		eb.setTitle(MythsMagicBot.getProperties().getBotInfo().getTitle());
		eb.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
		eb.setDescription(MythsMagicBot.getProperties().getBotInfo().getDescription());
		
		eb.addField("Author", MythsMagicBot.getProperties().getBotInfo().getAuthor(), true);
		eb.addField("Version", MythsMagicBot.getProperties().getVersion(), true);
		eb.addBlankField(true);
		
		StringJoiner sj = new StringJoiner(" | ");
		for (String link : MythsMagicBot.getProperties().getBotInfo().getLinks()) sj.add(link);
		eb.addField("Links", sj.toString(), false);
		
		event.reply(event.getUser().getAsMention()).addEmbeds(eb.build()).setEphemeral(isEphemeral(event)).queue();
	}
}
