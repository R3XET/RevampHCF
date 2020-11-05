package eu.revamp.hcf.utils.timer.events;

import eu.revamp.hcf.utils.timer.PlayerTimer;
import eu.revamp.hcf.utils.timer.Timer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Optional;
import java.util.UUID;

@Getter
public class TimerPauseEvent extends Event implements Cancellable
{
    private static HandlerList handlers = new HandlerList();
    private boolean paused;
    private Optional<UUID> userUUID;
    private Timer timer;
    @Setter private boolean cancelled;

    public HandlerList getHandlers() {
        return TimerPauseEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return TimerPauseEvent.handlers;
    }
    
    public TimerPauseEvent(Timer timer, boolean paused) {
        this.userUUID = Optional.empty();
        this.timer = timer;
        this.paused = paused;
    }
    
    public TimerPauseEvent(UUID userUUID, PlayerTimer timer, boolean paused) {
        this.userUUID = Optional.ofNullable(userUUID);
        this.timer = timer;
        this.paused = paused;
    }
}
