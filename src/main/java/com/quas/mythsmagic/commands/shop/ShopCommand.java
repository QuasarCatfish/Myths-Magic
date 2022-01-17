package com.quas.mythsmagic.commands.shop;

import com.quas.mythsmagic.commands.Command;
import com.quas.mythsmagic.commands.CommandInfo;
import com.quas.mythsmagic.database.Player;
import com.quas.mythsmagic.database.ShopItem;
import com.quas.mythsmagic.util.Constants;
import com.quas.mythsmagic.util.Util;

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
		eb.setDescription(String.format("You have %s and %s.", Util.quantity(player.getMoney(), "Jewel", "s"), Util.quantity(player.getStarterBundleTickets(), "Starter Bundle Ticket", "s")));
		
		for (ShopItem shop : ShopItem.getActive()) eb.addField(shop.getShopName(), shop.getShopDescription(), true);
		
		event.reply(event.getUser().getAsMention()).addEmbeds(eb.build()).setEphemeral(isEphemeral(event)).queue();
	}
}
