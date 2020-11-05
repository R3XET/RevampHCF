package eu.revamp.hcf.handlers.essentials;

import eu.revamp.hcf.RevampHCF;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.ArrayList;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;
@Getter @Setter
public class GodHandler extends Handler implements Listener
{
    private ArrayList<UUID> players;
    
    public GodHandler(RevampHCF instance) {
        super(instance);
        setPlayers(new ArrayList<>());
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
        getPlayers().clear();
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        getPlayers().remove(player.getUniqueId());
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            if (getPlayers().contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player)event.getEntity();
            Player damager = (Player)event.getDamager();
            if (getPlayers().contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
            if (getPlayers().contains(damager.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}
