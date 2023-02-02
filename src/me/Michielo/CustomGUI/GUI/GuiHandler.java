package me.Michielo.CustomGUI.GUI;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.Michielo.CustomGUI.Utils.BungeeTools;
import me.Michielo.CustomGUI.main.CustomGUI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GuiHandler implements Listener {
  
	private HashMap<Player, Date> lastClick = new HashMap<Player, Date>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
  public void onClick(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    List<String> Guis = CustomGUI.instance.getGUIs();
    for (int i = 0; i < Guis.size(); i++) {
      String GUIname = Guis.get(i);
      if (((Boolean)CustomGUI.instance.completed.get(GUIname)).booleanValue() && 
        event.getInventory().equals(GUI.inv.get(GUIname))) {
        if (event.getCurrentItem() == null)
          return; 
        FileConfiguration config = CustomGUI.getInstance().getConfig();
        Double getSlot = Double.valueOf(event.getSlot());
        int row = Integer.valueOf(String.valueOf(getSlot.doubleValue() / 9.0D).split("")[0]).intValue() + 1;
        int slot = Integer.valueOf(String.valueOf(getSlot.doubleValue() / 9.0D).split("")[2]).intValue() + 1;
        String functionType = config.getString(String.valueOf(GUIname) + ".row" + row + "." + slot + ".function.type");
        String internal_function = config.getString(String.valueOf(GUIname) + ".row" + row + "." + slot + ".function.internal_function");
        if (functionType == null)
          return; 
        if (functionType.equalsIgnoreCase("internal") && internal_function != null && internal_function.equalsIgnoreCase("clickable"))
          return; 
        event.setCancelled(true);
        if (lastClick.get(player) == null) lastClick.put(player, new Date(System.currentTimeMillis() - 5000));
        Date last = lastClick.get(player);
        Date window = new Date(System.currentTimeMillis() - 500);
        if (last.after(window)) return;
        lastClick.put(player, new Date(System.currentTimeMillis()));
        if (functionType.equalsIgnoreCase("internal")) {
          if (internal_function.equalsIgnoreCase("close")) {
            player.closeInventory();
            break;
          } 
          if (internal_function.toUpperCase().startsWith("OPEN")) {
            try {
              String[] Splitted = internal_function.split(" ");
              GUI.openGUI(player, Splitted[1]);
            } catch (NullPointerException e) {
              player.sendMessage(ChatColor.RED + "Something went wrong!");
            } 
            break;
          } 
        } 
        if (functionType.equalsIgnoreCase("connect")) {
          String connect = config.getString(String.valueOf(GUIname) + ".row" + row + "." + slot + ".function.connect");
          BungeeTools.connect(player, connect);
          break;
        } 
        if (functionType.equalsIgnoreCase("command")) {
          String commandSender = config.getString(String.valueOf(GUIname) + ".row" + row + "." + slot + ".function.command_executor");
          String command = config.getString(String.valueOf(GUIname) + ".row" + row + "." + slot + ".function.command");
          if (commandSender.equalsIgnoreCase("console")) {
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command);
            break;
          } 
          Bukkit.dispatchCommand((CommandSender)player, command);
          break;
        } 
        if (functionType.equalsIgnoreCase("placeholder"))
          return; 
        if (functionType.equalsIgnoreCase("link")) {
        	String hoverText = config.getString(String.valueOf(GUIname) + ".row" + row + "." + slot + ".function.hover_text");
        	String link = config.getString(String.valueOf(GUIname) + ".row" + row + "." + slot + ".function.link");
        	TextComponent mainComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', hoverText));
        	mainComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click me!").create()));
        	mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
        	player.spigot().sendMessage(mainComponent);
        	player.closeInventory();
        	break;
        }
        player.sendMessage(ChatColor.RED + "Something went wrong!");
      } 
    } 
  }
}
