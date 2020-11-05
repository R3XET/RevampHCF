package eu.revamp.hcf.factions.type;

import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class EndPortalFaction extends ClaimableFaction implements ConfigurationSerializable
{
    public EndPortalFaction() {
        super("EndPortal");
    }
    
    public EndPortalFaction(Map<String, Object> map) {
        super(map);
    }
    
    public boolean isDeathban() {
        return true;
    }
}
