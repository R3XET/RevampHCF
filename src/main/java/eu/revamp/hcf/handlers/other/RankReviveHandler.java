package eu.revamp.hcf.handlers.other;

import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import java.util.HashMap;
import eu.revamp.hcf.RevampHCF;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class RankReviveHandler extends Handler implements Listener
{
    private final Map<UUID, Long> cooldownData;
    
    public RankReviveHandler(RevampHCF plugin) {
        super(plugin);
        this.cooldownData = new HashMap<>();
    }
    
    @Override
    public void enable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.loadData(player);
        }
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.saveData(player);
        }
    }
    
    public void loadData(Player player) {
        if (this.getInstance().getConfig().contains(player.getUniqueId().toString())) {
            this.cooldownData.put(player.getUniqueId(), this.getInstance().getConfig().getLong(player.getUniqueId().toString()));
        }
    }
    
    public void saveData(Player player) {
        if (this.cooldownData.containsKey(player.getUniqueId())) {
            this.getInstance().getConfig().set(player.getUniqueId().toString(), this.cooldownData.get(player.getUniqueId()));
            try {
                this.getInstance().getConfig().save(this.getInstance().getConfig().getFile());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean isActive(Player player) {
        return this.cooldownData.containsKey(player.getUniqueId()) && System.currentTimeMillis() < this.cooldownData.get(player.getUniqueId());
    }
    
    public void apply(Player player) {
        PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(player.getUniqueId());
        if (targetProfile.getHighestRank().getDisplayName().equalsIgnoreCase("Revamp")) {
            this.cooldownData.put(player.getUniqueId(), System.currentTimeMillis() + 300000L);
        }
    }
    
    public long getMillisecondsLeft(Player player) {
        if (this.cooldownData.containsKey(player.getUniqueId())) {
            return Math.max(this.cooldownData.get(player.getUniqueId()) - System.currentTimeMillis(), 0L);
        }
        return 0L;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        this.loadData(player);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        this.saveData(player);
    }
}
