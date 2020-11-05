package eu.revamp.hcf.handlers.essentials;

import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBurnEvent;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class HCFHandler extends Handler implements Listener
{
    public HCFHandler(RevampHCF instance) {
        super(instance);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.isCancelled()) return;
        event.blockList().clear();
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() != BlockIgniteEvent.IgniteCause.SPREAD) return;
        event.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }
}
