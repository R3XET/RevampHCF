package eu.revamp.hcf.handlers.settings;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class KitMapHandler extends Handler implements Listener
{
    public KitMapHandler(RevampHCF plugin) {
        super(plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
    }
    
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
            if (event.isCancelled()) return;
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            if (item.getType() == Material.POTION) {
                new BukkitRunnable() {
                    public void run() {
                        player.setItemInHand(new ItemStack(Material.AIR));
                        player.updateInventory();
                    }
                }.runTaskLater(this.getInstance(), 1L);
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = player.getItemInHand();
        Block block = event.getClickedBlock();
        if (action == Action.RIGHT_CLICK_BLOCK) {
            if (block.getType() == Material.ANVIL || block.getType() == Material.ENCHANTMENT_TABLE) {
                event.setCancelled(true);
            }
            if (item == null || item.getType() == Material.AIR) return;
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (RevampHCF.getInstance().getConfiguration().isKitMap()) {
            Block block = event.getBlock();
            if (event.isCancelled()) return;
            if (block.getType() != Material.WEB) return;
            new BukkitRunnable() {
                public void run() {
                    block.setType(Material.AIR);
                }
            }.runTaskLater(this.getInstance(), 200L);
        }
    }
}
