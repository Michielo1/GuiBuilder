package me.Michielo.CustomGUI.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Michielo.CustomGUI.main.CustomGUI;

public class GUI {
  public static HashMap<String, Inventory> inv = new HashMap<>();
  
  public static HashMap<String, Boolean> finished = new HashMap<>();
  
  public static String error;
  
  public static Integer rowCalculator(CustomGUI instance, String str) {
    int i = 0;
    for (int l = 1; l < 7; l++) {
      for (int m = 1; m < 10; m++) {
        if (instance.getConfig().getString(String.valueOf(str) + ".row" + l + "." + m + ".item") != null)
          i = l; 
      } 
    } 
    return Integer.valueOf(i);
  }
  
  public static void GuiBuilder(String GUIname) {
    try {
      CustomGUI instance = CustomGUI.instance;
      Integer rows = rowCalculator(instance, GUIname);
      String title = instance.getConfig().getString(String.valueOf(GUIname) + ".name");
      Integer size = Integer.valueOf(9 * rows.intValue());
      Inventory inventory = Bukkit.createInventory(null, size.intValue(), ChatColor.translateAlternateColorCodes('&', title));
      ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
      ItemMeta meta = item.getItemMeta();
      for (int i = 0; i < rows.intValue(); i++) {
        Integer row = Integer.valueOf(i + 1);
        for (int l = 1; l < 10; l++) {
          String configItem = instance.getConfig().getString(String.valueOf(GUIname) + ".row" + row + "." + l + ".item");
          if (configItem != null) {
            Material configMat = Material.matchMaterial(configItem);
            if (configMat == null) {
              finished.put(GUIname, Boolean.valueOf(false));
              error = "The item material for: row" + row + " " + l + " is not correct!";
              break;
            } 
            item.setType(configMat);
            String name = instance.getConfig().getString(String.valueOf(GUIname) + ".row" + row + "." + l + ".name");
            if (name != null) {
              meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            } else {
              finished.put(GUIname, Boolean.valueOf(false));
              error = "The item name for: row" + row + " " + l + " is not correct!";
              break;
            } 
            List<String> lore = new ArrayList<>();
            int m = 1;
            while (instance.getConfig().getString(String.valueOf(GUIname) + ".row" + row + "." + l + ".lore" + m) != null) {
              lore.add(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString(String.valueOf(GUIname) + ".row" + row + "." + l + ".lore" + m)));
              m++;
            } 
            if (lore != null)
              meta.setLore(lore); 
            Integer place = Integer.valueOf(-1);
            if (row.intValue() == 1) {
              place = Integer.valueOf(l - row.intValue());
            } else {
              place = Integer.valueOf(l - 1 + (row.intValue() - 1) * 9);
            } 
            item.setItemMeta(meta);
            inventory.setItem(place.intValue(), item);
          } 
        } 
      } 
      finished.put(GUIname, Boolean.valueOf(true));
      inv.put(GUIname, inventory);
    } catch (NullPointerException|IllegalArgumentException e) {
      finished.put(GUIname, Boolean.valueOf(false));
      e.printStackTrace();
    } 
  }
  
  public static void openGUI(Player player, String str) {
    if (finished.get(str) == null) {
      player.sendMessage(ChatColor.RED + "This GUI does not exist!");
      return;
    } 
    if (!((Boolean)finished.get(str)).booleanValue()) {
      player.sendMessage(ChatColor.RED + "This GUI failed to build!");
      return;
    } 
    player.openInventory(inv.get(str));
  }
}