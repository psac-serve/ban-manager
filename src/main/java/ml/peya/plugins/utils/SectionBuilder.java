package ml.peya.plugins.utils;

import ml.peya.api.BanSection;

import java.util.Date;

public class SectionBuilder implements BanSection
{
    private final String id;
    private final String re;
    private final Date ban;
    private final Date ex;
    private final Date ub;
    private final boolean st;
    private final boolean isUb;

    public SectionBuilder(String id, String reason, Date banned, Date expire, Date unbanned, boolean staff, boolean isUnBanned)
    {
        this.id = id;
        this.re = reason;
        this.ban = banned;
        this.ex = expire;
        this.st = staff;
        this.ub = unbanned;
        this.isUb = isUnBanned;
    }

    @Override
    public String getID()
    {
        return id;
    }

    @Override
    public String getReason()
    {
        return re;
    }

    @Override
    public Date banned()
    {
        return ban;
    }

    @Override
    public Date expire()
    {
        return ex;
    }

    @Override
    public boolean hasStaff()
    {
        return st;
    }

    @Override
    public Date unbanned()
    {
        return ub;
    }

    @Override
    public boolean isUnbanned()
    {
        return isUb;
    }
}
