package ml.peya.api;

import java.util.Date;

public interface BanSection
{
    /**
     * BAN ID
     */

    String getID();

    /**
     * BAN理由
     */
    String getReason();

    /**
     * BAN日時
     */
    Date banned();

    /**
     * 解除予定日時
     */
    Date expire();

    /**
     * BAN解除日時
     */
    Date unbanned();

    /**
     * StaffによるBANかどうか
     */
    boolean hasStaff();

    /**
     * BAN解除されたかどうか
     */
    boolean isUnbanned();

    /**
     * BAN者
     */
    String bannedBy();

    /**
     * UNBAN者
     */
    String unBannedBy();

    /**
     * UNBAN理由
     */
    String unBanReason();
}
