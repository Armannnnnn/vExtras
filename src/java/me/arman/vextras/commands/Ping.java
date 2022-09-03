package me.arman.vextras.commands;

import me.arman.vextras.Main;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Ping implements CommandExecutor {
  private Main plugin;
  
  String prefix;
  
  public Ping(Main instance) {
    this.plugin = instance;
    this.prefix = Main.prefix;
  }
  
  public int getPing(Player p) {
    CraftPlayer cp = (CraftPlayer)p;
    EntityPlayer ep = cp.getHandle();
    return ep.ping;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("ping")) {
      if (!(sender instanceof Player)) {
        if (args.length == 0) {
          sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
              " Usage: /ping <player>");
          return true;
        } 
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
          sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + " " + args[0] + 
              " is not online!");
          return true;
        } 
        sender.sendMessage(String.valueOf(this.prefix) + ChatColor.GREEN + " " + 
            player.getName() + ChatColor.AQUA + "'s Ping: " + 
            ChatColor.GREEN + getPing(player) + "ms");
        return true;
      } 
      Player p = (Player)sender;
      if (args.length == 0) {
        if (!p.hasPermission("vextras.ping")) {
          p.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
              " You do not have access to check ping!");
          return true;
        } 
        p.sendMessage(String.valueOf(this.prefix) + ChatColor.AQUA + " Your Ping: " + 
            ChatColor.GREEN + getPing(p) + "ms");
        return true;
      } 
      Player target = Bukkit.getPlayer(args[0]);
      if (!p.hasPermission("vextras.ping.others")) {
        p.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
            " You do not have access to check others ping!");
        return true;
      } 
      if (target == null) {
        p.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + " " + args[0] + 
            " is not online!");
        return true;
      } 
      p.sendMessage(String.valueOf(this.prefix) + ChatColor.AQUA + " " + target.getName() + 
          "'s Ping: " + ChatColor.GREEN + getPing(target) + "ms");
    } 
    return false;
  }
}
