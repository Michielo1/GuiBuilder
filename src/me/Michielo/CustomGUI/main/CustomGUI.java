package me.Michielo.CustomGUI.main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.Michielo.CustomGUI.Commands.CommandsClass;
import me.Michielo.CustomGUI.GUI.GUI;
import me.Michielo.CustomGUI.GUI.GuiHandler;
import me.Michielo.CustomGUI.Utils.BungeeTools;

public class CustomGUI extends JavaPlugin {
	
	  // Declare the instance variable for the plugin
	  public static CustomGUI instance;

	  // Declare the startupfirst variable to track if the plugin has been started for the first time
	  private boolean startupfirst;

	  // Method called when the plugin is enabled
	  @Override
	  public void onEnable() {
	    // Set the startupfirst variable to false
	    startupfirst = false;
	    // Call the init method
	    init();
	    // Register the outgoing plugin channel for "BungeeCord"
	    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	    // Register the incoming plugin channel for "BungeeCord" and set the listener to the BungeeTools class
	    getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeTools());
	  }

	  // Method called when the plugin is disabled
	  @Override
	  public void onDisable() {
	    // Call the closeMessage method
	    closeMessage();
	  }

	  // Declare a HashMap to store errors associated with GUIs
	  private HashMap<String, String> errors = new HashMap<>();

	  // Declare a HashMap to store completion status for GUIs
	  public HashMap<String, Boolean> completed = new HashMap<>();
	  
	  // Initialization method for the plugin OR plugin reload
	  public void init() {
		    // Clear the errors and completed HashMaps just in case it's a reload instead of init
		    errors.clear();
		    completed.clear();
		    // Set the instance variable to the current instance of the plugin
		    instance = this;
		    // Save the default configuration for the plugin
		    saveDefaultConfig();
		    // Get the list of GUIs
		    List<String> GUIs = getGUIs();
		    // Loop through the GUIs
		    for (int i = 0; i < GUIs.size(); i++) {
		      // Call the GuiBuilder method of the GUI class with the current GUI
		      GUI.GuiBuilder(GUIs.get(i));
		      // If the GUI was built successfully
		      if (GUI.finished.get(GUIs.get(i))) {
		        // Add the GUI to the completed HashMap
		        this.completed.put(GUIs.get(i), true);
		      } else {
		        // Get the error message for the failed GUI
		        String error = GUI.error;
		        // Add the error message to the errors HashMap for the GUI
		        this.errors.put(GUIs.get(i), error);
		        // Add the GUI to the completed HashMap with a value of false
		        this.completed.put(GUIs.get(i), false);
		      } 
		    } 
		    // Call the startMessage method with the list of GUIs
		    startMessage(GUIs);
		    // If the plugin has not started for the first time
		    if (!startupfirst) {
		      // Set the executor for the "GuiBuilder" command to the CommandsClass class
		      getCommand("GuiBuilder").setExecutor(new CommandsClass());
		      // Register the GuiHandler as a listener for events
		      getServer().getPluginManager().registerEvents(new GuiHandler(), this);
		      // Set the startupfirst variable to true
		      startupfirst = true;
		    }
	  }
  
	  // Startup message
	  private void startMessage(List<String> guis) {
		  	// create StringBuilder to create the message
		    StringBuilder str = new StringBuilder();
		    str.append("\n \n={ GuiBuilder }= \n \n");
		    str.append("- Sponsored by Nodelegend \n");
		    str.append("  Loading... \n");
		    
		    // loop through all gui's to check if they've been loaded or encountered an error
		    for (String gui : guis) {
		      str.append("  - ")
		         .append(((Boolean)this.completed.get(gui)).booleanValue() ? "Loaded: " : "Failed to load: ")
		         .append(gui)
		         .append("\n");
		    }

		    str.append("\n={ GuiBuilder }=")
		       .append("\n \n");

		    Bukkit.getLogger().info(str.toString());
	  }
		  
	  
	  // Closing message
	  private void closeMessage() {
		  Bukkit.getLogger().info("\n\n={ GuiBuilder }=\n\n- Closed!\n\n={ GuiBuilder }= \n");
	  }
		  
	  // Gui getter
	  public List<String> getGUIs() {
		  return Arrays.asList(getConfig().getString("GUIlist").split(","));
	  }  
	  
	  // Instance getter
	  public static CustomGUI getInstance() {
		  return instance;
	  }
}