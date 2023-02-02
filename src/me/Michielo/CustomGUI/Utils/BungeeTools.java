package me.Michielo.CustomGUI.Utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.Michielo.CustomGUI.main.CustomGUI;

public class BungeeTools implements PluginMessageListener {
	  
  public void onPluginMessageReceived(String channel, Player player, byte[] message) {
    if (!channel.equalsIgnoreCase("BungeeCord"))
      return; 
    ByteArrayDataInput in = ByteStreams.newDataInput(message);
    String subchannel = in.readUTF();
    // for future
  }
  
  public static void connect(Player player, String server) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("Connect");
    out.writeUTF(server);
    player.sendPluginMessage((Plugin)CustomGUI.getInstance(), "BungeeCord", out.toByteArray());
  }
  
}