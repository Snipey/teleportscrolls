package dev.snipey.teleportscrolls.database;


import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

import dev.snipey.teleportscrolls.TeleportScrolls;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public abstract class Database {
  TeleportScrolls plugin;
  Connection connection;
  // The name of the table we created back in SQLite class.
  public String table = "waystones";

  public Database(TeleportScrolls instance){
    plugin = instance;
  }

  public abstract Connection getSQLConnection();

  public abstract void load();

  public void initialize(){
    connection = getSQLConnection();
    try{
      PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
      ps.setString(1, "test");
      ResultSet rs = ps.executeQuery();
      close(ps,rs);

    } catch (SQLException ex) {
      plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
    }
  }

  /*
   * Data:
   * Waystone Name - String
   * Player - UUID
   *
   */

  public ArrayList<String> getWaystoneInfo(Location loc) {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    int x = (int) loc.getX();
    int y = (int) loc.getY();
    int z = (int) loc.getZ();

    try {
      conn = getSQLConnection();
      ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE loc_x = '"+x+"' AND loc_y = '"+y+"' AND loc_z = '"+z+"';");

      rs = ps.executeQuery();
      ResultSetMetaData metadata = rs.getMetaData();
      int numberOfColumns = metadata.getColumnCount();
      ArrayList<String> arrayList = new ArrayList<>();
      while (rs.next()) {
        int i = 1;
        while(i <= numberOfColumns) {
          arrayList.add(rs.getString(i++));
        }
      }
      return arrayList;
    } catch (SQLException ex) {
      plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
    } finally {
      try {
        if (ps != null)
          ps.close();
        if (conn != null)
          conn.close();
      } catch (SQLException ex) {
        plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
      }
    }
    return null;
  }

  public boolean getWaystoneExist(Location loc) {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    int x = (int) loc.getX();
    int y = (int) loc.getY();
    int z = (int) loc.getZ();

    try {
      conn = getSQLConnection();
      ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE loc_x = '"+x+"' AND loc_y = '"+y+"' AND loc_z = '"+z+"';");

      rs = ps.executeQuery();

      while(rs.next()){
        if(rs != null){ // Tell database to search for the player you sent into the method. e.g getTokens(sam) It will look for sam.
          return true;
        }
      }
    } catch (SQLException ex) {
      plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
    } finally {
      try {
        if (ps != null)
          ps.close();
        if (conn != null)
          conn.close();
      } catch (SQLException ex) {
        plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
      }
    }
    return false;
  }

  // Now we need methods to save things to the database
  public void setWaystone(Player player, Location loc, String name) {
    Connection conn = null;
    PreparedStatement ps = null;
    try {
      conn = getSQLConnection();
      ps = conn.prepareStatement("REPLACE INTO " + table + " (name,player,world,loc_x,loc_y,loc_z) VALUES(?,?,?,?,?,?)");
      ps.setString(1, name);
      ps.setString(2, player.getUniqueId().toString());
      ps.setString(3, player.getWorld().getName());
      ps.setInt(4, (int) loc.getX());
      ps.setInt(5, (int) loc.getY());
      ps.setInt(6, (int) loc.getZ());
      ps.executeUpdate();
      return;
    } catch (SQLException ex) {
      plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
    } finally {
      try {
        if (ps != null)
          ps.close();
        if (conn != null)
          conn.close();
      } catch (SQLException ex) {
        plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
      }
    }
    return;
  }


  public void close(PreparedStatement ps,ResultSet rs){
    try {
      if (ps != null)
        ps.close();
      if (rs != null)
        rs.close();
    } catch (SQLException ex) {
      Error.close(plugin, ex);
    }
  }
}