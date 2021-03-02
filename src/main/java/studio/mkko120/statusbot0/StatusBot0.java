package studio.mkko120.statusbot0;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import studio.mkko120.statusbot0.statusserver.DBConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public final class StatusBot0 extends Plugin {
    private static StatusBot0 instance;

    public static StatusBot0 getInstance() {
        return instance;
    }

    private Configuration config;

    public Configuration getConfig() {
        return config;
    }

    @Override
    public void onEnable() {
        instance = this;
        loadConfig();
        // Plugin startup logic
        DBConnection.loadvars();
        DBConnection.resultUpdate();
        getLogger().log(Level.INFO, "ServerStatus[BUNGEE]: Active");
        getLogger().log(Level.INFO, "ServerStatus[BUNGEE]: " + Storage.serverArray.size());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new StatusCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(), "config.yml");
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!(file.exists())) {
            try (InputStream is = getResourceAsStream("config.yml")) {
                Files.copy(is, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
