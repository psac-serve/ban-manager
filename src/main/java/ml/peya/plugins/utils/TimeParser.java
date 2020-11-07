package ml.peya.plugins.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeParser
{

    static String regex = "^[0-9]+((year|y)|(month|mo)|(day|d)|(hour|h)|(minute|min|m)|(second|sec|s))(s)?$";

    public static Date convert(String... args)
    {
        Calendar c = Calendar.getInstance();
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        for (String arg : args)
        {
            Matcher m = p.matcher(arg);
            if (!m.find())
                continue;

            String unit = m.group(1);
            int num;
            try
            {
                num = Integer.parseInt(arg.replace(unit, "").replace("s", ""));

            }
            catch (Exception ignored)
            {
                continue;
            }

            switch (unit.toLowerCase())
            {
                case "year":
                case "y":
                    c.add(Calendar.YEAR, num);
                    break;
                case "month":
                case "mo":
                    c.add(Calendar.MONTH, num);
                    break;
                case "day":
                case "d":
                    c.add(Calendar.DAY_OF_MONTH, num);
                    break;
                case "hour":
                case "h":
                    c.add(Calendar.HOUR, num);
                    break;
                case "minute":
                case "min":
                case "m":
                    c.add(Calendar.MINUTE, num);
                    break;
                case "second":
                case "sec":
                case "s":
                    c.add(Calendar.SECOND, num);
                    break;
            }
        }

        return c.getTime();
    }

    private static final String d = MessageEngine.get("base.day");
    private static final String h = MessageEngine.get("base.hour");
    private static final String m = MessageEngine.get("base.minutes");
    private static final String s = MessageEngine.get("base.seconds");

    public static String convertFromDate(Date date)
    {
        long now = new Date().getTime();
        long ago = date.getTime();
        long diff = ago - now;

        long day = diff / (24 * 60 * 60 * 1000);
        long hour = diff / (60 * 60 * 1000) % 24;
        long minute = diff / (60 * 1000) % 60;
        long second = diff / 1000 % 60;

        return day + d + hour + h + minute + m + second + s;
    }

    public static String convertFromDate(Date from, Date date)
    {
        long now = from.getTime();
        long ago = date.getTime();
        long diff = ago - now;

        long day = diff / (24 * 60 * 60 * 1000);
        long hour = diff / (60 * 60 * 1000) % 24;
        long minute = diff / (60 * 1000) % 60;
        long second = diff / 1000 % 60;

        return day + d + hour + h + minute + m + second + s;
    }
}