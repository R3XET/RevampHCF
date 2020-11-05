package eu.revamp.hcf.factions.events;

import com.google.common.base.Preconditions;
import lombok.Getter;
import eu.revamp.hcf.factions.Faction;
import org.bukkit.event.Event;

public abstract class FactionEvent extends Event
{
    @Getter protected Faction faction;
    
    public FactionEvent(Faction faction) {
        this.faction = (Faction)Preconditions.checkNotNull((Object)faction, "Faction cannot be null");
    }
    
    FactionEvent(Faction faction, boolean async) {
        super(async);
        this.faction = (Faction)Preconditions.checkNotNull((Object)faction, "Faction cannot be null");
    }
    
}
