package dev.snipey.teleportscrolls.events;

import dev.snipey.teleportscrolls.managers.ScrollManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;

public class InteractScroll implements Listener {
  @EventHandler
  public void onWaystoneRightClick(PlayerInteractEvent e) {
    Block block = e.getClickedBlock();
    Player p = e.getPlayer();
    Action a = e.getAction();
    EquipmentSlot slot = e.getHand();
    ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();
    if (a == Action.RIGHT_CLICK_BLOCK && hand.getType() == Material.PAPER) {
      if (slot.name() != "OFF_HAND" && block.getType() == Material.EMERALD_BLOCK) {
        p.openInventory(ScrollManager.createScrollForge());
      }
    }
  }

  @EventHandler
  public static void onInventoryClick(InventoryClickEvent e) {
    InventoryView view = e.getView();
    Inventory inv = e.getInventory();
    InventoryAction a = e.getAction();
    HumanEntity ent = e.getWhoClicked();
    Player player = (Player) ent;
    int rawSlot = e.getRawSlot();
    if(rawSlot <= 27){
      if(ScrollManager.checkIfStatusRing(rawSlot)){
        e.setCancelled(true);
      } else {
        if(ScrollManager.checkIfCrafting(rawSlot)){
          player.sendMessage("" + a);
          switch (rawSlot){
            case 10:
              ScrollManager.changeBorder(inv, a, "slot1");
              break;
            case 13:
              ScrollManager.changeBorder(inv, a, "slot2");
              break;
          }
          // Update crafting slot to color
        } else {
          // Change wool if ready to craft scroll
          // Craft item and give to player if open inventory slot
          // Drop on ground if inventory full
          e.setCancelled(true);
        }
      }
    }
  }
}
