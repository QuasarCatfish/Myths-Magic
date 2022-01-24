package com.quas.mythsmagic.commands.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import com.quas.mythsmagic.commands.Command;
import com.quas.mythsmagic.commands.CommandInfo;
import com.quas.mythsmagic.database.Card;
import com.quas.mythsmagic.database.CardRarity;
import com.quas.mythsmagic.database.Pack;
import com.quas.mythsmagic.database.PackContent;
import com.quas.mythsmagic.database.StarterDeck;
import com.quas.mythsmagic.util.Constants;
import com.quas.mythsmagic.util.Util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@CommandInfo(name = "contents", desc = "Checks the contents of a pack or starter deck")
public class ContentsCommand extends Command {

	private static final String SET = "set";
	
	@Override
	public CommandData data() {
		OptionData set = new OptionData(OptionType.STRING, SET, "The pack or starter deck name", true);
		return super.data().addOptions(set);
	}
	
	@Override
	public void handle(SlashCommandEvent event) {
		String set = event.getOption(SET).getAsString().toLowerCase();
		
		// Check for closest pack name
		Pack[] packs = Pack.values();
		Pack bestPack = packs[0];
		int bestScore = Util.levenshtein(set, bestPack.getName());
		for (Pack pack : packs) {
			int score = Util.levenshtein(set, pack.getName());
			if (score < bestScore) {
				bestPack = pack;
				bestScore = score;
			}
		}
		
		// Check for closest starter deck name
		boolean isDeck = false;
		StarterDeck[] decks = StarterDeck.values();
		StarterDeck bestDeck = null;
		for (StarterDeck deck : decks) {
			int score = Util.levenshtein(set, deck.getName());
			if (score < bestScore) {
				isDeck = true;
				bestDeck = deck;
				bestScore = score;
			}
		}
		
		// If deck name is closer than pack name
		if (isDeck) {
//			HashMap<CardRarity, ArrayList<StarterDeckContent>> map = new HashMap<>();
			wip(event);
		}
		
		// If pack name is closer than deck name
		else {
			HashMap<CardRarity, ArrayList<PackContent>> map = new HashMap<>();
			for (CardRarity cr : CardRarity.values()) map.put(cr, new ArrayList<>());
			
			HashMap<Integer, Card> cardMap = new HashMap<>();
			for (Card c : Card.values()) cardMap.put(c.getCardId(), c);
			
			PackContent[] contents = PackContent.of(bestPack.getPackId());
			for (PackContent pc : contents) map.get(cardMap.get(pc.getCardId()).getCardRarity()).add(pc);
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle(bestPack.getName() + " Contents");
			eb.setColor(Constants.COLOR);
			
			for (CardRarity cr : CardRarity.values()) {
				if (cr == CardRarity.Unobtainable) continue;
				if (map.get(cr).isEmpty()) continue;
				
				StringJoiner sj = new StringJoiner("\n");
				for (PackContent pc : map.get(cr)) {
					Card c = cardMap.get(pc.getCardId());
					sj.add(String.format("%s (%s)", c.getName(), c.getCardType()));
				}
				
				eb.addField(cr.getName(), sj.toString(), true);
			}
			
			event.getHook().editOriginal(event.getUser().getAsMention()).setEmbeds(eb.build()).queue();
		}
	}
}
