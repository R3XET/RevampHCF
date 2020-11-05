package eu.revamp.hcf.timers;

import javax.annotation.Nullable;

import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.enums.ClaimChangeEnum;
import eu.revamp.hcf.factions.events.FactionClaimChangedEvent;
import eu.revamp.hcf.factions.events.FactionPlayerClaimEnterEvent;
import eu.revamp.hcf.factions.type.ClaimableFaction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.type.RoadFaction;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.visualise.VisualType;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.potion.PotionUtils;
import eu.revamp.spigot.utils.world.WorldUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import eu.revamp.hcf.utils.timer.events.TimerCooldown;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import java.util.Iterator;
import java.util.Collection;

import org.bukkit.event.player.PlayerTeleportEvent;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventHandler;
import com.google.common.base.Optional;
import eu.revamp.hcf.utils.timer.events.TimerClearEvent;
import org.bukkit.entity.Player;
import com.google.common.base.Predicate;
import org.bukkit.Bukkit;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.timer.PlayerTimer;

public class PvPTimerHandler extends PlayerTimer implements Listener
{
    private long item_pickup_delay;
    @Getter @Setter public Map<UUID, Long> item_pickup_delays;
    
    public PvPTimerHandler() {
        super("PvP Timer", TimeUnit.MINUTES.toMillis((long) RevampHCF.getInstance().getConfig().getDouble("COOLDOWNS.PVP_TIMER")));
        this.item_pickup_delay = TimeUnit.SECONDS.toMillis(20L);
        setItem_pickup_delays(new HashMap<>());
    }
    
    public long getTime() {
        return this.defaultCooldown;
    }
    
    @Override
    public void onExpire(UUID userUUID) {
        Player player = Bukkit.getPlayer(userUUID);
        if (player != null) {
            HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
            data.setPvpTimerCooldown(0);
            RevampHCF.getInstance().getHandlerManager().getVisualiseHandler().clearVisualBlocks(player, VisualType.CLAIM_BORDER, null);
        }
    }
    
    @EventHandler
    public void onTimerStop(TimerClearEvent event) {
        if (event.getTimer() == this) {
            Optional<UUID> optionalUserUUID = event.getUserUUID();
            if (optionalUserUUID.isPresent()) {
                this.onExpire(optionalUserUUID.get());
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        long remaining = this.getRemaining(entity.getUniqueId());
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(entity.getUniqueId());
        if (entity instanceof Player && remaining > 0L && data.getPvpTimerCooldown() > 0) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            public void run() {
                Player player = event.getPlayer();
                HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                data.setPvpTimerCooldown(data.getPvpTimerCooldown());
                if (data.getPvpTimerCooldown() > 0) {
                    PvPTimerHandler.this.setCooldown(player, player.getUniqueId(), data.getPvpTimerCooldown(), true);
                    if (RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation()).isSafezone()) {
                        PvPTimerHandler.this.setPaused(player.getUniqueId(), true);
                    }
                }
            }
        }.runTaskLater(RevampHCF.getInstance(), 2L);
    }
    
    @EventHandler
    public void onClaimChange(FactionClaimChangedEvent event) {
        if (event.getCause() == ClaimChangeEnum.CLAIM) return;
        Collection<ClaimZone> claims = event.getAffectedClaims();
        for (ClaimZone claim : claims) {
            Collection<Player> players = claim.getPlayers();
            if (players.isEmpty()) continue;
            Location location = new Location(claim.getWorld(), claim.getMinimumX() - 1, 0.0, claim.getMinimumZ() - 1);
            location = BukkitUtils.getHighestLocation(location, location);
            for (Player player : players) {
                HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                if (this.getRemaining(player) > 0L && data.getPvpTimerCooldown() > 0 && player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN)) {
                    player.sendMessage(CC.translate("&cLand was claimed where you were standing. "));
                    player.sendMessage(CC.translate("&cAs you still have your &a&lPvP Timer&c, you were teleported away."));
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        Location spawn = WorldUtils.destringifyLocation(RevampHCF.getInstance().getLocation().getString("World-Spawn.world-spawn"));
        player.teleport(spawn);
        data.setPvpTimerCooldown((int)this.defaultCooldown);
        this.setCooldown(event.getPlayer(), event.getPlayer().getUniqueId(), data.getPvpTimerCooldown(), true);
        if (RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation()).isSafezone()) {
            this.setPaused(player.getUniqueId(), true);
            player.sendMessage(CC.translate("&cYou now have your &a&lPvP Timer&c."));
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        World world = player.getWorld();
        Location location = player.getLocation();
        Collection<ItemStack> drops = event.getDrops();
        Location spawn = WorldUtils.destringifyLocation(RevampHCF.getInstance().getLocation().getString("World-Spawn.world-spawn"));
        if (!drops.isEmpty()) {
            Iterator<ItemStack> iterator = drops.iterator();
            long stamp = System.currentTimeMillis() + this.item_pickup_delay;
            while (iterator.hasNext()) {
                getItem_pickup_delays().put(world.dropItemNaturally(location, iterator.next()).getUniqueId(), stamp);
                player.teleport(spawn);
                iterator.remove();
            }
        }
        data.setPvpTimerCooldown((int)this.defaultCooldown);
        this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        long remaining = this.getRemaining(player);
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        if (remaining > 0L && data.getPvpTimerCooldown() > 0) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cYou cannot empty buckets as your &a&lPvP Timer &cis active."));
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockIgnite(BlockIgniteEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        long remaining = this.getRemaining(player);
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        if (remaining > 0L && data.getPvpTimerCooldown() > 0) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cYou cannot ignite blocks as your &a&lPvP Timer &cis active."));
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        TimerCooldown runnable = this.cooldowns.get(player.getUniqueId());
        if (runnable != null && runnable.getRemaining() > 0L && data.getPvpTimerCooldown() > 0) {
            data.setPvpTimerCooldown((int)runnable.getRemaining());
            //RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().saveYamlData(player);
            runnable.setPaused(true);
        }
    }
    
    @EventHandler
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        if (!player.hasPlayedBefore()) {
            if (this.canApply() && this.setCooldown(player, player.getUniqueId(), TimeUnit.MINUTES.toMillis(RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.FIRST_PVP_TIMER")), true)) {
                this.setPaused(player.getUniqueId(), true);
                player.sendMessage(CC.translate("&cYou now have your &a&lPvP Timer&c."));
            }
        }
        else if (this.isPaused(player) && this.getRemaining(player) > 0L && data.getPvpTimerCooldown() > 0 && !RevampHCF.getInstance().getFactionManager().getFactionAt(event.getSpawnLocation()).isSafezone()) {
            this.setPaused(player.getUniqueId(), false);
        }
    }
    
    @EventHandler
    public void onPlayerClaimEnterMonitor(FactionPlayerClaimEnterEvent event) {
        Player player = event.getPlayer();
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        if (event.getTo().getWorld().getEnvironment() == World.Environment.THE_END) {
            data.setPvpTimerCooldown(0);
            this.clearCooldown(player);
            return;
        }
        boolean flag = this.getRemaining(player.getUniqueId()) > 0L && data.getPvpTimerCooldown() > 0;
        if (flag) {
            Faction toFaction = event.getToFaction();
            Faction fromFaction = event.getFromFaction();
            if (fromFaction.isSafezone() && !toFaction.isSafezone()) {
                player.sendMessage(CC.translate("&cYour &a&lPvP Timer&c is now un-frozen."));
                data.setPvpTimerCooldown(data.getPvpTimerCooldown() - 1);
                this.setPaused(player.getUniqueId(), false);
            }
            else if (!fromFaction.isSafezone() && toFaction.isSafezone()) {
                player.sendMessage(CC.translate("&cYour &a&lPvP Timer&c is now frozen."));
                this.setPaused(player.getUniqueId(), true);
            }
        }
    }
    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerClaimEnter(FactionPlayerClaimEnterEvent event) {
        Player player = event.getPlayer();
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        Faction toFaction = event.getToFaction();
        if (toFaction instanceof ClaimableFaction && this.getRemaining(player) > 0L && data.getPvpTimerCooldown() > 0) {
            PlayerFaction playerFaction;
            if (event.getEnterCause() == FactionPlayerClaimEnterEvent.EnterCause.TELEPORT && toFaction instanceof PlayerFaction && (playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player)) != null && playerFaction == toFaction) {
                player.sendMessage(CC.translate("&cYou have entered your own claim, therefore your &a&lPvP Timer&c was cleared."));
                data.setPvpTimerCooldown(0);
                this.clearCooldown(player);
                return;
            }
            if (!toFaction.isSafezone() && !(toFaction instanceof RoadFaction)) {
                event.setCancelled(true);
                player.sendMessage(CC.translate("&cYou cannot enter " + toFaction.getDisplayName(player) + " &cwhilst your &a&lPvP Timer&c is active."));
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player) {
                Player victim = (Player)event.getEntity();
                Player damager = (Player)event.getDamager();
                HCFPlayerData victimData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(victim);
                HCFPlayerData damagerData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(damager);
                if (this.getRemaining(victim) > 0L && this.getRemaining(damager) > 0L && victimData.getPvpTimerCooldown() > 0 && damagerData.getPvpTimerCooldown() > 0) {
                    event.setCancelled(true);
                    damager.sendMessage(CC.translate("&c&l" + victim.getName() + " &chas his &a&lPvP Timer&c."));
                }
                else if (this.getRemaining(victim) > 0L && victimData.getPvpTimerCooldown() > 0) {
                    event.setCancelled(true);
                    damager.sendMessage(CC.translate("&c&l" + victim.getName() + " &chas his &a&lPvP Timer&c."));
                }
                else if (this.getRemaining(damager) > 0L && damagerData.getPvpTimerCooldown() > 0) {
                    event.setCancelled(true);
                    damager.sendMessage(CC.translate("&cYou cannot attack players whilst your &a&lPvP Timer &cis active."));
                }
            }
            else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile)event.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    Player shooter = (Player)projectile.getShooter();
                    if (shooter != event.getEntity()) {
                        Player player = (Player)event.getEntity();
                        HCFPlayerData HCFPlayerData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                        HCFPlayerData taggerData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(shooter);
                        if (this.getRemaining(player) > 0L && this.getRemaining(shooter) > 0L && HCFPlayerData.getPvpTimerCooldown() > 0 && taggerData.getPvpTimerCooldown() > 0) {
                            event.setCancelled(true);
                            shooter.sendMessage(CC.translate("&c&l" + player.getName() + " &chas his &a&lPvP Timer&c."));
                        }
                        else if (this.getRemaining(player) > 0L && HCFPlayerData.getPvpTimerCooldown() > 0) {
                            event.setCancelled(true);
                            shooter.sendMessage(CC.translate("&cYou cannot attack players whilst your &a&lPvP Timer &cis active."));
                        }
                        else if (this.getRemaining(shooter) > 0L && taggerData.getPvpTimerCooldown() > 0) {
                            event.setCancelled(true);
                            shooter.sendMessage(CC.translate("&c&l" + player.getName() + " &chas his &a&lPvP Timer&c."));
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();
        if (potion.getShooter() instanceof Player && PotionUtils.isDebuff(potion)) {
            for (LivingEntity livingEntity : event.getAffectedEntities()) {
                if (livingEntity instanceof Player) {
                    HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer((Player)livingEntity);
                    if (this.getRemaining((Player)livingEntity) <= 0L || data.getPvpTimerCooldown() <= 0) {
                        continue;
                    }
                    event.setIntensity(livingEntity, 0.0);
                }
            }
        }
    }
    
    @Override
    public boolean setCooldown(@Nullable Player player, UUID playerUUID, long duration, boolean overwrite, @Nullable Predicate<Long> callback) {
        return this.canApply() && super.setCooldown(player, playerUUID, duration, overwrite, callback);
    }
    
    private boolean canApply() {
        if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
            return !RevampHCF.getInstance().getHandlerManager().getEotwUtils().isEndOfTheWorld();
        }
        return !RevampHCF.getInstance().getHandlerManager().getEotwUtils().isEndOfTheWorld() && RevampHCF.getInstance().getHandlerManager().getSotwHandler().getSotwRunnable() == null && !RevampHCF.getInstance().getHandlerManager().getSotwHandler().isRunning();
    }
}
