package eu.revamp.hcf.handlers.settings;

import com.google.common.base.Optional;
import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.events.*;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.type.SpawnFaction;
import eu.revamp.hcf.factions.utils.games.KothFaction;
import eu.revamp.hcf.factions.utils.struction.RegenStatus;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FactionHandler extends Handler implements Listener
{
    private static long FACTION_JOIN_WAIT_MILLIS = TimeUnit.MINUTES.toMillis(2L);
    private static String FACTION_JOIN_WAIT_WORDS = DurationFormatUtils.formatDurationWords(FactionHandler.FACTION_JOIN_WAIT_MILLIS, true, true);
    private static String LAND_CHANGED_META_KEY = "landChangedMessage";
    private static long LAND_CHANGE_MSG_THRESHOLD = 225L;
    
    public FactionHandler(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionRenameMonitor(FactionRenameEvent event) {
        Faction faction = event.getFaction();
        if (faction instanceof KothFaction) {
            ((KothFaction)faction).getCaptureZone().setName(event.getNewName());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionCreate(FactionCreateEvent event) {
        Faction faction = event.getFaction();
        Player player = (Player)event.getSender();
        PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(player.getUniqueId());
        if (faction instanceof PlayerFaction) {
            String prefix = CC.translate(targetProfile.getAllPrefixes().replace("_", " ")).replace("star1", "\u2724").replace("star2", "\u273b").replace("star3", "\u2739").replace("star3", "\u273a").replace("star4", "\u2725").replace("star5", "\u2735");
            Bukkit.broadcastMessage(CC.translate("&aTeam &7» &eFaction &9" + faction.getName() + " &ehas been &acreated &eby &f" + prefix + player.getName()));
        }
    }
    
    private long getLastLandChangedMeta(Player player) {
        List<MetadataValue> value = player.getMetadata(FactionHandler.LAND_CHANGED_META_KEY);
        long millis = System.currentTimeMillis();
        long remaining = (value == null || value.isEmpty()) ? 0L : (value.get(0).asLong() - millis);
        if (remaining <= 0L) {
            player.setMetadata(FactionHandler.LAND_CHANGED_META_KEY, new FixedMetadataValue(RevampHCF.getInstance(), millis + FactionHandler.LAND_CHANGE_MSG_THRESHOLD));
        }
        return remaining;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void onPlayerClaimEnter(FactionPlayerClaimEnterEvent event) {
        Faction toFaction = event.getToFaction();
        Faction fromFaction = event.getFromFaction();
        Player player = event.getPlayer();
        if (toFaction.isSafezone() && !RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(player)) {
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.setSaturation(4.0f);
        }
        if (this.getLastLandChangedMeta(player) > 0L) return;
        if (toFaction instanceof SpawnFaction && !RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(player)) {
            player.sendMessage(CC.translate("&aTeam &7» &eEntering To &aSpawn &8- " + (toFaction.isDeathban() ? "&cDeathban" : "&aSafeZone") + "&e."));
        }
        if (fromFaction instanceof SpawnFaction && !RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().hasCooldown(player)) {
            player.sendMessage(CC.translate("&aTeam &7» &eLeaving from &aSpawn &8- " + (toFaction.isDeathban() ? "&cDeathban" : "&aSafeZone") + "&e."));
        }
        if (fromFaction.getName().equalsIgnoreCase("EOTW")) {
            player.sendMessage(CC.translate("&aTeam &7» &eLeaving from &4EOTW &8- " + (fromFaction.isDeathban() ? "&cDeathban" : "&aSafeZone") + "&e."));
        } else if (!fromFaction.getName().equalsIgnoreCase("EOTW") && !(fromFaction instanceof SpawnFaction)) {
            player.sendMessage(CC.translate("&aTeam &7» &eLeaving from " + fromFaction.getDisplayName(player) + " &8- " + (fromFaction.isDeathban() ? "&cDeathban" : "&aSafeZone") + "&e."));
        }
        if (toFaction.getName().equalsIgnoreCase("EOTW")) {
            player.sendMessage(CC.translate("&aTeam &7» &eEntering To &4EOTW &8- " + (toFaction.isDeathban() ? "&cDeathban" : "&aSafeZone") + "&e."));
        } else if (!fromFaction.getName().equalsIgnoreCase("EOTW") && !(fromFaction instanceof SpawnFaction)) {
            player.sendMessage(CC.translate("&aTeam &7» &eEntering To " + toFaction.getDisplayName(player) + " &8- " + (toFaction.isDeathban() ? "&cDeathban" : "&aSafeZone") + "&e."));
        } else {
            player.sendMessage(CC.translate("&aTeam &7» &eLeaving from " + fromFaction.getDisplayName(player)));
            player.sendMessage(CC.translate("&aTeam &7» &eEntering To " + toFaction.getDisplayName(player)));
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLeftFaction(FactionPlayerLeftEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            HCFPlayerData user = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
            user.setLastFactionLeaveMillis(System.currentTimeMillis());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerPreFactionJoin(FactionPlayerJoinEvent event) {
        Faction faction = event.getFaction();
        Optional<Player> optionalPlayer = event.getPlayer();
        if (faction instanceof PlayerFaction && optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            PlayerFaction playerFaction = (PlayerFaction)faction;
            if (!player.hasPermission("hcf.staff") && !RevampHCF.getInstance().getConfiguration().isKitMap() && !RevampHCF.getInstance().getHandlerManager().getEotwUtils().isEndOfTheWorld() && playerFaction.getRegenStatus() == RegenStatus.PAUSED) {
                event.setCancelled(true);
                player.sendMessage(CC.translate("&aTeam &7» &cYou cannot join factions that are not regenerating DTR."));
                return;
            }
            HCFPlayerData user = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
            long difference = user.getLastFactionLeaveMillis() - System.currentTimeMillis() + FactionHandler.FACTION_JOIN_WAIT_MILLIS;
            if (difference > 0L && !player.hasPermission("revamp.staff")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.GOLD + "Team" + ChatColor.GRAY + " » " + ChatColor.RED + "You cannot join factions after just leaving within " + FactionHandler.FACTION_JOIN_WAIT_WORDS + ". " + "You gotta wait another " + DurationFormatUtils.formatDurationWords(difference, true, true) + '.');
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onFactionLeave(FactionPlayerLeaveEvent event) {
        if (event.isForce() || event.isKick()) return;
        Faction faction = event.getFaction();
        if (faction instanceof PlayerFaction) {
            Optional<Player> optional = event.getPlayer();
            if (optional.isPresent()) {
                Player player = optional.get();
                if (RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation()) == faction) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.GOLD + "Team" + ChatColor.GRAY + " » " + ChatColor.RED + "You can leave this faction when you leave their claim.");
                }
            }
        }
    }


    @EventHandler @SuppressWarnings("deprecation")
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction == null) {
            player.sendMessage(Language.FACTIONS_NOFACTION.toString());
        }
        else {
            playerFaction.broadcast(ChatColor.GOLD + "Team" + ChatColor.GRAY + " » " + ChatColor.BOLD.toString() + ChatColor.GREEN + "Member Online: " + ChatColor.BOLD + playerFaction.getMember(player).getRole().getAstrix() + player.getName() + '.', player.getUniqueId());
        }
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
        if (playerFaction != null) {
            playerFaction.broadcast(ChatColor.GOLD + "Team" + ChatColor.GRAY + " » " + ChatColor.BOLD.toString() + ChatColor.RED + "Member Offline: " + ChatColor.BOLD + playerFaction.getMember(player).getRole().getAstrix() + player.getName() + '.');
        }
    }
}
