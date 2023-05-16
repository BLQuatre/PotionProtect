package fr.bl4.potionprotect;

import fr.bl4.potionprotect.data.Database;
import fr.bl4.potionprotect.data.SQLite;
import fr.bl4.potionprotect.listeners.PotionInteractListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PotionProtect extends JavaPlugin {

    @Getter private static PotionProtect plugin;
    @Getter private static List<UUID> blacklistedUUID;
    @Getter private Database db;

    @Override
    public void onEnable() {
        plugin = this;


        db = new SQLite(this);
        db.load();

        blacklistedUUID = db.getPlayers();

        // Register listeners
        Bukkit.getPluginManager().registerEvents(new PotionInteractListener(), this);
    }
}