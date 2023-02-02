package me.Michielo.CustomGUI.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import me.Michielo.CustomGUI.Commands.CommandsClass;
import me.Michielo.CustomGUI.GUI.GUI;
import me.Michielo.CustomGUI.GUI.GuiHandler;
import me.Michielo.CustomGUI.Utils.BungeeTools;

public class CustomGUI extends JavaPlugin {
  public static CustomGUI instance;
  private boolean startupfirst;
  
  public void onEnable() {
	startupfirst = false;
    init();
    getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
    getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "BungeeCord", (PluginMessageListener)new BungeeTools());
  }
  
  public void onDisable() {
    closeMessage();
  }
  
  private HashMap<String, String> errors = new HashMap<>();
  
  public HashMap<String, Boolean> completed = new HashMap<>();
  
  public void init() {
	// unload errors and completed
	errors.clear();
	completed.clear();
	// load
    instance = this;
    saveDefaultConfig();
    List<String> GUIs = getGUIs();
    for (int i = 0; i < GUIs.size(); i++) {
      GUI.GuiBuilder(GUIs.get(i));
      if (((Boolean)GUI.finished.get(GUIs.get(i))).booleanValue()) {
        this.completed.put(GUIs.get(i), Boolean.valueOf(true));
      } else {
        String error = GUI.error;
        this.errors.put(GUIs.get(i), error);
        this.completed.put(GUIs.get(i), Boolean.valueOf(false));
      } 
    } 

    startMessage(GUIs);
    if (!startupfirst) {
        getCommand("GuiBuilder").setExecutor((CommandExecutor)new CommandsClass());
        getServer().getPluginManager().registerEvents((Listener)new GuiHandler(), (Plugin)this);
        startupfirst = true;
    }
  }
  
  private void startMessage(List<String> guis) {
    String str = "\n \n";
    str = String.valueOf(str) + "={ GuiBuilder }= \n \n";
    str = String.valueOf(str) + "- Sponsored by GameHosted \n";
    str = String.valueOf(str) + "  Loading... \n";
    for (int i = 0; i < guis.size(); i++) {
      if (((Boolean)this.completed.get(guis.get(i))).booleanValue()) {
        str = String.valueOf(str) + "  - Loaded: " + (String)guis.get(i) + "\n";
      } else {
        str = String.valueOf(str) + "  - Failed to load: " + (String)guis.get(i) + "\n";
      } 
    } 
    str = String.valueOf(str) + "\n";
    str = String.valueOf(str) + "={ GuiBuilder }=";
    str = String.valueOf(str) + "\n \n";
    Bukkit.getLogger().info(str);
  }
  
  private void closeMessage() {
    Bukkit.getLogger().info("\n\n={ GuiBuilder }=\n\n- Closed!\n\n={ GuiBuilder }= \n");
  }
  
  public List<String> getGUIs() {
    List<String> list = new ArrayList<>();
    String str = getConfig().getString("GUIlist");
    String[] splitted = str.split(",");
    for (int i = 0; i < splitted.length; i++)
      list.add(splitted[i]); 
    return list;
  }
  
  public static CustomGUI getInstance() {
    return instance;
  }
}