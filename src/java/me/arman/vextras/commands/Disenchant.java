package me.arman.vextras.commands;

import java.util.List;
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

public class Disenchant implements CommandExecutor {
  private Main plugin;
  
  String prefix;
  
  public Disenchant(Main instance) {
    this.prefix = Main.prefix;
    this.plugin = instance;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("disenchant")) {
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
            " Usage: /disenchant [player]");
      } 
      if (p != null) {
        ItemStack item = p.getItemInHand();
        if (!p.hasPermission("disenchant.use")) {
          sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
              " You can not disenchant items!");
          return true;
        } 
        if (item.getType() == Material.TRIPWIRE_HOOK && 
          item.getEnchantmentLevel(Enchantment.DURABILITY) == 10) {
          sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
              " You can not disenchant crate keys!");
          return true;
        } 
        if (item.getEnchantments().size() < 1) {
          sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
              " The item has no enchantments!");
          return true;
        } 
        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
          List<String> newLore = meta.getLore();
          for (String lore : meta.getLore()) {
            String stripped = ChatColor.stripColor(lore);
            if (stripped.contains("Blazing Touch") || 
              stripped.contains("Key Hunter"))
              newLore.remove(lore); 
          } 
          meta.setLore(newLore);
          item.setItemMeta(meta);
        } 
        byte b;
        int i;
        Enchantment[] arrayOfEnchantment;
        for (i = (arrayOfEnchantment = Enchantment.values()).length, b = 0; b < i; ) {
          Enchantment e = arrayOfEnchantment[b];
          item.removeEnchantment(e);
          b++;
        } 
        sender.sendMessage(String.valueOf(this.prefix) + ChatColor.GREEN + 
            " You have removed all enchantments from the item!");
      } 
    } 
    return false;
  }
}
