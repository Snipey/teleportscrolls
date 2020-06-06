package dev.snipey.teleportscrolls.events;

import dev.snipey.teleportscrolls.TeleportScrolls;
import dev.snipey.teleportscrolls.managers.ScrollManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


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
    Inventory inv = ScrollManager.createScrollForgeMenu();
    if (a == Action.RIGHT_CLICK_BLOCK && hand.getType() == Material.PAPER) {
      if (!slot.name().equals("OFF_HAND") && block.getType() == Material.EMERALD_BLOCK) {
        p.openInventory(inv);
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
                p.teleport(loc);
                Firework firework = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
                FireworkMeta meta = firework.getFireworkMeta();

                meta.setPower(1);
                meta.addEffect(fx);
                firework.setFireworkMeta(meta);
                meta.setPower(0);
                firework.detonate();
                tasks.remove(p.getUniqueId());
              }
            }
          }.runTaskLater(plugin, 100L));
          p.sendMessage("Teleporting in 5 seconds");

        } else {
          p.sendMessage("No data detected");
        }
      }
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e) {
    Player p = e.getPlayer();

    if ((e.getTo().getX() != e.getFrom().getX()) || (e.getTo().getY() != e.getFrom().getY()) || (e.getTo().getZ() != e.getFrom().getZ())) {
      BukkitTask task = this.tasks.get(p.getUniqueId());
      if(task != null) {
        task.cancel();
        this.tasks.remove(p.getUniqueId());
        p.sendMessage(ChatColor.RED + "Teleport Canceled");
      }

    }
  }

  @EventHandler
  public static void onInventoryClick(InventoryClickEvent e) {
    InventoryView view = e.getView();
    Inventory inv = e.getInventory();
    InventoryAction a = e.getAction();
    HumanEntity ent = e.getWhoClicked();
    ItemStack clicked = e.getCurrentItem();
    Player player = (Player) ent;
    int rawSlot = e.getRawSlot();
    Location location = player.getLocation();
    Merchant merch = ScrollManager.createScrollForge(location);
    if (view.getTitle().equals("Waystone Menu") && clicked != null && e.getRawSlot() <= 8) {
      e.setCancelled(true);
      switch (clicked.getType()) {
        case ANVIL:
          player.openMerchant(merch, false);
        case RED_WOOL:
          player.openMerchant(merch, false);
        case PAPER:
          player.openMerchant(merch, false);
      }
    }
  }
}
