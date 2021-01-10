package ml.peya.plugins.Bungee;

import com.fasterxml.jackson.databind.*;
import ml.peya.api.*;
import ml.peya.plugins.*;
import ml.peya.plugins.utils.*;
import org.bukkit.entity.*;

import javax.annotation.*;
import java.net.*;
import java.util.*;

public class MinBan implements BanManagerAPI
{
    private final boolean pingTest;
    private boolean msgpack;
    Server server;

    public MinBan(String addr, String token, boolean msgpack)
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
        if (!node.get("success").asBoolean())
            PeyangGreatBanManager.getPlugin().logger.warning("Failed to ban the player: " + node.get("cause").asText());
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void banWithEffect(boolean msgDelay, String bannedBy, Player player, String reason, boolean hasStaff, @Nullable Date date)
    {
        throw new UnsupportedOperationException();
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

}
