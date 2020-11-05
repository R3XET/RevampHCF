package eu.revamp.hcf.scoreboard;

import com.google.common.collect.Iterables;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.factions.FocusCommand;
import eu.revamp.hcf.factions.events.FactionPlayerJoinedEvent;
import eu.revamp.hcf.factions.events.FactionPlayerLeftEvent;
import eu.revamp.hcf.factions.events.FactionRelationCreateEvent;
import eu.revamp.hcf.factions.events.FactionRelationRemoveEvent;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.utils.Handler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Getter
public class ScoreboardTagEvents extends Handler implements Listener {
    private final Map<UUID, PlayerScoreboard> sbData;
    
    public ScoreboardTagEvents(RevampHCF plugin) {
        super(plugin);
        this.sbData = new HashMap<>();
    }
    
    @Override
    public void enable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.loadData(player);
        }
        this.getInstance().getServer().getPluginManager().registerEvents(this, RevampHCF.getInstance());
    }
    
    @Override
    public void disable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (String scores : player.getScoreboard().getEntries()) {
                player.getScoreboard().resetScores(scores);
            }
        }
    }
    
    public void loadData(Player player) {
        Scoreboard scoreboard = RevampHCF.getInstance().getServer().getScoreboardManager().getNewScoreboard();
        player.setScoreboard(scoreboard);
        this.sbData.put(player.getUniqueId(), new PlayerScoreboard(player, scoreboard, RevampHCF.getInstance().getScoreboard().getString("SCOREBOARD.TITLE").replace("|", "\u2503")));
    }
    
    public PlayerScoreboard getScoreboardFor(Player player) {
        return this.sbData.get(player.getUniqueId());
    }
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        RevampHCF.getInstance().getHandlerManager().getScoreboardTagEvents().loadData(player);
        new BukkitRunnable() {
            public void run() {
                for (PlayerScoreboard board : ScoreboardTagEvents.this.sbData.values()) {
                    board.addUpdate(player);
                }
                RevampHCF.getInstance().getHandlerManager().getScoreboardTagEvents().sbData.get(player.getUniqueId()).addUpdates(Bukkit.getOnlinePlayers());
                PlayerFaction faction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
                if (FocusCommand.focus.containsKey(faction)) {
                    Player target = Bukkit.getPlayer(FocusCommand.focus.get(faction));
                    if (target == null) return;
                    if (player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
                        Scoreboard scoreboard = player.getScoreboard();
                        Team team = scoreboard.getTeam("faction-focus");
                        if (team != null) {
                            team.unregister();
                        }
                        team = scoreboard.registerNewTeam("faction-focus");
                        team.setPrefix(ChatColor.BLUE.toString() + ChatColor.BOLD.toString());
                        team.addEntry(target.getName());
                    }
                }
            }
        }.runTaskLaterAsynchronously(this.getInstance(), 5L);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (String entries : player.getScoreboard().getEntries()) {
            player.getScoreboard().resetScores(entries);
        }
        this.sbData.remove(player.getUniqueId());
    }
    
    @EventHandler
    public void onFactionPlayerJoined(FactionPlayerJoinedEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            Collection<Player> players = event.getFaction().getOnlinePlayers();
            new BukkitRunnable() {
                public void run() {
                    ScoreboardTagEvents.this.getScoreboardFor(player).addUpdates(players);
                    for (Player target : players) {
                        ScoreboardTagEvents.this.getScoreboardFor(target).addUpdate(player);
                    }
                    if (FocusCommand.focus.containsKey(event.getFaction())) {
                        Player focusTarget = Bukkit.getPlayer(FocusCommand.focus.get(event.getFaction()));
                        if (focusTarget == null) return;
                        if (player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
                            Scoreboard scoreboard = player.getScoreboard();
                            Team team = scoreboard.getTeam("faction-focus");
                            if (team != null) {
                                team.unregister();
                            }
                            team = scoreboard.registerNewTeam("faction-focus");
                            team.setPrefix(ChatColor.BLUE.toString() + ChatColor.BOLD.toString());
                            team.addEntry(focusTarget.getName());
                        }
                    }
                }
            }.runTaskAsynchronously(RevampHCF.getInstance());
        }
    }
    
    @EventHandler
    public void onFactionPlayerLeft(FactionPlayerLeftEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            Collection<Player> players = event.getFaction().getOnlinePlayers();
            new BukkitRunnable() {
                public void run() {
                    ScoreboardTagEvents.this.getScoreboardFor(player).addUpdates(players);
                    for (Player target : players) {
                        ScoreboardTagEvents.this.getScoreboardFor(target).addUpdate(player);
                    }
                    if (FocusCommand.focus.containsKey(event.getFaction())) {
                        Player focusTarget = Bukkit.getPlayer(FocusCommand.focus.get(event.getFaction()));
                        ScoreboardTagEvents.this.getScoreboardFor(player).addUpdate(focusTarget);
                    }
                }
            }.runTaskAsynchronously(RevampHCF.getInstance());
        }
    }
    
    @EventHandler
    public void onFactionRelationCreate(FactionRelationCreateEvent event) {
        new BukkitRunnable() {
            public void run() {
                Iterable<Player> updates = Iterables.concat(event.getSenderFaction().getOnlinePlayers(), event.getTargetFaction().getOnlinePlayers());
                for (PlayerScoreboard board : ScoreboardTagEvents.this.sbData.values()) {
                    board.addUpdates(updates);
                }
            }
        }.runTaskAsynchronously(RevampHCF.getInstance());
    }
    
    @EventHandler
    public void onFactionRelationRemove(FactionRelationRemoveEvent event) {
        new BukkitRunnable() {
            public void run() {
                Iterable<Player> updates = Iterables.concat(event.getSenderFaction().getOnlinePlayers(), event.getTargetFaction().getOnlinePlayers());
                for (PlayerScoreboard board : ScoreboardTagEvents.this.sbData.values()) {
                    board.addUpdates(updates);
                }
            }
        }.runTaskAsynchronously(RevampHCF.getInstance());
    }
}
