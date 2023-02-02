package me.Michielo.CustomGUI.Function;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Michielo.CustomGUI.GUI.GUI;
import me.Michielo.CustomGUI.Utils.BungeeTools;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Function {
	
	// variables
	private FunctionType type;
	private String internalFunction;
	private String connect;
	private String commandExecutor;
	private String command;
	private String hoverText;
	private String link;

	// constructor 
	public Function(FunctionType type, String internalFunction, String connect, String commandExecutor, String command, String hoverText, String link) {
		this.type = type;
	    this.internalFunction = internalFunction;
	    this.connect = connect;
	    this.commandExecutor = commandExecutor;
	    this.command = command;
	    this.hoverText = hoverText;
	    this.link = link;
	}

	// executing a function
	@SuppressWarnings("deprecation")
	public void execute(Player player) {
		// switch over all functiontypes and execute function
		switch (this.type) {
			case INTERNAL:
				switch (this.internalFunction) {
					case "close":
						player.closeInventory();
						break;
					case "open":
						GUI.openGUI(player, this.connect);
						break;
				}
				break;
			case CONNECT:
				BungeeTools.connect(player, this.connect);
				break;
			case COMMAND:
				CommandSender commandSender = (this.commandExecutor.equalsIgnoreCase("console")) ? Bukkit.getConsoleSender() : player;
				Bukkit.dispatchCommand(commandSender, this.command);
				break;
			case PLACEHOLDER:
				break;
			case LINK:
				TextComponent mainComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', this.hoverText));
				mainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click me!").create()));
				mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, this.link));
				player.spigot().sendMessage(mainComponent);
				player.closeInventory();
				break;
	    }
	 }
}