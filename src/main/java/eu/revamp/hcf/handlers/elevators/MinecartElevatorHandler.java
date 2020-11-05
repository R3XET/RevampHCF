package eu.revamp.hcf.handlers.elevators;

import org.bukkit.event.EventHandler;
import org.bukkit.Sound;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Minecart;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.Bukkit;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class MinecartElevatorHandler extends Handler implements Listener
{
    public MinecartElevatorHandler(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public void disable() {
    }
    
    public void EventListener() {
    }
    
    @EventHandler
    public void onEnterInTheMinecart(VehicleEnterEvent e) {
        if (!(e.getVehicle() instanceof Minecart) || !(e.getEntered() instanceof Player)) return;
        Player p = (Player)e.getEntered();
        Location minecart = e.getVehicle().getLocation();
        Location loc = new Location(p.getWorld(), minecart.getBlockX(), minecart.getBlockY(), minecart.getBlockZ());
        Material material = loc.getBlock().getType();
        if (material.equals(Material.FENCE_GATE) || material.equals(Material.SIGN_POST)) {
            e.setCancelled(true);
            if (p.isSneaking()) {
                p.teleport(this.getCoords(loc, loc.getBlockY(), 254.0));
                p.sendMessage("§eYou have been teleported to the top");
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 10.0f, 1.0f);
            }
        }
    }
    
    public Location getCoords(Location loc, int min, double d) {
        for (int tp = min; tp < d; ++tp) {
            Material material1 = new Location(loc.getWorld(), loc.getBlockX(), tp, loc.getBlockZ()).getBlock().getType();
            Material material2 = new Location(loc.getWorld(), loc.getBlockX(), tp + 1, loc.getBlockZ()).getBlock().getType();
            if (material1.equals(Material.AIR) && material2.equals(Material.AIR)) {
                return new Location(loc.getWorld(), loc.getBlockX(), tp, loc.getBlockZ());
            }
        }
        return new Location(loc.getWorld(), loc.getBlockX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()), loc.getBlockZ());
    }
}
