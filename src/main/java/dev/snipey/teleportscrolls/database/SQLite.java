package dev.snipey.teleportscrolls.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import dev.snipey.teleportscrolls.database.Database; // import the database class.
import dev.snipey.teleportscrolls.TeleportScrolls; // import your main class


public class SQLite extends Database {
  String dbname;
  public SQLite(TeleportScrolls instance){
    super(instance);
    dbname = plugin.getConfig().getString("SQLite.Filename", "waystones"); // Set the table name here e.g player_kills
  }

  public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS waystones (" + // make sure to put your table name in here too.
      "`player` varchar(32) NOT NULL," + // This creates the different colums you will save data too. varchar(32) Is a string, int = integer
      "`name` int(11) NOT NULL," +
      "`loc_x` int(11) NOT NULL," +
      "`loc_y` int(11) NOT NULL," +
      "`loc_z` int(11) NOT NULL," +
      "PRIMARY KEY (`name`)" +  // This is creating 3 colums Player, Kills, Total. Primary key is what you are going to use as your indexer. Here we want to use player so
      ");"; // we can search by player, and get kills and total. If you some how were searching kills it would provide total and player.


  // SQL creation stuff, You can leave the blow stuff untouched.
  public Connection getSQLConnection() {
    File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
    if (!dataFolder.exists()){
      try {
        dataFolder.createNewFile();
      } catch (IOException e) {
        plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
      }
    }
    try {
      if(connection!=null&&!connection.isClosed()){
        return connection;
      }
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
      return connection;
    } catch (SQLException ex) {
      plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
    } catch (ClassNotFoundException ex) {
      plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
    }
    return null;
  }

  public void load() {
    connection = getSQLConnection();
    try {
      Statement s = connection.createStatement();
      s.executeUpdate(SQLiteCreateTokensTable);
      s.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    initialize();
  }
}