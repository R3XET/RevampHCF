package eu.revamp.hcf.factions.type;

import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class SpawnFaction extends ClaimableFaction implements ConfigurationSerializable
{
    public SpawnFaction() {
        super("Spawn");
        this.safezone = true;
    }
    
    public SpawnFaction(Map<String, Object> map) {
        super(map);
    }
    
    public boolean isDeathban() {
        return false;
    }
}
