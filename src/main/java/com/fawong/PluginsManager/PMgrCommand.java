package com.fawong.PluginsManager;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.Server;

/**
 * Handle events for all pmgr commands
 * @author fawong
 */
public class PMgrCommand implements CommandExecutor {
  private final PluginsManager plugin;
  private PluginManager pm;

  public PMgrCommand(PluginsManager instance) {
    plugin = instance;
    pm = instance.getServer().getPluginManager();
  }

  // Command related code here
  @Override
  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    String type = "";
    String plugin = "";

    if (args.length >= 2) {
      type = args[0];
      plugin = args[1];
      enableDisablePlugin(type, plugin);
    } else {
    }

    if (sender instanceof Player) {
      sender.sendMessage(type + " " + plugin);
    }
    return true;
  }

  private void enableDisablePlugin(String type, String plugin) {
    if (type.equalsIgnoreCase("disable")) {
      pm.disablePlugin(pm.getPlugin(plugin));
    } else if (type.equalsIgnoreCase("enable")) {
      pm.enablePlugin(pm.getPlugin(plugin));
    }
    this.plugin.getLogger().log(Level.INFO, plugin);
  }
}
