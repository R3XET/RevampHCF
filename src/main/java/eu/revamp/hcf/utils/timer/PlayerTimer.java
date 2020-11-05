package eu.revamp.hcf.utils.timer;

import eu.revamp.hcf.utils.timer.events.TimerExtendEvent;
import eu.revamp.hcf.utils.timer.events.TimerStartEvent;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import eu.revamp.hcf.utils.timer.events.TimerPauseEvent;
import eu.revamp.hcf.utils.timer.events.TimerClearEvent;
import org.bukkit.Bukkit;
import com.google.common.base.Optional;
import eu.revamp.hcf.utils.timer.events.TimerExpireEvent;
import eu.revamp.hcf.utils.timer.events.TimerCooldown;
import java.util.UUID;
import java.util.Map;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.concurrent.ConcurrentHashMap;

public abstract class PlayerTimer extends Timer
{
    private ConcurrentHashMap<Player, ArrayList<Timer>> timers;
    public boolean persistable;
    public Map<UUID, TimerCooldown> cooldowns;
    
    public PlayerTimer(String name, long defaultCooldown) {
        this(name, defaultCooldown, true);
    }
    
    public PlayerTimer(String name, long defaultCooldown, boolean persistable) {
        super(name, defaultCooldown);
        this.cooldowns = new ConcurrentHashMap<>();
        this.persistable = persistable;
    }
    
    public void onExpire(UUID userUUID) {
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onTimerExpireLoadReduce(TimerExpireEvent event) {
        if (event.getTimer() == this) {
            Optional<UUID> optionalUserUUID = event.getUserUUID();
            if (optionalUserUUID.isPresent()) {
                UUID userUUID = optionalUserUUID.get();
                this.onExpire(userUUID);
                this.clearCooldown(userUUID);
            }
        }
    }
    
    public void clearCooldown(Player player) {
        this.clearCooldown(player.getUniqueId());
    }
    
    public TimerCooldown clearCooldown(UUID playerUUID) {
        TimerCooldown runnable = this.cooldowns.remove(playerUUID);
        if (runnable != null) {
            runnable.cancel();
            Bukkit.getPluginManager().callEvent(new TimerClearEvent(playerUUID, this));
            return runnable;
        }
        return null;
    }
    
    public boolean hasTimer(Player player, Timer timerType) {
        if (this.timers.get(player) == null) {
            return false;
        }
        for (Timer timer : this.timers.get(player)) {
            if (timer.getName().equals(timerType)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isPaused(Player player) {
        return this.isPaused(player.getUniqueId());
    }
    
    public boolean isPaused(UUID playerUUID) {
        TimerCooldown runnable = this.cooldowns.get(playerUUID);
        return runnable != null && runnable.isPaused();
    }
    
    public void setPaused(UUID playerUUID, boolean paused) {
        TimerCooldown runnable = this.cooldowns.get(playerUUID);
        if (runnable != null && runnable.isPaused() != paused) {
            TimerPauseEvent event = new TimerPauseEvent(playerUUID, this, paused);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                runnable.setPaused(paused);
            }
        }
    }
    
    public boolean hasCooldown(UUID userUUID) {
        return this.getRemaining(userUUID) > 0L;
    }
    
    public boolean hasCooldown(Player player) {
        return this.getRemaining(player) > 0L;
    }
    
    public long getRemaining(Player player) {
        return this.getRemaining(player.getUniqueId());
    }
    
    public long getRemaining(UUID playerUUID) {
        TimerCooldown runnable = this.cooldowns.get(playerUUID);
        return (runnable == null) ? 0L : runnable.getRemaining();
    }
    
    public boolean setCooldown(@Nullable Player player, UUID playerUUID) {
        return this.setCooldown(player, playerUUID, this.defaultCooldown, false);
    }
    
    public boolean setCooldown(@Nullable Player player, UUID playerUUID, long duration, boolean overwrite) {
        return this.setCooldown(player, playerUUID, duration, overwrite, null);
    }
    
    public boolean setCooldown(@Nullable Player player, UUID playerUUID, long duration, boolean overwrite, @Nullable Predicate<Long> currentCooldownPredicate) {
        TimerCooldown runnable = (duration > 0L) ? this.cooldowns.get(playerUUID) : this.clearCooldown(playerUUID);
        if (runnable == null) {
            Bukkit.getPluginManager().callEvent(new TimerStartEvent(player, playerUUID, this, duration));
            runnable = new TimerCooldown(this, playerUUID, duration);
            this.cooldowns.put(playerUUID, runnable);
            return true;
        }
        long remaining = runnable.getRemaining();
        if (!overwrite && remaining > 0L && duration <= remaining) {
            return false;
        }
        TimerExtendEvent event = new TimerExtendEvent(player, playerUUID, this, remaining, duration);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        boolean flag = true;
        if (currentCooldownPredicate != null) {
            flag = currentCooldownPredicate.apply(remaining);
        }
        if (flag) {
            runnable.setRemaining(duration);
        }
        return flag;
    }
}
