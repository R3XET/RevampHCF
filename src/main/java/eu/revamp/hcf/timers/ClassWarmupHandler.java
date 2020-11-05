package eu.revamp.hcf.timers;

import java.util.Collection;

import eu.revamp.hcf.classes.utils.ArmorClass;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import com.google.common.base.Preconditions;
import eu.revamp.hcf.utils.timer.events.TimerCooldown;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.timer.PlayerTimer;

public class ClassWarmupHandler extends PlayerTimer implements Listener
{
    protected Map<UUID, ArmorClass> classWarmups;
    private RevampHCF plugin;

    public ClassWarmupHandler(RevampHCF plugin) {
        super("Class Warmup", TimeUnit.SECONDS.toMillis(RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.CLASS_WARMUP")));
        this.classWarmups = new HashMap<>();
        this.plugin = plugin;
        new BukkitRunnable() {
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    ClassWarmupHandler.this.attemptEquip(player);
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public void disable() {
        if (!this.classWarmups.isEmpty()) {
            this.classWarmups.clear();
        }
    }

    @Override
    public TimerCooldown clearCooldown(UUID playerUUID) {
        TimerCooldown runnable = super.clearCooldown(playerUUID);
        if (runnable != null) {
            this.classWarmups.remove(playerUUID);
            return runnable;
        }
        return null;
    }

    @Override
    public void onExpire(UUID userUUID) {
        Player player = Bukkit.getPlayer(userUUID);
        if (player == null) {
            return;
        }
        ArmorClass pvpClass = this.classWarmups.remove(userUUID);
        Preconditions.checkNotNull((Object)pvpClass, "Attempted to equip a class for %s, but nothing was added", player.getName());
        this.plugin.getHandlerManager().getArmorClassManager().setEquippedClass(player, pvpClass);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerQuitEvent event) {
        this.plugin.getHandlerManager().getArmorClassManager().setEquippedClass(event.getPlayer(), null);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.attemptEquip(event.getPlayer());
    }

    //@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEquipmentSet(Player player) {
        HumanEntity humanEntity = player.getPlayer();
        if (humanEntity instanceof Player) {
            this.attemptEquip((Player)humanEntity);
        }
    }

    private void attemptEquip(Player player) {
        ArmorClass current = this.plugin.getHandlerManager().getArmorClassManager().getEquippedClass(player);
        if (current != null) {
            if (current.isApplicableFor(player)) {
                return;
            }
            this.plugin.getHandlerManager().getArmorClassManager().setEquippedClass(player, null);
        }
        else if ((current = this.classWarmups.get(player.getUniqueId())) != null) {
            if (current.isApplicableFor(player)) {
                return;
            }
            this.clearCooldown(player.getUniqueId());
        }
        Collection<ArmorClass> pvpClasses = this.plugin.getHandlerManager().getArmorClassManager().getPvpClasses();
        for (ArmorClass pvpClass : pvpClasses) {
            if (pvpClass.isApplicableFor(player)) {
                this.classWarmups.put(player.getUniqueId(), pvpClass);
                this.setCooldown(player, player.getUniqueId(), pvpClass.getWarmupDelay(), false);
                break;
            }
        }
    }
}
