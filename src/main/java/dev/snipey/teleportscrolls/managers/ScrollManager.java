package dev.snipey.teleportscrolls.managers;

import dev.snipey.teleportscrolls.TeleportScrolls;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ScrollManager {
  private static final TeleportScrolls plugin = JavaPlugin.getPlugin(TeleportScrolls.class);

  public static Inventory createScrollForgeMenu() {
    Inventory inv = Bukkit.createInventory(null, 9, "Waystone Menu");
    ItemStack craft = new ItemStack(Material.ANVIL);
    ItemMeta meta = craft.getItemMeta();
    meta.setDisplayName(ChatColor.GREEN + "Craft Blueprint");

    ItemStack wool = new ItemStack(Material.RED_WOOL);
    ItemMeta woolmeta = wool.getItemMeta();
    woolmeta.setDisplayName(ChatColor.RED + "Delete Waystone");
    wool.setItemMeta(woolmeta);


    craft.setItemMeta(meta);
    inv.setItem(0, craft); // Craft Blueprint
    inv.setItem(4, new ItemStack(wool)); // Delete Waystone
    inv.setItem(8, new ItemStack(Material.PAPER)); // Waystone Info
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

  public static Merchant createScrollForge(Location loc) {
    // create the actual merchant
    Merchant merchant = Bukkit.createMerchant(ChatColor.LIGHT_PURPLE + "Scroll Forge");
    // Create a list of recipes for this merchant
    List<MerchantRecipe> recipes = new ArrayList<>();
    recipes.add(BlueprintScroll(loc));
    // TODO Check what upgrade level the waystone is
    recipes.add(TierOne(loc));
    recipes.add(TierTwo(loc));
    recipes.add(TierThree(loc));
    // Add this list of recipes to our merchant
    merchant.setRecipes(recipes);
    // Open this merchant inventory to the player
    return merchant;
  }

  private static MerchantRecipe TierOne(Location loc) {
    ItemStack scroll = createScroll(500, loc);

    ItemStack blueprint = BlueprintScrollItem(null);

    MerchantRecipe recipe = new MerchantRecipe(scroll, 10);
    recipe.addIngredient(blueprint);
    recipe.addIngredient(new ItemStack(Material.GOLD_BLOCK, 2));
    return recipe;
  }

  private static MerchantRecipe TierTwo(Location loc) {
    ItemStack scroll = createScroll(1000, loc);

    ItemStack blueprint = BlueprintScrollItem(null);

    MerchantRecipe recipe = new MerchantRecipe(scroll, 10);
    recipe.addIngredient(blueprint);
    recipe.addIngredient(new ItemStack(Material.EMERALD_BLOCK, 3));
    return recipe;
  }

  private static MerchantRecipe TierThree(Location loc) {
    ItemStack scroll = createScroll(1500, loc);

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

  public static ItemStack createScroll(int charges, Location loc) {
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
    lore.add(ChatColor.GRAY + "Waystone Name");
    lore.add("");
    lore.add(ChatColor.YELLOW + "Charges" + ChatColor.YELLOW + "" + ": " + ChatColor.LIGHT_PURPLE + charges);
    meta.setLore(lore);
    scroll.setItemMeta(meta);
    return scroll;
  }

}
