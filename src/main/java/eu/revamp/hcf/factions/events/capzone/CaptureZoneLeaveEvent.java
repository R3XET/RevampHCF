package eu.revamp.hcf.factions.events.capzone;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import eu.revamp.hcf.factions.utils.games.CapturableFaction;
import org.bukkit.entity.Player;
import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import eu.revamp.hcf.factions.events.FactionEvent;

public class CaptureZoneLeaveEvent extends FactionEvent implements Cancellable
{
    private static HandlerList handlers = new HandlerList();
    @Getter @Setter private boolean cancelled;
    @Getter private CaptureZone captureZone;
    @Getter private Player player;

    public CaptureZoneLeaveEvent(Player player, CapturableFaction capturableFaction, CaptureZone captureZone) {
        super(capturableFaction);
        Preconditions.checkNotNull((Object)player, "Player cannot be null");
        Preconditions.checkNotNull((Object)captureZone, "Capture zone cannot be null");
        this.captureZone = captureZone;
        this.player = player;
    }
    
    @Override
    public CapturableFaction getFaction() {
        return (CapturableFaction)super.getFaction();
    }
    @Override
    public HandlerList getHandlers() {
        return CaptureZoneLeaveEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return CaptureZoneLeaveEvent.handlers;
    }
}
