package eu.revamp.hcf.timers;

import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.time.TimeUtils;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import eu.revamp.hcf.RevampHCF;
import java.util.UUID;
import java.util.List;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class SOTWHandler extends Handler implements Listener
{
    @Getter private List<UUID> enabledSotws;
    @Getter public SotwRunnable sotwRunnable;
    public long Sotw;
    @Getter private SOTWTask sotwTask;
    @Getter private SOTWAlertTask sotwAlertTask;

    public SOTWHandler(RevampHCF plugin) {
        super(plugin);
        this.enabledSotws = new ArrayList<>();
        this.sotwTask = null;
        this.sotwAlertTask = null;
    }

    public boolean isSotwEnabled(Player player) {
        return this.getEnabledSotws().contains(player.getUniqueId());
    }

    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && this.isRunning() && !RevampHCF.getInstance().getHandlerManager().getSotwHandler().isSotwEnabled((Player)entity)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        Location loc = new Location(player.getWorld(), 0.0, 80.0, 0.0);
        if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ() || from.getPitch() != to.getPitch() || from.getYaw() != to.getYaw()) return;
        if (this.isRunning() && player.getWorld().getEnvironment() == World.Environment.THE_END && to.getBlockY() <= -15 && !player.isDead()) {
            player.teleport(loc);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!this.isRunning()) return;
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER) {
            event.setCancelled(true);
        }
    }

    public void start(long millis) {
        if (this.sotwRunnable == null) {
            (this.sotwRunnable = new SotwRunnable(this, millis)).runTaskLater(RevampHCF.getInstance(), millis / 50L);
        }
    }

    public void startSOTW(int seconds) {
        SOTWTask task = new SOTWTask(seconds);
        task.runTaskTimerAsynchronously(this.getInstance(), 20L, 20L);
        this.sotwTask = task;
        SOTWAlertTask task2 = new SOTWAlertTask(seconds);
        task2.runTaskTimerAsynchronously(this.getInstance(), 1200L, 1200L);
        this.sotwAlertTask = task2;
    }

    public void stopSOTW() {
        if (this.isRunning()) {
            this.getEnabledSotws().clear();
            this.sotwTask.cancel();
            this.sotwTask = null;
            this.sotwAlertTask.cancel();
            this.sotwAlertTask = null;
        }
    }

    public boolean isRunning() {
        return this.sotwTask != null;
    }

    public boolean cancel() {
        if (this.sotwRunnable != null) {
            this.sotwRunnable.cancel();
            this.sotwRunnable = null;
            return true;
        }
        return false;
    }

    public static class SotwRunnable extends BukkitRunnable
    {
        private SOTWHandler sotwTimer;
        private long startMillis;
        private long endMillis;
        private long Sotw;

        public SotwRunnable(SOTWHandler sotwTimer, long duration) {
            this.sotwTimer = sotwTimer;
            this.startMillis = System.currentTimeMillis();
            this.endMillis = this.startMillis + duration;
            this.Sotw = 0L;
        }

        public long getRemaining() {
            return this.endMillis - System.currentTimeMillis();
        }

        public boolean isSOTW() {
            return System.currentTimeMillis() < this.Sotw;
        }

        public long getSotwMillisecondsLeft() {
            return Math.max(this.Sotw - System.currentTimeMillis(), 0L);
        }

        public void run() {
            Bukkit.broadcastMessage(CC.translate("&7&m--------------------------------"));
            Bukkit.broadcastMessage(CC.translate("&a&lSOTW &4&lhas ended!"));
            Bukkit.broadcastMessage(CC.translate("&4&lGood Lock everyone!"));
            Bukkit.broadcastMessage(CC.translate("&7&m--------------------------------"));
            RevampHCF.getInstance().getHandlerManager().getSotwHandler().getEnabledSotws().clear();
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.playSound(online.getLocation(), Sound.ENDERMAN_DEATH, 5.0f, 5.0f);
            }
            this.cancel();
            this.sotwTimer.sotwRunnable = null;
        }
    }

    public class SOTWTask extends BukkitRunnable
    {
        @Getter public int time;

        public SOTWTask(int time) {
            this.time = time;
        }

        public void run() {
            if (this.time > 0) {
                --this.time;
            }
            else {
                this.cancel();
                sotwTask = null;
            }
        }
    }

    public class SOTWAlertTask extends BukkitRunnable
    {
        @Getter private int time;
        
        public SOTWAlertTask(int time) {
            this.time = time;
        }
        
        public void run() {
            if (SOTWHandler.this.isRunning()) {
                Bukkit.broadcastMessage("§f§m-------------------------------");
                Bukkit.broadcastMessage("§a§lSOTW§7:");
                Bukkit.broadcastMessage(" §7§ §c" + TimeUtils.formatMinutes(SOTWHandler.this.getSotwTask().getTime()) + " Minutes Left");
                Bukkit.broadcastMessage("§f§m-------------------------------");
            }
            else {
                this.cancel();
                sotwAlertTask = null;
            }
        }
    }
}
