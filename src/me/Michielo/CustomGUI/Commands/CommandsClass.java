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
    if (args.length >= 1) {
      if (args[0].equalsIgnoreCase("open")) {
        if (sender.hasPermission("GUIBuilder.open"))
          if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 2) {
              GUI.openGUI(player, args[1]);
            } else {
              player.sendMessage(ChatColor.RED + "Please use /customgui open <Gui name>");
            } 
          } else {
            sender.sendMessage(ChatColor.RED + "This is only for players!");
          }  
        return true;
      } 
      if (args[0].equalsIgnoreCase("reload")) {
        if (sender.hasPermission("GUIBuilder.reload")) {
          CustomGUI.instance.reloadConfig();
          CustomGUI.instance.init();
          sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&areloaded"));
        } else {
          sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
        } 
        return true;
      } 
    } 
    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUnknown command!"));
    return true;
  }
}