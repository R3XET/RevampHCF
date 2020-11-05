package eu.revamp.hcf.timers;

import com.google.common.base.Optional;
import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.events.FactionPlayerClaimEnterEvent;
import eu.revamp.hcf.factions.events.FactionPlayerJoinEvent;
import eu.revamp.hcf.factions.events.FactionPlayerLeaveEvent;
import eu.revamp.hcf.factions.type.SpawnFaction;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.utils.timer.PlayerTimer;
import eu.revamp.hcf.utils.timer.events.TimerClearEvent;
import eu.revamp.hcf.utils.timer.events.TimerStartEvent;
import eu.revamp.hcf.visualise.VisualType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SpawnTagHandler extends PlayerTimer implements Listener
{
    public SpawnTagHandler(RevampHCF plugin) {
        super("Spawn Tag", TimeUnit.SECONDS.toMillis(RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.SPAWN_TAG")));
    }
    
    public long getTime() {
        return this.defaultCooldown;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onTimerStop(TimerClearEvent event) {
        if (event.getTimer() == this) {
            Optional<UUID> optionalUserUUID = event.getUserUUID();
            if (optionalUserUUID.isPresent()) {
                this.onExpire(optionalUserUUID.get());
            }
        }
    }
    
    @Override
    public void onExpire(UUID userUUID) {
        Player player = Bukkit.getPlayer(userUUID);
        if (player != null) {
            HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
            data.setSpawnTagCooldown(0L);
            RevampHCF.getInstance().getHandlerManager().getVisualiseHandler().clearVisualBlocks(player, VisualType.SPAWN_BORDER, null);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onFactionJoin(FactionPlayerJoinEvent event) {
        final Optional<Player> optional = event.getPlayer();
        if (optional.isPresent()) {
            Player player = optional.get();
            long remaining = this.getRemaining(player);
            HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
            if (remaining > 0L && data.getSpawnTagCooldown() > 0L) {
                event.setCancelled(true);
                player.sendMessage(Language.COMBAT_JOIN_TAGGED.toString());
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onFactionLeave(FactionPlayerLeaveEvent event) {
        if (event.isForce()) return;
        Optional<Player> optional = event.getPlayer();
        if (optional.isPresent()) {
            Player player = optional.get();
            long remaining = this.getRemaining(player);
            HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
            if (remaining > 0L && data.getSpawnTagCooldown() > 0L) {
                event.setCancelled(true);
                CommandSender sender = event.getSender();
                if (sender == player) {
                    sender.sendMessage(Language.COMBAT_KICK_TAGGED.toString().replace("%player%", player.getName()));
                }
                else {
                    sender.sendMessage(Language.COMBAT_LEAVE_TAGGED.toString());
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerClaimEnterMonitor(FactionPlayerClaimEnterEvent event) {
        Player player = event.getPlayer();
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        boolean flag = this.getRemaining(player.getUniqueId()) > 0L && data.getSpawnTagCooldown() > 0L;
        if (flag) {
            Faction toFaction = event.getToFaction();
            Faction fromFaction = event.getFromFaction();
            if (!fromFaction.isSafezone() && toFaction.isSafezone()) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player) {
                if (event.isCancelled()) return;
                Player victim = (Player)event.getEntity();
                Player damager = (Player)event.getDamager();
                HCFPlayerData victimData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(victim);
                HCFPlayerData attackedData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(damager);
                this.setCooldown(damager, damager.getUniqueId(), this.defaultCooldown, false);
                victimData.setSpawnTagCooldown(this.defaultCooldown);
                this.setCooldown(victim, victim.getUniqueId(), this.defaultCooldown, false);
                attackedData.setSpawnTagCooldown(this.defaultCooldown);
            }
            else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile)event.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    Player shooter = (Player)projectile.getShooter();
                    if (shooter != event.getEntity()) {
                        Player player = (Player)event.getEntity();
                        Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation());
                        if (factionAt instanceof SpawnFaction){
                            event.setCancelled(true);
                            return;
                        }
                        HCFPlayerData shooterData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(shooter);
                        HCFPlayerData HCFPlayerData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                        this.setCooldown(shooter, shooter.getUniqueId(), this.defaultCooldown, false);
                        shooterData.setSpawnTagCooldown(this.defaultCooldown);
                        this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, false);
                        HCFPlayerData.setSpawnTagCooldown(this.defaultCooldown);
                    }
                }
            }
            else if (event.getEntity() instanceof Villager) {
                Villager villager = (Villager)event.getEntity();
                if (event.getDamager() instanceof Player && villager.getCustomName() != null) {
                    Player damager = (Player)event.getDamager();
                    HCFPlayerData dData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(damager);
                    this.setCooldown(damager, damager.getUniqueId(), this.defaultCooldown, false);
                    dData.setSpawnTagCooldown(this.defaultCooldown);
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onTimerStart(TimerStartEvent event) {
        if (event.getTimer() == this) {
            java.util.Optional<Player> optional = event.getPlayer();
            if (optional.isPresent()) {
                Player player = optional.get();
                player.sendMessage(Language.COMBAT_TAGGED.toString().replace("%time%", String.valueOf(RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.SPAWN_TAG"))));
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        data.setSpawnTagCooldown(0L);
        this.clearCooldown(event.getPlayer().getUniqueId());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPreventClaimEnterMonitor(FactionPlayerClaimEnterEvent event) {
        Player player = event.getPlayer();
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        if (event.getEnterCause() == FactionPlayerClaimEnterEvent.EnterCause.TELEPORT && !event.getFromFaction().isSafezone() && event.getToFaction().isSafezone()) {
            data.setSpawnTagCooldown(0L);
            this.clearCooldown(event.getPlayer());
        }
    }
}
