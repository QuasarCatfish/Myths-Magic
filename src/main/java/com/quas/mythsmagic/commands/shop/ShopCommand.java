package com.quas.mythsmagic.commands.shop;

import com.quas.mythsmagic.commands.Command;
import com.quas.mythsmagic.commands.CommandInfo;
import com.quas.mythsmagic.database.Player;
import com.quas.mythsmagic.database.ShopItem;
import com.quas.mythsmagic.util.Constants;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandInfo(name = "shop", desc = "Checks the packs, decks, and bundles you can buy")
public class ShopCommand extends Command {

	@Override
	public void handle(SlashCommandEvent event) {
		Player player = Player.of(event.getUser());
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Constants.COLOR);
		
		eb.setTitle("Myths & Magic - Card Shop");
		eb.setDescription(String.format("You have %,d Jewels and %,d Starter Bundle Tickets.", player.getMoney(), player.getStarterBundleTickets()));
		
		for (ShopItem shop : ShopItem.getActive()) {
			eb.addField(shop.getShopName(), shop.getShopDescription(), true);
		}
		
		event.replyFormat("%s, it's not time for your bonus yet. Come back in %s.", event.getUser().getAsMention()).setEphemeral(isEphemeral(event)).queue();
	}
}
