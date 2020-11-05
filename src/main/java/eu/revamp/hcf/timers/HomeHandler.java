package eu.revamp.hcf.timers;

import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.FactionManager;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Collection;

import org.bukkit.entity.Entity;
import eu.revamp.hcf.utils.timer.events.TimerCooldown;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.Location;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.timer.PlayerTimer;

public class HomeHandler extends PlayerTimer implements Listener
{
    private Map<UUID, Location> destinationMap;
    private RevampHCF plugin;
    
    public HomeHandler(RevampHCF plugin) {
        super("Home", TimeUnit.SECONDS.toMillis(RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.HOME")));
        this.destinationMap = new HashMap<>();
        this.plugin = plugin;
    }
    
    public Location getDestination(Player player) {
        return this.destinationMap.get(player.getUniqueId());
    }
    
    @Override
    public TimerCooldown clearCooldown(UUID uuid) {
        TimerCooldown runnable = super.clearCooldown(uuid);
        if (runnable != null) {
            this.destinationMap.remove(uuid);
            return runnable;
        }
        return null;
    }
    @SuppressWarnings("deprecation")
    public int getNearbyEnemies(Player player, int distance) {
        FactionManager factionManager = this.plugin.getFactionManager();
        Faction playerFaction = factionManager.getPlayerFaction(player.getUniqueId());
        int count = 0;
        Collection<Entity> nearby = player.getNearbyEntities(distance, distance, distance);
        for (Entity entity : nearby) {
            if (entity instanceof Player) {
                Player target = (Player)entity;
                if (!target.canSee(player)) {
                    continue;
                }
                if (!player.canSee(target)) {
                    continue;
                }
                if (playerFaction != null && factionManager.getPlayerFaction(target) == playerFaction) {
                    continue;
                }
                ++count;
            }
        }
        return count;
    }
    
    public boolean teleport(Player player, Location location, long millis, PlayerTeleportEvent.TeleportCause cause) {
        this.cancelTeleport(player, null);
        boolean result;
        if (millis <= 0L) {
            result = player.teleport(location, cause);
            this.clearCooldown(player.getUniqueId());
        }
        else {
            UUID uuid = player.getUniqueId();
            this.destinationMap.put(uuid, location.clone());
            this.setCooldown(player, uuid, millis, true);
            result = true;
        }
        return result;
    }
    
    public void cancelTeleport(Player player, String reason) {
        UUID uuid = player.getUniqueId();
        if (this.getRemaining(uuid) > 0L) {
            this.clearCooldown(uuid);
            if (reason != null && !reason.isEmpty()) {
                player.sendMessage(reason);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) return;
        this.cancelTeleport(event.getPlayer(), CC.translate("&cYou moved a block, therefore cancelling your teleport."));
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            this.cancelTeleport((Player)entity, CC.translate("&cYou took damage, teleportation cancelled!"));
        }
    }
    
    @Override
    public void onExpire(UUID userUUID) {
        Player player = Bukkit.getPlayer(userUUID);
        if (player == null) return;
        Location destination = this.destinationMap.remove(userUUID);
        if (destination != null) {
            destination.getChunk();
            player.teleport(destination, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
    }
}
