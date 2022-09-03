package me.arman.vextras.commands;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import me.arman.vextras.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Prefix implements CommandExecutor {
  private Main plugin;
  
  HashMap<String, Long> lastUsage = new HashMap<>();
  
  public Prefix(Main instance) {
    this.plugin = instance;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("prefix")) {
      if (!(sender instanceof Player)) {
        if (args.length < 2) {
          sender.sendMessage(ChatColor.RED + 
              "/prefix <player> <prefix>");
          return true;
        } 
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        File file1 = new File(this.plugin.getDataFolder(), String.valueOf(File.separator) + 
            "users");
        File file2 = new File(file1, String.valueOf(File.separator) + 
            target.getUniqueId().toString() + ".yml");
        this.plugin.createFileIfNotFound(file2);
        YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(file2);
        if (args[1].equalsIgnoreCase("reset")) {
          yamlConfiguration1.set("Prefix", null);
          try {
            yamlConfiguration1.save(file2);
          } catch (IOException iOException) {}
          sender.sendMessage(ChatColor.GREEN + "Reset " + 
              target.getName() + "'s prefix!");
          return true;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 1; j < args.length; j++)
          stringBuilder.append(String.valueOf(args[j]) + " "); 
        String str = stringBuilder.toString();
        yamlConfiguration1.set("Prefix", str);
        try {
          yamlConfiguration1.save(file2);
        } catch (IOException iOException) {}
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
              ChatColor.GREEN + "Set " + target.getName() + 
              "'s prefix to " + str));
        return true;
      } 
      Player p = (Player)sender;
      File userdata = new File(this.plugin.getDataFolder(), String.valueOf(File.separator) + 
          "users");
      File f = new File(userdata, String.valueOf(File.separator) + 
          p.getUniqueId().toString() + ".yml");
      this.plugin.createFileIfNotFound(f);
      YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
      if (!p.hasPermission("vextras.prefix")) {
        p.sendMessage(ChatColor.RED + 
            "You can not set an additional prefix!");
        return true;
      } 
      if (args.length < 1) {
        p.sendMessage(ChatColor.RED + "/prefix <prefix>");
        return true;
      } 
      if (args[0].equalsIgnoreCase("reset")) {
        yamlConfiguration.set("Prefix", null);
        try {
          yamlConfiguration.save(f);
        } catch (IOException iOException) {}
        p.sendMessage(ChatColor.GREEN + "Reset your prefix!");
        return true;
      } 
      long lastUsed = 0L;
      if (this.lastUsage.containsKey(p.getName()))
        lastUsed = ((Long)this.lastUsage.get(p.getName())).longValue(); 
      int cdmillis = 900000;
      if (System.currentTimeMillis() - lastUsed <= cdmillis) {
        int timeLeft = (int)(900L - (System.currentTimeMillis() - lastUsed) / 1000L);
        if (timeLeft >= 60) {
          p.sendMessage(ChatColor.RED + "Wait " + (timeLeft / 60) + 
              " minutes to set your prefix!");
        } else {
          p.sendMessage(ChatColor.RED + "Wait " + timeLeft + 
              " seconds to set your prefix!");
        } 
        return true;
      } 
      StringBuilder b = new StringBuilder();
      for (int i = 0; i < args.length; i++)
        b.append(String.valueOf(args[i]) + " "); 
      String prefix = b.toString();
      String raw = ChatColor.stripColor(prefix.toLowerCase());
      if (raw.contains("owner") || raw.contains("admin") || 
        raw.contains("mod") || raw.contains("helper") || 
        raw.contains("&l") || raw.contains("&n") || 
        raw.contains("&m") || raw.contains("&k") || 
        raw.contains("&l") || raw.contains("&o")) {
        p.sendMessage(ChatColor.RED + 
            "Your prefix contained a banned word/color and was not set!");
        return true;
      } 
      if (raw.length() > 16) {
        p.sendMessage(ChatColor.RED + 
            "Your prefix can't contain more than 15 characters!");
        return true;
      } 
      yamlConfiguration.set("Prefix", prefix);
      try {
        yamlConfiguration.save(f);
      } catch (IOException iOException) {}
      p.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            ChatColor.GREEN + "Set your prefix to " + prefix));
      this.lastUsage
        .put(p.getName(), Long.valueOf(System.currentTimeMillis()));
    } 
    return false;
  }
}
