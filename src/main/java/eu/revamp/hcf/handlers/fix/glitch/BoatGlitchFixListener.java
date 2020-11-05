package eu.revamp.hcf.handlers.fix.glitch;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.Bukkit;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class BoatGlitchFixListener extends Handler implements Listener
{
    public BoatGlitchFixListener(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onVehicleCreate(VehicleCreateEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (vehicle instanceof Boat) {
            Boat boat = (Boat)vehicle;
            Block belowBlock = boat.getLocation().add(0.0, -1.0, 0.0).getBlock();
            if (belowBlock.getType() != Material.WATER && belowBlock.getType() != Material.STATIONARY_WATER) {
                boat.remove();
            }
        }
    }
}
