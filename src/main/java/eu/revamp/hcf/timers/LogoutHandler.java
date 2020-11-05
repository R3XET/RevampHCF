package eu.revamp.hcf.timers;

import lombok.Getter;
import net.mineaus.lunar.LunarClientAPI;
import org.bukkit.Material;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerMoveEvent;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.HashMap;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;
@Getter
public class LogoutHandler extends Handler implements Listener
{
    private HashMap<UUID, LogoutTask> logoutTasks;
    private HashMap<UUID, Long> warmup;
    private ConcurrentHashMap<Player, Integer> teleporting;
    
    public LogoutHandler(RevampHCF plugin) {
        super(plugin);
        this.logoutTasks = new HashMap<>();
        this.warmup = new HashMap<>();
        this.teleporting = new ConcurrentHashMap<>();
    }
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
        this.logoutTasks.clear();
        this.warmup.clear();
    }
    
    public void applyWarmup(Player player) {
        if (LunarClientAPI.getInstance().isAuthenticated(player)) {
            try {
                LunarClientAPI.getInstance().sendCooldown(player, "Logout", Material.FENCE_GATE, RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.LOGOUT"));
            } catch (IOException ignored) { }
        }
        this.warmup.put(player.getUniqueId(), System.currentTimeMillis() + RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.LOGOUT") * 1000);
    }
    
    public boolean isActive(Player player) {
        return this.warmup.containsKey(player.getUniqueId()) && System.currentTimeMillis() < this.warmup.get(player.getUniqueId());
    }
    
    public long getMillisecondsLeft(Player player) {
        if (this.warmup.containsKey(player.getUniqueId())) {
            return Math.max(this.warmup.get(player.getUniqueId()) - System.currentTimeMillis(), 0L);
        }
        return 0L;
    }
    
    public void createLogout(Player player) {
        LogoutTask logoutTask = new LogoutTask(player);
        logoutTask.runTaskLater(RevampHCF.getInstance(), 600L);
        this.applyWarmup(player);
        this.logoutTasks.put(player.getUniqueId(), logoutTask);
    }
    
    public void removeLogout(Player player) {
        if (this.logoutTasks.containsKey(player.getUniqueId())) {
            this.logoutTasks.get(player.getUniqueId()).cancel();
            this.logoutTasks.remove(player.getUniqueId());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getPitch() != to.getPitch() || from.getYaw() != to.getYaw()) return;
        if (this.logoutTasks.containsKey(player.getUniqueId()) && (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ())) {
            this.logoutTasks.get(player.getUniqueId()).cancel();
            this.logoutTasks.remove(player.getUniqueId());
            if (this.teleporting.containsKey(player)) {
                int runnable = this.teleporting.get(player);
                Bukkit.getScheduler().cancelTask(runnable);
                this.teleporting.remove(player);
                if (LunarClientAPI.getInstance().isAuthenticated(player)) {
                    try {
                        LunarClientAPI.getInstance().sendCooldown(player, "Logout", Material.FENCE_GATE, 0);
                    } catch (IOException ignored) { }
                }
                player.sendMessage(CC.translate("&e&lLOGOUT &c&lCANCELLED!"));
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (this.logoutTasks.containsKey(player.getUniqueId())) {
                this.logoutTasks.get(player.getUniqueId()).cancel();
                this.logoutTasks.remove(player.getUniqueId());
            }
            if (this.teleporting.containsKey(player)) {
                int runnable = this.teleporting.get(player);
                Bukkit.getScheduler().cancelTask(runnable);
                this.teleporting.remove(player);
                if (this.logoutTasks.containsKey(player.getUniqueId())) {
                    this.logoutTasks.get(player.getUniqueId()).cancel();
                    this.logoutTasks.remove(player.getUniqueId());
                }
                if (LunarClientAPI.getInstance().isAuthenticated(player)) {
                    try {
                        LunarClientAPI.getInstance().sendCooldown(player, "Logout", Material.FENCE_GATE, 0);
                    } catch (IOException ignored) { }
                }
                player.sendMessage(CC.translate("&e&lLOGOUT &c&lCANCELLED!"));
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (this.logoutTasks.containsKey(player.getUniqueId())) {
            this.logoutTasks.get(player.getUniqueId()).cancel();
            this.logoutTasks.remove(player.getUniqueId());
        }
        if (this.teleporting.containsKey(player)) {
            int runnable = this.teleporting.get(player);
            Bukkit.getScheduler().cancelTask(runnable);
            this.teleporting.remove(player);
            if (this.logoutTasks.containsKey(player.getUniqueId())) {
                this.logoutTasks.get(player.getUniqueId()).cancel();
                this.logoutTasks.remove(player.getUniqueId());
            }
            if (LunarClientAPI.getInstance().isAuthenticated(player)) {
                try {
                    LunarClientAPI.getInstance().sendCooldown(player, "Logout", Material.FENCE_GATE, 0);
                } catch (IOException ignored) { }
            }
            player.sendMessage(CC.translate("&e&lLOGOUT &c&lCANCELLED!"));
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if (this.logoutTasks.containsKey(player.getUniqueId())) {
            this.logoutTasks.get(player.getUniqueId()).cancel();
            this.logoutTasks.remove(player.getUniqueId());
        }
        if (this.teleporting.containsKey(player)) {
            int runnable = this.teleporting.get(player);
            Bukkit.getScheduler().cancelTask(runnable);
            this.teleporting.remove(player);
        }
    }

    public class LogoutTask extends BukkitRunnable
    {
        private Player player;
        
        public LogoutTask(Player player) {
            this.player = player;
        }
        
        public void run() {
            this.player.setMetadata("LogoutCommand", new FixedMetadataValue(RevampHCF.getInstance(), true));
            this.player.kickPlayer(CC.translate("&aYou have been safely logged out from the server!"));
            LogoutHandler.this.logoutTasks.remove(this.player.getUniqueId());
        }
    }
}
