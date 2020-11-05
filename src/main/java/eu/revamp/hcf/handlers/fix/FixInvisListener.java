package eu.revamp.hcf.handlers.fix;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.events.potion.PotionEffectAddEvent;
import eu.revamp.spigot.events.potion.PotionEffectExpireEvent;
import eu.revamp.spigot.events.potion.PotionEffectRemoveEvent;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FixInvisListener extends Handler implements Listener {

    public static ArrayList<UUID> INVIS_PLAYER = new ArrayList<>();

    public FixInvisListener(RevampHCF plugin) {
        super(plugin);
    }
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler
    public void onAddEffect(PotionEffectAddEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getEffect().getType().equals(PotionEffectType.INVISIBILITY)) {
                if (!INVIS_PLAYER.contains(player.getUniqueId())) { //Only check once. Save some performance.
                    INVIS_PLAYER.add(player.getUniqueId()); //Only add the UUID once
                }

                /**
                 * Code below could affect the performance
                 */
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        /*for (Player players : Bukkit.getOnlinePlayers()) {
                            plugin.getSidebar().getBoards().get(players.getUniqueId()).getTab().addUpdates(Bukkit.getOnlinePlayers());
                        }*/

                    }
                }.runTaskLater(this.getInstance(), 10);
            }
        }
    }

    @EventHandler
    public void onExpire(PotionEffectExpireEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getEffect().getType().equals(PotionEffectType.INVISIBILITY)) {
                Player player = (Player) event.getEntity();
                UUID uuid = player.getUniqueId();
                if (INVIS_PLAYER.contains(uuid)) { //First check

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            /*for (Player players : Bukkit.getOnlinePlayers()) {
                                plugin.getSidebar().getBoards().get(players.getUniqueId()).getTab().addUpdates(Bukkit.getOnlinePlayers());
                            }*/

                            INVIS_PLAYER.removeIf(uuid -> INVIS_PLAYER.contains(uuid));
                        }
                    }.runTaskLater(this.getInstance(), 20);
                }
            }
        }
    }

    @EventHandler
    public void onEffectExtend(PotionEffectRemoveEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getEffect().getType().equals(PotionEffectType.INVISIBILITY)) {
                Player player = (Player) event.getEntity();
                UUID uuid = player.getUniqueId();
                if (INVIS_PLAYER.contains(uuid)) { //First check

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            /*for (Player players : Bukkit.getOnlinePlayers()) {
                                plugin.getSidebar().getBoards().get(players.getUniqueId()).getTab().addUpdates(Bukkit.getOnlinePlayers());
                            }*/

                            INVIS_PLAYER.removeIf(uuid -> INVIS_PLAYER.contains(uuid));
                        }
                    }.runTaskLater(this.getInstance(), 20);
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND)) {
            Player player = event.getPlayer();
            int visibleDistance = Bukkit.getViewDistance() * 8; //Server distance * 4 = a chunk

            // Fix the visibility issue one tick later
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.getInstance(), () -> {
                // Refresh nearby clients
                final List<Player> nearby = getPlayersWithin(player, visibleDistance);

                // Hide every player
                updateEntities(nearby, false);

                // Then show them again
                Bukkit.getScheduler().scheduleSyncDelayedTask(FixInvisListener.this.getInstance(), () -> updateEntities(nearby, true), 1);

            }, 15);
        }
    }

    private void updateEntities(List<Player> players, boolean visible) {
        // Hide every player
        for (Player observer : players) {
            for (Player player : players) {
                if (observer.getEntityId() != player.getEntityId()) {
                    if (visible)
                        observer.showPlayer(player);
                    else
                        observer.hidePlayer(player);
                }
            }
        }
    }

    private List<Player> getPlayersWithin(Player player, int distance) {
        List<Player> res = new ArrayList<>();
        int d2 = distance * distance;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld() == player.getWorld()
                    && p.getLocation().distanceSquared(player.getLocation()) <= d2) {
                PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(p.getUniqueId());
                if (targetProfile != null && !targetProfile.isInStaffMode()) {
                    res.add(p);
                }
            }
        }
        return res;
    }



    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (INVIS_PLAYER.contains(event.getPlayer().getUniqueId())) {
            INVIS_PLAYER.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (INVIS_PLAYER.contains(event.getEntity().getUniqueId())) {
            INVIS_PLAYER.remove(event.getEntity().getUniqueId());
        }
    }

}
