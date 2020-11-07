package ml.peya.plugins.commands;

import ml.peya.plugins.PeyangGreatBanManager;
import ml.peya.plugins.utils.ErrorMessageSender;
import ml.peya.plugins.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static ml.peya.plugins.utils.MessageEngine.get;
import static ml.peya.plugins.utils.MessageEngine.pair;

public class CommandUnban implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (ErrorMessageSender.unPermMessage(sender, "pybans.unban"))
            return true;

        if (args.length < 1)
        {
            sender.sendMessage(get("error.invalidArgument"));
            return true;
        }
        UUID player = PlayerUtils.getPlayerAllowOffline(args[0]);

        if (player == null)
        {
            sender.sendMessage(get("error.playerNotFound"));
            return true;
        }

        String reason = "UnBanned by an operator.";

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
                if (PeyangGreatBanManager.getAPI().getBanInfo(player) == null)
                {
                    sender.sendMessage(get("error.notBanned"));
                    return;
                }

                PeyangGreatBanManager.getAPI().pardon(player, finalReason, "~CONSOLE");

                sender.sendMessage(get("message.unban.playerUnBanned", pair("player", Bukkit.getOfflinePlayer(player).getName())));
            }
        }.runTaskAsynchronously(PeyangGreatBanManager.getPlugin());

        return true;
    }
}
