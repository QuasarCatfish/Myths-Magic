package com.quas.mythsmagic.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import com.quas.mythsmagic.commands.CommandInfo.DeferType;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class Command extends ListenerAdapter {

	public static String ANY_PLAYER = "any";
	
	private List<Subcommand> subcommands = Collections.synchronizedList(new ArrayList<Subcommand>());
	public final void addSubcommand(Subcommand sc) {
		subcommands.add(sc);
	}
	
	public CommandData data() {
		CommandInfo ci = this.getClass().getAnnotation(CommandInfo.class);
		return new CommandData(ci.name(), ci.desc());
	}
	
	@Override
	public final void onSlashCommand(SlashCommandEvent event) {
		super.onSlashCommand(event);
		
		CommandInfo ci = this.getClass().getAnnotation(CommandInfo.class);
		if (!event.getName().equals(ci.name())) return;
		
		if (subcommands.isEmpty()) {
			if (ci.requiresPermissionView() && !event.getTextChannel().canTalk()) {
				noPermission(event);
			} else {
				if (ci.deferSlash() == DeferType.Reply) event.deferReply(isEphemeral(event)).queue(hook -> handle(event));
				else handle(event);
			}
		} else {
			for (Subcommand sc : subcommands) {
				CommandInfo sci = sc.getClass().getAnnotation(CommandInfo.class);
				if (!event.getSubcommandName().equals(sci.name())) continue;
				
				if (sci.requiresPermissionView() && !event.getTextChannel().canTalk()) {
					noPermission(event);
				} else {
					if (sci.deferSlash() == DeferType.Reply) event.deferReply(isEphemeral(event)).queue(hook -> sc.handle(event));
					else sc.handle(event);
				}
			}
		}
	}
	
	public void handle(SlashCommandEvent event) {
		wip(event);
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		super.onButtonClick(event);
		
		CommandInfo ci = this.getClass().getAnnotation(CommandInfo.class);
		String[] components = getComponents(event.getComponentId());
		if (!components[0].equals(ci.name())) return;
		
		if (subcommands.isEmpty()) {
			if (components[1].equals(ANY_PLAYER) || components[1].equals(event.getUser().getId())) {
				if (ci.deferButton() == DeferType.Reply) event.deferReply(isEphemeral(event)).queue(hook -> handle(event));
				else if (ci.deferButton() == DeferType.Edit) event.deferEdit().queue(hook -> handle(event));
				else handle(event);
			} else {
				noPermission(event);
			}
		} else {
			if (components[2].equals(ANY_PLAYER) || components[2].equals(event.getUser().getId())) {
				for (Subcommand sc : subcommands) {
					CommandInfo sci = sc.getClass().getAnnotation(CommandInfo.class);
					if (!components[1].equals(sci.name())) continue;
					
					if (sci.deferButton() == DeferType.Reply) event.deferReply(isEphemeral(event)).queue(hook -> sc.handle(event));
					else if (sci.deferButton() == DeferType.Edit) event.deferEdit().queue(hook -> sc.handle(event));
					else sc.handle(event);
				}
			} else {
				noPermission(event);
			}
		}
	}
	
	public void handle(ButtonClickEvent event) {
		wip(event);
	}
	
	@Override
	public void onSelectionMenu(SelectionMenuEvent event) {
		super.onSelectionMenu(event);
		
		CommandInfo ci = this.getClass().getAnnotation(CommandInfo.class);
		String[] components = getComponents(event.getComponentId());
		if (!components[0].equals(ci.name())) return;
		
		if (subcommands.isEmpty()) {
			if (components[1].equals(ANY_PLAYER) || components[1].equals(event.getUser().getId())) {
				if (ci.deferSelect() == DeferType.Reply) event.deferReply(isEphemeral(event)).queue(hook -> handle(event));
				else if (ci.deferSelect() == DeferType.Edit) event.deferEdit().queue(hook -> handle(event));
				else handle(event);
			} else {
				noPermission(event);
			}
		} else {
			if (components[2].equals(ANY_PLAYER) || components[2].equals(event.getUser().getId())) {
				for (Subcommand sc : subcommands) {
					CommandInfo sci = sc.getClass().getAnnotation(CommandInfo.class);
					if (!components[1].equals(sci.name())) continue;
					
					if (sci.deferSelect() == DeferType.Reply) event.deferReply(isEphemeral(event)).queue(hook -> sc.handle(event));
					else if (sci.deferSelect() == DeferType.Edit) event.deferEdit().queue(hook -> sc.handle(event));
					else sc.handle(event);
				}
			} else {
				noPermission(event);
			}
		}
	}
	
	public void handle(SelectionMenuEvent event) {
		wip(event);
	}
	
	///////////////////////////////////
	
	protected final String componentId(String player, Object...args) {
		StringJoiner sj = new StringJoiner(":");
		
		CommandInfo ci = this.getClass().getAnnotation(CommandInfo.class);
		if (ci.parent() != void.class) sj.add(ci.parent().getAnnotation(CommandInfo.class).name());
		sj.add(ci.name());
		sj.add(player);
		
		for (Object obj : args) sj.add(Objects.toString(obj));
		return sj.toString();
	}
	
	protected final String[] getComponents(String componentId) {
		return componentId.split(":");
	}
	
	protected final void wip(GenericInteractionCreateEvent event) {
		reply(event, "This command is currently a work in progress. Please try again later.");
	}
	
	protected final void noPermission(GenericInteractionCreateEvent event) {
		reply(event, "You do not have permission to use this interaction.");
	}
	
	protected final void reply(GenericInteractionCreateEvent event, String format, Object...args) {
		if (event.isAcknowledged()) event.getHook().editOriginalFormat(format, args).queue();
		else event.replyFormat(format, args).setEphemeral(true).queue();
	}
	
	protected final boolean isEphemeral(GenericInteractionCreateEvent event) {
		return false;
	}
}
