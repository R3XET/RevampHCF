package eu.revamp.hcf.handlers.essentials;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
@Getter @Setter
public class BackHandler extends Handler implements Listener
{
    private Map<String, Location> playerLocation;
    
    public BackHandler(RevampHCF instance) {
        super(instance);
        setPlayerLocation(new HashMap<>());
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
        getPlayerLocation().clear();
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
        Player player = event.getPlayer();
        RevampHCF.getInstance().getHandlerManager().getBackHandler().getPlayerLocation().remove(player.getName());
        getPlayerLocation().put(player.getName(), player.getLocation());
    }
    
    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player)event.getEntity();
        RevampHCF.getInstance().getHandlerManager().getBackHandler().getPlayerLocation().remove(player.getName());
        getPlayerLocation().put(player.getName(), player.getLocation());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        getPlayerLocation().remove(player.getName());
    }
}
