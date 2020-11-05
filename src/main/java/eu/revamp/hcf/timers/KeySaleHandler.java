package eu.revamp.hcf.timers;

import eu.revamp.spigot.utils.time.TimeUtils;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.scheduler.BukkitRunnable;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class KeySaleHandler extends Handler implements Listener
{
    @Getter private keysaleRunnable keysaleRunnable;
    public long keysale;
    @Getter private keysaleTask keysaleTask;
    @Getter private keysaleAlertTask keysaleAlertTask;
    
    public KeySaleHandler(RevampHCF plugin) {
        super(plugin);
        this.keysaleTask = null;
        this.keysaleAlertTask = null;
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    public void start(long millis) {
        if (this.keysaleRunnable == null) {
            (this.keysaleRunnable = new keysaleRunnable(this, millis)).runTaskLater(RevampHCF.getInstance(), millis / 50L);
        }
    }
    
    public void startkeysale(int seconds) {
        final keysaleTask task = new keysaleTask(seconds);
        task.runTaskTimerAsynchronously(this.getInstance(), 20L, 20L);
        this.keysaleTask = task;
        final keysaleAlertTask task2 = new keysaleAlertTask(seconds);
        task2.runTaskTimerAsynchronously(this.getInstance(), 1200L, 1200L);
        this.keysaleAlertTask = task2;
    }
    
    public void stopkeysale() {
        if (this.isRunning()) {
            this.keysaleTask.cancel();
            this.keysaleTask = null;
            this.keysaleAlertTask.cancel();
            this.keysaleAlertTask = null;
        }
    }

    public boolean isRunning() {
        return this.keysaleTask != null;
    }
    
    public boolean cancel() {
        if (this.keysaleRunnable != null) {
            this.keysaleRunnable.cancel();
            this.keysaleRunnable = null;
            return true;
        }
        return false;
    }

    public class keysaleRunnable extends BukkitRunnable
    {
        private KeySaleHandler keysaleTimer;
        private long startMillis;
        private long endMillis;
        private long keysale;
        
        public keysaleRunnable(KeySaleHandler keysaleTimer, final long duration) {
            this.keysaleTimer = keysaleTimer;
            this.startMillis = System.currentTimeMillis();
            this.endMillis = this.startMillis + duration;
            this.keysale = 0L;
        }
        
        public long getRemaining() {
            return this.endMillis - System.currentTimeMillis();
        }
        
        public boolean iskeysale() {
            return System.currentTimeMillis() < this.keysale;
        }
        
        public long getkeysaleMillisecondsLeft() {
            return Math.max(this.keysale - System.currentTimeMillis(), 0L);
        }
        
        public void run() {
            for (String str : RevampHCF.getInstance().getLanguage().getStringList("KEYSALE.ENDED")) {
                Bukkit.broadcastMessage(CC.translate(str));
            }
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.playSound(online.getLocation(), Sound.CAT_MEOW, 1.0f, 1.0f);
            }
            this.cancel();
            keysaleRunnable = null;
        }
    }
    
    public class keysaleTask extends BukkitRunnable
    {
        @Getter private int time;
        
        public keysaleTask(int time) {
            this.time = time;
        }
        
        public void run() {
            if (this.time > 0) {
                --this.time;
            }
            else {
                this.cancel();
                keysaleTask = null;
            }
        }
    }
    
    public class keysaleAlertTask extends BukkitRunnable
    {
        @Getter private int time;
        
        public keysaleAlertTask(int time) {
            this.time = time;
        }
        
        public void run() {
            if (KeySaleHandler.this.isRunning()) {
                for (String str : RevampHCF.getInstance().getLanguage().getStringList("KEYSALE.RUNNING")) {
                    Bukkit.broadcastMessage(CC.translate(str).replaceAll("%time%", TimeUtils.formatMinutes(KeySaleHandler.this.getKeysaleTask().getTime())));
                }
            } else {
                this.cancel();
                keysaleAlertTask = null;
            }
        }
    }
}
