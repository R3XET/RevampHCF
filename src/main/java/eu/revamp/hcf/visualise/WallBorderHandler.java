package eu.revamp.hcf.visualise;

import java.util.*;

import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.factions.Faction;

import eu.revamp.hcf.games.cuboid.Cuboid;
import org.bukkit.util.Vector;
import eu.revamp.hcf.factions.type.RoadFaction;
import eu.revamp.hcf.factions.type.ClaimableFaction;
import eu.revamp.hcf.factions.utils.zone.ClaimZone;

import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import org.bukkit.scheduler.BukkitTask;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class WallBorderHandler extends Handler implements Listener
{
    private static int BORDER_PURPOSE_INFORM_THRESHOLD = 66;
    private static int WALL_BORDER_HEIGHT_BELOW_DIFF = 3;
    private static int WALL_BORDER_HEIGHT_ABOVE_DIFF = 4;
    private static int WALL_BORDER_HORIZONTAL_DISTANCE = 7;
    private boolean useTaskInstead;
    private Map<UUID, BukkitTask> wallBorderTask;
    private Random random;

    public WallBorderHandler(RevampHCF plugin) {
        super(plugin);
        this.wallBorderTask = new HashMap<>();
        this.random = new Random();
        this.useTaskInstead = (random.nextBoolean() && false);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, RevampHCF.getInstance());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!this.useTaskInstead) return;
        BukkitTask task = this.wallBorderTask.remove(event.getPlayer().getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (this.useTaskInstead) {
            this.wallBorderTask.put(player.getUniqueId(), new WarpTimerRunnable(this, player).runTaskTimerAsynchronously(RevampHCF.getInstance(), 3L, 3L));
            return;
        }
        Location now = player.getLocation();
        new BukkitRunnable() {
            public void run() {
                Location location = player.getLocation();
                if (now.equals(location)) {
                    WallBorderHandler.this.handlePositionChanged(player, location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
                }
            }
        }.runTaskLater(RevampHCF.getInstance(), 4L);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (this.useTaskInstead) return;
        Location to = event.getTo();
        int toX = to.getBlockX();
        int toY = to.getBlockY();
        int toZ = to.getBlockZ();
        Location from = event.getFrom();
        if (from.getBlockX() != toX || from.getBlockY() != toY || from.getBlockZ() != toZ) {
            this.handlePositionChanged(event.getPlayer(), to.getWorld(), toX, toY, toZ);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        this.onPlayerMove(event);
    }
    
    private void handlePositionChanged(Player player, World toWorld, int toX, int toY, int toZ) {
        HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        VisualType visualType;
        if (RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().getRemaining(player) > 0L && data.getSpawnTagCooldown() > 0L) {
            visualType = VisualType.SPAWN_BORDER;
        }
        else {
            if (RevampHCF.getInstance().getConfiguration().isKitMap() || RevampHCF.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().getRemaining(player) <= 0L || data.getPvpTimerCooldown() <= 0) return;
            visualType = VisualType.CLAIM_BORDER;
        }
        RevampHCF.getInstance().getHandlerManager().getVisualiseHandler().clearVisualBlocks(player, visualType, visualBlock -> {
            assert visualBlock != null;
            Location other = visualBlock.getLocation();
            return other.getWorld().equals(toWorld) && (Math.abs(toX - other.getBlockX()) > WallBorderHandler.WALL_BORDER_HORIZONTAL_DISTANCE || Math.abs(toY - other.getBlockY()) > WallBorderHandler.WALL_BORDER_HEIGHT_ABOVE_DIFF || Math.abs(toZ - other.getBlockZ()) > WallBorderHandler.WALL_BORDER_HORIZONTAL_DISTANCE);
        });
        int minHeight = toY - WallBorderHandler.WALL_BORDER_HEIGHT_BELOW_DIFF;
        int maxHeight = toY + WallBorderHandler.WALL_BORDER_HEIGHT_ABOVE_DIFF;
        int minX = toX - WallBorderHandler.WALL_BORDER_HORIZONTAL_DISTANCE;
        int maxX = toX + WallBorderHandler.WALL_BORDER_HORIZONTAL_DISTANCE;
        int minZ = toZ - WallBorderHandler.WALL_BORDER_HORIZONTAL_DISTANCE;
        int maxZ = toZ + WallBorderHandler.WALL_BORDER_HORIZONTAL_DISTANCE;
        Collection<ClaimZone> added = new HashSet<>();
        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                Faction faction = RevampHCF.getInstance().getFactionManager().getFactionAt(toWorld, x, z);
                if (faction instanceof ClaimableFaction) {
                    if (visualType == VisualType.SPAWN_BORDER) {
                        if (!faction.isSafezone()) continue;
                    }
                    else if (visualType == VisualType.CLAIM_BORDER) {
                        if (faction instanceof RoadFaction) continue;
                        if (faction.isSafezone()) continue;
                    }
                    Collection<ClaimZone> claims = ((ClaimableFaction)faction).getClaims();
                    for (ClaimZone claim : claims) {
                        if (toWorld.equals(claim.getWorld())) {
                            added.add(claim);
                        }
                    }
                }
            }
        }
        if (!added.isEmpty()) {
            int generated = 0;
            Iterator<ClaimZone> iterator = added.iterator();
            while (iterator.hasNext()) {
                ClaimZone claim2 = iterator.next();
                Collection<Vector> edges = claim2.edges();
                for (Vector edge : edges) {
                    if (Math.abs(edge.getBlockX() - toX) > WallBorderHandler.WALL_BORDER_HORIZONTAL_DISTANCE) continue;
                    if (Math.abs(edge.getBlockZ() - toZ) > WallBorderHandler.WALL_BORDER_HORIZONTAL_DISTANCE) continue;
                    Location location = edge.toLocation(toWorld);
                    if (location == null) continue;
                    Location first = location.clone();
                    first.setY(minHeight);
                    Location second = location.clone();
                    second.setY(maxHeight);
                    if (this.useTaskInstead) {
                        generated += RevampHCF.getInstance().getHandlerManager().getVisualiseHandler().generateAsync(player, new Cuboid(first, second), visualType, false).size();
                    }
                    else {
                        generated += RevampHCF.getInstance().getHandlerManager().getVisualiseHandler().generate(player, new Cuboid(first, second), visualType, false).size();
                    }
                }
                iterator.remove();
            }
        }
    }
    
    private static class WarpTimerRunnable extends BukkitRunnable
    {
        private WallBorderHandler listener;
        private Player player;
        private double lastX;
        private double lastY;
        private double lastZ;
        
        public WarpTimerRunnable(WallBorderHandler listener, Player player) {
            this.lastX = Double.MAX_VALUE;
            this.lastY = Double.MAX_VALUE;
            this.lastZ = Double.MAX_VALUE;
            this.listener = listener;
            this.player = player;
        }
        
        public void run() {
            Location location = this.player.getLocation();
            double x = location.getBlockX();
            double y = location.getBlockY();
            double z = location.getBlockZ();
            if (this.lastX == x && this.lastY == y && this.lastZ == z) return;
            this.lastX = x;
            this.lastY = y;
            this.lastZ = z;
            this.listener.handlePositionChanged(this.player, this.player.getWorld(), (int)x, (int)y, (int)z);
        }
        
        public synchronized void cancel() throws IllegalStateException {
            super.cancel();
            this.listener = null;
            this.player = null;
        }
    }
}
