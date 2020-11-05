package eu.revamp.hcf.factions.events;

import lombok.Getter;
import lombok.Setter;
import eu.revamp.hcf.factions.utils.struction.Relation;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

@Getter
public class FactionRelationCreateEvent extends Event implements Cancellable
{
    private static HandlerList handlers = new HandlerList();
    @Setter private boolean cancelled;
    private PlayerFaction senderFaction;
    private PlayerFaction targetFaction;
    private Relation relation;

    public FactionRelationCreateEvent(PlayerFaction senderFaction, final PlayerFaction targetFaction, final Relation relation) {
        this.senderFaction = senderFaction;
        this.targetFaction = targetFaction;
        this.relation = relation;
    }
    @Override
    public HandlerList getHandlers() {
        return FactionRelationCreateEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return FactionRelationCreateEvent.handlers;
    }
}
