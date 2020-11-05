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

public class RebootHandler extends Handler implements Listener
{
    @Getter private rebootRunnable rebootRunnable;
    @Getter private rebootTask rebootTask;
    @Getter private rebootAlertTask rebootAlertTask;

    public RebootHandler(RevampHCF plugin) {
        super(plugin);
        this.rebootTask = null;
        this.rebootAlertTask = null;
    }

    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    public void start(long millis) {
        if (this.rebootRunnable == null) {
            (this.rebootRunnable = new rebootRunnable(this, millis)).runTaskLater(RevampHCF.getInstance(), millis / 50L);
        }
    }

    public void startReboot(int seconds) {
        rebootTask task = new rebootTask(seconds);
        task.runTaskTimerAsynchronously(this.getInstance(), 20L, 20L);
        this.rebootTask = task;
        rebootAlertTask task2 = new rebootAlertTask(seconds);
        task2.runTaskTimerAsynchronously(this.getInstance(), 1200L, 1200L);
        this.rebootAlertTask = task2;
    }

    public void stopReboot() {
        if (this.isRunning()) {
            this.rebootTask.cancel();
            this.rebootTask = null;
            this.rebootAlertTask.cancel();
            this.rebootAlertTask = null;
        }
    }

    public boolean isRunning() {
        return this.rebootTask != null;
    }

    public boolean cancel() {
        if (this.rebootRunnable != null) {
            this.rebootRunnable.cancel();
            this.rebootRunnable = null;
            return true;
        }
        return false;
    }

    public class rebootRunnable extends BukkitRunnable
    {
        private RebootHandler rebootTimer;
        private long startMillis;
        private long endMillis;
        private long reboot;

        public rebootRunnable(RebootHandler rebootTimer, long duration) {
            this.rebootTimer = rebootTimer;
            this.startMillis = System.currentTimeMillis();
            this.endMillis = this.startMillis + duration;
            this.reboot = 0L;
        }

        public long getRemaining() {
            return this.endMillis - System.currentTimeMillis();
        }

        public boolean isreboot() {
            return System.currentTimeMillis() < this.reboot;
        }

        public long getrebootMillisecondsLeft() {
            return Math.max(this.reboot - System.currentTimeMillis(), 0L);
        }

        public void run() {
            Bukkit.broadcastMessage(CC.translate("&7&m------------------"));
            Bukkit.broadcastMessage(CC.translate("&c&lRebooting Server!"));
            Bukkit.broadcastMessage(CC.translate("&7&m------------------"));
            Bukkit.shutdown();
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.playSound(online.getLocation(), Sound.PORTAL_TRAVEL, 3.0f, 3.0f);
            }
            this.cancel();
            rebootRunnable = null;
        }
    }

    public class rebootTask extends BukkitRunnable
    {
        @Getter private int time;

        public rebootTask(int time) {
            this.time = time;
        }

        public void run() {
            if (this.time > 0) {
                --this.time;
            }
            else {
                this.cancel();
                rebootTask = null;
            }
        }
    }

    public class rebootAlertTask extends BukkitRunnable
    {
        @Getter private int time;
        
        public rebootAlertTask(int time) {
            this.time = time;
        }
        
        public void run() {
            if (RebootHandler.this.isRunning()) {
                Bukkit.broadcastMessage(CC.translate("&f&m-------------------------------"));
                Bukkit.broadcastMessage(CC.translate("&a&lReboot Started&7:"));
                Bukkit.broadcastMessage(CC.translate(" &7& &c" + TimeUtils.formatMinutes(RebootHandler.this.getRebootTask().getTime()) + " Minutes Left"));
                Bukkit.broadcastMessage(CC.translate("&f&m-------------------------------"));
            }
            else {
                this.cancel();
                rebootAlertTask = null;
            }
        }
    }
}
