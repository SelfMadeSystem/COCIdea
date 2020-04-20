package uwu.smsgamer.cocidea;

import org.bukkit.plugin.java.JavaPlugin;
import uwu.smsgamer.cocidea.block.manager.BlockManager;
import uwu.smsgamer.cocidea.config.ConfigManager;
import uwu.smsgamer.cocidea.entity.CustomEntityRegistry;
import uwu.smsgamer.cocidea.entity.entities.shulker.CustomShulker;
import uwu.smsgamer.cocidea.entity.entities.shulkerbullet.CustomShulkerBullet;
import uwu.smsgamer.cocidea.event.EventManager;

public final class COCIdea extends JavaPlugin {
    public static COCIdea instance;

    public BlockManager blockManager;
    public EventManager eventManager;

    @Override
    public void onEnable() {// TODO: 2020-04-13 Add a command manager. 
        instance = this;
        ConfigManager.loadConfig(instance, "blocks");
        ConfigManager.loadConfig(instance, "weapons");
        blockManager = new BlockManager();
        getServer().getPluginManager().registerEvents(eventManager = new EventManager(), this);
        CustomEntityRegistry.registerCustomEntity(69, "Shlucker", CustomShulker.class);
        CustomEntityRegistry.registerCustomEntity(25, "ShluckerBullet", CustomShulkerBullet.class);
    }

    @Override
    public void onDisable() {
    }
}
