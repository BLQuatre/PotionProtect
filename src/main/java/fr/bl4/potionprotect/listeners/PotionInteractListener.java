package fr.bl4.potionprotect.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PotionInteractListener implements Listener {

    /**
     * Use to cancel the event when player is in the blacklist and use a potion
     */
    @EventHandler
    public void onPotionInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        // Check if the player has interacted with an item (if he doesn't : return)
        if (item == null) return;

        // Check if player's item is not a potion or splash potion or lingering potion (if it's not : return)
        if (item.getType() != Material.POTION && item.getType() != Material.SPLASH_POTION && item.getType() != Material.LINGERING_POTION ) return;

        // Cancel the event and send a message to the player
        event.setCancelled(true);
        event.getPlayer().sendMessage("§cYou can't interact with potions !");
    }
}
