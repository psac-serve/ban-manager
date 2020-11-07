package ml.peya.plugins;

import ml.peya.api.BanManagerAPI;
import ml.peya.plugins.commands.CommandBan;
import ml.peya.plugins.commands.CommandBans;
import ml.peya.plugins.commands.CommandHelp;
import ml.peya.plugins.commands.CommandTempBan;
import ml.peya.plugins.commands.CommandUnban;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PeyangGreatBanManager extends JavaPlugin
{//いろいろめんどいのでJavaDoc見送り

    public static FileConfiguration config;
    public static boolean isAgent = false;
    public static boolean isBungee = false;
    private static BanManagerAPI ban;
    private static PeyangGreatBanManager plugin;
    public Logger logger = getLogger();

    /**
     * API取得
     *
     * @return API
     */
    public static BanManagerAPI getAPI()
    {
        return ban;
    }

    public static PeyangGreatBanManager getPlugin()
    {
        return plugin;
    }

    @Override
    public void onEnable()
    {
        plugin = this;
        ban = new BAN(config.getString("server.addr"), config.getString("server.token"));

        saveDefaultConfig();
        config = getConfig();

        isBungee = config.getBoolean("bungeecord");

        getCommand("ban").setExecutor(new CommandBan());
        getCommand("unban").setExecutor(new CommandUnban());
        getCommand("tempban").setExecutor(new CommandTempBan());
        getCommand("banhelp").setExecutor(new CommandHelp());
        getCommand("bans").setExecutor(new CommandBans());
        getServer().getPluginManager().registerEvents(new Events(), this);

        logger.info("------[PeyangGreatBanManager]------");

        logger.info("     Mode: Server");
        logger.info("     ADDR: " + config.getString("server.addr"));

        if (ban.isTested())
            logger.info("PingTest has completed successfully.");
        else
        {
            logger.info("Failed to PingTest.");
            logger.info("Please check the server address or token.");
            return;
        }
        isAgent = true;


        logger.info("PeyangGreatBanManager has activated!");
    }
}
