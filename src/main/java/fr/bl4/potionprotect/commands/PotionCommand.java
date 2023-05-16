package fr.bl4.potionprotect.commands;

import fr.bl4.potionprotect.PotionProtect;
import fr.bl4.potionprotect.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PotionCommand implements TabExecutor {

    private final PotionProtect plugin = PotionProtect.getPlugin();
    private final List<UUID> blacklistedUUID = PotionProtect.getBlacklistedUUID();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        // Check if player has 'potion.blacklist' permission. If not, send a message and do nothing.
        if (!sender.hasPermission("potion.blacklist")) {
            sender.sendMessage(Config.noPermission);
            return true;
        }

        if (args.length < 1) {
            // TODO: Send usage
            sender.sendMessage("Usage: /potion blacklist <player> to add or remove player from blacklist");
        } else if (args[0].equals("blacklist")) {

            // If there are no args[1]:
            // Send the list of player's name in the blacklist
            // If there are args[1] and it's a player:
            // Add the player if he's not in the blacklist or remove if he's in the blacklist
            if (args[1] == null) {

                List<String> blacklistedPlayers = new ArrayList<>();
                blacklistedUUID.forEach(uuid -> {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        blacklistedPlayers.add(player.getName());
                    }
                });

                sender.sendMessage("§fBlacklisted Players: " + String.join(", ", blacklistedPlayers));


            } else {


                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage("§cPlayer not found");
                    return true;
                }

                UUID playerUUID = player.getUniqueId();
                if (blacklistedUUID.contains(playerUUID)) {
                    blacklistedUUID.remove(playerUUID);
                    plugin.getDb().removePlayer(player);
                    sender.sendMessage(player.getName() + " has been removed from the blacklist");
                } else {
                    blacklistedUUID.add(playerUUID);
                    plugin.getDb().addPlayer(player);
                    sender.sendMessage(player.getName() + " has been added to the blacklist");
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return null;
    }
}
