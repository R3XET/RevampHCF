package eu.revamp.hcf.handlers.signs;

import java.util.Map;
import java.util.Iterator;
import java.util.Collection;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Arrays;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.block.Sign;
import com.google.common.collect.HashMultimap;

import java.util.UUID;
import com.google.common.collect.Multimap;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class SignHandler extends Handler implements Listener
{
    private Multimap<UUID, SignChange> signUpdateMap;
    
    public SignHandler(RevampHCF plugin) {
        super(plugin);
        this.signUpdateMap = HashMultimap.create();
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, RevampHCF.getInstance());
    }
    
    @Override
    public void disable() {
        this.cancelTasks(null);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerQuitEvent event) {
        this.cancelTasks(event.getPlayer(), null, false);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.cancelTasks(event.getPlayer(), null, false);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        this.cancelTasks(event.getPlayer(), null, false);
    }
    
    public boolean showLines(Player player, Sign sign, String[] newLines, long ticks, boolean forceChange) {
        String[] lines = sign.getLines();
        if (Arrays.equals(lines, newLines)) {
            return false;
        }
        Collection<SignChange> signChanges = this.getSignChanges(player);
        Iterator<SignChange> iterator = signChanges.iterator();
        while (iterator.hasNext()) {
            SignChange signChange = iterator.next();
            if (signChange.sign.equals(sign)) {
                if (!forceChange && Arrays.equals(signChange.newLines, newLines)) {
                    return false;
                }
                signChange.runnable.cancel();
                iterator.remove();
                break;
            }
        }
        Location location = sign.getLocation();
        player.sendSignChange(location, newLines);
        SignChange signChange2;
        if (signChanges.add(signChange2 = new SignChange(sign, newLines))) {
            Block block = sign.getBlock();
            BlockState previous = block.getState();
            BukkitRunnable runnable = new BukkitRunnable() {
                public void run() {
                    if (SignHandler.this.signUpdateMap.remove(player.getUniqueId(), signChange2) && previous.equals(block.getState())) {
                        player.sendSignChange(location, lines);
                    }
                }
            };
            runnable.runTaskLater(RevampHCF.getInstance(), ticks);
            signChange2.runnable = runnable;
        }
        return true;
    }
    
    public Collection<SignChange> getSignChanges(Player player) {
        return this.signUpdateMap.get(player.getUniqueId());
    }
    
    public void cancelTasks(Sign sign) {
        Iterator<SignChange> iterator = this.signUpdateMap.values().iterator();
        while (iterator.hasNext()) {
            SignChange signChange = iterator.next();
            if (sign == null || signChange.sign.equals(sign)) {
                signChange.runnable.cancel();
                signChange.sign.update();
                iterator.remove();
            }
        }
    }
    
    public void cancelTasks(Player player, Sign sign, boolean revertLines) {
        UUID uuid = player.getUniqueId();
        Iterator<Map.Entry<UUID, SignChange>> iterator = this.signUpdateMap.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, SignChange> entry = iterator.next();
            if (entry.getKey().equals(uuid)) {
                SignChange signChange = entry.getValue();
                if (sign != null && !signChange.sign.equals(sign)) {
                    continue;
                }
                if (revertLines) {
                    player.sendSignChange(signChange.sign.getLocation(), signChange.sign.getLines());
                }
                signChange.runnable.cancel();
                iterator.remove();
            }
        }
    }
    
    private static class SignChange
    {
        public BukkitRunnable runnable;
        public Sign sign;
        public String[] newLines;
        
        public SignChange(Sign sign, String[] newLines) {
            this.sign = sign;
            this.newLines = newLines;
        }
    }
}
