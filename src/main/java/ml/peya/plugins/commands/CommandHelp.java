package ml.peya.plugins.commands;

import ml.peya.plugins.utils.ErrorMessageSender;
import ml.peya.plugins.utils.MessageEngine;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandHelp implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (ErrorMessageSender.invalidLengthMessage(sender, args, 0, 1) ||
                ErrorMessageSender.unPermMessage(sender, "pgbm.help"))
            return true;

        final boolean[] flag = {false};
        sender.sendMessage(MessageEngine.get("base.prefix"));

        if (args.length == 1)
        {
            if (MessageEngine.get("command.help." + args[0]) != null)
                sender.sendMessage(MessageEngine.get("command.help." + args[0]));
            else
                sender.sendMessage(MessageEngine.get("error.pgbm.help"));
            return true;
        }

        ArrayList<String> nodes = new ArrayList<>(Arrays.asList("ban", "tempban", "unban", "banhelp", "bans"));

        nodes.parallelStream()
                .filter((s) -> sender.hasPermission("pgbm." + s))
                .forEach((s) -> {
                    sender.spigot().sendMessage(new ComponentBuilder(MessageEngine.get("command.shelp." + s))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.AQUA + "クリックして詳細を表示").create()))
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/banhelp " + s))
                            .create());
                    flag[0] = true;
                });
        if (!flag[0])
            sender.sendMessage(MessageEngine.get("error.pgbm.notPage"));
        return true;
    }
}
