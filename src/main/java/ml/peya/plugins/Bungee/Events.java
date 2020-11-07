package ml.peya.plugins.Bungee;

import ml.peya.api.BanSection;
import ml.peya.plugins.utils.GGID;
import ml.peya.plugins.utils.TimeParser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Date;
import java.util.HashMap;

public class Events implements Listener
{
    @EventHandler
    public void onPlayerConnect(LoginEvent e)
    {
        final BanSection info = PeyangBanManager.api.getBanInfo(e.getConnection().getUniqueId());
        if (info == null)
            return;

        final String id = info.getID();
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
