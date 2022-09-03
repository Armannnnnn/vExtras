package me.arman.vextras.commands;

import me.arman.vextras.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Unname implements CommandExecutor {
  private Main plugin;
  
  String prefix;
  
  public Unname(Main instance) {
    this.prefix = Main.prefix;
    this.plugin = instance;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("unname")) {
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
          sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
              " That player is not online.");
        } 
      } else {
        sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
            " Usage: /unname [player]");
      } 
      if (p != null) {
        ItemStack item = p.getItemInHand();
        if (!p.hasPermission("unname.use")) {
          sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
              " You can not unname items!");
          return true;
        } 
        if (!item.hasItemMeta()) {
          sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
              " This item does not have a name!");
          return true;
        } 
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) {
          sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
              " This item does not have a name!");
          return true;
        } 
        if (item.getType() == Material.TRIPWIRE_HOOK && 
          item.getEnchantmentLevel(Enchantment.DURABILITY) == 10) {
          sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
              " You can not unname crate keys!");
          return true;
        } 
        if (item.getType() == Material.EGG)
          if (meta.getDisplayName().equals(
              ChatColor.LIGHT_PURPLE + "Easter Egg")) {
            sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
                " You cannot unname an Easter Egg!");
            return true;
          }  
        if (item.getType() == Material.MOB_SPAWNER) {
          sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
              " You can not unname mob spawners!");
          return true;
        } 
        if (!meta.hasDisplayName()) {
          sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
              " This item does not have a name!");
          return true;
        } 
        meta.setDisplayName(null);
        item.setItemMeta(meta);
        sender.sendMessage(String.valueOf(this.prefix) + ChatColor.GREEN + 
            " You have removed the name from your item!");
      } 
    } 
    return false;
  }
}
