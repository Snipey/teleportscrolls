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
    Merchant merch = ScrollManager.createScrollForge();
    if (a == Action.RIGHT_CLICK_BLOCK && hand.getType() == Material.PAPER) {
      if (slot.name() != "OFF_HAND" && block.getType() == Material.EMERALD_BLOCK) {
        p.openMerchant(merch, false);
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

  }
}
