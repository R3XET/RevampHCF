package eu.revamp.hcf.games.cuboid;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

public class CoordinatePair
{
    @Getter private String worldName;
    @Getter private int x;
    @Getter private int z;
    
    public CoordinatePair(Block block) {
        this(block.getWorld(), block.getX(), block.getZ());
    }
    
    public CoordinatePair(World world, final int x, final int z) {
        this.worldName = world.getName();
        this.x = x;
        this.z = z;
    }
    
    public World getWorld() {
        return Bukkit.getWorld(this.worldName);
    }
}
