package fr.bl4.potionprotect;

import fr.bl4.potionprotect.data.Database;
import fr.bl4.potionprotect.data.SQLite;
import lombok.Getter;
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
        blacklistedUUID = new ArrayList<>();

        db = new SQLite(this);
        db.load();
    }
}