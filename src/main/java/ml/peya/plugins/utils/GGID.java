package ml.peya.plugins.utils;

import java.util.*;

public class GGID
{
    public static String calc(String ban)
    {
        StringBuilder builder = new StringBuilder();
        Random random = new Random(ban.hashCode());
        for (int i = 0; i < 7; i++)
            builder.append(random.nextInt(9));
        return builder.toString();
    }
}
