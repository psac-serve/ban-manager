package ml.peya.plugins;

import com.fasterxml.jackson.databind.JsonNode;
import ml.peya.api.BanManagerAPI;
import ml.peya.api.BanSection;
import ml.peya.plugins.utils.MessageEngine;
import ml.peya.plugins.utils.PlayerUtils;
import ml.peya.plugins.utils.SectionBuilder;
import ml.peya.plugins.utils.TimeParser;
import ml.peya.plugins.utils.UrlBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static ml.peya.plugins.PeyangGreatBanManager.config;

public class BAN implements BanManagerAPI
{

    @Override
    public void ban(UUID player, String reason, boolean hasStaff, @Nullable Date date)
    {
        JsonNode node = PeyangGreatBanManager.server.quickAccess("/ban", "PUT", UrlBuilder.ban(player, reason, hasStaff, date));
        if (!node.get("success").asBoolean())
            PeyangGreatBanManager.getPlugin().logger.warning("Failed to ban the player: " + node.get("cause").asText());
    }

    @Override
    public void pardon(UUID player)
    {
        PeyangGreatBanManager.server.quickAccess("/unban", "DELETE", "uuid=" + player);
    }

    private Long numParse(String num)
    {
        try
        {
            return Long.parseLong(num);
        }
        catch (Exception ignored)
        {
            return null;
        }
    }

    @Override
    public BanSection getBanInfo(UUID uuid)
    {

        JsonNode node = PeyangGreatBanManager.server.quickAccess("/getban", "GET", "uuid=" + uuid.toString());

        if (node == null || !node.get("success").asBoolean())
            return null;

        node = node.get("ban");


        return toSection(node);
    }

    @Override
    public ArrayList<BanSection> getBans(UUID player)
    {
        ArrayList<BanSection> sec = new ArrayList<>();

        JsonNode node = PeyangGreatBanManager.server.quickAccess("/bans", "GET", "uuid=" + player.toString());
        if (node == null || !node.get("success").asBoolean())
            return sec;

        node = node.get("bans");
        for (int i = 0; i < node.size(); i++)
        {
            JsonNode b = node.get(i);
            sec.add(toSection(b));
        }

        return sec;
    }

    private BanSection toSection(JsonNode node)
    {
        Long date = node.get("bannedDate").isNull() ? null: numParse(node.get("bannedDate").asText());
        Long ex = node.get("expire").isNull() ? null: numParse(node.get("expire").asText());
        Long un = node.get("unbannedDate").isNull() ? null: numParse(node.get("unbannedDate").asText());
        return new SectionBuilder(
                node.get("id").asText(),
                node.get("reason").asText(),
                date == null ? null: new Date(date),
                ex == null ? null: new Date(ex),
                un == null ? null: new Date(un),
                node.get("hasStaff").asBoolean(),
                node.get("unBanned").asBoolean()
        );
    }

    @Override
    public void banWithEffect(boolean msgDelay, Player player, String reason, boolean hasStaff, @Nullable Date date)
    {
        HashMap<String, Object> map = new HashMap<>();

        String id = RandomStringUtils.randomAlphanumeric(8);

        map.put("reason", reason);
        map.put("ggid", PlayerUtils.getGGID(id.hashCode()));
        map.put("id", id);

        Decorations.decoration(player);

        if (!msgDelay)
        {
            for (Player s : Bukkit.getOnlinePlayers())
            {
                if (s.hasPermission("pgbm.notification"))
                    MessageEngine.get("kick.broadcast");
            }
        }

        String message = MessageEngine.get("ban.permReason", map);

        if (date != null)
        {
            map.put("date", TimeParser.convertFromDate(date));
            message = MessageEngine.get("ban.tempReason", map);
        }


        if (!player.isOnline())
            player.kickPlayer(message);

        String finalMessage = message;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (msgDelay)
                    Bukkit.broadcast(MessageEngine.get("kick.broadcast"), "pgbm.notification");
                if (config.getBoolean("decoration.lightning"))
                    Decorations.lighting(player);

                ban(player.getUniqueId(), reason, hasStaff, date);

                if (player.isOnline())
                    player.kickPlayer(finalMessage);

                this.cancel();
            }
        }.runTaskLater(PeyangGreatBanManager.getPlugin(), Math.multiplyExact(PeyangGreatBanManager.config.getInt("decorations.delay"), 20));
    }

}
