package dev.snipey.teleportscrolls.managers;

import dev.snipey.teleportscrolls.TeleportScrolls;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ScrollManager {
  private static final TeleportScrolls plugin = JavaPlugin.getPlugin(TeleportScrolls.class);

  public static Inventory createScrollForgeMenu(ArrayList<String> info, Location loc) {
    Inventory inv = Bukkit.createInventory(null, 9, "Waystone Menu");
    ItemStack craft = new ItemStack(Material.ANVIL);
    ItemMeta meta = craft.getItemMeta();
    meta.setDisplayName(ChatColor.GREEN + "Craft Blueprint");
    PersistentDataContainer data = meta.getPersistentDataContainer();
    if(!data.has(plugin.xKey, PersistentDataType.INTEGER)){
      data.set(plugin.xKey, PersistentDataType.INTEGER, loc.getBlockX());
      data.set(plugin.yKey, PersistentDataType.INTEGER, loc.getBlockY());
      data.set(plugin.zKey, PersistentDataType.INTEGER, loc.getBlockZ());
    }
    craft.setItemMeta(meta);

    ItemStack wool = new ItemStack(Material.RED_WOOL);
    ItemMeta woolmeta = wool.getItemMeta();
    woolmeta.setDisplayName(ChatColor.RED + "Delete Waystone");
    wool.setItemMeta(woolmeta);

    ItemStack paper = new ItemStack(Material.PAPER);
    ItemMeta papermeta = paper.getItemMeta();
    UUID puid = UUID.fromString(info.get(1));
    ArrayList<String> lore = new ArrayList<String>();
    lore.add(ChatColor.GRAY + info.get(0));
    lore.add(ChatColor.BLUE + "Owner: " + Bukkit.getOfflinePlayer(puid).getName());
    papermeta.setLore(lore);
    papermeta.setDisplayName(ChatColor.YELLOW + "Waystone Info");
    paper.setItemMeta(papermeta);


    inv.setItem(0, craft); // Craft Blueprint
    inv.setItem(4, new ItemStack(wool)); // Delete Waystone
    inv.setItem(8, new ItemStack(paper)); // Waystone Info
    return inv;
  }

  public static Inventory scrollForgeMenuConfirmation(Location loc) {
    Inventory inv = Bukkit.createInventory(null, 9, "Delete Waystone?");

    ItemStack confirm = new ItemStack(Material.GREEN_WOOL);
    ItemMeta cmeta = confirm.getItemMeta();
    cmeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Confirm");
    confirm.setItemMeta(cmeta);

    ItemStack deny = new ItemStack(Material.RED_WOOL);
    ItemMeta dmeta = confirm.getItemMeta();
    dmeta.setDisplayName(ChatColor.RED + "" + ChatColor.UNDERLINE + "Cancel");
    deny.setItemMeta(dmeta);

    inv.setItem(2, new ItemStack(confirm)); // Confirm
    inv.setItem(6, new ItemStack(deny));   // Cancel
    return inv;
  }

  public static Merchant createScrollForge(String name, Location loc) {
    // create the actual merchant
    Merchant merchant = Bukkit.createMerchant(ChatColor.LIGHT_PURPLE + "Scroll Forge");
    // Create a list of recipes for this merchant
    List<MerchantRecipe> recipes = new ArrayList<>();
    recipes.add(BlueprintScroll(loc));
    // TODO Check what upgrade level the waystone is
    recipes.add(TierOne(name, loc));
    recipes.add(TierTwo(name, loc));
    recipes.add(TierThree(name, loc));
    // Add this list of recipes to our merchant
    merchant.setRecipes(recipes);
    // Open this merchant inventory to the player
    return merchant;
  }

  private static MerchantRecipe TierOne(String name, Location loc) {
    ItemStack scroll = createScroll(name, 500, loc);

    ItemStack blueprint = BlueprintScrollItem(null);

    MerchantRecipe recipe = new MerchantRecipe(scroll, 10);
    recipe.addIngredient(blueprint);
    recipe.addIngredient(new ItemStack(Material.GOLD_BLOCK, 2));
    return recipe;
  }

  private static MerchantRecipe TierTwo(String name, Location loc) {
    ItemStack scroll = createScroll(name, 1000, loc);

    ItemStack blueprint = BlueprintScrollItem(null);

    MerchantRecipe recipe = new MerchantRecipe(scroll, 10);
    recipe.addIngredient(blueprint);
    recipe.addIngredient(new ItemStack(Material.EMERALD_BLOCK, 3));
    return recipe;
  }

  private static MerchantRecipe TierThree(String name, Location loc) {
    ItemStack scroll = createScroll(name, 1500, loc);

    ItemStack blueprint = BlueprintScrollItem(null);

    MerchantRecipe recipe = new MerchantRecipe(scroll, 10);
    recipe.addIngredient(blueprint);
    recipe.addIngredient(new ItemStack(Material.DIAMOND_BLOCK, 3));
    return recipe;
  }

  private static MerchantRecipe BlueprintScroll(Location loc) {
    ItemStack blueprint = BlueprintScrollItem(loc);

    MerchantRecipe recipe = new MerchantRecipe(blueprint, 10);
    recipe.addIngredient(new ItemStack(Material.PAPER));
    recipe.addIngredient(new ItemStack(Material.MAGMA_CREAM, 5));
    return recipe;
  }

  private static ItemStack BlueprintScrollItem(Location loc) {
    ItemStack blueprint = new ItemStack(Material.PAPER);

    ItemMeta meta = blueprint.getItemMeta();
    meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Scroll Blueprint");
    if (loc != null) {
      ArrayList<String> lore = new ArrayList<String>();
      meta.setLore(lore);
    }
    blueprint.setItemMeta(meta);
    return blueprint;
  }

  public static ItemStack createScroll(String name, int charges, Location loc) {
    ItemStack scroll = new ItemStack(Material.PAPER);
    ItemMeta meta = scroll.getItemMeta();

    PersistentDataContainer data = meta.getPersistentDataContainer();
    if(!data.has(plugin.xKey, PersistentDataType.INTEGER)){
      data.set(plugin.xKey, PersistentDataType.INTEGER, loc.getBlockX());
      data.set(plugin.yKey, PersistentDataType.INTEGER, loc.getBlockY());
      data.set(plugin.zKey, PersistentDataType.INTEGER, loc.getBlockZ());
    }

    ArrayList<String> lore = new ArrayList<String>();
    meta.setDisplayName(ChatColor.DARK_PURPLE + "Scroll of Teleportation");
    lore.add(ChatColor.GRAY + name);
    lore.add("");
    lore.add(ChatColor.YELLOW + "Charges" + ChatColor.YELLOW + "" + ": " + ChatColor.LIGHT_PURPLE + charges);
    meta.setLore(lore);
    scroll.setItemMeta(meta);
    return scroll;
  }

  public static void openForgeMenu(Player p, Location loc) {
    ArrayList<String> info = plugin.getDatabase().getWaystoneInfo(loc);
    Inventory forge = createScrollForgeMenu(info, loc);
    p.openInventory(forge);
  }

  public static boolean checkWaystoneExists(Block start, int radius){
    for(double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++){
      for(double y = start.getLocation().getY() - radius; y <= start.getLocation().getY() + radius; y++){
        for(double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++){
          Location loc = new Location(start.getWorld(), x, y, z);
          if(plugin.getDatabase().getWaystoneExist(loc)){
            return true;
          }
        }
      }
    }
    return false;
  }
}
