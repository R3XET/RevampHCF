package eu.revamp.hcf.timers;

import eu.revamp.hcf.RevampHCF;
import lombok.Getter;
import eu.revamp.hcf.utils.Handler;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
@Getter
public class GlowstoneHandler extends Handler implements Listener
{
    private glowstoneRunnable glowstoneRunnable;
    public long glowstone;
    private glowstoneTask glowstoneTask;
    private glowstoneAlertTask glowstoneAlertTask;
    
    public GlowstoneHandler(RevampHCF plugin) {
        super(plugin);
        this.glowstoneTask = null;
        this.glowstoneAlertTask = null;
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    public void start(long millis) {
        if (this.glowstoneRunnable == null) {
            (this.glowstoneRunnable = new glowstoneRunnable(this, millis)).runTaskLater(RevampHCF.getInstance(), millis / 50L);
        }
    }
    
    public void startglowstone(int seconds) {
        glowstoneTask task = new glowstoneTask(seconds);
        task.runTaskTimerAsynchronously(this.getInstance(), 20L, 20L);
        this.glowstoneTask = task;
        glowstoneAlertTask task2 = new glowstoneAlertTask(seconds);
        task2.runTaskTimerAsynchronously(this.getInstance(), 1200L, 1200L);
        this.glowstoneAlertTask = task2;
    }
    
    public void stopglowstone() {
        if (this.isRunning()) {
            this.glowstoneTask.cancel();
            this.glowstoneTask = null;
            this.glowstoneAlertTask.cancel();
            this.glowstoneAlertTask = null;
        }
    }

    public boolean isRunning() {
        return this.glowstoneTask != null;
    }
    
    public boolean cancel() {
        if (this.glowstoneRunnable != null) {
            this.glowstoneRunnable.cancel();
            this.glowstoneRunnable = null;
            return true;
        }
        return false;
    }

    public class glowstoneRunnable extends BukkitRunnable
    {
        private GlowstoneHandler glowstoneTimer;
        private long startMillis;
        private long endMillis;
        private long glowstone;
        
        public glowstoneRunnable(GlowstoneHandler glowstoneTimer, long duration) {
            this.glowstoneTimer = glowstoneTimer;
            this.startMillis = System.currentTimeMillis();
            this.endMillis = this.startMillis + duration;
            this.glowstone = 0L;
        }
        
        public long getRemaining() {
            return this.endMillis - System.currentTimeMillis();
        }
        
        public boolean isglowstone() {
            return System.currentTimeMillis() < this.glowstone;
        }
        
        public long getglowstoneMillisecondsLeft() {
            return Math.max(this.glowstone - System.currentTimeMillis(), 0L);
        }
        
        public void run() {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.playSound(online.getLocation(), Sound.BLAZE_HIT, 1.0f, 1.0f);
            }
            this.cancel();
            glowstoneRunnable = null;
        }
    }
    
    public class glowstoneTask extends BukkitRunnable
    {
        private int time;
        
        public glowstoneTask(int time) {
            this.time = time;
        }
        
        public void run() {
            if (this.time > 0) {
                --this.time;
            }
            else {
                this.cancel();
                glowstoneTask = null;
            }
        }
    }
    
    public class glowstoneAlertTask extends BukkitRunnable
    {
        private int time;
        
        public glowstoneAlertTask(int time) {
            this.time = time;
        }
        
        public void run() {
            if (!GlowstoneHandler.this.isRunning()) {
                this.cancel();
                glowstoneTask = null;
            }
        }
    }
}
