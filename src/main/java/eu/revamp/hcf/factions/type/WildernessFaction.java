package eu.revamp.hcf.factions.type;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.command.CommandSender;
import java.util.Map;
import eu.revamp.hcf.factions.Faction;

public class WildernessFaction extends Faction
{
    public WildernessFaction() {
        super("The Wilderness");
    }
    
    public WildernessFaction(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public String getDisplayName(CommandSender sender) {
        return RevampHCF.getInstance().getConfiguration().getWildernessColor() + this.getName();
    }
}
