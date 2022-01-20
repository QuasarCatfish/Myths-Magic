package com.quas.mythsmagic.commands.shop;

import com.quas.mythsmagic.commands.Command;
import com.quas.mythsmagic.commands.CommandInfo;
import com.quas.mythsmagic.database.DB;
import com.quas.mythsmagic.database.Player;
import com.quas.mythsmagic.database.ShopItem;
import com.quas.mythsmagic.util.Util;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@CommandInfo(name = "buy", desc = "Buys the specified pack, deck, or bundles, if able")
public class BuyCommand extends Command {
	
	private static final String ITEM = "item";
	private static final String QUANTITY = "quantity";

	@Override
	public CommandData data() {
		OptionData item = new OptionData(OptionType.STRING, ITEM, "The pack, deck, or bundle you wish to buy", true);
		OptionData quantity = new OptionData(OptionType.INTEGER, QUANTITY, "The number you want to buy (default 1)");
		return super.data().addOptions(item, quantity);
	}
	
	@Override
	public void handle(SlashCommandEvent event) {
		Player player = Player.of(event.getUser());
		
		long quantity = event.getOption(QUANTITY) == null ? 1 : event.getOption(QUANTITY).getAsLong();
		if (quantity <= 0) {
			event.getHook().editOriginalFormat("%s, you cannot purchase %,d of an item.", event.getUser().getAsMention(), quantity).queue();
			return;
		}
		
		// Find item whose name most closely matches
		String itemName = event.getOption(ITEM).getAsString();
		ShopItem[] items = ShopItem.getActive();
		ShopItem best = items[0];
		int bestScore = Util.levenshtein(itemName, best.getShopName());
		
		for (ShopItem item : items) {
			int score = Util.levenshtein(itemName, item.getShopName());
			if (score < bestScore) {
				best = item;
				bestScore = score;
			}
		}
		
		// Able to buy with starter bundle tickets
		if (best.getPrice().isBuyableWithStarterBundleTickets() && player.getStarterBundleTickets() >= best.getPrice().getStarterBundleTicketsPrice() * quantity) {
			DB.update("update `players` set `starterBundleTickets` = `starterBundleTickets` - ? where `playerId` = ?;", best.getPrice().getStarterBundleTicketsPrice() * quantity, player.getPlayerId());
		}
		
		// Able to buy with jewels
		else if (best.getPrice().isBuyableWithJewels() && player.getMoney() >= best.getPrice().getJewelsPrice() * quantity) {
			DB.update("update `players` set `money` = `money` - ? where `playerId` = ?;", best.getPrice().getJewelsPrice() * quantity, player.getPlayerId());
		}
		
		// Not able to buy
		else {
			event.getHook().editOriginalFormat("%s, you cannot afford to buy the %s.", event.getUser().getAsMention(), best.getShopName()).queue();
			return;
		}
		
		// Buy the item
		if (best.hasStarterDeck()) DB.update("insert into `playerstarterdecks` values(?, ?, ?) on duplicate key update `quantity` = `quantity` + ?;", player.getPlayerId(), best.getStarterDeckId(), quantity, quantity);
		if (best.hasPacks()) {
			for (int q = 0; q < best.getPackIds().length; q++) {
				DB.update("insert into `playerpacks` values(?, ?, ?) on duplicate key update `quantity` = `quantity` + ?;", player.getPlayerId(), best.getPackIds()[q], best.getPackQuantities()[q] * quantity, best.getPackQuantities()[q] * quantity);
			}
		}
		
		event.getHook().editOriginalFormat("%s, you bought %,dx %s for %s.", event.getUser().getAsMention(), quantity, best.getShopName(), best.getPrice()).queue();
	}
}
