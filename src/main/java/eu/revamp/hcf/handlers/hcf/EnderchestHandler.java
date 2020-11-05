package eu.revamp.hcf.handlers.hcf;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.EnderChest;

public class EnderchestHandler extends Handler implements Listener {

    public EnderchestHandler(RevampHCF instance) {
        super(instance);
    }
    @Override
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    @EventHandler
    public void onEnderChestOpen(PlayerInteractEvent event) {
        if (!this.getInstance().getConfiguration().isKitMap() && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ENDER_CHEST) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!this.getInstance().getConfiguration().isKitMap() && event.getInventory() instanceof EnderChest) {
            event.setCancelled(true);
        }
    }
}
