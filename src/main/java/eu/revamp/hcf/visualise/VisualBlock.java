package eu.revamp.hcf.visualise;

import lombok.Getter;
import org.bukkit.Location;

public class VisualBlock
{
    @Getter private VisualType visualType;
    @Getter private VisualBlockData blockData;
    @Getter private Location location;
    
    public VisualBlock(VisualType visualType, final VisualBlockData blockData, final Location location) {
        this.visualType = visualType;
        this.blockData = blockData;
        this.location = location;
    }
}
