package eu.revamp.hcf.utils.timer.events;

import com.google.common.base.Optional;
import eu.revamp.hcf.utils.timer.Timer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class TimerExpireEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    @Getter private final Optional<UUID> userUUID;
    @Getter private final Timer timer;

    public TimerExpireEvent(Timer timer) {
        this.userUUID = Optional.absent();
        this.timer = timer;
    }
    
    public TimerExpireEvent(UUID userUUID, Timer timer) {
        this.userUUID = Optional.fromNullable(userUUID);
        this.timer = timer;
    }
    public HandlerList getHandlers() {
        return TimerExpireEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return TimerExpireEvent.handlers;
    }
}
