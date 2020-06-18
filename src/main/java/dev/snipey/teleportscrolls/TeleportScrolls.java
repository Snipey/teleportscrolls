package dev.snipey.teleportscrolls;

import dev.snipey.teleportscrolls.commands.CommandManager;
import dev.snipey.teleportscrolls.database.Database;
import dev.snipey.teleportscrolls.database.SQLite;
import dev.snipey.teleportscrolls.events.InteractScroll;
import dev.snipey.teleportscrolls.managers.SignMenuFactory;
import dev.snipey.teleportscrolls.util.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;



public final class TeleportScrolls extends JavaPlugin implements Listener {

  public final NamespacedKey xKey = new NamespacedKey(this, "x");
  public final NamespacedKey yKey = new NamespacedKey(this, "y");
  public final NamespacedKey zKey = new NamespacedKey(this, "z");

  private SignMenuFactory signMenuFactory;

  private Database db;
  @Override
  public void onEnable() {
    this.signMenuFactory = new SignMenuFactory(this);
    this.db = new SQLite(this);
    this.db.load();
    getServer().getPluginManager().registerEvents(new InteractScroll(), this);
    getCommand("scroll").setExecutor(new CommandManager());
    ConfigManager fileManager = new ConfigManager(this);
    fileManager.getConfig("config.yml").copyDefaults(true).save();
    registerRecepies();
  }

  @Override
  public void onDisable() {

  }

  public Database getDatabase() {
    return this.db;
  }

  private void registerRecepies() {
    // Our custom variable which we will be changing around.
    ItemStack item = new ItemStack(Material.STICK);
    // The meta of the diamond sword where we can change the name, and properties of the item.
    ItemMeta meta = item.getItemMeta();
    // We will initialise the next variable after changing the properties of the sword
    // This sets the name of the item.
    meta.setDisplayName(ChatColor.GREEN + "Waystone Tool");
    // Set the meta of the sword to the edited meta.
    item.setItemMeta(meta);
    // create a NamespacedKey for your recipe
    NamespacedKey key = new NamespacedKey(this, "waystone_tool");
    // Create our custom recipe variable
    ShapedRecipe recipe = new ShapedRecipe(key, item);
    // Here we will set the places. E and S can represent anything, and the letters can be anything. Beware; this is case sensitive.
    recipe.shape("XEX", "ESE", " S ");
    // Set what the letters represent.
    // E = Emerald, S = Stick, X = MAGMA_CREAM
    recipe.setIngredient('X', Material.MAGMA_CREAM);
    recipe.setIngredient('E', Material.EMERALD);
    recipe.setIngredient('S', Material.STICK);
    // Finally, add the recipe to the bukkit recipes
    Bukkit.addRecipe(recipe);
  }

  public SignMenuFactory getSignMenuFactory() {
    return this.signMenuFactory;
  }
}
