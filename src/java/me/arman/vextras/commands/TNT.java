package me.arman.vextras.commands;

import me.arman.vextras.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TNT implements CommandExecutor {
  private Main plugin;
  
  String prefix;
  
  public TNT(Main instance) {
    this.plugin = instance;
    this.prefix = ChatColor.translateAlternateColorCodes('&', "&4&lTNT&8&l>");
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("tnt")) {
      if (!(sender instanceof Player)) {
        sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
            " Console can not autocraft TNT!");
        return true;
      } 
      Player p = (Player)sender;
      if (!p.hasPermission("vextras.tnt")) {
        p.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
            " You do not have access to autocraft TNT!");
        return true;
      } 
      PlayerInventory i = p.getInventory();
      ItemStack s = new ItemStack(Material.SAND, 4);
      ItemStack g = new ItemStack(Material.SULPHUR, 5);
      ItemStack t = new ItemStack(Material.TNT, 2);
      int amount = 0;
      if (i.containsAtLeast(s, 4) && i.containsAtLeast(g, 5)) {
        while (i.containsAtLeast(s, 4) && i.containsAtLeast(g, 5)) {
          i.removeItem(new ItemStack[] { s });
          i.removeItem(new ItemStack[] { g });
          i.addItem(new ItemStack[] { t });
          amount++;
        } 
        amount *= 2;
        p.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + " You have autocrafted " + 
            amount + " TNT!");
        p.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
            " (You get 2x more TNT when crafting!)");
      } else {
        p.sendMessage(String.valueOf(this.prefix) + 
            ChatColor.RED + 
            " You need at least 4 Sand and 5 Gunpowder to autocraft TNT!");
        return true;
      } 
    } 
    return false;
  }
}
