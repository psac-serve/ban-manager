package ml.peya.plugins.commands;

import ml.peya.plugins.*;
import ml.peya.plugins.utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.scheduler.*;

import java.util.*;
import java.util.regex.*;

import static ml.peya.plugins.utils.MessageEngine.*;

public class CommandTempBan implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (ErrorMessageSender.unPermMessage(sender, "pybans.tempban"))
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
                    sender.sendMessage(get("error.alreadyBanned"));
                    return;
                }


                if (Bukkit.getOfflinePlayer(pl).isOnline())
                    PeyangGreatBanManager.getAPI().banWithEffect(
                            true,
                            sender instanceof ConsoleCommandSender ? "~CONSOLE": sender.getName(),
                            Bukkit.getPlayer(pl),
                            reason.toString(),
                            false,
                            TimeParser.convert(argList.toArray(new String[0]))
                    );
                else
                    PeyangGreatBanManager.getAPI().ban(
                            pl,
                            sender instanceof ConsoleCommandSender ? "~CONSOLE": sender.getName(),
                            reason.toString(),
                            false,
                            TimeParser.convert(argList.toArray(new String[0]))
                    );
                sender.sendMessage(get("message.ban.playerBanned", pair("player", Bukkit.getOfflinePlayer(pl).getName())));

            }
        }.runTaskAsynchronously(PeyangGreatBanManager.getPlugin());

        return true;
    }
}
