package dev.snipey.teleportscrolls.database;

import dev.snipey.teleportscrolls.TeleportScrolls;

import java.util.logging.Level;

public class Error {
  public static void execute(TeleportScrolls plugin, Exception ex){
    plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
  }
  public static void close(TeleportScrolls plugin, Exception ex){
    plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
  }
}