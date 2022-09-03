package me.arman.vextras.events;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.arman.vextras.Main;
import me.arman.vextras.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class GeneralEvents implements Listener {
  private Main plugin;
  
  String prefix = Main.prefix;
  
  public static HashMap<String, String> groupColors;
  
  public GeneralEvents(Main instance) {
    this.plugin = instance;
    groupColors = new HashMap<>();
    groupColors.put("default", "GRAY");
    groupColors.put("citizen", "WHITE");
    groupColors.put("donator", "GREEN");
    groupColors.put("elite", "DARK_AQUA");
    groupColors.put("champion", "AQUA");
    groupColors.put("hero", "DARK_PURPLE");
    groupColors.put("god", "GOLD");
    groupColors.put("mythic", "DARK_GREEN");
    groupColors.put("legend", "RED");
    groupColors.put("helper", "YELLOW");
    groupColors.put("mod", "BLUE");
    groupColors.put("admin", "DARK_RED");
    groupColors.put("head-admin", "DARK_RED");
    groupColors.put("owner", "RED");
  }
  
  @EventHandler
  public void onCreatureSpawn(CreatureSpawnEvent e) {
    CreatureSpawnEvent.SpawnReason sr = e.getSpawnReason();
    EntityType et = e.getEntity().getType();
    if (et == EntityType.ENDER_DRAGON) {
      if (e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL))
        e.setCancelled(true); 
    } else if (et == EntityType.WITHER) {
      if (sr == CreatureSpawnEvent.SpawnReason.BUILD_WITHER)
        e.setCancelled(true); 
    } else if (et == EntityType.IRON_GOLEM) {
      if (sr == CreatureSpawnEvent.SpawnReason.SPAWNER) {
        Random r = new Random();
        int num = r.nextInt(100) + 1;
        if (num <= 25)
          e.setCancelled(true); 
      } 
    } else if (et == EntityType.CHICKEN && (
      sr == CreatureSpawnEvent.SpawnReason.NATURAL || sr == CreatureSpawnEvent.SpawnReason.EGG || 
      sr == CreatureSpawnEvent.SpawnReason.SPAWNER)) {
      Random r = new Random();
      int num = r.nextInt(100) + 1;
      if (num <= 25)
        e.setCancelled(true); 
    } 
  }
  
  @EventHandler
  public void onDeath(EntityDeathEvent e) {
    if (e.getEntityType() == EntityType.IRON_GOLEM) {
      List<ItemStack> drops = new ArrayList<>();
      e.getDrops().clear();
      Random r = new Random();
      int num = r.nextInt(100) + 1;
      if (num <= 25) {
        drops.add(new ItemStack(Material.IRON_INGOT, 2));
      } else {
        drops.add(new ItemStack(Material.IRON_INGOT, 1));
      } 
      e.getDrops().addAll(drops);
    } 
  }
  
  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    e.setJoinMessage(null);
    Player p = e.getPlayer();
    FPlayer fp = FPlayers.getInstance().getByPlayer(p);
    if (!p.hasPermission("vextras.ignorejoin")) {
      TextComponent join = new TextComponent(ChatColor.DARK_GRAY + "[" + 
          ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + 
          ChatColor.GREEN + p.getName() + ChatColor.GRAY + 
          " has joined");
      join.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (
            new ComponentBuilder(ChatColor.AQUA + "Faction: " + 
              ChatColor.GREEN + fp.getFaction().getTag() + "\n" + 
              ChatColor.AQUA + "Power: " + ChatColor.GREEN + 
              fp.getPowerRounded() + "/" + 
              fp.getPowerMaxRounded() + "\n" + ChatColor.AQUA + 
              "Balance: " + ChatColor.GREEN + "$" + 
              Main.economy.getBalance((OfflinePlayer)p))).create()));
      join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, 
            "/f who " + p.getName()));
      for (Player all : Bukkit.getOnlinePlayers())
        all.spigot().sendMessage((BaseComponent)join); 
    } 
    File userdata = new File(this.plugin.getDataFolder(), String.valueOf(File.separator) + 
        "users");
    File f = new File(userdata, String.valueOf(File.separator) + p.getUniqueId().toString() + 
        ".yml");
    this.plugin.createFileIfNotFound(f);
    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
    List<String> ips = yamlConfiguration.getStringList("ips");
    String ip = p.getAddress().getHostName();
    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    for (String finalString : ips) {
      if (finalString.contains(ip))
        return; 
    } 
    ips.add(String.valueOf(ip) + ":" + format.format(now));
    yamlConfiguration.set("ips", ips);
    try {
      yamlConfiguration.save(f);
    } catch (IOException iOException) {}
  }
  
  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    e.setQuitMessage(null);
    Player p = e.getPlayer();
    FPlayer fp = FPlayers.getInstance().getByPlayer(p);
    if (!p.hasPermission("vextras.ignorejoin")) {
      TextComponent quit = new TextComponent(ChatColor.DARK_GRAY + "[" + 
          ChatColor.RED + "-" + ChatColor.DARK_GRAY + "] " + 
          ChatColor.RED + p.getName() + ChatColor.GRAY + 
          " has left");
      quit.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (
            new ComponentBuilder(ChatColor.AQUA + "Faction: " + 
              ChatColor.GREEN + fp.getFaction().getTag() + "\n" + 
              ChatColor.AQUA + "Power: " + ChatColor.GREEN + 
              fp.getPowerRounded() + "/" + 
              fp.getPowerMaxRounded() + "\n" + ChatColor.AQUA + 
              "Balance: " + ChatColor.GREEN + "$" + 
              Main.economy.getBalance((OfflinePlayer)p))).create()));
      quit.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, 
            "/f who " + p.getName()));
      for (Player all : Bukkit.getOnlinePlayers())
        all.spigot().sendMessage((BaseComponent)quit); 
    } 
    File userdata = new File(this.plugin.getDataFolder(), String.valueOf(File.separator) + 
        "users");
    File f = new File(userdata, String.valueOf(File.separator) + p.getUniqueId().toString() + 
        ".yml");
    this.plugin.createFileIfNotFound(f);
    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
    List<String> ips = yamlConfiguration.getStringList("ips");
    String ip = p.getAddress().getHostName();
    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    for (String finalString : ips) {
      if (finalString.contains(ip))
        return; 
    } 
    ips.add(String.valueOf(ip) + ":" + format.format(now));
    yamlConfiguration.set("ips", ips);
    try {
      yamlConfiguration.save(f);
    } catch (IOException iOException) {}
  }
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
    ChatColor color;
    Player p = e.getPlayer();
    String msg = e.getMessage();
    File userdata = new File(this.plugin.getDataFolder(), String.valueOf(File.separator) + 
        "users");
    File f = new File(userdata, String.valueOf(File.separator) + p.getUniqueId().toString() + 
        ".yml");
    this.plugin.createFileIfNotFound(f);
    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
    if (f.exists() && yamlConfiguration.getString("ChatColor") != null) {
      color = ChatColor.valueOf(yamlConfiguration.getString("ChatColor"));
    } else {
      color = ChatColor.RESET;
    } 
    msg = color + msg;
    e.setMessage(msg);
    if (f.exists() && yamlConfiguration.getString("NameColor") != null) {
      color = ChatColor.valueOf(yamlConfiguration.getString("NameColor"));
    } else {
      color = ChatColor.valueOf(groupColors.get(Main.perms
            .getPrimaryGroup(p)));
    } 
    if (!p.getPlayerListName().equalsIgnoreCase(color + p.getName()))
      p.setPlayerListName(color + p.getName()); 
    if (!p.getDisplayName().equalsIgnoreCase(color + p.getDisplayName()))
      p.setDisplayName(color + p.getDisplayName()); 
    if (yamlConfiguration.getString("Prefix") != null) {
      String format = e.getFormat();
      e.setFormat(String.valueOf(ChatColor.translateAlternateColorCodes('&', 
              yamlConfiguration.getString("Prefix"))) + 
          format);
    } 
  }
  
  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    if (!e.isCancelled()) {
      Player breaker = e.getPlayer();
      FPlayer fp = FPlayers.getInstance().getByPlayer(breaker);
      Faction faction = Board.getInstance().getFactionAt(
          fp.getLastStoodAt());
      Block broken = e.getBlock();
      if (broken != null) {
        Material blocktype = broken.getType();
        ItemStack inhand = breaker.getItemInHand();
        if (inhand != null && 
          breaker.getGameMode() == GameMode.SURVIVAL) {
          if (breaker.hasPermission("vextras.spawnercost") && 
            inhand
            .getEnchantmentLevel(Enchantment.SILK_TOUCH) >= 1 && 
            blocktype == Material.MOB_SPAWNER) {
            if (fp.isInOthersTerritory() || 
              faction.isWarZone() || 
              faction.isSafeZone()) {
              e.setCancelled(true);
              return;
            } 
            if (Main.economy.getBalance((OfflinePlayer)breaker) < 10000.0D) {
              breaker.sendMessage(String.valueOf(this.prefix) + 
                  ChatColor.RED + 
                  " Mining spawners with silk touch costs $10,000!");
              e.setCancelled(true);
              return;
            } 
            Main.economy.withdrawPlayer((OfflinePlayer)breaker, 
                10000.0D);
            breaker.sendMessage(String.valueOf(this.prefix) + 
                ChatColor.GREEN + 
                " You have mined a spawner with silk touch for $10,000!");
          } 
          if (inhand.hasItemMeta()) {
            ItemMeta im = inhand.getItemMeta();
            if (im.hasLore()) {
              if (im.getLore().contains(
                  ChatColor.AQUA + "Blazing Touch I")) {
                boolean hasFortune = false;
                if (inhand
                  .getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) >= 1)
                  hasFortune = true; 
                if (blocktype == Material.IRON_ORE || 
                  blocktype == Material.GOLD_ORE) {
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
                  e.setCancelled(true);
                  broken.setType(Material.AIR);
                  breaker.playEffect(
                      broken.getLocation(), 
                      Effect.SMOKE, 1);
                  breaker.setExp(breaker.getExp() + 3.0F);
                  if (blocktype == Material.IRON_ORE) {
                    ItemStack toDrop = new ItemStack(
                        Material.IRON_INGOT, 
                        dropCount);
                    broken.getWorld()
                      .dropItemNaturally(
                        broken.getLocation(), 
                        toDrop);
                  } else if (blocktype == Material.GOLD_ORE) {
                    ItemStack itemStack = new ItemStack(
                        Material.GOLD_INGOT, 
                        dropCount);
                  } 
                } 
              } 
              boolean hasEnchant = false;
              int level = 0;
              for (String lore : im.getLore()) {
                if (lore.startsWith(ChatColor.AQUA + 
                    "Key Hunter")) {
                  hasEnchant = true;
                  String[] split = lore.split(" ");
                  level = Utils.valueOf(split[2]);
                } 
              } 
              if (hasEnchant && 
                broken.getType().isBlock()) {
                int chance;
                Random r = new Random();
                if (level == 1) {
                  chance = 2000;
                } else if (level == 2) {
                  chance = 1400;
                } else {
                  chance = 1000;
                } 
                if (r.nextInt(chance) == 1) {
                  int key = r.nextInt(100);
                  String tier = null;
                  if (key < 50) {
                    tier = "Common";
                  } else if (key < 75) {
                    tier = "Rare";
                  } else if (key < 90) {
                    tier = "Mythical";
                  } else {
                    tier = "Legendary";
                  } 
                  Bukkit.dispatchCommand(
                      (CommandSender)Bukkit.getConsoleSender(), 
                      "cratekey give " + 
                      breaker.getName() + 
                      " " + tier + " 1");
                  breaker.sendMessage(
                      ChatColor.translateAlternateColorCodes(
                        '&', 
                        "&7&l[&a&lKeyHunter] &aYou received a &b" + 
                        tier + 
                        " &acrate key from breaking &b" + 
                        broken.getType()
                        .name()));
                } 
              } 
              if (im.getLore().contains(
                  ChatColor.AQUA + "Ambit I")) {
                e.setCancelled(true);
                inhand.setDurability((short)(inhand.getDurability() + 1));
                breaker.updateInventory();
                if (broken.getLocation().getBlockY() < breaker
                  .getLocation().getBlockY()) {
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX() + 1, 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ());
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX() - 1, 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ());
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX(), 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ() + 1);
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX(), 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ() - 1);
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX() + 1, 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ() + 1);
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX() + 1, 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ() - 1);
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX() - 1, 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ() + 1);
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX() - 1, 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ() - 1);
                } else if (broken.getLocation().getBlockY() == breaker
                  .getLocation().getBlockY()) {
                  if (Utils.getDirection(breaker).equals(
                      "N") || 
                    Utils.getDirection(breaker)
                    .equals("S")) {
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() + 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() - 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY(), 
                        broken.getLocation()
                        .getBlockZ() + 1);
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY(), 
                        broken.getLocation()
                        .getBlockZ() - 1);
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() + 1, 
                        broken.getLocation()
                        .getBlockZ() + 1);
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() - 1, 
                        broken.getLocation()
                        .getBlockZ() - 1);
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() - 1, 
                        broken.getLocation()
                        .getBlockZ() + 1);
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() + 1, 
                        broken.getLocation()
                        .getBlockZ() - 1);
                  } else {
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() + 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() - 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), 
                        broken.getLocation()
                        .getBlockX() + 1, 
                        broken.getLocation()
                        .getBlockY(), 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), 
                        broken.getLocation()
                        .getBlockX() - 1, 
                        broken.getLocation()
                        .getBlockY(), 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), 
                        broken.getLocation()
                        .getBlockX() + 1, 
                        broken.getLocation()
                        .getBlockY() + 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), 
                        broken.getLocation()
                        .getBlockX() - 1, 
                        broken.getLocation()
                        .getBlockY() - 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), 
                        broken.getLocation()
                        .getBlockX() + 1, 
                        broken.getLocation()
                        .getBlockY() - 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), 
                        broken.getLocation()
                        .getBlockX() - 1, 
                        broken.getLocation()
                        .getBlockY() + 1, 
                        broken.getLocation()
                        .getBlockZ());
                  } 
                } else if (broken.getLocation().getBlockY() == breaker
                  .getLocation().getBlockY() + 1) {
                  if (Utils.getDirection(breaker).equals(
                      "N") || 
                    Utils.getDirection(breaker)
                    .equals("S")) {
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() + 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() - 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY(), 
                        broken.getLocation()
                        .getBlockZ() + 1);
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY(), 
                        broken.getLocation()
                        .getBlockZ() - 1);
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() + 1, 
                        broken.getLocation()
                        .getBlockZ() + 1);
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() - 1, 
                        broken.getLocation()
                        .getBlockZ() - 1);
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() - 1, 
                        broken.getLocation()
                        .getBlockZ() + 1);
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() + 1, 
                        broken.getLocation()
                        .getBlockZ() - 1);
                  } else {
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() + 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), broken
                        .getLocation().getBlockX(), 
                        broken.getLocation()
                        .getBlockY() - 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), 
                        broken.getLocation()
                        .getBlockX() + 1, 
                        broken.getLocation()
                        .getBlockY(), 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), 
                        broken.getLocation()
                        .getBlockX() - 1, 
                        broken.getLocation()
                        .getBlockY(), 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), 
                        broken.getLocation()
                        .getBlockX() + 1, 
                        broken.getLocation()
                        .getBlockY() + 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), 
                        broken.getLocation()
                        .getBlockX() - 1, 
                        broken.getLocation()
                        .getBlockY() - 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), 
                        broken.getLocation()
                        .getBlockX() + 1, 
                        broken.getLocation()
                        .getBlockY() - 1, 
                        broken.getLocation()
                        .getBlockZ());
                    Utils.breakRadius(breaker, broken
                        .getWorld(), 
                        broken.getLocation()
                        .getBlockX() - 1, 
                        broken.getLocation()
                        .getBlockY() + 1, 
                        broken.getLocation()
                        .getBlockZ());
                  } 
                } else {
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX() + 1, 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ());
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX() - 1, 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ());
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX(), 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ() + 1);
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX(), 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ() - 1);
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX() + 1, 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ() + 1);
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX() + 1, 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ() - 1);
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX() - 1, 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ() + 1);
                  Utils.breakRadius(breaker, broken
                      .getWorld(), broken
                      .getLocation().getBlockX() - 1, 
                      broken.getLocation()
                      .getBlockY(), broken
                      .getLocation()
                      .getBlockZ() - 1);
                } 
                Utils.breakRadius(breaker, broken
                    .getWorld(), broken.getLocation()
                    .getBlockX(), broken.getLocation()
                    .getBlockY(), broken.getLocation()
                    .getBlockZ());
              } 
            } 
          } 
        } 
      } 
    } 
  }
  
  @EventHandler
  public void commandPreprocess(PlayerCommandPreprocessEvent e) {
    String pluginRef;
    Pattern p = Pattern.compile("^/([a-zA-Z0-9_]+):");
    Matcher m = p.matcher(e.getMessage());
    if (m.find()) {
      pluginRef = m.group(1);
    } else {
      return;
    } 
    byte b;
    int i;
    Plugin[] arrayOfPlugin;
    for (i = (arrayOfPlugin = Bukkit.getServer().getPluginManager().getPlugins()).length, b = 0; b < i; ) {
      Plugin plugin = arrayOfPlugin[b];
      if (plugin.getName().toLowerCase().equals(pluginRef.toLowerCase()) || 
        pluginRef.toLowerCase().equals("bukkit") || 
        pluginRef.toLowerCase().equals("minecraft") || 
        pluginRef.toLowerCase().equals("spigot")) {
        if (e.getPlayer().hasPermission("vextras.bypassblocker"))
          return; 
        e.getPlayer().sendMessage(
            String.valueOf(this.prefix) + ChatColor.RED + " You can not do that!");
        e.setCancelled(true);
        break;
      } 
      b++;
    } 
    if (m.find())
      pluginRef = m.group(1); 
  }
}
