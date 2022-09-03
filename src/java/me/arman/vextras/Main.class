package me.arman.vextras;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import me.arman.vextras.commands.ChatColors;
import me.arman.vextras.commands.CustomEnchant;
import me.arman.vextras.commands.Disenchant;
import me.arman.vextras.commands.FixAll;
import me.arman.vextras.commands.IPLookup;
import me.arman.vextras.commands.NameColors;
import me.arman.vextras.commands.Near;
import me.arman.vextras.commands.NightVision;
import me.arman.vextras.commands.Ping;
import me.arman.vextras.commands.Prefix;
import me.arman.vextras.commands.TNT;
import me.arman.vextras.commands.Unname;
import me.arman.vextras.events.GeneralEvents;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  public PluginManager pm;
  
  public static Economy economy = null;
  
  public static Permission perms = null;
  
  public PluginDescriptionFile pdffile;
  
  public static String prefix = ChatColor.DARK_GRAY + "[" + 
    ChatColor.DARK_RED + "vExtras" + ChatColor.DARK_GRAY + "]" + 
    ChatColor.RESET;
  
  public void onEnable() {
    this.pm = Bukkit.getPluginManager();
    this.pdffile = getDescription();
    vaultCheck();
    setupEconomy();
    setupPermissions();
    registerCommands();
    registerEvents();
    setTabColors();
  }
  
  private void registerCommands() {
    getCommand("NightVision").setExecutor((CommandExecutor)new NightVision(this));
    getCommand("Unname").setExecutor((CommandExecutor)new Unname(this));
    getCommand("Disenchant").setExecutor((CommandExecutor)new Disenchant(this));
    getCommand("NameColor").setExecutor((CommandExecutor)new NameColors(this));
    getCommand("Color").setExecutor((CommandExecutor)new ChatColors(this));
    getCommand("TNT").setExecutor((CommandExecutor)new TNT(this));
    getCommand("Ping").setExecutor((CommandExecutor)new Ping(this));
    getCommand("VNear").setExecutor((CommandExecutor)new Near(this));
    getCommand("FixAll").setExecutor((CommandExecutor)new FixAll(this));
    getCommand("Prefix").setExecutor((CommandExecutor)new Prefix(this));
    getCommand("IPLookup").setExecutor((CommandExecutor)new IPLookup(this));
    getCommand("CustomEnchant").setExecutor((CommandExecutor)new CustomEnchant(this));
  }
  
  private void registerEvents() {
    this.pm.registerEvents((Listener)new GeneralEvents(this), (Plugin)this);
  }
  
  private void vaultCheck() {
    if (Bukkit.getPluginManager().getPlugin("Vault") == null || 
      !Bukkit.getPluginManager().getPlugin("Vault").isEnabled()) {
      getLogger()
        .info(Level.SEVERE + 
          "Warning: No Vault dependency found, plugin will not work!");
      getServer().getPluginManager().disablePlugin((Plugin)this);
      return;
    } 
  }
  
  private void setTabColors() {
    for (Player p : getServer().getOnlinePlayers()) {
      ChatColor color;
      File userdata = new File(getDataFolder(), String.valueOf(File.separator) + "users");
      File f = new File(userdata, String.valueOf(File.separator) + 
          p.getUniqueId().toString() + ".yml");
      createFileIfNotFound(f);
      YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
      if (f.exists() && yamlConfiguration.getString("NameColor") != null) {
        color = ChatColor.valueOf(yamlConfiguration.getString("NameColor"));
      } else {
        color = ChatColor.RESET;
      } 
      p.setPlayerListName(color + p.getName());
    } 
  }
  
  public void createFileIfNotFound(File f) {
    if (!f.exists())
      try {
        f.createNewFile();
      } catch (IOException iOException) {} 
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (cmd.getName().equalsIgnoreCase("vextras"))
      sender.sendMessage(String.valueOf(prefix) + ChatColor.GREEN + " Version " + 
          this.pdffile.getVersion() + " by Arman"); 
    return false;
  }
  
  private boolean setupEconomy() {
    RegisteredServiceProvider<Economy> economyProvider = getServer()
      .getServicesManager().getRegistration(Economy.class);
    if (economyProvider != null)
      economy = (Economy)economyProvider.getProvider(); 
    return (economy != null);
  }
  
  private boolean setupPermissions() {
    RegisteredServiceProvider<Permission> rsp = getServer()
      .getServicesManager().getRegistration(Permission.class);
    perms = (Permission)rsp.getProvider();
    return (perms != null);
  }
}
