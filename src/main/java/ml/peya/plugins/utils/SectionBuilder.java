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
    private final String bd;
    private final String ud;
    private final String ubr;

    public SectionBuilder(String id, String reason, Date banned, Date expire, Date unbanned, boolean staff, boolean isUnBanned, String bannedBy, String unBannedBy, String unBanReason)
    {
        this.id = id;
        this.re = reason;
        this.ban = banned;
        this.ex = expire;
        this.st = staff;
        this.ub = unbanned;
        this.isUb = isUnBanned;
        this.ud = unBannedBy;
        this.bd = bannedBy;
        this.ubr = unBanReason;
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

    @Override
    public String unBannedBy()
    {
        return ud;
    }

    @Override
    public String bannedBy()
    {
        return bd;
    }

    @Override
    public String unBanReason()
    {
        return ubr;
    }
}
