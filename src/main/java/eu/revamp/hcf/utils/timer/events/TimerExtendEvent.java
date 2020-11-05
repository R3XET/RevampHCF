package eu.revamp.hcf.utils.timer.events;

import eu.revamp.hcf.utils.timer.Timer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@Getter
public class TimerExtendEvent extends Event implements Cancellable
{
    private static HandlerList handlers = new HandlerList();
    private Optional<Player> player;
    private Optional<UUID> userUUID;
    private Timer timer;
    @Setter private long previousDuration;
    @Setter private boolean cancelled;
    private long newDuration;

    public TimerExtendEvent(Timer timer, long previousDuration, long newDuration) {
        this.player = Optional.empty();
        this.userUUID = Optional.empty();
        this.timer = timer;
        this.previousDuration = previousDuration;
        this.newDuration = newDuration;
    }
    
    public TimerExtendEvent(@Nullable Player player, UUID uniqueId, Timer timer, long previousDuration, long newDuration) {
        this.player = Optional.ofNullable(player);
        this.userUUID = Optional.ofNullable(uniqueId);
        this.timer = timer;
        this.previousDuration = previousDuration;
        this.newDuration = newDuration;
    }
    @Override
    public HandlerList getHandlers() {
        return TimerExtendEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return TimerExtendEvent.handlers;
    }
}
