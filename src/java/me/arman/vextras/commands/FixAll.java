package me.arman.vextras.commands;

import java.util.Arrays;
import java.util.HashMap;
import me.arman.vextras.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FixAll implements CommandExecutor {
  private Main plugin;
  
  HashMap<String, Long> lastUsage = new HashMap<>();
  
  public FixAll(Main instance) {
    this.plugin = instance;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("fixall")) {
      if (!(sender instanceof Player)) {
        sender.sendMessage(ChatColor.RED + "Console can't fix all.");
        return true;
      } 
      Player p = (Player)sender;
      if (!p.hasPermission("vextras.fixall")) {
        p.sendMessage(ChatColor.RED + "You can't fix all!");
        return true;
      } 
      long lastUsed = 0L;
      if (this.lastUsage.containsKey(p.getName()))
        lastUsed = ((Long)this.lastUsage.get(p.getName())).longValue(); 
      int cdmillis = 28800000;
      if (System.currentTimeMillis() - lastUsed <= cdmillis) {
        int timeLeft = (int)(28800L - (System.currentTimeMillis() - lastUsed) / 1000L);
        if (timeLeft >= 3600) {
          p.sendMessage(ChatColor.RED + "Wait " + (timeLeft / 3600) + 
              " hours to fix all!");
        } else if (timeLeft >= 60) {
          p.sendMessage(ChatColor.RED + "Wait " + (timeLeft / 60) + 
              " minutes to fix all!");
        } else {
          p.sendMessage(ChatColor.RED + "Wait " + timeLeft + 
              " seconds to fix all!");
        } 
        return true;
      } 
      Material[] tools = { 
          Material.DIAMOND_PICKAXE, 
          Material.DIAMOND_SWORD, Material.DIAMOND_SPADE, 
          Material.DIAMOND_AXE, Material.DIAMOND_HOE, 
          Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, 
          Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, 
          Material.IRON_PICKAXE, 
          Material.IRON_SWORD, 
          Material.IRON_SPADE, Material.IRON_AXE, Material.IRON_HOE, 
          Material.IRON_HELMET, Material.IRON_CHESTPLATE, 
          Material.IRON_LEGGINGS, Material.IRON_BOOTS, 
          Material.GOLD_PICKAXE, Material.GOLD_SWORD, 
          Material.GOLD_SPADE, Material.GOLD_AXE, Material.GOLD_HOE, 
          Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, 
          Material.GOLD_LEGGINGS, Material.GOLD_BOOTS, 
          Material.STONE_PICKAXE, Material.STONE_SWORD, 
          Material.STONE_SPADE, 
          Material.STONE_AXE, 
          Material.STONE_HOE, Material.CHAINMAIL_HELMET, 
          Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, 
          Material.CHAINMAIL_BOOTS, Material.WOOD_PICKAXE, 
          Material.WOOD_SWORD, Material.WOOD_SPADE, 
          Material.WOOD_AXE, 
          Material.WOOD_HOE, 
          Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, 
          Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, 
          Material.FLINT_AND_STEEL, Material.SHEARS, Material.BOW, 
          Material.FISHING_ROD, Material.ANVIL };
      byte b;
      int i;
      ItemStack[] arrayOfItemStack;
      for (i = (arrayOfItemStack = p.getInventory().getContents()).length, b = 0; b < i; ) {
        ItemStack item = arrayOfItemStack[b];
        if (item != null && 
          item.getDurability() != 0 && 
          Arrays.<Material>asList(tools).contains(item.getType()))
          item.setDurability((short)0); 
        b++;
      } 
      for (i = (arrayOfItemStack = p.getInventory().getArmorContents()).length, b = 0; b < i; ) {
        ItemStack item = arrayOfItemStack[b];
        if (item != null && 
          item.getDurability() != 0 && 
          Arrays.<Material>asList(tools).contains(item.getType()))
          item.setDurability((short)0); 
        b++;
      } 
      p.updateInventory();
      p.sendMessage(ChatColor.GREEN + "Repaired your items!");
      this.lastUsage
        .put(p.getName(), Long.valueOf(System.currentTimeMillis()));
    } 
    return false;
  }
}
