package ml.peya.plugins.Bungee;

import ml.peya.api.*;
import ml.peya.plugins.utils.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.*;
import net.md_5.bungee.event.*;

import java.util.*;

public class Events implements Listener
{

    public static String convertFromDate(Date date)
    {
        long now = new Date().getTime();
        long ago = date.getTime();
        long diff = ago - now;

        long day = diff / 86400000L;
        long hour = diff / 3600000L % 24L;
        long minute = diff / 60000L % 60L;
        long second = diff / 1000L % 60L;

        return day + BungeeMessageEngine.get("base.day") + hour + BungeeMessageEngine.get("base.hour") + minute + BungeeMessageEngine.get("base.mintues") + second + BungeeMessageEngine.get("base.seconds");
    }

    @EventHandler
    public void onPlayerConnect(LoginEvent e)
    {
        final BanSection info = PeyangBanManager.api.getBanInfo(e.getConnection().getUniqueId());
        if (info == null)
            return;

        final String id = info.getID().substring(info.getID().length() - 8).toUpperCase();
        final String reason = info.getReason();
        final Date expire = info.expire();

        final String type;

        final HashMap<String, Object> pair = new HashMap<>();

        pair.put("reason", reason);
        pair.put("ggid", GGID.calc(id));
        pair.put("id", id);

        if (expire != null)
        {
            type = "ban.tempReason";
            pair.put("date", TimeParser.convertFromDate(expire));
            if (expire.before(new Date()))
            {
                ProxyServer.getInstance().getScheduler()
                        .runAsync(PeyangBanManager.getPlugin(), () -> {
                            PeyangBanManager.api.pardon(e.getConnection().getUniqueId(), "Tempban expired", "~BanBot");
                        });
                return;
            }
        }
        else
            type = "ban.permReason";

        e.setCancelled(true);
        e.setCancelReason(new ComponentBuilder(BungeeMessageEngine.get(type, pair)).create());
    }
}
