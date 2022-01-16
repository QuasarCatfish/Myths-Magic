package com.quas.mythsmagic.commands.base;

import com.quas.mythsmagic.MythsMagicBot;
import com.quas.mythsmagic.commands.Command;
import com.quas.mythsmagic.commands.CommandInfo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandInfo(name = "botinfo", desc = "Lists information about the bot")
public class BotInfoCommand extends Command {

	@Override
	public void handle(SlashCommandEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(event.getJDA().getSelfUser().getName());
		eb.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
		
		eb.addField("Author", "Quas", true);
		eb.addField("Version", MythsMagicBot.getProperties().getVersion(), true);
		
		event.reply(event.getUser().getAsMention()).addEmbeds(eb.build()).setEphemeral(isEphemeral(event)).queue();
	}
}
