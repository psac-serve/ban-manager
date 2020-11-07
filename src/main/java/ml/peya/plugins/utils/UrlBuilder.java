package ml.peya.plugins.utils;

import javax.annotation.Nullable;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

public class UrlBuilder
{
    public static String ban(UUID player, String reason, boolean hasStaff, @Nullable Date expire)
    {
        try
        {
            return "uuid=" + player + "&staff=" + hasStaff + "&reason=" + URLEncoder.encode(reason, "UTF-8") + (expire == null ? "&expire=_PERM": "&expire=" + expire.getTime());
        }
        catch (Exception e)
        {
            return "uuid=" + player + "&staff=" + hasStaff + "&reason=Banned%20by%20an%20operator." + (expire == null ? "&expire=_PERM": "&expire=" + expire.getTime());
        }
    }
}
