package com.quas.mythsmagic.commands.duel;

import java.util.concurrent.ThreadLocalRandom;

import com.quas.mythsmagic.commands.Command;
import com.quas.mythsmagic.commands.CommandInfo;
import com.quas.mythsmagic.commands.CommandInfo.DeferType;
import com.quas.mythsmagic.database.Player;
import com.quas.mythsmagic.util.Constants;
import com.quas.mythsmagic.util.Rating;
import com.quas.mythsmagic.util.Util;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.Button;

@CommandInfo(name = "duel", desc = "Challenges the specified player to a duel", requiresPermissionView = true, deferSlash = DeferType.None)
public class DuelCommand extends Command {

	private static final String OPPONENT = "opponent";
	
	private static final String DUEL_ACCEPT = "duelaccept";
	private static final String DUEL_REJECT = "duelreject";
	
	@Override
	public CommandData data() {
		OptionData opponent = new OptionData(OptionType.USER, OPPONENT, "The player to challenge", true);
		return super.data().addOptions(opponent);
	}
	
	@Override
	public void handle(SlashCommandEvent event) {
		// Check if player has valid deck
		Player player = Player.of(event.getUser());
		if (!player.hasValidDeck()) {
			event.replyFormat("%s, you do not have a valid deck.", event.getUser().getAsMention()).setEphemeral(true).queue();
			return;
		}
		
		// Check if opponent exists and has valid deck
		User oppUser = event.getOption(OPPONENT).getAsUser();
		Player opponent = Player.of(oppUser.getIdLong());
		if (opponent == null || !opponent.hasValidDeck()) {
			event.replyFormat("%s, %s does not have a valid deck.", event.getUser().getAsMention(), oppUser.getName()).setEphemeral(true).queue();
			return;
		} else if (player.getPlayerId() == opponent.getPlayerId()) {
			event.replyFormat("%s, you cannot duel against yourself.", event.getUser().getAsMention()).setEphemeral(true).queue();
			return;
		}
		
		Button accept = Button.success(componentId(oppUser.getId(), DUEL_ACCEPT, event.getUser().getId()), "Accept");
		Button reject = Button.danger(componentId(oppUser.getId(), DUEL_REJECT, event.getUser().getId()), "Reject");
		event.replyFormat("%s, you have been challenged to a duel by %s. Do you accept?", oppUser.getAsMention(), event.getUser().getAsMention()).addActionRow(accept, reject).queue();
	}
	
	@Override
	public void handle(ButtonClickEvent event) {
		String[] components = getComponents(event.getComponentId());
		switch (components[2]) {
			// Player accepts the duel challenge
			case DUEL_ACCEPT -> {
				// Duel invite expires after 5 minutes
				if (Util.getMillis(event.getTimeCreated()) - Util.getMillis(event.getMessage().getTimeCreated()) > Constants.DUEL_COMMAND_TIMEOUT_TIME) {
					event.getHook().editOriginalFormat("The duel invitation has expired.", event.getUser().getAsMention()).setActionRows().queue();
				} else {
					// Play game
					Player a = Player.of(event.getUser());
					Player b = Player.of(Long.parseLong(components[3]));
					if (ThreadLocalRandom.current().nextDouble() < Rating.chanceToWin(a, b)) {
						event.getHook().editOriginalFormat("%s won the duel against %s.", a.getName(), b.getName()).setActionRows().queue();
						Rating.updateRating(a, b, false);
					} else {
						event.getHook().editOriginalFormat("%s won the duel against %s.", b.getName(), a.getName()).setActionRows().queue();
						Rating.updateRating(b, a, false);
					}
				}
			}
			
			// Player rejects the duel challenge
			case DUEL_REJECT -> {
				// Duel invite expires after 5 minutes
				if (Util.getMillis(event.getTimeCreated()) - Util.getMillis(event.getMessage().getTimeCreated()) > Constants.DUEL_COMMAND_TIMEOUT_TIME) {
					event.getHook().editOriginalFormat("The duel invitation has expired.", event.getUser().getAsMention()).setActionRows().queue();
				} else {
					event.getHook().editOriginalFormat("%s has rejected the duel.", event.getUser().getAsMention()).setActionRows().queue();
				}
			}
		}
	}
}
