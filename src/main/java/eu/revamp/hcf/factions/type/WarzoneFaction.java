package eu.revamp.hcf.factions.type;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.command.CommandSender;
import java.util.Map;
import eu.revamp.hcf.factions.Faction;

public class WarzoneFaction extends Faction
{
    public WarzoneFaction() {
        super("Warzone");
    }
    
    public WarzoneFaction(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public String getDisplayName(CommandSender sender) {
        return RevampHCF.getInstance().getConfiguration().getWarzoneColor() + this.getName();
    }
}
