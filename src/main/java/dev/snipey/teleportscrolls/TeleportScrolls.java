package dev.snipey.teleportscrolls;

import dev.snipey.teleportscrolls.commands.CommandManager;
import dev.snipey.teleportscrolls.events.InteractScroll;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;



public final class TeleportScrolls extends JavaPlugin implements Listener {

  public final NamespacedKey xKey = new NamespacedKey(this, "x");
  public final NamespacedKey yKey = new NamespacedKey(this, "y");
  public final NamespacedKey zKey = new NamespacedKey(this, "z");

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(new InteractScroll(), this);
    getCommand("scroll").setExecutor(new CommandManager());
  }

  @Override
  public void onDisable() {

  }
}
