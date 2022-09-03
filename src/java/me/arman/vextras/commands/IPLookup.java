package me.arman.vextras.commands;

import java.io.File;
import java.util.List;
import me.arman.vextras.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class IPLookup implements CommandExecutor {
  private Main plugin;
  
  String prefix;
  
  public IPLookup(Main instance) {
    this.prefix = Main.prefix;
    this.plugin = instance;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("iplookup")) {
      if (!sender.hasPermission("vextras.iplookup")) {
        sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
            " You can't use this feature!");
        return true;
      } 
      if (args.length < 1) {
        sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
            " Usage: /IPLookup <username>");
        return true;
      } 
      OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
      File userdata = new File(this.plugin.getDataFolder(), String.valueOf(File.separator) + 
          "users");
      File f = new File(userdata, String.valueOf(File.separator) + 
          target.getUniqueId().toString() + ".yml");
      if (!f.exists()) {
        sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + " " + 
            target.getName() + " has no logged IPs!");
        return true;
      } 
      YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
      List<String> ips = yamlConfiguration.getStringList("ips");
      sender.sendMessage(String.valueOf(this.prefix) + ChatColor.GREEN + " " + 
          target.getName() + "'s logged IPs:");
      for (String ip : ips)
        sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + " " + ip); 
    } 
    return false;
  }
}
