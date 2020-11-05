package eu.revamp.hcf.games;

import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.events.FactionRemoveEvent;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.utils.games.ConquestFaction;
import eu.revamp.hcf.factions.utils.games.EventFaction;
import eu.revamp.hcf.factions.utils.zone.CaptureZone;
import eu.revamp.hcf.timers.GameHandler;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.ChatColor;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.Sound;

import java.util.List;
import com.google.common.collect.Ordering;
import com.google.common.collect.ImmutableMap;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.Bukkit;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import eu.revamp.hcf.RevampHCF;

import java.util.Map;
import java.util.Comparator;
import org.bukkit.event.Listener;


public class ConquestType implements EventGameType, Listener
{
    public static long MINIMUM_CONTROL_TIME_ANNOUNCE = TimeUnit.SECONDS.toMillis(5L);
    public static long DEFAULT_CAP_MILLIS = TimeUnit.SECONDS.toMillis(30L);
    public static Comparator<Map.Entry<PlayerFaction, Integer>> POINTS_COMPARATOR = ((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
    public final Map<PlayerFaction, Integer> factionPointsMap;
    public RevampHCF plugin;

    public ConquestType(RevampHCF plugin) {
        this.factionPointsMap = Collections.synchronizedMap(new LinkedHashMap<>());
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionRemove(FactionRemoveEvent event) {
        Faction faction = event.getFaction();
        if (faction instanceof PlayerFaction) {
            synchronized (this.factionPointsMap) {
                this.factionPointsMap.remove(faction);
            }
            // monitorexit(this.factionPointsMap)
        }
    }
    
    public Map<PlayerFaction, Integer> getFactionPointsMap() {
        return ImmutableMap.copyOf(this.factionPointsMap);
    }
    
    public int getPoints(PlayerFaction faction) {
        synchronized (this.factionPointsMap) {
            // monitorexit(this.factionPointsMap)
            return Utils.firstNonNull(this.factionPointsMap.get(faction), 0);
        }
    }
    
    public int setPoints(PlayerFaction faction, int amount) {
        if (amount <= 0) {
            return amount;
        }
        synchronized (this.factionPointsMap) {
            this.factionPointsMap.put(faction, amount);
            List<Map.Entry<PlayerFaction, Integer>> entries = Ordering.from(ConquestType.POINTS_COMPARATOR).sortedCopy(this.factionPointsMap.entrySet());
            this.factionPointsMap.clear();
            for (Map.Entry<PlayerFaction, Integer> entry : entries) {
                this.factionPointsMap.put(entry.getKey(), entry.getValue());
            }
        }
        // monitorexit(this.factionPointsMap)
        return amount;
    }
    
    public int takePoints(PlayerFaction faction, int amount) {
        return this.setPoints(faction, this.getPoints(faction) - amount);
    }
    
    public int addPoints(PlayerFaction faction, int amount) {
        return this.setPoints(faction, this.getPoints(faction) + amount);
    }
    
    @Override
    public GameType getEventType() {
        return GameType.CONQUEST;
    }
    
    @Override
    public void tick(GameHandler eventTimer, EventFaction eventFaction) {
        ConquestFaction conquestFaction = (ConquestFaction)eventFaction;
        List<CaptureZone> captureZones = conquestFaction.getCaptureZones();
        for (CaptureZone captureZone : captureZones) {
            captureZone.updateScoreboardRemaining();
            Player cappingPlayer = captureZone.getCappingPlayer();
            if (cappingPlayer == null) {
                continue;
            }
            long remainingMillis = captureZone.getRemainingCaptureMillis();
            if (remainingMillis <= 0L) {
                UUID uuid = cappingPlayer.getUniqueId();
                PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(uuid);
                if (playerFaction != null) {
                    int newPoints = this.addPoints(playerFaction, 1);
                    if (newPoints >= RevampHCF.getInstance().getConfiguration().getConquestWinPoints()) {
                        synchronized (this.factionPointsMap) {
                            this.factionPointsMap.clear();
                        }
                        // monitorexit(this.factionPointsMap)
                        this.plugin.getHandlerManager().getTimerManager().getGameHandler().handleWinner(cappingPlayer);
                        return;
                    }
                    captureZone.setRemainingCaptureMillis(captureZone.getDefaultCaptureMillis());
                    Bukkit.broadcastMessage(CC.translate("&8[&6&l" + eventFaction.getName() + "&8] " + "&6&l" + playerFaction.getName() + " &egained &9" + 1 + " &epoint for capturing &6&l" + captureZone.getDisplayName() + "&e. &8(&f" + newPoints + '/' + RevampHCF.getInstance().getConfiguration().getConquestWinPoints() + "&8)"));
                    cappingPlayer.playSound(cappingPlayer.getLocation(), Sound.NOTE_BASS, 1.0f, 1.0f);
                }
                return;
            }
            int remainingSeconds = (int)Math.round(remainingMillis / 1000.0);
            if (remainingSeconds % 5 != 0) {
                continue;
            }
            cappingPlayer.sendMessage(CC.translate("&8[&6&l" + eventFaction.getName() + "&8] " + "&eAttempting to control &6&l" + captureZone.getDisplayName() + "&e. &8(&f" + remainingSeconds + "s&8)"));
            cappingPlayer.playSound(cappingPlayer.getLocation(), Sound.CLICK, 1.0f, 1.0f);
        }
    }
    
    @Override
    public void onContest(EventFaction eventFaction, GameHandler eventTimer) {
        Bukkit.broadcastMessage(CC.translate(((eventFaction instanceof ConquestFaction) ? "" : "&8[&6&l" + eventFaction.getName() + "&8] &6&l") + eventFaction.getName() + " &ecan now be contested."));
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            online.playSound(online.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
        }
    }
    
    @Override
    public boolean onControlTake(Player player, CaptureZone captureZone) {
        if (this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId()) == null) {
            player.sendMessage(ChatColor.RED + "You must be in a faction to capture for Conquest.");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
            return false;
        }
        return true;
    }
    
    @Override
    public void onControlLoss(Player player, CaptureZone captureZone, EventFaction eventFaction) {
        long remainingMillis = captureZone.getRemainingCaptureMillis();
        if (remainingMillis > 0L && captureZone.getDefaultCaptureMillis() - remainingMillis > ConquestType.MINIMUM_CONTROL_TIME_ANNOUNCE) {
            Bukkit.broadcastMessage(CC.translate("&8[&6&l" + eventFaction.getName() + "&8] &6&l" + player.getName() + " &ewas knocked off &6&l" + captureZone.getDisplayName() + "&e."));
            player.playSound(player.getLocation(), Sound.ARROW_HIT, 1.0f, 1.0f);
        }
    }
    
    @Override
    public void stopTiming() {
        synchronized (this.factionPointsMap) {
            this.factionPointsMap.clear();
        }
        // monitorexit(this.factionPointsMap)
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL) @SuppressWarnings("deprecation")
    public void onPlayerDeath(PlayerDeathEvent event) {
        Faction currentEventFac = this.plugin.getHandlerManager().getTimerManager().getGameHandler().getEventFaction();
        if (currentEventFac instanceof ConquestFaction) {
            Player player = event.getEntity();
            PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
            if (playerFaction != null) {
                int oldPoints = this.getPoints(playerFaction);
                if (oldPoints == 0) return;
                if (this.getPoints(playerFaction) <= 20) {
                    this.setPoints(playerFaction, 0);
                }
                else {
                    this.takePoints(playerFaction, 20);
                }
                event.setDeathMessage(CC.translate("&8[&6&l" + currentEventFac.getName() + "&8] " + "&6&l" + playerFaction.getName() + " &elost &6&l" + Math.min(20, oldPoints) + " &epoints because &6&l" + player.getName() + " &edied. &8(&f" + this.getPoints(playerFaction) + '/' + RevampHCF.getInstance().getConfiguration().getConquestWinPoints() + "&8)"));
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
        }
    }
}
