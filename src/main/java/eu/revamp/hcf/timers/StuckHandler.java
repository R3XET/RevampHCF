package eu.revamp.hcf.timers;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.FactionManager;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.hcf.utils.timer.PlayerTimer;
import eu.revamp.hcf.utils.timer.events.TimerCooldown;
import eu.revamp.spigot.utils.world.WorldUtils;
import lombok.NonNull;
import net.mineaus.lunar.LunarClientAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class StuckHandler extends PlayerTimer implements Listener
{
    public static int MAX_MOVE_DISTANCE = RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.STUCK_MAX_BLOCKS_MOVE_DISTANCE");
    private Map<UUID, Location> startedLocations;
    
    public StuckHandler() {
        super("Stuck", TimeUnit.MINUTES.toMillis((long) RevampHCF.getInstance().getConfig().getDouble("COOLDOWNS.STUCK")));
        this.startedLocations = new HashMap<>();
    }
    
    @Override
    public TimerCooldown clearCooldown(UUID uuid) {
        TimerCooldown runnable = super.clearCooldown(uuid);
        if (runnable != null) {
            this.startedLocations.remove(uuid);
            return runnable;
        }
        return null;
    }
    
    @Override
    public boolean setCooldown(Player player, UUID playerUUID, long millis, boolean force) {
        if (player != null && super.setCooldown(player, playerUUID, millis, force)) {
            if (LunarClientAPI.getInstance().isAuthenticated(player)) {
                try {
                    LunarClientAPI.getInstance().sendCooldown(player, "Stuck", Material.BIRCH_DOOR, RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.STUCK") * 60);
                } catch (IOException ignored) { }
            }
            this.startedLocations.put(playerUUID, player.getLocation());
            return true;
        }
        return false;
    }
    
    private void checkMovement(Player player, Location from, Location to) {
        UUID uuid = player.getUniqueId();
        if (this.getRemaining(uuid) > 0L) {
            if (from == null) {
                this.clearCooldown(uuid);
                return;
            }
            int xDiff = Math.abs(from.getBlockX() - to.getBlockX());
            int yDiff = Math.abs(from.getBlockY() - to.getBlockY());
            int zDiff = Math.abs(from.getBlockZ() - to.getBlockZ());
            if (xDiff > MAX_MOVE_DISTANCE || yDiff > MAX_MOVE_DISTANCE || zDiff > MAX_MOVE_DISTANCE) {
                this.clearCooldown(uuid);
                if (LunarClientAPI.getInstance().isAuthenticated(player)) {
                    try {
                        LunarClientAPI.getInstance().sendCooldown(player, "Stuck", Material.BIRCH_DOOR, 0);
                    } catch (IOException ignored) { }
                }
                player.sendMessage(Language.STUCK_MOVED.toString().replace("%distance%", String.valueOf(MAX_MOVE_DISTANCE)));
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (this.getRemaining(uuid) > 0L) {
            Location from = this.startedLocations.get(uuid);
            this.checkMovement(player, from, event.getTo());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (this.getRemaining(uuid) > 0L) {
            Location from = this.startedLocations.get(uuid);
            this.checkMovement(player, from, event.getTo());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (this.getRemaining(event.getPlayer().getUniqueId()) > 0L) {
            this.clearCooldown(uuid);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (this.getRemaining(event.getPlayer().getUniqueId()) > 0L) {
            this.clearCooldown(uuid);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (this.getRemaining(player) > 0L) {
                if (LunarClientAPI.getInstance().isAuthenticated(player)) {
                    try {
                        LunarClientAPI.getInstance().sendCooldown(player, "Stuck", Material.BIRCH_DOOR, 0);
                    } catch (IOException ignored) { }
                }
                player.sendMessage(Language.STUCK_DAMAGED.toString());
                this.clearCooldown(player);
            }
        }
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onExpire(UUID userUUID) {
        Player player = Bukkit.getPlayer(userUUID);
        if (player == null) return;
        FactionManager factionManager = RevampHCF.getInstance().getFactionManager();
        PlayerFaction faction = factionManager.getPlayerFaction(player);
        Location loc;
        Location spawn = WorldUtils.destringifyLocation(RevampHCF.getInstance().getLocation().getString("World-Spawn.world-spawn"));
        if (faction == null) {
            loc = spawn.clone().add(0.5, 0.0, 0.5);
        }
        else if (faction.getHome() == null) {
            loc = spawn.clone().add(0.5, 0.0, 0.5);
        }
        else {
            loc = faction.getHome();
        }
        if (player.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN)) {
            player.sendMessage(Language.STUCK_TELEPORTED.toString());
        }
    }
}
