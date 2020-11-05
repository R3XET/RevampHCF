package eu.revamp.hcf.factions.events;

import lombok.Getter;
import lombok.Setter;
import eu.revamp.hcf.factions.utils.struction.Relation;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class FactionRelationRemoveEvent extends Event implements Cancellable
{
    private static HandlerList handlers = new HandlerList();
    @Getter @Setter private boolean cancelled;
    @Getter private PlayerFaction senderFaction;
    @Getter private PlayerFaction targetFaction;
    @Getter private Relation relation;

    public FactionRelationRemoveEvent(PlayerFaction senderFaction, PlayerFaction targetFaction, Relation relation) {
        this.senderFaction = senderFaction;
        this.targetFaction = targetFaction;
        this.relation = relation;
    }
    @Override
    public HandlerList getHandlers() {
        return FactionRelationRemoveEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return FactionRelationRemoveEvent.handlers;
    }
    
}
