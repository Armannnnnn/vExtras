package me.arman.vextras.commands;

import java.util.ArrayList;
import java.util.List;
import me.arman.vextras.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Near implements CommandExecutor {
  private Main plugin;
  
  String prefix;
  
  public Near(Main instance) {
    this.prefix = Main.prefix;
    this.plugin = instance;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("vnear")) {
      if (!(sender instanceof Player)) {
        sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
            " Console can not track nearby players!");
        return true;
      } 
      Player p = (Player)sender;
      List<Player> players = new ArrayList<>();
      int range = 0;
      if (p.hasPermission("vnear.near")) {
        range = 25;
      } else if (p.hasPermission("vnear.group.citizen")) {
        range = 50;
      } else if (p.hasPermission("vnear.group.donator")) {
        range = 75;
      } else if (p.hasPermission("vnear.group.elite")) {
        range = 100;
      } else if (p.hasPermission("vnear.group.champion")) {
        range = 125;
      } else if (p.hasPermission("vnear.group.hero")) {
        range = 150;
      } else if (p.hasPermission("vnear.group.god")) {
        range = 175;
      } else if (p.hasPermission("vnear.group.mythic")) {
        range = 200;
      } else if (p.hasPermission("vnear.group.legend")) {
        range = 225;
      } 
      for (Entity e : p.getNearbyEntities(range, range, range)) {
        if (e instanceof Player)
          players.add((Player)e); 
      } 
      if (players.size() < 1) {
        sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
            " Nobody is near you!");
        return true;
      } 
      sender.sendMessage(String.valueOf(this.prefix) + ChatColor.GREEN + 
          " Players near you:");
      for (Player a : players) {
        boolean hidden = a.hasPermission("vNear.hidden");
        boolean bypass = p.hasPermission("vNear.hidden.bypass");
        if (((hidden ? 0 : 1) | bypass) != 0) {
          int i = (int)a.getLocation().distance(p.getLocation());
          sender.sendMessage(ChatColor.RED + a.getDisplayName() + 
              ChatColor.DARK_GRAY + " (" + ChatColor.DARK_RED + 
              i + " Blocks Away" + ChatColor.DARK_GRAY + ")");
          if (a.hasPermission("vNear.warn"))
            a.playSound(a.getLocation(), Sound.SUCCESSFUL_HIT, 
                1.0F, 1.0F); 
          if (a.hasPermission("vNear.found"))
            a.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
                "Someone has found you with /near!"); 
        } 
      } 
    } 
    return false;
  }
}
