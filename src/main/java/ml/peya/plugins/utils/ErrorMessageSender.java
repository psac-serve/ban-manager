package ml.peya.plugins.utils;

import org.bukkit.command.*;

import static ml.peya.plugins.utils.MessageEngine.*;

public class ErrorMessageSender
{
    public static boolean unPermMessage(CommandSender sender, String perm)
    {
        if (sender.hasPermission(perm))
            return false;
        sender.sendMessage(get("error.notHavePermission"));
        return true;
    }

    public static boolean invalidLengthMessage(CommandSender sender, String[] args, int min, int max)
    {
        if (args.length < min || args.length > max)
        {
            sender.sendMessage(get("error.invalidArgument"));
            return true;
        }

        return false;
    }
}
