package fr.bl4.potionprotect.data;

import fr.bl4.potionprotect.PotionProtect;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;


public class SQLite extends Database {

    String dbName;

    public SQLite(PotionProtect plugin) {
        super(plugin);

        dbName = plugin.getConfig().getString("sql.database", "potions_blacklist");
    }

    @Override
    public Connection getSQLConnection() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File dataFile = new File(plugin.getDataFolder(), "data.db");
        if (!dataFile.exists()){
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: data.db");
            }
        }

        try {
            if (connection != null && !connection.isClosed()){
                return connection;
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFile);
            return connection;

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"[DATABASE] SQLite exception on initialize", ex);
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS " + dbName + " (`uuid` varchar(32) NOT NULL, `name` varchar(20), PRIMARY KEY (`uuid`));");

            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"[DATABASE] SQLite exception on load", ex);
        }
    }
}