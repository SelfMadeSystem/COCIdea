package uwu.smsgamer.cocidea.event;

import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import uwu.smsgamer.cocidea.COCIdea;
import uwu.smsgamer.cocidea.block.blocks.CBlock;
import uwu.smsgamer.cocidea.block.blocks.DamageBlock;
import uwu.smsgamer.cocidea.block.blocks.HitBlock;
import uwu.smsgamer.cocidea.block.manager.BlockManager;
import uwu.smsgamer.cocidea.config.ConfigManager;
import uwu.smsgamer.cocidea.entity.entities.shulker.CustomShulker;
import uwu.smsgamer.cocidea.entity.entities.shulkerbullet.CustomShulkerBullet;

import java.util.HashMap;
import java.util.Map;

public class EventManager implements Listener {
    private BlockManager bm;
    private YamlConfiguration blocksConfig;
    private YamlConfiguration weaponsConfig;
    //          Player    LastAttackInMs DmgType
    private Map<String, Map<String, Long>> lastDamage = new HashMap<>();

    public EventManager() {
        this.bm = COCIdea.instance.blockManager;
        this.blocksConfig = ConfigManager.configs.get("blocks");
        this.weaponsConfig = ConfigManager.configs.get("weapons");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(COCIdea.instance, new Loopable(), 1, 1);
    }

    private static class Loopable implements Runnable {
        @Override
        public void run() {
            for (CBlock block : COCIdea.instance.blockManager.blocks) {
                block.onUpdate();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    block.onUpdate(player);
                }
            }
        }
    }

    //
    // Blocks
    //

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            CBlock block = bm.getBlock(event.getClickedBlock().getLocation());
            if (block != null) {
                if (lastDamage.get(event.getPlayer().getName()) == null) {
                    lastDamage.put(event.getPlayer().getName(), new HashMap<>());
                } else if (lastDamage.get(event.getPlayer().getName()).get(block.damageType) != null) {
                    if (lastDamage.get(event.getPlayer().getName()).get(block.damageType) +
                      (weaponsConfig.getInt("DamageTypes." + block.damageType) * 50) < System.currentTimeMillis()) {
                        lastDamage.get(event.getPlayer().getName()).remove(block.damageType);
                    }
                }
                lastDamage.get(event.getPlayer().getName()).put(block.damageType, System.currentTimeMillis());
                block.damage(event.getItem());
                if (block.dead)
                    block.death();
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (bm.getBlock(event.getBlock().getLocation()) != null) {
            if (bm.getBlock(event.getBlock().getLocation()).dead)
                event.setCancelled(true);
            else {
                event.setCancelled(true);
                bm.getBlock(event.getBlock().getLocation()).death();
                bm.removeBlock(event.getBlock().getLocation());
                event.getBlock().getLocation().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
            }
        } else {
            event.setCancelled(true);
            event.getBlock().getLocation().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType().equals(Material.BEDROCK)) {
            CBlock block = new DamageBlock(event.getBlockPlaced().getLocation(), event.getBlockPlaced().getType(),
              "uWu", 4);
            bm.addBlock(block);
        }
        ConfigurationSection section = blocksConfig.getConfigurationSection(event.getBlockPlaced().getType().name());
        if (section == null)
            return;
        CBlock block = new HitBlock(event.getBlockPlaced().getLocation(), event.getBlockPlaced().getType(),
          section.getString("DamageType"), (float) section.getDouble("Health"), (float) section.getDouble("Armor"));
        bm.addBlock(block);

    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/cocidea")) {
            String[] args = event.getMessage().substring(9).split(" ");
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("shlucker")) {
                    event.setCancelled(true);

                    Player player = event.getPlayer();
                    Location loc = player.getLocation();
                    World nmsworld = ((CraftWorld) loc.getWorld()).getHandle();
                    CustomShulker customShulker = new CustomShulker(nmsworld);
                    customShulker.setCustomNameVisible(true);
                    customShulker.setPosition(loc.getX(), loc.getY(), loc.getZ());
                    nmsworld.addEntity(customShulker);

                    event.getPlayer().sendMessage("Spawned a shlukkr!");
                }
                if (args[0].equalsIgnoreCase("buwwet")) {
                    event.setCancelled(true);

                    Player player = event.getPlayer();
                    Location loc = player.getLocation();
                    World nmsworld = ((CraftWorld) loc.getWorld()).getHandle();
                    CustomShulkerBullet customShulker = new CustomShulkerBullet(nmsworld);
                    customShulker.speed = 0;
                    customShulker.dontMove = true;
                    customShulker.dontDie = true;
                    customShulker.damage = Float.parseFloat(args[4]);
                    customShulker.setPosition(loc.getX() + Double.parseDouble(args[1]), loc.getY() + Double.parseDouble(args[2]),
                      loc.getZ() + Double.parseDouble(args[3]));
                    nmsworld.addEntity(customShulker);

                    event.getPlayer().sendMessage("Spawned a bUwWeT!");
                }
                if (args[0].equalsIgnoreCase("damage")) {
                    event.setCancelled(true);

                    spawnDamage(args, event.getPlayer());

                    event.getPlayer().sendMessage("Spawned a Damage!");
                }
                if (args[0].equalsIgnoreCase("rd")) {
                    event.setCancelled(true);
                    CBlock block = COCIdea.instance.blockManager.getClosestBlock(event.getPlayer().getLocation(), 5D);
                    if (block != null) {
                        COCIdea.instance.blockManager.blocks.remove(block);
                        event.getPlayer().sendMessage("Removed a Damage!");
                    } else {
                        event.getPlayer().sendMessage("Not Damages within 5 blocks of you!");
                    }
                }
            }
        }
    }

    private void spawnDamage(String[] args, Player player) {
        Location loc = player.getLocation().getBlock().getLocation().add(0.5, 0.5, 0.5);

        Material material = args.length > 1 ? Material.valueOf(args[1]) : Material.AIR;
        float damage = args.length > 2 ? Float.parseFloat(args[2]) : 1;
        boolean particles = args.length <= 3 || Boolean.parseBoolean(args[3]);
        float size = args.length > 4 ? Float.parseFloat(args[4]) : 1;
        boolean notFull = args.length <= 5 || Boolean.parseBoolean(args[5]);
        int hexColor = args.length > 6 ? Integer.parseInt(args[6], 16) : 0xFF0000;

        CBlock block = new DamageBlock(loc, material,
          "uWu", damage, particles, size, notFull, hexColor);
        bm.addBlock(block);
    }

    //
    // Entities
    //
}
