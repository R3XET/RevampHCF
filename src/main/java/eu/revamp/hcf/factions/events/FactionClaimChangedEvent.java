package eu.revamp.hcf.factions.events;

import eu.revamp.hcf.factions.enums.ClaimChangeEnum;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;

public class FactionClaimChangedEvent extends Event
{
    private static HandlerList handlers = new HandlerList();
    @Getter private CommandSender sender;
    @Getter private ClaimChangeEnum cause;
    @Getter private Collection<ClaimZone> affectedClaims;

    public FactionClaimChangedEvent(CommandSender sender, final ClaimChangeEnum cause, final Collection<ClaimZone> affectedClaims) {
        this.sender = sender;
        this.cause = cause;
        this.affectedClaims = affectedClaims;
    }
    public static HandlerList getHandlerList() {
        return FactionClaimChangedEvent.handlers;
    }
    @Override
    public HandlerList getHandlers() {
        return FactionClaimChangedEvent.handlers;
    }
}
