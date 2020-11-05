package eu.revamp.hcf.handlers.games;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Bukkit;
import java.util.HashMap;
import eu.revamp.hcf.RevampHCF;
import java.util.Map;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class EndPortalHandler extends Handler implements Listener
{
    private Map<String, LocationPair> playerSelections;
    
    public EndPortalHandler(RevampHCF plugin) {
        super(plugin);
        this.playerSelections = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.hasItem() && e.getClickedBlock() != null) {
            if (!e.getPlayer().hasPermission("hcf.staff")) return;
            ItemStack itemStack = e.getItem();
            Block b = e.getClickedBlock();
            if (itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("§cEndPortal Maker")) {
                LocationPair locationPair = this.playerSelections.get(e.getPlayer().getName());
                if (locationPair == null) {
                    locationPair = new LocationPair(null, null);
                    this.playerSelections.put(e.getPlayer().getName(), locationPair);
                }
                if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (b.getType() != Material.ENDER_PORTAL_FRAME) {
                        e.getPlayer().sendMessage(ChatColor.RED + "You must select an end portal frame.");
                        return;
                    }
                    locationPair.setFirstLoc(b.getLocation());
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Successfully set the first location.");
                }
                else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (b.getType() != Material.ENDER_PORTAL_FRAME) {
                        e.getPlayer().sendMessage(ChatColor.RED + "You must select an end portal frame.");
                        return;
                    }
                    if (locationPair.getFirstLoc() == null) {
                        e.getPlayer().sendMessage(ChatColor.RED + "Please set the first location (by left clicking the end portal frame).");
                        return;
                    }
                    locationPair.setSecondLoc(b.getLocation());
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Successfully set the second location.");
                    Location firstLoc = locationPair.getFirstLoc();
                    Location secondLoc = locationPair.getSecondLoc();
                    if (firstLoc.distance(secondLoc) > 6.0) {
                        e.getPlayer().sendMessage(ChatColor.RED + "You cannot create an end portal that big.");
                        return;
                    }
                    if (firstLoc.getBlockY() != secondLoc.getBlockY()) {
                        e.getPlayer().sendMessage(ChatColor.RED + "Make sure that the portals have the same elevation.");
                        return;
                    }
                    int minX = Math.min(firstLoc.getBlockX(), secondLoc.getBlockX());
                    int minY = Math.min(firstLoc.getBlockY(), secondLoc.getBlockY());
                    int minZ = Math.min(firstLoc.getBlockZ(), secondLoc.getBlockZ());
                    int maxX = Math.max(firstLoc.getBlockX(), secondLoc.getBlockX());
                    int maxY = Math.max(firstLoc.getBlockY(), secondLoc.getBlockY());
                    int maxZ = Math.max(firstLoc.getBlockZ(), secondLoc.getBlockZ());
                    for (int x = minX; x <= maxX; ++x) {
                        for (int y = minY; y <= maxY; ++y) {
                            for (int z = minZ; z <= maxZ; ++z) {
                                Block block = b.getWorld().getBlockAt(x, y, z);
                                if (block.isEmpty()) {
                                    block.setType(Material.ENDER_PORTAL);
                                }
                            }
                        }
                    }
                    e.setCancelled(true);
                    new BukkitRunnable() {
                        public void run() {
                            e.getPlayer().setItemInHand(null);
                            e.getPlayer().updateInventory();
                        }
                    }.runTask(this.getInstance());
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Created an end portal");
                    this.playerSelections.remove(e.getPlayer().getName());
                }
            }
        }
    }
    
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack itemStack = e.getItemDrop().getItemStack();
        if (itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("§cEndPortal Maker")) {
            e.getItemDrop().remove();
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        ItemStack i = e.getPlayer().getItemInHand();
        if (i.getType().equals(Material.BLAZE_ROD) && i.getItemMeta().hasDisplayName() && i.getItemMeta().getDisplayName().equals("§cEndPortal Maker")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        this.playerSelections.remove(e.getPlayer().getName());
    }
    
    @EventHandler
    public void onKick(PlayerKickEvent e) {
        this.playerSelections.remove(e.getPlayer().getName());
    }

    @Getter @Setter
    private static class LocationPair
    {
        private Location firstLoc;
        private Location secondLoc;
        
        public LocationPair(Location firstLoc, Location secondLoc) {
            this.firstLoc = firstLoc;
            this.secondLoc = secondLoc;
        }
    }
}
