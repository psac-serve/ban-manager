package ml.peya.plugins;

import ml.peya.api.BanSection;
import ml.peya.plugins.utils.GGID;
import ml.peya.plugins.utils.MessageEngine;
import ml.peya.plugins.utils.TimeParser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;

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
