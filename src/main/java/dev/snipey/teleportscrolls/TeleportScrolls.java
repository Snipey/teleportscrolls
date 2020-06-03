package dev.snipey.teleportscrolls;

import dev.snipey.teleportscrolls.commands.CommandManager;
import dev.snipey.teleportscrolls.events.InteractScroll;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;



public final class TeleportScrolls extends JavaPlugin implements Listener {

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(new InteractScroll(), this);
    getCommand("scroll").setExecutor(new CommandManager());
  }

  @Override
  public void onDisable() {

  }
}
