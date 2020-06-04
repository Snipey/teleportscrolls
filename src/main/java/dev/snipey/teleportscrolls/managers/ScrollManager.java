package dev.snipey.teleportscrolls.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ScrollManager {

  public static Inventory createScrollForgeMenu() {
    Inventory inv = Bukkit.createInventory(null, 9, "Waystone Menu");
    ItemStack craft = new ItemStack(Material.ANVIL);
    inv.setItem(0, null);
    inv.setItem(0, null);
    inv.setItem(0, null);
    inv.setItem(0, craft); // Craft Blueprint
    inv.setItem(0, null); // Delete Waystone
    inv.setItem(0, null);
    inv.setItem(0, null); // Waystone Info
    inv.setItem(0, null);
    return inv;
  }

  public static Merchant createScrollForge() {
    // create the actual merchant
    Merchant merchant = Bukkit.createMerchant(ChatColor.LIGHT_PURPLE + "Scroll Forge");
    // Create a list of recipes for this merchant
    List<MerchantRecipe> recipes = new ArrayList<>();
    recipes.add(BlueprintScroll());
    // TODO Check what upgrade level the waystone is
    recipes.add(TierOne());
    recipes.add(TierTwo());
    recipes.add(TierThree());
    // Add this list of recipes to our merchant
    merchant.setRecipes(recipes);
    // Open this merchant inventory to the player
    return merchant;
  }

  private static MerchantRecipe TierOne() {
    ItemStack scroll = createScroll(500);

    ItemStack blueprint = BlueprintScrollItem();

    MerchantRecipe recipe = new MerchantRecipe(scroll, 10);
    recipe.addIngredient(blueprint);
    recipe.addIngredient(new ItemStack(Material.GOLD_BLOCK, 2));
    return recipe;
  }

  private static MerchantRecipe TierTwo() {
    ItemStack scroll = createScroll(1000);

    ItemStack blueprint = BlueprintScrollItem();

    MerchantRecipe recipe = new MerchantRecipe(scroll, 10);
    recipe.addIngredient(blueprint);
    recipe.addIngredient(new ItemStack(Material.EMERALD_BLOCK, 3));
    return recipe;
  }

  private static MerchantRecipe TierThree() {
    ItemStack scroll = createScroll(1500);

    ItemStack blueprint = BlueprintScrollItem();

    MerchantRecipe recipe = new MerchantRecipe(scroll, 10);
    recipe.addIngredient(blueprint);
    recipe.addIngredient(new ItemStack(Material.DIAMOND_BLOCK, 3));
    return recipe;
  }

  private static MerchantRecipe BlueprintScroll() {
    ItemStack blueprint = BlueprintScrollItem();

    MerchantRecipe recipe = new MerchantRecipe(blueprint, 10);
    recipe.addIngredient(new ItemStack(Material.PAPER));
    recipe.addIngredient(new ItemStack(Material.MAGMA_CREAM, 5));
    return recipe;
  }

  private static ItemStack BlueprintScrollItem(){
    ItemStack blueprint = new ItemStack(Material.PAPER);
    ItemMeta bluemeta = blueprint.getItemMeta();
    bluemeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Scroll Blueprint");
    blueprint.setItemMeta(bluemeta);
    return blueprint;
  }

  public static ItemStack createScroll(int charges) {
    ItemStack scroll = new ItemStack(Material.PAPER);
    ItemMeta meta = scroll.getItemMeta();
    ArrayList<String> lore = new ArrayList<String>();
    meta.setDisplayName(ChatColor.DARK_PURPLE + "Scroll of Teleportation");
    lore.add(ChatColor.YELLOW + "Charges" + ChatColor.YELLOW + "" + ": " + ChatColor.LIGHT_PURPLE + charges);
    meta.setLore(lore);
    scroll.setItemMeta(meta);
    return scroll;
  }
}
