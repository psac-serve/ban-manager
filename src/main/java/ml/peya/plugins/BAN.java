package ml.peya.plugins;

import com.fasterxml.jackson.databind.*;
import ml.peya.api.*;
import ml.peya.plugins.utils.*;
import org.apache.commons.lang3.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.*;

import javax.annotation.*;
import java.net.*;
import java.util.*;

public class BAN implements BanManagerAPI
{
    private final boolean pingTest;
    private boolean msgpack;
    Server server;

    public BAN(String addr, String token, boolean msgpack)
    {
        this.server = new Server(addr, token);
        pingTest = server.pingTest();
        this.msgpack = msgpack;
    }

    @Override
    public boolean isTested()
    {
        return pingTest;
    }

    @Override
    public void ban(UUID player, String bannedBy, String reason, boolean hasStaff, @Nullable Date date)
    {
        JsonNode node = server.quickAccess("/ban", "PUT", UrlBuilder.ban(player, bannedBy, reason, hasStaff, date), msgpack);
        if (node == null || node.get("success") == null || !node.get("success").asBoolean())
        {
            if (node != null)
                PeyangGreatBanManager.getPlugin().logger.warning("Failed to ban the player: " + node.get("cause").asText());
            PeyangGreatBanManager.getPlugin().logger.warning("Failed to ban the player: Unknown");

        }
    }

    @Override
    public void pardon(UUID player, String reason, String unBannedBy)
    {

        try
        {
            server.quickAccess("/unban", "DELETE", "uuid=" + player + "&reason=" + URLEncoder.encode(reason, "UTF-8") + "&by=" + unBannedBy, msgpack);
        }
        catch (Exception e)
        {
            server.quickAccess("/unban", "DELETE", "uuid=" + player + "&reason=UnBanned%20by%20an%20operator." + "&by=" + unBannedBy, msgpack);
        }

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

        JsonNode node = server.quickAccess("/getban", "GET", "uuid=" + uuid.toString(), msgpack);

        if (node == null || !node.get("success").asBoolean())
            return null;

        node = node.get("ban");


        return toSection(node);
    }

    @Override
    public ArrayList<BanSection> getBans(UUID player)
    {
        ArrayList<BanSection> sec = new ArrayList<>();

        JsonNode node = server.quickAccess("/bans", "GET", "uuid=" + player.toString(), msgpack);
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
                node.get("unBanned").asBoolean(),
                node.get("bannedBy") == null ? null: node.get("bannedBy").asText(),
                node.get("unBannedBy") == null ? null: node.get("unBannedBy").asText(),
                node.get("unBanReason") == null ? null: node.get("unBanReason").asText()
        );
    }

    @Override
    public void banWithEffect(boolean msgDelay, String bannedby, Player player, String reason, boolean hasStaff, @Nullable Date date)
    {
        HashMap<String, Object> map = new HashMap<>();

        String id = RandomStringUtils.randomAlphanumeric(8);

        map.put("reason", reason);
        map.put("ggid", PlayerUtils.getGGID(id.hashCode()));
        map.put("id", id);

        if (!msgDelay)
        {
            for (Player s : Bukkit.getOnlinePlayers())
            {
                if (s.hasPermission("pybans.notification"))
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
        ban(player.getUniqueId(), bannedby, reason, hasStaff, date);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (msgDelay)
                    Bukkit.broadcast(MessageEngine.get("kick.broadcast"), "pybans.notification");

                if (player.isOnline())
                    player.kickPlayer(finalMessage);

                this.cancel();
            }
        }.runTaskLater(PeyangGreatBanManager.getPlugin(), Math.multiplyExact(PeyangGreatBanManager.config.getInt("kick.lag"), 20));
    }

}
