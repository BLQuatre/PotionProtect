package fr.bl4.potionprotect.config;

import fr.bl4.potionprotect.PotionProtect;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final PotionProtect PLUGIN = PotionProtect.getPlugin();

    @Getter private static FileConfiguration config;

    /**
     * Set up config file and reload
     * @see #reload()
     */
    public static void setup() {
        PLUGIN.saveDefaultConfig();
        reload();
    }

    /**
     * Reload config from disk and load new values
     * @see #load()
     */
    public static void reload() {
        PLUGIN.reloadConfig();
        config = PLUGIN.getConfig();
        load();
    }

    // All messages
    public static String prefix;
    public static String reload;
    public static String noPermission;
    public static String unknownPlayer;
    public static String cantInteract;

    /**
     * Load new values
     */
    private static void load() {
        prefix = getFormattedString("prefix", "&8[&cPotionProtect&8]");
        reload = getFormattedString("messages.reload", "{PREFIX} &aPlugin reloaded !");

        noPermission = getFormattedString("messages.no_permission", "{PREFIX} &cYou don't have permission !");
        unknownPlayer = getFormattedString("messages.unknown_player", "{PREFIX} &cUnknown player !");
        cantInteract = getFormattedString("messages.cant_interact", "{PREFIX} &cYou can't interact with potions !");
    }

    /**
     * Get string from the config
     * @param path the path to the string
     * @param defaultValue necessary
     * @return the string with color format
     */
    protected static String getFormattedString(String path, String defaultValue) {
        try {
            return ChatColor.translateAlternateColorCodes('&', config.getString(path, defaultValue).replaceAll("\\{PREFIX}", prefix));
        } catch (Exception e) {
            Bukkit.getLogger().warning("Missing message in the file: (" + config.getName() + ") | Path: (" + path + ")");
            return ChatColor.translateAlternateColorCodes('&', defaultValue.replaceAll("\\{PREFIX}", prefix));
        }
    }

    /**
     * Get string list from the config
     * @param path the path to the string list
     * @return the string list with color format
     */
    protected static List<String> getFormattedStringList(String path) {
        List<String> list;
        try {
            list = config.getStringList(path);
            list.forEach(string -> ChatColor.translateAlternateColorCodes('&', string));
        } catch (Exception e) {
            Bukkit.getLogger().warning("Missing message in the file: (" + config.getName() + ") | Path: (" + path + ")");
            return new ArrayList<>();
        }
        return list;
    }
}