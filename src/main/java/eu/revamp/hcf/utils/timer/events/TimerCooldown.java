package eu.revamp.hcf.utils.timer.events;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.timer.Timer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class TimerCooldown
{
    private Timer timer;
    private UUID owner;
    private BukkitTask eventNotificationTask;
    private long expiryMillis;
    private long pauseMillis;

    public TimerCooldown(Timer timer, long duration) {
        this.owner = null;
        this.timer = timer;
        this.setRemaining(duration);
    }

    public TimerCooldown(Timer timer, UUID playerUUID, long duration) {
        this.timer = timer;
        this.owner = playerUUID;
        this.setRemaining(duration);
    }

    public Timer getTimer() {
        return this.timer;
    }

    public long getRemaining() {
        return this.getRemaining(false);
    }

    public void setRemaining(long remaining) {
        this.setExpiryMillis(remaining);
    }

    public long getRemaining(boolean ignorePaused) {
        if (!ignorePaused && this.pauseMillis != 0L) {
            return this.pauseMillis;
        }
        return this.expiryMillis - System.currentTimeMillis();
    }

    public long getExpiryMillis() {
        return this.expiryMillis;
    }

    private void setExpiryMillis(long remainingMillis) {
        long expiryMillis = System.currentTimeMillis() + remainingMillis;
        if (expiryMillis == this.expiryMillis) {
            return;
        }
        this.expiryMillis = expiryMillis;
        if (remainingMillis > 0L) {
            if (this.eventNotificationTask != null) {
                this.eventNotificationTask.cancel();
            }
            this.eventNotificationTask = new BukkitRunnable() {
                public void run() {
                    Bukkit.getPluginManager().callEvent(new TimerExpireEvent(TimerCooldown.this.owner, TimerCooldown.this.timer));
                }
            }.runTaskLater(RevampHCF.getInstance(), remainingMillis / 50L);
        }
    }

    public long getPauseMillis() {
        return this.pauseMillis;
    }

    public void setPauseMillis(long pauseMillis) {
        this.pauseMillis = pauseMillis;
    }

    public boolean isPaused() {
        return this.pauseMillis != 0L;
    }

    public void setPaused(boolean paused) {
        if (paused != this.isPaused()) {
            if (paused) {
                this.pauseMillis = this.getRemaining(true);
                this.cancel();
            }
            else {
                this.setExpiryMillis(this.pauseMillis);
                this.pauseMillis = 0L;
            }
        }
    }

    public boolean cancel() {
        if (this.eventNotificationTask != null) {
            this.eventNotificationTask.cancel();
            return true;
        }
        return false;
    }
}