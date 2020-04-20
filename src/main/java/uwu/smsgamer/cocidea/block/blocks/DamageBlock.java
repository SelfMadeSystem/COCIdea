package uwu.smsgamer.cocidea.block.blocks;

import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Player;

public class DamageBlock extends CBlock {
    public float damage;
    public boolean particles;
    public float size;
    public boolean notFull;
    public Color color;

    public DamageBlock(Location location, Material type, String damageType, float damage) {
        super(location, type, damageType);
        this.damage = damage;
        this.dead = true;
        this.size = 1;
        this.particles = false;
        this.notFull = false;
    }

    public DamageBlock(Location location, Material type, String damageType, float damage, boolean particles, float size, boolean notFull, int hexColor) {
        super(location, type, damageType);
        this.damage = damage;
        this.dead = true;
        this.size = size;
        this.particles = particles;
        this.notFull = notFull;
        this.color = Color.fromRGB(hexColor);
        location.getWorld().getBlockAt(location).setType(Material.AIR);
    }

    public void onUpdate() {
        if (this.particles) {
            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    for (int z = -1; z < 2; z++) {
                        Location loc = location.getBlock().getLocation().add(0.5, 0.5, 0.5)
                          .add(x * size/2, y * size/2, z * size/2);

                        location.getWorld().spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY(), loc.getZ(),
                          0, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1);
                    }
                }
            }
        }
    }

    @Override
    public void onUpdate(Player player) {
        //Bukkit.broadcastMessage(player.toString());
        if (!player.getLocation().getWorld().equals(location.getWorld()))
            return;
        if (Math.abs(player.getLocation().getX() - this.location.getX()) > 5)
            return;
        if (Math.abs(player.getLocation().getY() - this.location.getY()) > 5)
            return;
        if (Math.abs(player.getLocation().getZ() - this.location.getZ()) > 5)
            return;
        AxisAlignedBB thisB;
        {
            final Location tempLoc = this.location.getBlock().getLocation();
            tempLoc.add(0.5, 0.5, 0.5);
            thisB = new AxisAlignedBB(tempLoc.getX() - this.size / 2, tempLoc.getY() - this.size / 2, tempLoc.getZ() - this.size / 2,
              tempLoc.getX() + this.size / 2, tempLoc.getY() + this.size / 2, tempLoc.getZ() + this.size / 2);
        }
        AxisAlignedBB entityB = ((CraftEntity) player).getHandle().getBoundingBox();
        //Bukkit.broadcastMessage(thisB + "  " + entityB);
        if(notFull) {
            if(thisB.d > entityB.a && thisB.a < entityB.d &&
              thisB.e > entityB.b && thisB.b < entityB.e &&
              thisB.f > entityB.c && thisB.c < entityB.f) {
                player.damage(damage);
            }
        } else {
            if (thisB.d >= entityB.a && thisB.a <= entityB.d &&
              thisB.e >= entityB.b && thisB.b <= entityB.e &&
              thisB.f >= entityB.c && thisB.c <= entityB.f) {
                player.damage(damage);
            }
        }
    }
}
