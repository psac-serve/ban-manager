package ml.peya.plugins.commands;

import ml.peya.plugins.*;
import ml.peya.plugins.utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.scheduler.*;

import java.util.*;

import static ml.peya.plugins.utils.MessageEngine.*;

public class CommandBan implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (ErrorMessageSender.unPermMessage(sender, "pybans.ban"))
            return true;
        if (args.length < 1)
        {
            sender.sendMessage(get("error.invalidArgument"));
            return true;
        }

        UUID player = PlayerUtils.getPlayerAllowOffline(args[0]);
        if (player == null)
        {
            sender.sendMessage(MessageEngine.get("error.playerNotFound"));
            return true;
        }

        String reason = "Banned by Operator.";

        if (args.length >= 2)
        {
            ArrayList<String> reasons = new ArrayList<>(Arrays.asList(args));
            reasons.remove(0);

            reason = String.join(" ", reasons);
        }

        String finalReason = reason;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {

                if (PeyangGreatBanManager.getAPI().getBanInfo(player) != null)
                {
                    sender.sendMessage(get("error.alreadyBanned"));
                    return;
                }


                if (Bukkit.getOfflinePlayer(player).isOnline())
                    PeyangGreatBanManager.getAPI().banWithEffect(
                            true,
                            sender instanceof ConsoleCommandSender ? "~CONSOLE": sender.getName(),
                            Bukkit.getPlayer(player),
                            finalReason,
                            false,
                            null
                    );
                else
                    PeyangGreatBanManager.getAPI().ban(
                            player,
                            sender instanceof ConsoleCommandSender ? "~CONSOLE": sender.getName(),
                            finalReason,
                            false,
                            null
                    );

                sender.sendMessage(get("message.ban.playerBanned", pair("player", Bukkit.getOfflinePlayer(player).getName())));

            }
        }.runTaskAsynchronously(PeyangGreatBanManager.getPlugin());

        return true;
    }
}
