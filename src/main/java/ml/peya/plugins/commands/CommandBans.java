package ml.peya.plugins.commands;

import ml.peya.api.BanSection;
import ml.peya.plugins.PeyangGreatBanManager;
import ml.peya.plugins.utils.ErrorMessageSender;
import ml.peya.plugins.utils.MessageEngine;
import ml.peya.plugins.utils.PlayerUtils;
import ml.peya.plugins.utils.TimeParser;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

public class CommandBans implements CommandExecutor
{
    public static boolean iN(String g)
    {
        if (g.contains("."))
            return false;
        long num;
        try
        {
            num = Long.parseLong(g);
        }
        catch (Exception ignored)
        {
            return false;
        }
        return num >= 0;
    }

    public static int n(String n)
    {
        try
        {
            long a = Long.parseLong(n);
            int b = Integer.parseInt(n);
            if (a != b || b < 0)
                return 0;
            return b;
        }
        catch (Exception ignored)
        {
            return 0;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (ErrorMessageSender.unPermMessage(sender, "pybans.bans") || ErrorMessageSender.invalidLengthMessage(sender, args, 1, 2))
            return true;


        int page = 1;
        if (args.length == 2 && iN(args[1]))
            page = n(args[1]);

        if (page == 0)
            page = 1;

        UUID player;
        if (args[0].equals("-a"))
            player = PlayerUtils.getPlayerAllowOffline(args[1]);
        else
            player = PlayerUtils.getPlayerAllowOffline(args[0]);
        if (player == null)
        {
            sender.sendMessage(MessageEngine.get("error.playerNotFound"));
            return true;
        }


        final int finalPage = page;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                ArrayList<BanSection> sections = PeyangGreatBanManager.getAPI().getBans(player);
                int count = 0;
                int start = 10 * (finalPage - 1);

                String x = MessageEngine.get(
                        "message.bans.nm",
                        new HashMap<String, Object>()
                        {{
                            put("name", Bukkit.getOfflinePlayer(player).getName());
                            put("s", finalPage);
                            put("mx", ((int) Math.ceil(sections.size() / 10.0)));
                        }}
                );

                sender.sendMessage(x); //prefix

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                if (sections.size() == 0)
                    return;
                for (int i = 0; i < start + 10; i++)
                {
                    count++;
                    if (i < start)
                        continue;

                    if (sections.size() <= i)
                        break;

                    BanSection section = sections.get(i);

                    String type = ChatColor.YELLOW + (section.expire() == null ? "PermaBan - ": "TempBan - ");
                    String banned = format.format(section.banned()) + " ";
                    String reason = ChatColor.WHITE + ChatColor.ITALIC.toString() + section.getReason() + " ";
                    String forS = section.expire() == null ? "": ChatColor.RESET + ChatColor.YELLOW.toString() + " for " +
                            TimeParser.convertFromDate(section.banned(), section.expire());
                    String unb = !section.isUnbanned() ? "":
                            ChatColor.RESET + ChatColor.YELLOW.toString() + " unbanned " + format.format(section.unbanned());
                    String unbr = !section.isUnbanned() ? "": ChatColor.ITALIC.toString() + ChatColor.WHITE + " " + section.unBanReason() + " ";

                    sender.sendMessage(
                            ChatColor.GOLD.toString() + (i + 1) + ". " +
                                    type +
                                    banned +
                                    reason +
                                    by(section.bannedBy()) +
                                    forS +
                                    unb +
                                    unbr +
                                    (section.isUnbanned() ? by(section.unBannedBy()): "")
                    );
                }

                ComponentBuilder builder = new ComponentBuilder(finalPage != 1 ? ChatColor.GOLD + "[<<]": "");
                if (finalPage != 1)
                    builder.event(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/bans " + args[0] + " " + (finalPage - 1)
                    ));
                else
                    builder.append(ChatColor.GOLD + "----");
                builder = new ComponentBuilder(builder);
                StringBuilder a = new StringBuilder();
                IntStream.range(0, Objects.requireNonNull(x).length() - 8).forEach((s) -> a.append("-"));
                builder.append(ChatColor.GOLD + a.toString());
                if (count < sections.size())
                    builder.append(ChatColor.GOLD + "[>>]")
                            .event(new ClickEvent(
                                    ClickEvent.Action.RUN_COMMAND,
                                    "/bans " + args[0] + " " + (finalPage + 1)
                            ));
                else
                    builder.append(ChatColor.GOLD + "----");
                sender.spigot().sendMessage(builder.create());
            }
        }.runTaskAsynchronously(PeyangGreatBanManager.getPlugin());

        return true;
    }

    private static String by(String name)
    {
        return ChatColor.RESET + ChatColor.YELLOW.toString() + "by " + name;
    }
}
