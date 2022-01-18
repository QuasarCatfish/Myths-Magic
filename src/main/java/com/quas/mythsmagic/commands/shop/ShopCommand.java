package com.quas.mythsmagic.commands.shop;

import com.quas.mythsmagic.commands.Command;
import com.quas.mythsmagic.commands.CommandInfo;
import com.quas.mythsmagic.database.Player;
import com.quas.mythsmagic.database.ShopItem;
import com.quas.mythsmagic.database.ShopType;
import com.quas.mythsmagic.util.Constants;
import com.quas.mythsmagic.util.Util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@CommandInfo(name = "shop", desc = "Checks the packs, decks, and bundles you can buy")
public class ShopCommand extends Command {

	private static final String SHOP_TYPE = "shop-type";
	
	@Override
	public CommandData data() {
		OptionData shopType = new OptionData(OptionType.STRING, SHOP_TYPE, "The category of the shop to look at", true);
		for (ShopType type : ShopType.values()) {
			if (type == ShopType.None) continue;
			shopType.addChoice(type.getName(), type.name());
		}
		
		return super.data().addOptions(shopType);
	}
	
	@Override
	public void handle(SlashCommandEvent event) {
		Player player = Player.of(event.getUser());
		ShopType shopType = ShopType.valueOf(event.getOption(SHOP_TYPE).getAsString());
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Constants.COLOR);
		
		eb.setTitle("Myths & Magic - " + shopType);
		eb.setDescription(String.format("You have %s and %s.", Util.quantity(player.getMoney(), "Jewel", "s"), Util.quantity(player.getStarterBundleTickets(), "Starter Bundle Ticket", "s")));
		
		for (ShopItem shop : ShopItem.getActive(shopType)) eb.addField(shop.getShopName(), shop.getShopDescription(), true);
		
		event.reply(event.getUser().getAsMention()).addEmbeds(eb.build()).setEphemeral(isEphemeral(event)).queue();
	}
}
