package fr.bl4.potionprotect.listeners;

import fr.bl4.potionprotect.PotionProtect;
import fr.bl4.potionprotect.config.Config;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class PotionInteractListener implements Listener {

    private final List<UUID> blacklistedUUID = PotionProtect.getBlacklistedUUID();

    /**
     * Use to cancel the event when player is in the blacklist and use a potion
     */
    @EventHandler
    public void onPotionInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        // Check if the player has interacted with an item (if he doesn't : Do nothing)
        if (item == null) return;

        // Check if player's item is not a potion or splash potion or lingering potion (if it's not : Do nothing)
        if (item.getType() != Material.POTION && item.getType() != Material.SPLASH_POTION && item.getType() != Material.LINGERING_POTION)
            return;

        // Check if player is not in the blacklist (if he's not in : Do nothing)
        if (!blacklistedUUID.contains(event.getPlayer().getUniqueId())) return;
        
        // Cancel the event and send a message to the player
        event.setCancelled(true);
        event.getPlayer().sendMessage(Config.cantInteract);
    }
}