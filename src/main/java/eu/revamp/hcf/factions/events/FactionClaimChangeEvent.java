package eu.revamp.hcf.factions.events;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import eu.revamp.hcf.factions.type.ClaimableFaction;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import java.util.Collection;
import eu.revamp.hcf.factions.enums.ClaimChangeEnum;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

@Getter
public class FactionClaimChangeEvent extends Event implements Cancellable
{
    private static HandlerList handlers = new HandlerList();
    @Setter private boolean cancelled;
    private ClaimChangeEnum cause;
    private Collection<ClaimZone> affectedClaims;
    private ClaimableFaction claimableFaction;
    private CommandSender sender;

    public FactionClaimChangeEvent(CommandSender sender, ClaimChangeEnum cause, Collection<ClaimZone> affectedClaims, ClaimableFaction claimableFaction) {
        Preconditions.checkNotNull((Object)sender, "CommandSender cannot be null");
        Preconditions.checkNotNull((Object)cause, "Cause cannot be null");
        Preconditions.checkNotNull((Object)affectedClaims, "Affected claims cannot be null");
        Preconditions.checkNotNull((Object)affectedClaims.isEmpty(), "Affected claims cannot be empty");
        Preconditions.checkNotNull((Object)claimableFaction, "ClaimableFaction cannot be null");
        this.sender = sender;
        this.cause = cause;
        this.affectedClaims = affectedClaims;
        this.claimableFaction = claimableFaction;
    }
    @Override
    public HandlerList getHandlers() {
        return FactionClaimChangeEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return FactionClaimChangeEvent.handlers;
    }
}
