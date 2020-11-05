package eu.revamp.hcf.timers;

import eu.revamp.hcf.factions.events.capzone.CaptureZoneEnterEvent;
import eu.revamp.hcf.factions.events.capzone.CaptureZoneLeaveEvent;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.games.ConquestFaction;
import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import eu.revamp.hcf.handlers.signs.EventSignHandler;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.date.DateTimeFormats;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Collection;
import com.google.common.collect.Iterables;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import java.time.ZoneId;
import java.util.Date;
import java.time.LocalDateTime;

import org.bukkit.scheduler.BukkitRunnable;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.util.concurrent.TimeUnit;

import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.timer.GlobalTimer;

public class GameHandler extends GlobalTimer implements Listener
{
    private static long RESCHEDULE_FREEZE_MILLIS = TimeUnit.SECONDS.toMillis(RevampHCF.getInstance().getConfig().getInt("TIME-BETWEEN-STARTS"));
    private static String RESCHEDULE_FREEZE_WORDS = DurationFormatUtils.formatDurationWords(GameHandler.RESCHEDULE_FREEZE_MILLIS, true, true);
    @Getter private long startStamp;
    private long lastContestedEventMillis;
    @Getter private EventFaction eventFaction;

    public GameHandler(RevampHCF plugin) {
        super("Event", 0L);
        new BukkitRunnable() {
            public void run() {
                if (GameHandler.this.eventFaction != null) {
                    GameHandler.this.eventFaction.getEventType().getEventTracker().tick(GameHandler.this, GameHandler.this.eventFaction); //errore
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }
    
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.of(DateTimeFormats.SERVER_TIME_ZONE.getID())).toInstant());
    }
    
    public void broadcastWarning(EventFaction eventFaction, long timeTill) {
        if (this.getRemaining() <= 0L) {
            timeTill = timeTill / 1000L / 60L * 1000L * 60L;
            Bukkit.broadcastMessage("Koth" + ChatColor.LIGHT_PURPLE + eventFaction.getName() + ChatColor.YELLOW + " is starting in " + DurationFormatUtils.formatDurationWords(timeTill, true, true));
        }
    }
    
    @Override
    public boolean clearCooldown() {
        boolean result = super.clearCooldown();
        if (this.eventFaction != null) {
            for (CaptureZone captureZone : this.eventFaction.getCaptureZones()) {
                captureZone.setCappingPlayer(null);
            }
            this.eventFaction.setDeathban(true);
            this.eventFaction.getEventType().getEventTracker().stopTiming();
            this.eventFaction = null;
            this.startStamp = -1L;
            result = true;
        }
        return result;
    }
    
    @Override
    public long getRemaining() {
        if (this.eventFaction == null) return 0L;
        if (this.eventFaction instanceof KothFaction) return ((KothFaction)this.eventFaction).getCaptureZone().getRemainingCaptureMillis();
        return super.getRemaining();
    }
    @SuppressWarnings("deprecation")
    public void handleWinner(Player winner) {
        PlayerFaction capper = RevampHCF.getInstance().getFactionManager().getPlayerFaction(winner.getUniqueId());
        if (this.eventFaction == null) return;
        Bukkit.broadcastMessage(CC.translate("&8[&6&l" + this.eventFaction.getEventType().getDisplayName() + "&8] &6&l" + winner.getName() + " &ehas captured &6&l" + this.eventFaction.getName() + " &eafter &6&l" + DurationFormatUtils.formatDurationWords(this.getUptime(), true, true) + " &eof up-time."));
        if (this.eventFaction.getEventType().getDisplayName().equalsIgnoreCase("koth")) {
            GameHandler eventTimer = RevampHCF.getInstance().getHandlerManager().getTimerManager().getGameHandler();
            if (!eventTimer.clearCooldown()) return;
            capper.setPoints(capper.getPoints() + RevampHCF.getInstance().getConfig().getInt("POINTS.WIN-PER-KOTH"));
            capper.broadcast("&a" + winner.getName() + " &eHas gotten &61 &epoint &efor your faction by capping a KOTH");
            for (String command : RevampHCF.getInstance().getConfig().getStringList("KOTH.WINNER-PRIZE")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", winner.getName()));
            }
        }
        else if (this.eventFaction.getEventType().getDisplayName().equalsIgnoreCase("conquest")) {
            capper.setPoints(capper.getPoints() + RevampHCF.getInstance().getConfig().getInt("POINTS.WIN-PER-CONQUEST"));
            capper.broadcast("&a" + winner.getName() + " &eHas gotten &61 &epoint &efor your faction by capping the Conquest");
            for (String command: RevampHCF.getInstance().getConfig().getStringList("CONQUEST.WINNER-PRIZE")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%ign%", winner.getName()));
            }
        }
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(winner);
        if (this.eventFaction instanceof KothFaction) {
            if (playerFaction == null) return;
            playerFaction.setKothCaptures(playerFaction.getKothCaptures() + 1);
        }
        if (this.eventFaction instanceof ConquestFaction) {
            if (playerFaction == null) return;
            playerFaction.setConquestCaptures(playerFaction.getConquestCaptures() + 1);
        }
        World world = winner.getWorld();
        if (winner.getInventory().firstEmpty() == -1) {
            world.dropItemNaturally(winner.getLocation(), EventSignHandler.getEventSign(this.eventFaction.getName(), winner.getName()));
        }
        else {
            winner.getInventory().addItem(EventSignHandler.getEventSign(this.eventFaction.getName(), winner.getName()));
        }
        this.clearCooldown();
    }
    
    public boolean tryContesting(EventFaction eventFaction, CommandSender sender) {
        if (this.eventFaction != null) {
            sender.sendMessage(ChatColor.RED + "There is already an active event, use /koth cancel to end it.");
            return false;
        }
        if (eventFaction instanceof KothFaction) {
            KothFaction kothFaction = (KothFaction)eventFaction;
            if (kothFaction.getCaptureZone() == null) {
                sender.sendMessage(ChatColor.RED + "Cannot schedule " + eventFaction.getName() + " as its' capture zone is not set.");
                return false;
            }
        }
        else if (eventFaction instanceof ConquestFaction) {
            ConquestFaction conquestFaction = (ConquestFaction)eventFaction;
            Collection<ConquestFaction.ConquestZone> zones = conquestFaction.getConquestZones();
            ConquestFaction.ConquestZone[] values;
            for (int length = (values = ConquestFaction.ConquestZone.values()).length, i = 0; i < length; ++i) {
                ConquestFaction.ConquestZone zone = values[i];
                if (!zones.contains(zone)) {
                    sender.sendMessage(ChatColor.RED + "Cannot schedule " + eventFaction.getName() + " as capture zone '" + zone.getDisplayName() + ChatColor.RED + "' is not set.");
                    return false;
                }
            }
        }
        long millis = System.currentTimeMillis();
        if (this.lastContestedEventMillis + GameHandler.RESCHEDULE_FREEZE_MILLIS - millis > 0L) {
            sender.sendMessage(ChatColor.RED + "Cannot reschedule games within " + GameHandler.RESCHEDULE_FREEZE_WORDS + '.');
            return false;
        }
        this.lastContestedEventMillis = millis;
        this.startStamp = millis;
        this.eventFaction = eventFaction;
        eventFaction.getEventType().getEventTracker().onContest(eventFaction, this);
        if (eventFaction instanceof ConquestFaction) {
            this.setRemaining(1000L, true);
            this.setPaused(true);
        }
        Collection<CaptureZone> captureZones = eventFaction.getCaptureZones();
        for (CaptureZone captureZone : captureZones) {
            if (captureZone.isActive()) {
                Player player = Iterables.getFirst(captureZone.getCuboid().getPlayers(), null);
                if (player == null || !eventFaction.getEventType().getEventTracker().onControlTake(player, captureZone)) {
                    continue;
                }
                captureZone.setCappingPlayer(player);
            }
        }
        eventFaction.setDeathban(false);
        return true;
    }
    
    public long getUptime() {
        return System.currentTimeMillis() - this.startStamp;
    }

    private void handleDisconnect(Player player) {
        Preconditions.checkNotNull((Object)player);
        if (this.eventFaction == null) return;
        Collection<CaptureZone> captureZones = this.eventFaction.getCaptureZones();
        for (CaptureZone captureZone : captureZones) {
            if (Objects.equal(captureZone.getCappingPlayer(), player)) {
                captureZone.setCappingPlayer(null);
                this.eventFaction.getEventType().getEventTracker().onControlLoss(player, captureZone, this.eventFaction);
                break;
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        this.handleDisconnect(event.getEntity());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLogout(PlayerQuitEvent event) {
        this.handleDisconnect(event.getPlayer());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        this.handleDisconnect(event.getPlayer());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCaptureZoneEnter(CaptureZoneEnterEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(CC.translate("&aTeam &7» &eEntering To " + event.getFaction().getDisplayName(player) + " &d(Capzone) &8- &cDeathban&e."));
        if (this.eventFaction == null) return;
        CaptureZone captureZone = event.getCaptureZone();
        PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(player.getUniqueId());
        if (!this.eventFaction.getCaptureZones().contains(captureZone)) return;
        if ((player.getGameMode().equals(GameMode.CREATIVE) && this.eventFaction.getEventType().getEventTracker().onControlTake(player, captureZone)) || (targetProfile.isInStaffMode() && this.eventFaction.getEventType().getEventTracker().onControlTake(player, captureZone)) || (targetProfile.isVanished() && this.eventFaction.getEventType().getEventTracker().onControlTake(player, captureZone))) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cYou cannot cap games on gamemode 1/staffmode/vanish"));
        }
        if (RevampHCF.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId()) == null && captureZone.getCappingPlayer() == null && this.eventFaction.getEventType().getEventTracker().onControlTake(player, captureZone)) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cYou cannot cap without a team"));
        }
        if (!player.getGameMode().equals(GameMode.CREATIVE) && !targetProfile.isInStaffMode() && !targetProfile.isVanished() && captureZone.getCappingPlayer() == null && this.eventFaction.getEventType().getEventTracker().onControlTake(player, captureZone)) {
            captureZone.setCappingPlayer(player);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCaptureZoneLeave(CaptureZoneLeaveEvent event) {
        Player player = event.getPlayer();
        CaptureZone captureZone = event.getCaptureZone();
        player.sendMessage(CC.translate("&aTeam &7» &eLeaving from " + event.getFaction().getDisplayName(player) + " &d(Capzone) &8- &cDeathban&e."));
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && Objects.equal(event.getFaction(), this.eventFaction) && Objects.equal(player, captureZone.getCappingPlayer())) {
            captureZone.setCappingPlayer(null);
            this.eventFaction.getEventType().getEventTracker().onControlLoss(player, captureZone, this.eventFaction);
            for (Player target : captureZone.getCuboid().getPlayers()) {
                if (target != null && !target.equals(player) && this.eventFaction.getEventType().getEventTracker().onControlTake(target, captureZone)) {
                    captureZone.setCappingPlayer(target);
                    break;
                }
            }
        }
    }
}
