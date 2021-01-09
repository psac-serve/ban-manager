package ml.peya.plugins.utils;

import javax.annotation.*;
import java.net.*;
import java.util.*;

public class UrlBuilder
{
    public static String ban(UUID player, String bannedBy, String reason, boolean hasStaff, @Nullable Date expire)
    {
        try
        {
            return "uuid=" + player + "&by=" + bannedBy + "&staff=" + hasStaff + "&reason=" + URLEncoder.encode(reason, "UTF-8") + (expire == null ? "&expire=_PERM": "&expire=" + expire.getTime());
        }
        catch (Exception e)
        {
            return "uuid=" + player + "&by=" + bannedBy + "&staff=" + hasStaff + "&reason=Banned%20by%20an%20operator." + (expire == null ? "&expire=_PERM": "&expire=" + expire.getTime());
        }
    }
}
