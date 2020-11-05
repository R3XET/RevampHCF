package eu.revamp.hcf.timers;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.time.TimeUtils;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class SaleOFFHandler extends Handler implements Listener
{
    @Getter private saleoffRunnable saleoffRunnable;
    public long saleOff;
    @Getter private saleoffTask saleoffTask;
    @Getter private saleoffAlertTask saleoffAlertTask;
    
    public SaleOFFHandler(RevampHCF plugin) {
        super(plugin);
        this.saleoffTask = null;
        this.saleoffAlertTask = null;
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    public void start(long millis) {
        if (this.saleoffRunnable == null) {
            (this.saleoffRunnable = new saleoffRunnable(this, millis)).runTaskLater(RevampHCF.getInstance(), millis / 50L);
        }
    }
    
    public void startSaleOff(int seconds) {
        saleoffTask task = new saleoffTask(seconds);
        task.runTaskTimerAsynchronously(this.getInstance(), 20L, 20L);
        this.saleoffTask = task;
        saleoffAlertTask task2 = new saleoffAlertTask(seconds);
        task2.runTaskTimerAsynchronously(this.getInstance(), 1200L, 1200L);
        this.saleoffAlertTask = task2;
    }
    
    public void stopSaleOff() {
        if (this.isRunning()) {
            this.saleoffTask.cancel();
            this.saleoffTask = null;
            this.saleoffAlertTask.cancel();
            this.saleoffAlertTask = null;
        }
    }

    public boolean isRunning() {
        return this.saleoffTask != null;
    }
    
    public boolean cancel() {
        if (this.saleoffRunnable != null) {
            this.saleoffRunnable.cancel();
            this.saleoffRunnable = null;
            return true;
        }
        return false;
    }

    public class saleoffRunnable extends BukkitRunnable
    {
        private SaleOFFHandler saleOffTimer;
        private long startMillis;
        private long endMillis;
        private long saleoff;
        
        public saleoffRunnable(SaleOFFHandler saleOffTimer, long duration) {
            this.saleOffTimer = saleOffTimer;
            this.startMillis = System.currentTimeMillis();
            this.endMillis = this.startMillis + duration;
            this.saleoff = 0L;
        }
        
        public long getRemaining() {
            return this.endMillis - System.currentTimeMillis();
        }
        
        public boolean issaleoff() {
            return System.currentTimeMillis() < this.saleoff;
        }
        
        public long getsaleoffMillisecondsLeft() {
            return Math.max(this.saleoff - System.currentTimeMillis(), 0L);
        }
        
        public void run() {
            for (String str : RevampHCF.getInstance().getLanguage().getStringList("SALEOFF.ENDED")) {
                Bukkit.broadcastMessage(CC.translate(str));
            }
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.playSound(online.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 1.0f);
            }
            this.cancel();
            saleoffRunnable = null;
        }
    }
    
    public class saleoffTask extends BukkitRunnable
    {
        @Getter private int time;
        
        public saleoffTask(int time) {
            this.time = time;
        }
        
        public void run() {
            if (this.time > 0) {
                --this.time;
            }
            else {
                this.cancel();
                saleoffRunnable = null;
            }
        }
    }
    
    public class saleoffAlertTask extends BukkitRunnable
    {
        @Getter private int time;
        
        public saleoffAlertTask(int time) {
            this.time = time;
        }
        
        public void run() {
            if (SaleOFFHandler.this.isRunning()) {
                for (String str : RevampHCF.getInstance().getLanguage().getStringList("SALEOFF.RUNNING")) {
                    Bukkit.broadcastMessage(CC.translate(str).replaceAll("%time%", TimeUtils.formatMinutes(SaleOFFHandler.this.getSaleoffTask().getTime())));
                }
            }
            else {
                this.cancel();
                saleoffRunnable = null;
            }
        }
    }
}
