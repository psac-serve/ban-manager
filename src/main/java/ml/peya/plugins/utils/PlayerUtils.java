package ml.peya.plugins.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.CraftOfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

public class PlayerUtils
{
    public static UUID getPlayerAllowOffline(String playerName)
    {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null)
        {

            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers())
            {
                if (offlinePlayer.getName().toLowerCase().equals(playerName.toLowerCase()))
                    if (offlinePlayer.hasPlayedBefore())
                        return ((CraftOfflinePlayer) offlinePlayer).getProfile().getId();
            }
            return null;
        }
        else
            return player.getUniqueId();
    }

    public static String getGGID(long seed)
    {
        StringBuilder builder = new StringBuilder();
        Random random = new Random(seed);
        for (int i = 0; i < 7; i++)
            builder.append(random.nextInt(9));
        return builder.toString();
    }
}
