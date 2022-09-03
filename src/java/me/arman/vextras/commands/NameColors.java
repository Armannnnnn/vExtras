package me.arman.vextras.commands;

import java.io.File;
import java.io.IOException;
import me.arman.vextras.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class NameColors implements CommandExecutor {
  private Main plugin;
  
  public NameColors(Main instance) {
    this.plugin = instance;
  }
  
  public void color(Player p, String message) {
    p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("namecolor")) {
      if (!(sender instanceof Player)) {
        if (args.length < 2) {
          sender.sendMessage(ChatColor.RED + 
              "Usage: /namecolor <player <color>");
          return true;
        } 
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        Player player = null;
        if (ChatColor.valueOf(args[1].toUpperCase()) == null) {
          sender.sendMessage(ChatColor.RED + args[1] + 
              " is an invalid name color!");
          return true;
        } 
        args[1] = args[1].toUpperCase();
        File file1 = new File(this.plugin.getDataFolder(), String.valueOf(File.separator) + 
            "users");
        File file2 = new File(file1, String.valueOf(File.separator) + 
            target.getUniqueId().toString() + ".yml");
        this.plugin.createFileIfNotFound(file2);
        YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(file2);
        if (args[1].equals("RESET")) {
          yamlConfiguration1.set("NameColor", null);
          try {
            yamlConfiguration1.save(file2);
          } catch (IOException iOException) {}
          sender.sendMessage(ChatColor.GREEN + 
              "You have successfully unset " + target.getName() + 
              "'s name color!");
          if (target.isOnline()) {
            player = (Player)target;
            player.setPlayerListName(ChatColor.valueOf(args[0]) + 
                target.getName());
          } 
          return true;
        } 
        yamlConfiguration1.set("NameColor", args[1]);
        sender.sendMessage(ChatColor.GREEN + "You have set " + 
            target.getName() + "'s name color to " + 
            ChatColor.valueOf(args[1]) + args[1]);
        try {
          yamlConfiguration1.save(file2);
        } catch (IOException iOException) {}
        return true;
      } 
      Player p = (Player)sender;
      if (!p.hasPermission("namecolor.color")) {
        color(p, "&cYou do not have permission to set your name color!");
        return true;
      } 
      if (args.length < 1) {
        color(p, "&cUsage: /namecolor <color name>");
        color(p, "&aAvailable Colors:");
        color(p, "&rRESET");
        color(p, "&2DARK_GREEN");
        color(p, "&3DARK_AQUA");
        color(p, "&4DARK_RED");
        color(p, "&5DARK_PURPLE");
        color(p, "&6GOLD");
        color(p, "&7GRAY");
        color(p, "&8DARK_GRAY");
        color(p, "&9BLUE");
        color(p, "&aGREEN");
        color(p, "&bAQUA");
        color(p, "&cRED");
        color(p, "&dLIGHT_PURPLE");
        color(p, "&eYELLOW");
        color(p, "&fWHITE");
        return true;
      } 
      if (ChatColor.valueOf(args[0].toUpperCase()) == null) {
        color(p, 
            "&c " + 
            args[0] + 
            " was an invalid name color! Type /namecolor for a list of colors!");
        return true;
      } 
      if (args[0].equalsIgnoreCase("BOLD") || 
        args[0].equalsIgnoreCase("ITALIC") || 
        args[0].equalsIgnoreCase("UNDERLINE") || 
        args[0].equalsIgnoreCase("MAGIC") || 
        args[0].equalsIgnoreCase("BLACK")) {
        color(p, 
            "&c " + 
            args[0] + 
            " That was an invalid name color! Type /namecolor for a list of colors!");
        return true;
      } 
      args[0] = args[0].toUpperCase();
      File userdata = new File(this.plugin.getDataFolder(), String.valueOf(File.separator) + 
          "users");
      File f = new File(userdata, String.valueOf(File.separator) + 
          p.getUniqueId().toString() + ".yml");
      this.plugin.createFileIfNotFound(f);
      YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
      if (args[0].equals("RESET")) {
        yamlConfiguration.set("NameColor", null);
        try {
          yamlConfiguration.save(f);
        } catch (IOException iOException) {}
        color(p, "&aYou have successfully unset your name color!");
        p.setPlayerListName(ChatColor.valueOf(args[0]) + p.getName());
        return true;
      } 
      yamlConfiguration.set("NameColor", args[0]);
      try {
        yamlConfiguration.save(f);
      } catch (IOException iOException) {}
      p.setPlayerListName(ChatColor.valueOf(args[0]) + p.getName());
      color(p, "&aYou have successfully set your name color to " + 
          ChatColor.valueOf(args[0]) + args[0]);
    } 
    return false;
  }
  
  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    final ChatColor color;
    final Player p = e.getPlayer();
    File userdata = new File(this.plugin.getDataFolder(), String.valueOf(File.separator) + 
        "users");
    File f = new File(userdata, String.valueOf(File.separator) + p.getUniqueId().toString() + 
        ".yml");
    this.plugin.createFileIfNotFound(f);
    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
    if (f.exists() && yamlConfiguration.getString("NameColor") != null) {
      color = ChatColor.valueOf(yamlConfiguration.getString("NameColor"));
    } else {
      color = ChatColor.RESET;
    } 
    Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, new Runnable() {
          public void run() {
            p.setPlayerListName(color + p.getName());
            p.setDisplayName(color + p.getDisplayName());
          }
        },  10L);
  }
}
