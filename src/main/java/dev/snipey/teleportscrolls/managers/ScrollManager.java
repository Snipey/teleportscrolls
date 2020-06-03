package dev.snipey.teleportscrolls.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ScrollManager {
  // 0 1 2 | 3 4 5 | 6 7 8
  // 9   11 | 12 14 | 15 17
  // 18 19 20 | 21 22 23 | 24 25 26

  public static final List<Integer> SCROLL_FORGE_BORDER_SLOT_1 = Collections.unmodifiableList(
      Arrays.asList(0, 1, 2,
          9, 11,
          18, 19, 20));
  public static final List<Integer> SCROLL_FORGE_BORDER_SLOT_2 = Collections.unmodifiableList(
      Arrays.asList(3, 4, 5,
          12, 14,
          21, 22, 23));
  public static final List<Integer> SCROLL_FORGE_BORDER_SLOT_3 = Collections.unmodifiableList(
      Arrays.asList(6, 7, 8,
          15, 17,
          24, 25, 26));

  public static Inventory createScrollForge() {
    Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLUE + "Scroll Forge");
    ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
    ItemMeta gmeta = glass.getItemMeta();
    gmeta.setDisplayName(" ");
    glass.setItemMeta(gmeta);

    ItemStack wool = new ItemStack(Material.RED_WOOL);
    ItemMeta wmeta = wool.getItemMeta();
    wmeta.setDisplayName(ChatColor.RED + "Craft Scroll");
    wool.setItemMeta(wmeta);

    for (Iterator<Integer> iterator = SCROLL_FORGE_BORDER_SLOT_1.iterator(); iterator.hasNext(); ) {
      int slot = iterator.next();
      inv.setItem(slot, glass);
    }
    for (Iterator<Integer> iterator = SCROLL_FORGE_BORDER_SLOT_2.iterator(); iterator.hasNext(); ) {
      int slot = iterator.next();
      inv.setItem(slot, glass);
    }
    for (Iterator<Integer> iterator = SCROLL_FORGE_BORDER_SLOT_3.iterator(); iterator.hasNext(); ) {
      int slot = iterator.next();
      inv.setItem(slot, glass);
    }
    inv.setItem(16, wool);
    return inv;
  }

  public void SetSlotStatus(int slot, Player p){
    InventoryView inv = p.getOpenInventory();
    switch (slot){
      case 10:
        p.sendMessage("dawg");
    }
  }

  public ItemStack createScroll(int charges) {
    ItemStack scroll = new ItemStack(Material.PAPER);
    ItemMeta meta = scroll.getItemMeta();
    ArrayList<String> lore = new ArrayList<String>();
    meta.setDisplayName(ChatColor.DARK_PURPLE + "Scroll of Teleportation");
    lore.add(ChatColor.YELLOW + "Charges" + ChatColor.YELLOW + "" + ": " + ChatColor.LIGHT_PURPLE + charges);
    meta.setLore(lore);
    scroll.setItemMeta(meta);
    return scroll;
  }

  public static boolean checkIfStatusRing(int slot){
    switch (slot){
      case 10:
      case 13:
      case 16:
        return false;
      default:
        return true;
    }
  }
  public static boolean checkIfCrafting(int slot){
    switch (slot){
      case 10:
      case 13:
        return true;
      default:
        return false;
    }
  }

  public static void changeBorder(Inventory inv, InventoryAction action, String type){

    switch (action){
      case PICKUP_ALL:
      case PICKUP_ONE:
      case PICKUP_SOME:
      case PICKUP_HALF:
        modifyBorder(inv, type);
      case NOTHING:
    }

  }
  public static void modifyBorder(Inventory inv, String type){
    ItemStack yellowGlass = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
    ItemStack greenGlass = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
    ItemStack redGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    if(type.matches("slot1")){
      for (Iterator<Integer> iterator = SCROLL_FORGE_BORDER_SLOT_1.iterator(); iterator.hasNext(); ) {
        int slot = iterator.next();
        inv.setItem(slot, greenGlass);
      }
    }
    if(type.matches("slot2")){
      for (Iterator<Integer> iterator = SCROLL_FORGE_BORDER_SLOT_2.iterator(); iterator.hasNext(); ) {
        int slot = iterator.next();
        inv.setItem(slot, yellowGlass);
      }
    }
  }
}
