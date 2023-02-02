package me.Michielo.CustomGUI.GUI;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.Michielo.CustomGUI.Function.Function;
import me.Michielo.CustomGUI.Function.FunctionType;
import me.Michielo.CustomGUI.main.CustomGUI;

public class GuiHandler implements Listener {
  
	// hashmap to check for spam clickers
	// spamming tends to mess up bungee stuff
	private HashMap<Player, Date> lastClickMap = new HashMap<Player, Date>();
	
	// onclick handler
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		// get the player
	    Player player = (Player) event.getWhoClicked();
	    
	    // check if the item is not null
	    if (event.getCurrentItem() == null) {
	        return;
	    }
	    
	    // get all the gui's in an attempt to find a match
	    List<String> guis = CustomGUI.instance.getGUIs();
	    for (String str : guis ) {
		    String guiName = str;
		    
		    if (guiName == null) {
		        return;
		    }	
		    
		    if (!event.getInventory().equals(GUI.inv.get(guiName))) {
		    	return;
		    }
		    
		    // match found
		    // check if the gui is completed or not
		    boolean isGuiCompleted = CustomGUI.instance.completed.get(guiName);
		    if (!isGuiCompleted) {
		        return;
		    }
		    
		    // cehck if player is spam clicking
		    if (isSpamClick(player)) {
		        return;
		    }
		    
		    // all things look good, cancel event and execute function
		    event.setCancelled(true);
		    
		    FileConfiguration config = CustomGUI.instance.getConfig();
		    
		    // calculating row and slow to match configuration options
		    int row = (int) (event.getSlot() / 9) + 1;
		    int slot = (int) (event.getSlot() % 9) + 1;
		    
		    // get function and execute
		    Function function = getFunction(config, row, slot, guiName);
		    function.execute(player);
	    }
	}

	// check if the last time the player clicked < 500 and if so return true
	private boolean isSpamClick(Player player) {
	    Date lastClick = lastClickMap.get(player);
	    if (lastClick == null) {
	        lastClickMap.put(player, new Date());
	        return false;
	    }
	    Date now = new Date();
	    if (now.getTime() - lastClick.getTime() < 500) {
	        return true;
	    }
	    lastClickMap.put(player, now);
	    return false;
	}
	
	// just reading the configuration file and getting a Function instance from it
	private Function getFunction(FileConfiguration config, int row, int slot, String guiName) {
	    // Function(FunctionType type, String internalFunction, String connect, String commandExecutor, String command, String hoverText, String link)
		FunctionType type = FunctionType.valueOf(config.getString(String.valueOf(guiName) + ".row" + row + "." + slot + ".function.type").toUpperCase());
        String internal_function = config.getString(String.valueOf(guiName) + ".row" + row + "." + slot + ".function.internal_function");	
        String connect = config.getString(String.valueOf(guiName) + ".row" + row + "." + slot + ".function.connect");
        String commandExecutor = config.getString(String.valueOf(guiName) + ".row" + row + "." + slot + ".function.command_executor");
        String command = config.getString(String.valueOf(guiName) + ".row" + row + "." + slot + ".function.command");
        String hoverText = config.getString(String.valueOf(guiName) + ".row" + row + "." + slot + ".function.hover_text");
        String link = config.getString(String.valueOf(guiName) + ".row" + row + "." + slot + ".function.link");
	    Function function = new Function(type, internal_function, connect, commandExecutor, command, hoverText, link);
	    return function;
	}
}
