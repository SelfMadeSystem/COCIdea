package uwu.smsgamer.cocidea.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    public static Map<String, YamlConfiguration> configs = new HashMap<>();

    public static void loadConfig(JavaPlugin plugin, String name) {
        configs.remove(name);
        File configFile = new File(plugin.getDataFolder(), name + ".yml");
        if (!configFile.exists())
            plugin.saveResource(name + ".yml", false);
        configs.put(name, YamlConfiguration.loadConfiguration(configFile));
    }

    public static void saveConfig(JavaPlugin plugin, String name) {
        try {
            configs.get(name).save(plugin.getDataFolder().getAbsolutePath() + File.separator + name + ".yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
