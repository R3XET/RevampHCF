package eu.revamp.hcf.utils.timer.events;

import eu.revamp.hcf.utils.timer.Timer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class TimerStartEvent extends Event
{
    private static HandlerList handlers = new HandlerList();
    @Getter private Optional<Player> player;
    @Getter private Optional<UUID> userUUID;
    @Getter private Timer timer;
    @Getter private long duration;

    public TimerStartEvent(Timer timer, long duration) {
        this.player = Optional.empty();
        this.userUUID = Optional.empty();
        this.timer = timer;
        this.duration = duration;
    }
    
    public TimerStartEvent(@Nullable Player player, UUID uniqueId, Timer timer, long duration) {
        this.player = Optional.ofNullable(player);
        this.userUUID = Optional.ofNullable(uniqueId);
        this.timer = timer;
        this.duration = duration;
    }
    public HandlerList getHandlers() {
        return TimerStartEvent.handlers;
    }
    public static HandlerList getHandlerList() {
        return TimerStartEvent.handlers;
    }
}
