package me.arman.vextras.commands;

import java.util.Arrays;
import java.util.List;
import me.arman.vextras.Main;
import me.arman.vextras.Utils;
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

public class CustomEnchant implements CommandExecutor {
  private Main plugin;
  
  String prefix;
  
  public CustomEnchant(Main instance) {
    this.plugin = instance;
    this.prefix = Main.prefix;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("customenchant")) {
      if (!sender.hasPermission("vextras.customenchant")) {
        sender.sendMessage(String.valueOf(this.prefix) + ChatColor.RED + 
            " You can not put custom enchantments on items!");
        return true;
      } 
      if (args.length < 3) {
        sender.sendMessage(String.valueOf(this.prefix) + 
            ChatColor.RED + 
            " Usage: /customenchant <player> <enchantment> <level>");
        return true;
      } 
      Player target = Bukkit.getPlayer(args[0]);
      if (target == null) {
        sender.sendMessage(String.valueOf(args[0]) + ChatColor.RED + " is not online!");
        return true;
      } 
      ItemStack item = target.getItemInHand();
      if (item == null) {
        sender.sendMessage(String.valueOf(target.getName()) + ChatColor.RED + 
            " has no item in their hand!");
        return true;
      } 
      Material[] tools = { 
          Material.DIAMOND_PICKAXE, 
          Material.DIAMOND_SWORD, Material.DIAMOND_SPADE, 
          Material.DIAMOND_AXE, Material.IRON_PICKAXE, 
          Material.IRON_SWORD, Material.IRON_SPADE, 
          Material.IRON_AXE, Material.GOLD_PICKAXE, 
          Material.GOLD_SWORD, 
          Material.GOLD_SPADE, 
          Material.GOLD_AXE, Material.STONE_PICKAXE, 
          Material.STONE_SWORD, Material.STONE_SPADE, 
          Material.STONE_AXE, Material.WOOD_PICKAXE, 
          Material.WOOD_SWORD, Material.WOOD_SPADE, Material.WOOD_AXE };
      if (!Arrays.<Material>asList(tools).contains(item.getType())) {
        sender.sendMessage(String.valueOf(target.getName()) + ChatColor.RED + " has a " + 
            item.getType().name() + 
            ". This can not have custom enchantments!");
        return true;
      } 
      ItemMeta meta = item.getItemMeta();
      if (!Utils.isInt(args[2])) {
        sender.sendMessage(ChatColor.RED + args[2] + 
            " is not a valid enchantment level!");
        return true;
      } 
      int level = Integer.parseInt(args[2]);
      if (args[1].equalsIgnoreCase("blazingtouch")) {
        if (level != 1) {
          sender.sendMessage(ChatColor.RED + 
              "Blazing touch can not have level " + 
              Utils.getRoman(level));
          return true;
        } 
        if (item.containsEnchantment(Enchantment.SILK_TOUCH)) {
          sender.sendMessage(ChatColor.RED + 
              "Blazing touch can not go on a silk touch pickaxe!");
          target.sendMessage(ChatColor.RED + 
              "Blazing touch can not go on a silk touch pickaxe!");
          return true;
        } 
        if (meta.hasLore()) {
          if (meta.getLore().contains(
              ChatColor.AQUA + "Blazing Touch " + 
              Utils.getRoman(level)))
            sender.sendMessage(ChatColor.AQUA + target.getName() + 
                "'s " + ChatColor.GREEN + 
                item.getType().name() + ChatColor.AQUA + 
                " already has Blazing Touch " + 
                Utils.getRoman(level)); 
          meta.getLore().add(
              ChatColor.AQUA + "Blazing Touch " + 
              Utils.getRoman(level));
        } else {
          meta.setLore(Arrays.asList(new String[] { ChatColor.AQUA + 
                  "Blazing Touch " + Utils.getRoman(level) }));
        } 
        item.setItemMeta(meta);
        sender.sendMessage(ChatColor.AQUA + "Added Blazing Touch " + 
            ChatColor.GREEN + Utils.getRoman(level) + 
            ChatColor.AQUA + " to " + target.getName() + "'s " + 
            ChatColor.GREEN + item.getType().name());
        if (!target.getName().equalsIgnoreCase(sender.getName()))
          target.sendMessage(ChatColor.AQUA + "Added Blazing Touch " + 
              ChatColor.GREEN + Utils.getRoman(level) + 
              ChatColor.AQUA + " to your " + ChatColor.GREEN + 
              item.getType().name()); 
      } else if (args[1].equalsIgnoreCase("keyhunter")) {
        if (meta.hasLore()) {
          List<String> newLore = meta.getLore();
          for (String lore : meta.getLore()) {
            if (ChatColor.stripColor(lore).contains("Key Hunter")) {
              newLore.remove(lore);
              newLore.add(ChatColor.AQUA + "Key Hunter " + 
                  Utils.getRoman(level));
              meta.setLore(newLore);
            } 
          } 
          meta.getLore().add(
              ChatColor.AQUA + "Key Hunter " + 
              Utils.getRoman(level));
        } else {
          meta.setLore(Arrays.asList(new String[] { ChatColor.AQUA + "Key Hunter " + 
                  Utils.getRoman(level) }));
        } 
        item.setItemMeta(meta);
        sender.sendMessage(ChatColor.AQUA + "Added Key Hunter " + 
            ChatColor.GREEN + Utils.getRoman(level) + 
            ChatColor.AQUA + " to " + target.getName() + "'s " + 
            ChatColor.GREEN + item.getType().name());
        if (!target.getName().equalsIgnoreCase(sender.getName()))
          target.sendMessage(ChatColor.AQUA + "Added Key Hunter " + 
              ChatColor.GREEN + Utils.getRoman(level) + 
              ChatColor.AQUA + " to your " + ChatColor.GREEN + 
              item.getType().name()); 
      } else if (args[1].equalsIgnoreCase("ambit")) {
        if (level != 1) {
          sender.sendMessage(ChatColor.RED + 
              "Ambit can not have level " + 
              Utils.getRoman(level));
          return true;
        } 
        if (meta.hasLore()) {
          List<String> newLore = meta.getLore();
          for (String lore : meta.getLore()) {
            if (ChatColor.stripColor(lore).contains("Ambit")) {
              newLore.remove(lore);
              newLore.add(ChatColor.AQUA + "Ambit " + 
                  Utils.getRoman(level));
              meta.setLore(newLore);
            } 
          } 
          meta.getLore().add(
              ChatColor.AQUA + "Ambit " + Utils.getRoman(level));
        } else {
          meta.setLore(Arrays.asList(new String[] { ChatColor.AQUA + "Ambit " + 
                  Utils.getRoman(level) }));
        } 
        item.setItemMeta(meta);
        sender.sendMessage(ChatColor.AQUA + "Added Ambit " + 
            ChatColor.GREEN + Utils.getRoman(level) + 
            ChatColor.AQUA + " to " + target.getName() + "'s " + 
            ChatColor.GREEN + item.getType().name());
        if (!target.getName().equalsIgnoreCase(sender.getName()))
          target.sendMessage(ChatColor.AQUA + "Added Ambit " + 
              ChatColor.GREEN + Utils.getRoman(level) + 
              ChatColor.AQUA + " to your " + ChatColor.GREEN + 
              item.getType().name()); 
      } else {
        sender.sendMessage(ChatColor.RED + 
            args[1] + 
            " is not a valid custom enchantment! Valid custom enchantments:");
        sender.sendMessage(ChatColor.RED + "blazingtouch");
        sender.sendMessage(ChatColor.RED + "keyhunter");
        sender.sendMessage(ChatColor.RED + "ambit");
      } 
    } 
    return false;
  }
}
