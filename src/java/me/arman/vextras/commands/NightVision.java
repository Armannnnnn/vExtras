package me.arman.vextras.commands;

import me.arman.vextras.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVision implements CommandExecutor {
  private Main plugin;
  
  String prefix;
  
  public NightVision(Main instance) {
    this.prefix = Main.prefix;
    this.plugin = instance;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("nightvision")) {
      Player p = null;
      if (sender instanceof Player) {
        p = (Player)sender;
      } else if (args.length > 0) {
        String playerstring = args[0];
        Player remoteplayer = 
          (Player)Bukkit.getOfflinePlayer(playerstring);
        if (remoteplayer.isOnline()) {
          p = remoteplayer;
        } else {
          sender.sendMessage(String.valueOf(Main.prefix) + ChatColor.RED + 
              " That player is not online.");
        } 
      } else {
        sender.sendMessage(String.valueOf(Main.prefix) + ChatColor.RED + 
            " Usage: /nv [player]");
      } 
      if (p != null) {
        if (!p.hasPermission("vr.nightvision")) {
          sender.sendMessage(String.valueOf(Main.prefix) + ChatColor.RED + 
              " You can not use night vision!");
          return true;
        } 
        if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
          p.removePotionEffect(PotionEffectType.NIGHT_VISION);
          sender.sendMessage(String.valueOf(Main.prefix) + ChatColor.RED + 
              " Night vision disabled! Use /nv to enable!");
        } else {
          p.addPotionEffect(new PotionEffect(
                PotionEffectType.NIGHT_VISION, 2147483647, 3));
          sender.sendMessage(String.valueOf(Main.prefix) + ChatColor.GREEN + 
              " Night vision enabled! Use /nv to disable!");
        } 
      } 
    } 
    return false;
  }
}
