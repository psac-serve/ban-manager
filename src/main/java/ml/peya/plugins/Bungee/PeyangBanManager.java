package ml.peya.plugins.Bungee;

import ml.peya.api.BanManagerAPI;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PeyangBanManager extends Plugin
{

    public static Configuration config;
    private static PeyangBanManager proxy;
    public static BanManagerAPI api;
    public static Logger logger;

    public static PeyangBanManager getPlugin()
    {
        return proxy;
    }

    @Override
    public void onEnable()
    {
        logger = getLogger();
        proxy = this;
        BungeeCordConfiguration cordConfiguration = new BungeeCordConfiguration("bungee-config.yml");
        try
        {
            getLogger().info("Loading config...");
            cordConfiguration.loadConfig();
            config = cordConfiguration.getConfig();
        }
        catch (IOException e)
        {
            getLogger().log(Level.SEVERE, "A critical error has occurred.");
            e.printStackTrace();
            return;

        }

        api = new MinBan(config.getString("server.addr"), config.getString("server.token"));

        if (api.isTested())
            logger.info("PingTest has completed successfully.");
        else
        {
            logger.info("Failed to PingTest.");
            logger.info("Please check the server address or token.");

            api = null;
            return;
        }

        getProxy().getPluginManager().registerListener(this, new Events());

        BungeeMessageEngine.initialize();

        getLogger().info("PeyangSuperbAntiCheatProxy has been activated!");

    }
}
