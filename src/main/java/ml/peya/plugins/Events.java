package ml.peya.plugins;

import ml.peya.api.*;
import ml.peya.plugins.utils.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.*;

import java.util.*;

public class Events implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(AsyncPlayerPreLoginEvent e)
    {
        if (PeyangGreatBanManager.isBungee)
            return;
        final BanSection info = PeyangGreatBanManager.getAPI().getBanInfo(e.getUniqueId());

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
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        PeyangGreatBanManager.getAPI().pardon(e.getUniqueId(), "Tempban expired", "~BanBot");
                    }
                }.runTaskAsynchronously(PeyangGreatBanManager.getPlugin());
                return;
            }
        }
        else
            type = "ban.permReason";
        e.setKickMessage(MessageEngine.get(type, pair));
        e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
    }

}
