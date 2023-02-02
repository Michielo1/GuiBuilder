package me.Michielo.CustomGUI.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Michielo.CustomGUI.GUI.GUI;
import me.Michielo.CustomGUI.main.CustomGUI;
import net.md_5.bungee.api.ChatColor;

public class CommandsClass implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		  if (args.length < 1) {
		    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUnknown command!"));
		    return true;
		  }

		  String subCommand = args[0].toLowerCase();
		  switch (subCommand) {
		    case "open":
		      if (!sender.hasPermission("GUIBuilder.open")) {
		        sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
		        return true;
		      }

		      if (!(sender instanceof Player)) {
		        sender.sendMessage(ChatColor.RED + "This is only for players!");
		        return true;
		      }

		      if (args.length != 2) {
		        sender.sendMessage(ChatColor.RED + "Please use /customgui open <Gui name>");
		        return true;
		      }

		      GUI.openGUI((Player) sender, args[1]);
		      return true;

		    case "reload":
		      if (!sender.hasPermission("GUIBuilder.reload")) {
		        sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
		        return true;
		      }

		      CustomGUI.instance.reloadConfig();
		      CustomGUI.instance.init();
		      sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&areloaded"));
		      return true;

		    default:
		      sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUnknown command!"));
		      return true;
		  }
	}
	
}