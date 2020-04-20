package uwu.smsgamer.cocidea.block.blocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import uwu.smsgamer.cocidea.COCIdea;
import uwu.smsgamer.cocidea.config.ConfigManager;

public class HitBlock extends CBlock {
    public float health;
    public float armor;
    public HitBlock(Location location, Material type, String damageType, float health, float armor) {
        super(location, type, damageType);
        this.health = health;
        this.armor = armor;
    }

    public void damage(ItemStack itemStack) {
        Material weapon = itemStack == null ? Material.AIR : itemStack.getType();
        float damage = (float) ConfigManager.configs.get("weapons").getDouble("DEFAULT." + damageType);
        if (ConfigManager.configs.get("weapons").contains(weapon.name() + "." + damageType))
            damage = (float) ConfigManager.configs.get("weapons").getDouble(weapon.name() + "." + damageType);

        if (damage > armor / 2f) {
            health -= damage - armor / 2f;
        }

        Bukkit.broadcastMessage(damage + "  " + health + "  " + (damage - armor / 2f) + "  " + armor);
        if (health <= 0) {
            dead = true;/*
            location.getWorld().getBlockAt(location).setType(Material.AIR);
            COCIdea.instance.blockManager.removeBlock(location);*/
        }
    }
}
