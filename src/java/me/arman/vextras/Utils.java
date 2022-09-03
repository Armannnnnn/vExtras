package me.arman.vextras;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Utils {
  private static WorldGuardPlugin getWorldGuard() {
    Plugin p = Bukkit.getPluginManager().getPlugin("WorldGuard");
    if (p == null || !p.isEnabled())
      return null; 
    return (WorldGuardPlugin)p;
  }
  
  static final char[] symbol = new char[] { 'M', 'D', 'C', 'L', 'X', 'V', 'I' };
  
  static final int[] value = new int[] { 1000, 500, 100, 50, 10, 5, 1 };
  
  public static int valueOf(String roman) {
    roman = roman.toUpperCase();
    if (roman.length() == 0)
      return 0; 
    for (int i = 0; i < symbol.length; i++) {
      int pos = roman.indexOf(symbol[i]);
      if (pos >= 0)
        return value[i] - valueOf(roman.substring(0, pos)) + 
          valueOf(roman.substring(pos + 1)); 
    } 
    throw new IllegalArgumentException("Invalid Roman Symbol.");
  }
  
  public static String getRoman(int number) {
    String[] riman = { 
        "M", "XM", "CM", "D", "XD", "CD", "C", "XC", "L", 
        "XL", 
        "X", "IX", "V", "IV", "I" };
    int[] arab = { 
        1000, 990, 900, 500, 490, 400, 100, 90, 50, 40, 
        10, 9, 
        5, 4, 1 };
    StringBuilder result = new StringBuilder();
    int i = 0;
    while (number > 0 || arab.length == i - 1) {
      while (number - arab[i] >= 0) {
        number -= arab[i];
        result.append(riman[i]);
      } 
      i++;
    } 
    return result.toString();
  }
  
  public static boolean isInt(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException numberFormatException) {
      return false;
    } 
  }
  
  public static String getDirection(Player player) {
    double rotation = ((player.getLocation().getYaw() - 90.0F) % 360.0F);
    if (rotation < 0.0D)
      rotation += 360.0D; 
    if (0.0D <= rotation && rotation < 22.5D)
      return "N"; 
    if (22.5D <= rotation && rotation < 67.5D)
      return "NE"; 
    if (67.5D <= rotation && rotation < 112.5D)
      return "E"; 
    if (112.5D <= rotation && rotation < 157.5D)
      return "SE"; 
    if (157.5D <= rotation && rotation < 202.5D)
      return "S"; 
    if (202.5D <= rotation && rotation < 247.5D)
      return "SW"; 
    if (247.5D <= rotation && rotation < 292.5D)
      return "W"; 
    if (292.5D <= rotation && rotation < 337.5D)
      return "NW"; 
    if (337.5D <= rotation && rotation < 360.0D)
      return "N"; 
    return null;
  }
  
  public static boolean getFaction(Player p, Location loc) {
    if (Bukkit.getPluginManager().getPlugin("Factions") == null || 
      !Bukkit.getPluginManager().getPlugin("Factions").isEnabled())
      return false; 
    FLocation floc = new FLocation(loc);
    Faction faction = Board.getInstance().getFactionAt(floc);
    FPlayer fp = FPlayers.getInstance().getByPlayer(p);
    if (ChatColor.stripColor(faction.getTag()).equalsIgnoreCase(
        "Wilderness") || 
      faction.getTag().equalsIgnoreCase(fp.getFaction().getTag()))
      return false; 
    return true;
  }
  
  public static void breakRadius(Player p, World w, int x, int y, int z) {
    Location loc = new Location(w, x, y, z);
    Block b = w.getBlockAt(x, y, z);
    if (b != null && !getFaction(p, loc))
      breakNaturally(p, w, x, y, z); 
  }
  
  public static void breakNaturally(Player p, World w, int x, int y, int z) {
    Location loc = new Location(w, x, y, z);
    Block b = w.getBlockAt(x, y, z);
    if (b.getType() == Material.CHEST) {
      Chest c = (Chest)b.getState();
      Inventory inv = c.getInventory();
      byte b1;
      int i;
      ItemStack[] arrayOfItemStack;
      for (i = (arrayOfItemStack = inv.getContents()).length, b1 = 0; b1 < i; ) {
        ItemStack item = arrayOfItemStack[b1];
        if (item != null)
          w.dropItemNaturally(loc, item); 
        b1++;
      } 
    } else if (b.getType() == Material.FURNACE) {
      Furnace f = (Furnace)b.getState();
      FurnaceInventory furnaceInventory = f.getInventory();
      byte b1;
      int i;
      ItemStack[] arrayOfItemStack;
      for (i = (arrayOfItemStack = furnaceInventory.getContents()).length, b1 = 0; b1 < i; ) {
        ItemStack item = arrayOfItemStack[b1];
        if (item != null)
          w.dropItemNaturally(loc, item); 
        b1++;
      } 
    } else if (b.getType() == Material.BREWING_STAND_ITEM) {
      BrewingStand br = (BrewingStand)b.getState();
      BrewerInventory brewerInventory = br.getInventory();
      byte b1;
      int i;
      ItemStack[] arrayOfItemStack;
      for (i = (arrayOfItemStack = brewerInventory.getContents()).length, b1 = 0; b1 < i; ) {
        ItemStack item = arrayOfItemStack[b1];
        if (item != null)
          w.dropItemNaturally(loc, item); 
        b1++;
      } 
    } 
    ItemStack inhand = p.getItemInHand();
    ItemMeta im = inhand.getItemMeta();
    if (b.getType() != Material.BEDROCK && 
      b.getType() != Material.MOB_SPAWNER)
      if (b.getType() != Material.IRON_ORE && 
        b.getType() != Material.GOLD_ORE) {
        if (!inhand.containsEnchantment(Enchantment.SILK_TOUCH)) {
          if (inhand
            .containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            int dropCount = 1 + inhand
              .getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
            p.getWorld().dropItemNaturally(loc, 
                new ItemStack(b.getType(), dropCount));
            b.setType(Material.AIR);
          } else {
            w.getBlockAt(x, y, z).breakNaturally();
          } 
        } else if (b.getType() == Material.STONE && 
          b.getData() == 0) {
          p.getWorld().dropItemNaturally(loc, 
              new ItemStack(Material.STONE, 1));
          b.setType(Material.AIR);
        } else if (b.getType() == Material.COAL_ORE) {
          p.getWorld().dropItemNaturally(loc, 
              new ItemStack(Material.COAL_ORE, 1));
          b.setType(Material.AIR);
        } else if (b.getType() == Material.IRON_ORE) {
          p.getWorld().dropItemNaturally(loc, 
              new ItemStack(Material.IRON_ORE, 1));
          b.setType(Material.AIR);
        } else if (b.getType() == Material.GOLD_ORE) {
          p.getWorld().dropItemNaturally(loc, 
              new ItemStack(Material.GOLD_ORE, 1));
          b.setType(Material.AIR);
        } else if (b.getType() == Material.DIAMOND_ORE) {
          p.getWorld().dropItemNaturally(loc, 
              new ItemStack(Material.DIAMOND_ORE, 1));
          b.setType(Material.AIR);
        } else if (b.getType() == Material.EMERALD_ORE) {
          p.getWorld().dropItemNaturally(loc, 
              new ItemStack(Material.EMERALD_ORE, 1));
          b.setType(Material.AIR);
        } else if (b.getType() == Material.REDSTONE_ORE) {
          p.getWorld().dropItemNaturally(loc, 
              new ItemStack(Material.REDSTONE_ORE, 1));
          b.setType(Material.AIR);
        } else if (b.getType() == Material.LAPIS_ORE) {
          p.getWorld().dropItemNaturally(loc, 
              new ItemStack(Material.LAPIS_ORE, 1));
          b.setType(Material.AIR);
        } else if (b.getType() == Material.GRASS) {
          p.getWorld().dropItemNaturally(loc, 
              new ItemStack(Material.GRASS, 1));
          b.setType(Material.AIR);
        } else if (b.getType() == Material.QUARTZ_ORE) {
          p.getWorld().dropItemNaturally(loc, 
              new ItemStack(Material.QUARTZ_ORE, 1));
          b.setType(Material.AIR);
        } else if (b.getType() == Material.GLASS) {
          p.getWorld().dropItemNaturally(loc, 
              new ItemStack(Material.GLASS, 1));
          b.setType(Material.AIR);
        } else if (b.getType() == Material.MYCEL) {
          p.getWorld().dropItemNaturally(loc, 
              new ItemStack(Material.MYCEL, 1));
          b.setType(Material.AIR);
        } else if (b.getType() == Material.THIN_GLASS) {
          p.getWorld().dropItemNaturally(loc, 
              new ItemStack(Material.THIN_GLASS, 1));
          b.setType(Material.AIR);
        } else if (b.getType() == Material.STAINED_GLASS) {
          ItemStack glass = new ItemStack(Material.STAINED_GLASS, 
              1);
          glass.setDurability((short)b.getData());
          p.getWorld().dropItemNaturally(loc, glass);
          b.setType(Material.AIR);
        } else if (b.getType() == Material.STAINED_GLASS_PANE) {
          ItemStack glass = new ItemStack(
              Material.STAINED_GLASS_PANE, 1);
          glass.setDurability((short)b.getData());
          p.getWorld().dropItemNaturally(loc, glass);
          b.setType(Material.AIR);
        } else {
          w.getBlockAt(x, y, z).breakNaturally();
        } 
      } else if (im.hasLore()) {
        if (im.getLore().contains(
            ChatColor.AQUA + "Blazing Touch I")) {
          boolean hasFortune = false;
          if (inhand
            .getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) >= 1)
            hasFortune = true; 
          int dropCount = 1;
          if (hasFortune) {
            int lev = inhand
              .getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
            if (lev > 10) {
              dropCount = 12;
            } else {
              dropCount = lev + 1;
            } 
          } 
          p.playEffect(b.getLocation(), Effect.SMOKE, 1);
          p.setExp(p.getExp() + 3.0F);
          if (b.getType() == Material.IRON_ORE) {
            ItemStack toDrop = new ItemStack(Material.IRON_INGOT, 
                dropCount);
            w.dropItemNaturally(loc, toDrop);
          } else if (b.getType() == Material.GOLD_ORE) {
            ItemStack toDrop = new ItemStack(Material.GOLD_INGOT, 
                dropCount);
            w.dropItemNaturally(loc, toDrop);
          } 
          b.setType(Material.AIR);
        } else {
          w.getBlockAt(x, y, z).breakNaturally();
        } 
      }  
  }
}
