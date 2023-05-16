package fr.bl4.potionprotect.data;

import fr.bl4.potionprotect.PotionProtect;
import fr.bl4.potionprotect.config.Config;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Database is required to keep blacklisted players even after a restart.
 */
public abstract class Database {

    PotionProtect plugin;
    Connection connection;

    // Table's name on sql
    private static String table;

    public Database(PotionProtect plugin){
        this.plugin = plugin;

        table = Config.getConfig().getString("sql.table", "potion_protect_uuid");
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    /**
     * Get all players in the blacklist
     * @return the list of blacklisted players (UUID)
     */
    public List<UUID> getPlayers() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;

        List<UUID> players = new ArrayList<>();

        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table);

            rs = ps.executeQuery();
            while (rs.next()) {
                players.add(UUID.fromString(rs.getString("uuid")));
            }

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "[DATABASE] Couldn't execute MySQL statement: ", ex);

        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();

            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "[DATABASE] Failed to close MySQL connection: ", ex);
            }
        }
        return players;
    }


    /**
     * Add a player to the database
     * @param player player to add
     */
    public void addPlayer(Player player) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getSQLConnection();

            ps = conn.prepareStatement("INSERT INTO " + table + "(uuid, name) VALUES (?,?);");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, player.getName());

            ps.executeUpdate();

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "[DATABASE] Couldn't execute MySQL statement: ", ex);

        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();

            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "[DATABASE] Failed to close MySQL connection: ", ex);
            }
        }
    }

    /**
     * Remove a player from the database
     * @param player player to remove
     */
    public void removePlayer(Player player) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getSQLConnection();

            ps = conn.prepareStatement("DELETE FROM " + table + " WHERE uuid = ?;");
            ps.setString(1, player.getUniqueId().toString());

            ps.executeUpdate();

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);

        } finally {

            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();

            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
            }
        }
    }
}