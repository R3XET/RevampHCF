package eu.revamp.hcf.factions.events;

import eu.revamp.hcf.factions.utils.struction.Raidable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class FactionDTRChangeEvent extends Event implements Cancellable
{
    private static HandlerList handlers = new HandlerList();
    @Setter private boolean cancelled;
    private DtrUpdateCause cause;
    private Raidable raidable;
    private double originalDtr;
    @Setter private double newDtr;

    public FactionDTRChangeEvent(DtrUpdateCause cause, Raidable raidable, double originalDtr, double newDtr) {
        this.cause = cause;
        this.raidable = raidable;
        this.originalDtr = originalDtr;
        this.newDtr = newDtr;
    }
    public boolean isCancelled() {
        return this.cancelled || Math.abs(this.newDtr - this.originalDtr) == 0.0;
    }
    @Override
    public HandlerList getHandlers() {
        return FactionDTRChangeEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return FactionDTRChangeEvent.handlers;
    }

    public enum DtrUpdateCause
    {
        REGENERATION("REGENERATION", 0), 
        MEMBER_DEATH("MEMBER_DEATH", 1);
        
        DtrUpdateCause(String s, int n) {
        }
    }
}
