package ml.peya.plugins.commands;

import ml.peya.plugins.PeyangGreatBanManager;
import ml.peya.plugins.utils.ErrorMessageSender;
import ml.peya.plugins.utils.PlayerUtils;
import ml.peya.plugins.utils.TimeParser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.security.KeyFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;

import static ml.peya.plugins.utils.MessageEngine.*;

public class CommandTempBan implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (ErrorMessageSender.unPermMessage(sender, "pgbm.tempban"))
            return true;
        if (args.length < 2)
        {
            sender.sendMessage(get("error.invalidArgument"));
            return true;
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {



                ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));

                UUID pl = null;

                StringBuilder reason = new StringBuilder();

                for (String arg : argList)
                {
                    UUID player = PlayerUtils.getPlayerAllowOffline(arg);
                    if (player != null)
                    {
                        if (pl == null)
                            pl = player;

                        continue;
                    }


                    if (Pattern.compile(
                            "^[0-9]+((year|y)|(month|mo)|(day|d)|(hour|h)|(minute|min|m)|(second|sec|s))(s)?$",
                            Pattern.CASE_INSENSITIVE
                    ).matcher(arg).find())
                        continue;

                    reason.append(arg).append(" ");
                }

                if (pl == null)
                {
                    sender.sendMessage(get("error.playerNotFound"));
                    return;
                }


                if (PeyangGreatBanManager.getAPI().getBanInfo(pl) != null)
                {
                    sender.sendMessage(get("error.error.alreadyBanned"));
                    return;
                }


                if (Bukkit.getOfflinePlayer(pl).isOnline())
                    PeyangGreatBanManager.getAPI().banWithEffect(false, Bukkit.getPlayer(pl), reason.toString(), false, TimeParser.convert(argList.toArray(new String[0])));
                else
                    PeyangGreatBanManager.getAPI().ban(pl, reason.toString(), false,null);

            }
        }.runTaskAsynchronously(PeyangGreatBanManager.getPlugin());

        return true;
    }
}
