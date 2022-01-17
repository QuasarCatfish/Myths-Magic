package com.quas.mythsmagic.commands.player;

import java.util.StringJoiner;

import com.quas.mythsmagic.commands.Command;
import com.quas.mythsmagic.commands.CommandInfo;
import com.quas.mythsmagic.database.Player;
import com.quas.mythsmagic.database.PlayerPack;
import com.quas.mythsmagic.database.PlayerStarterDeck;
import com.quas.mythsmagic.util.Constants;
import com.quas.mythsmagic.util.Util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandInfo(name = "profile", desc = "Displays your player information")
public class ProfileCommand extends Command {

	@Override
	public void handle(SlashCommandEvent event) {
		Player player = Player.of(event.getUser());
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Constants.COLOR);
		
		eb.setTitle(player.getName() + "'s Profile");
		eb.setThumbnail(event.getUser().getEffectiveAvatarUrl());
		
		StringJoiner sjDesc = new StringJoiner("\n");
		sjDesc.add("**Balance:** " + Util.quantity(player.getMoney(), "Jewel", "s"));
		if (player.getStarterBundleTickets() > 0) sjDesc.add("**Starter Bundle Tickets:** " + Util.quantity(player.getStarterBundleTickets(), "Ticket", "s"));
		eb.setDescription(sjDesc.toString());
		
		StringJoiner sjPacks = new StringJoiner("\n");
		sjPacks.setEmptyValue("You don't own any Booster Packs.");
		for (PlayerPack pack : PlayerPack.of(player.getPlayerId())) sjPacks.add(String.format("%,dx %s", pack.getQuantity(), pack.getPack().getName()));
		eb.addField("Booster Packs", sjPacks.toString(), true);
		
		StringJoiner sjStarterDecks = new StringJoiner("\n");
		sjStarterDecks.setEmptyValue("You don't own any Starter Decks.");
		for (PlayerStarterDeck deck : PlayerStarterDeck.of(player.getPlayerId())) sjStarterDecks.add(String.format("%,dx %s", deck.getQuantity(), deck.getStarterDeck().getName()));
		eb.addField("Starter Decks", sjStarterDecks.toString(), true);
		
		event.reply(event.getUser().getAsMention()).addEmbeds(eb.build()).setEphemeral(isEphemeral(event)).queue();
	}
}
