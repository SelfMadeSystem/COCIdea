package uwu.smsgamer.cocidea.block.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uwu.smsgamer.cocidea.COCIdea;

public class CBlock {
    public Location location;
    public Material type;
    public String damageType;
    public boolean dead;

    public CBlock(Location location, Material type, String damageType) {
        this.location = location;
        this.type = type;
        this.damageType = damageType;
        dead = false;
    }

    public void death() {
        dead = true;
        location.getWorld().getBlockAt(location).setType(Material.AIR);
        COCIdea.instance.blockManager.blocks.remove(this);
    }

    public void damage(ItemStack weapon) {
    }

    public void onUpdate() {
    }

    public void onUpdate(Player player) {
    }
}
