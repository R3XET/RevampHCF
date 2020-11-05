package eu.revamp.hcf.factions.events;

import eu.revamp.hcf.factions.Faction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class FactionPlayerClaimEnterEvent extends Event implements Cancellable
{
    private static HandlerList handlers = new HandlerList();
    @Setter private boolean cancelled;
    private Player player;
    private Faction fromFaction;
    private Faction toFaction;
    private Location from;
    private Location to;
    private EnterCause enterCause;

    public FactionPlayerClaimEnterEvent(Player player, Location from, Location to, Faction fromFaction, Faction toFaction, EnterCause enterCause) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.fromFaction = fromFaction;
        this.toFaction = toFaction;
        this.enterCause = enterCause;
    }

    public static HandlerList getHandlerList() {
        return FactionPlayerClaimEnterEvent.handlers;
    }
    @Override
    public HandlerList getHandlers() {
        return FactionPlayerClaimEnterEvent.handlers;
    }
    public enum EnterCause
    {
        TELEPORT("TELEPORT", 0), 
        MOVEMENT("MOVEMENT", 1);
        
        EnterCause(String s, int n) {
        }
    }
}
