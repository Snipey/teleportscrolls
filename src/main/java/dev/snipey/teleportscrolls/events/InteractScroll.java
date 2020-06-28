package dev.snipey.teleportscrolls.events;

import com.google.common.collect.Lists;
import dev.snipey.teleportscrolls.TeleportScrolls;
import dev.snipey.teleportscrolls.managers.ScrollManager;
import dev.snipey.teleportscrolls.managers.Structure;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;


public class InteractScroll implements Listener {

  // Teleport timer
  public Map<UUID, BukkitTask> tasks = new HashMap<>();
  private static final TeleportScrolls plugin = JavaPlugin.getPlugin(TeleportScrolls.class);
  private final FireworkEffect fx = FireworkEffect.builder().withColor(Color.WHITE).with(FireworkEffect.Type.BALL).build();

  @EventHandler
  public void onScrollRightClick(PlayerInteractEvent e) {
    Block block = e.getClickedBlock();
    Player p = e.getPlayer();
    Action a = e.getAction();
    EquipmentSlot slot = e.getHand();
    ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();
    boolean isQuarryStructure = false;
    if (block != null) {
      isQuarryStructure = Structure.WAYSTONE.test(block);
    }

    if (a == Action.RIGHT_CLICK_BLOCK && hand.getItemMeta().getDisplayName().contains("Waystone Tool") && isQuarryStructure && p.isSneaking()) {
      if (slot.name().equals("OFF_HAND")) return;
      if (!plugin.getDatabase().getWaystoneExist(block.getLocation())) {
        plugin.getSignMenuFactory().newMenu(Lists.newArrayList("", "&4^^^^^^^^", "&3Waystone", "&3Name"))
            .reopenIfFail()
            .response((player, lines) -> {
              if (lines != null) {
                if (!lines[0].equals("")) {
                  plugin.getDatabase().setWaystone(p, block.getLocation(), lines[0]);
                } else {
                  p.sendMessage(ChatColor.RED + "You need to type a unique name");
                }
                return true;
              }
              return false;
            })
            .open(p);
      } else {
        p.sendMessage("Waystone Exists already");
      }
    }
    if (a == Action.RIGHT_CLICK_BLOCK && hand.getType() == Material.PAPER && isQuarryStructure) {
      if (slot.name().equals("OFF_HAND")) return;
      if (plugin.getDatabase().getWaystoneExist(block.getLocation())) {
        switch (block.getType()) {
          case EMERALD_BLOCK:
            ScrollManager.openForgeMenu(p, block.getLocation());
        }
      }
    } else if (a == Action.RIGHT_CLICK_AIR && hand.getType() == Material.PAPER) {
      if (!slot.name().equals("OFF_HAND")) {
        ItemMeta meta = hand.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (data.has(plugin.xKey, PersistentDataType.INTEGER)) {
          int x = data.get(plugin.xKey, PersistentDataType.INTEGER);
          int y = data.get(plugin.yKey, PersistentDataType.INTEGER);
          int z = data.get(plugin.zKey, PersistentDataType.INTEGER);
//          p.sendMessage("X: " + x + " Y: " + y + " Z: " + z);
          if (tasks.containsKey(p.getUniqueId())) return;
          tasks.put(p.getUniqueId(), new BukkitRunnable() {
            @Override
            public void run() {
              if (tasks.containsKey(p.getUniqueId())) {
                Location loc = new Location(p.getWorld(), x, y, z);
                Location eloc = new Location(p.getWorld(), x, y + 2, z);
                p.teleport(loc);
                Firework firework = (Firework) p.getWorld().spawnEntity(eloc, EntityType.FIREWORK);
                FireworkMeta meta = firework.getFireworkMeta();
                meta.addEffect(fx);
                firework.setFireworkMeta(meta);
                meta.setPower(0);
                firework.detonate();
                tasks.remove(p.getUniqueId());
              }
            }
          }.runTaskLater(plugin, 100L));
          p.sendMessage(ChatColor.GREEN + "Teleporting in 5 seconds...");
          p.sendMessage(ChatColor.YELLOW + "Dont Move!");
        } else {
          p.sendMessage("No data detected");
        }
      }
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e) {
    Player p = e.getPlayer();
    if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
      BukkitTask task = this.tasks.get(p.getUniqueId());
      if (task != null) {
        task.cancel();
        this.tasks.remove(p.getUniqueId());
        p.sendMessage(ChatColor.RED + "Teleport Canceled");
      }
    }
  }

  @EventHandler
  public static void onInventoryClick(InventoryClickEvent e) {
    InventoryView view = e.getView();
    HumanEntity ent = e.getWhoClicked();
    ItemStack clicked = e.getCurrentItem();
    Player player = (Player) ent;
    Location location = player.getLocation();
    PersistentDataContainer data = clicked.getItemMeta().getPersistentDataContainer();
    int x = 0, y = 0, z = 0;
    if (data.has(plugin.xKey, PersistentDataType.INTEGER)) {
      x = data.get(plugin.xKey, PersistentDataType.INTEGER);
      y = data.get(plugin.yKey, PersistentDataType.INTEGER);
      z = data.get(plugin.zKey, PersistentDataType.INTEGER);

    }
    Location loc = new Location(e.getWhoClicked().getWorld(), x, y, z);
    ArrayList<String> info = plugin.getDatabase().getWaystoneInfo(loc);
    Merchant merch = ScrollManager.createScrollForge(info.get(0), location);
    if (view.getTitle().equals("Waystone Menu") && clicked != null && e.getRawSlot() <= 8) {
      e.setCancelled(true);
      switch (clicked.getType()) {
        case ANVIL:
          player.openMerchant(merch, false);
          break;
        case RED_WOOL:
          player.openInventory(ScrollManager.scrollForgeMenuConfirmation(null));
          break;
        case PAPER:
          break;
      }
    }
    if (view.getTitle().equals("Delete Waystone?") && clicked != null && e.getRawSlot() <= 8) {
      e.setCancelled(true);
      if (ScrollManager.checkWaystoneExists(location.getBlock(), 3)){
        switch (clicked.getType()){
          case RED_WOOL:
            view.close();
            break;
          case GREEN_WOOL:
            player.sendMessage(ChatColor.GREEN + "Waystone Deleted!");
            view.close();
            break;
        }
      }
    }
  }

  @EventHandler
  public static void onStructureBreak(BlockBreakEvent e) {
    Block block = e.getBlock();
    Player p = e.getPlayer();
    boolean isStructure = Structure.WAYSTONE.test(block);
    if (isStructure && ScrollManager.checkWaystoneExists(block, 3)) {
      e.setCancelled(true);
    }
  }


}
