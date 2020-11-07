package ml.peya.api;

import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public interface BanManagerAPI
{
    /**
     * プレイヤーをBANします。
     *
     * @param player   プレイヤー
     * @param reason   理由
     * @param hasStaff STAFFによるBANかどうか
     * @param date     日時
     */
    void ban(UUID player, String reason, boolean hasStaff, @Nullable Date date);

    /**
     * プレイヤーのBANを解除します。
     *
     * @param player プレイヤー
     */
    void pardon(UUID player);

    /**
     * BANの詳細を取得します。
     * BANがない場合、nullを返します。
     *
     * @param uuid 対象
     * @return Info
     */
    BanSection getBanInfo(UUID uuid);

    /**
     * プレイヤーのBAN履歴を取得します。
     * 注) 現在BANされている場合、その情報も格納されます。
     *
     * @param player 対象
     * @return 中身
     */
    ArrayList<BanSection> getBans(UUID player);

    /**
     * プレイヤーをBANします。
     *
     * @param msgDelay メッセージを送信するまでの遅延
     * @param player   プレイヤー
     * @param reason   理由
     * @param date     日時
     */
    void banWithEffect(boolean msgDelay, Player player, String reason, boolean hasStaff, @Nullable Date date);
}
