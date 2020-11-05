package eu.revamp.hcf.utils.timer.events;

import lombok.Getter;
import eu.revamp.hcf.utils.timer.Timer;
import java.util.UUID;
import com.google.common.base.Optional;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class TimerClearEvent extends Event
{
    private static HandlerList handlers = new HandlerList();
    @Getter private Optional<UUID> userUUID;
    @Getter private Timer timer;

    public TimerClearEvent(Timer timer) {
        this.userUUID = Optional.absent();
        this.timer = timer;
    }
    
    public TimerClearEvent(UUID userUUID, Timer timer) {
        this.userUUID = Optional.of(userUUID);
        this.timer = timer;
    }
    public HandlerList getHandlers() {
        return TimerClearEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return TimerClearEvent.handlers;
    }
}
