package eu.revamp.hcf.handlers.fix.security;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.Bukkit;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class AntiCreativeBypassHandler extends Handler implements Listener
{
    public AntiCreativeBypassHandler(RevampHCF plugin) {
        super(plugin);
    }
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().isOp()) {
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockPlaceCreative(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && !player.hasPermission("hcf.staffplus")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You are not allowed to be in gamemode! Setting you to default gamemode!");
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockBreakCreative(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE && !player.hasPermission("hcf.staffplus")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You are not allowed to be in gamemode! Setting you to default gamemode!");
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryCreative(InventoryCreativeEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (humanEntity instanceof Player && !humanEntity.hasPermission("hcf.staffplus")) {
            event.setCancelled(true);
            humanEntity.setGameMode(GameMode.SURVIVAL);
        }
    }
}
