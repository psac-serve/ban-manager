package ml.peya.plugins;

import ml.peya.api.BanManagerAPI;
import ml.peya.plugins.commands.CommandBan;
import ml.peya.plugins.commands.CommandBans;
import ml.peya.plugins.commands.CommandHelp;
import ml.peya.plugins.commands.CommandTempBan;
import ml.peya.plugins.commands.CommandUnban;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PeyangGreatBanManager extends JavaPlugin
{//いろいろめんどいのでJavaDoc見送り

    public Logger logger = getLogger();
    private static BanManagerAPI ban;
    public static FileConfiguration config;
    public static Server server = null;
    public static boolean isAgent = false;
    private static PeyangGreatBanManager plugin;
    @Override
    public void onEnable()
    {
        plugin = this;
        ban = new BAN();

        saveDefaultConfig();
        config = getConfig();

        getCommand("ban").setExecutor(new CommandBan());
        getCommand("unban").setExecutor(new CommandUnban());
        getCommand("tempban").setExecutor(new CommandTempBan());
        getCommand("banhelp").setExecutor(new CommandHelp());
        getCommand("bans").setExecutor(new CommandBans());

        logger.info("------[PeyangGreatBanManager]------");

        logger.info("     Mode: Server");
        logger.info("     ADDR: " + config.getString("server.addr"));
        server = new Server(config.getString("server.addr"));
        if (server.pingTest())
            logger.info("PingTest has completed successfully.");
        else
        {
            logger.info("Failed to PingTest.");
            logger.info("Please check the server address or token.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        isAgent = true;


        logger.info("PeyangGreatBanManager has activated!");
    }

    /**
     * API取得
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
}
