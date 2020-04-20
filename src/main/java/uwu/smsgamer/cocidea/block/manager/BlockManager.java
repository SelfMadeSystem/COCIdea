package uwu.smsgamer.cocidea.block.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import uwu.smsgamer.cocidea.block.blocks.CBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockManager {
    public List<CBlock> blocks = new ArrayList<>();

    public void addBlock(CBlock cBlock) {
        if (getBlock(cBlock.location) != null)
            blocks.remove(getBlockAt(cBlock.location));
        blocks.add(cBlock);
    }

    public void removeBlock(Location location) {
        if (getBlock(location) != null) {
            getBlock(location).death();
            blocks.remove(getBlockAt(location));
        }
    }

    public CBlock getBlock(Location location) {
        for (CBlock block : blocks) {
            if (block.location.getX() == location.getX() &&
              block.location.getY() == location.getY() &&
              block.location.getZ() == location.getZ()) {
                return block;
            }
        }
        return null;
    }

    public int getBlockAt(Location location) {
        for (int i = 0; i < blocks.size(); i++) {
            CBlock block = blocks.get(i);
            if (block.location.getX() == location.getX() &&
              block.location.getY() == location.getY() &&
              block.location.getZ() == location.getZ()) {
                return i;
            }
        }
        return -1;
    }

    public CBlock getClosestBlock(Location location, double v) {
        CBlock toReturn = null;
        for (CBlock block : blocks) {
            if (block.location.distance(location) < v) {
                toReturn = block;
                v = block.location.distance(location);
            }
        }
        return toReturn;
    }
}
