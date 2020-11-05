package eu.revamp.hcf.handlers.blocks;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.HashMap;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.Material;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class BlockPickupHandler extends Handler implements Listener
{
    private Map<UUID, List<Material>> material;
    
    public BlockPickupHandler(RevampHCF plugin) {
        super(plugin);
        this.material = new HashMap<>();
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, RevampHCF.getInstance());
    }
    
    public HashMap<UUID, List<Material>> getListMaterial() {
        return (HashMap<UUID, List<Material>>) this.material;
    }
    
    @EventHandler
    public void onPlayerPickupItems(PlayerPickupItemEvent event) {
        if (this.material.containsKey(event.getPlayer().getUniqueId()) && this.material.get(event.getPlayer().getUniqueId()).contains(event.getItem().getItemStack().getType())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerJoinPickupBlock(PlayerJoinEvent event) {
        this.material.remove(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onPlayerQuitPickupBlock(PlayerQuitEvent event) {
        this.material.remove(event.getPlayer().getUniqueId());
    }
}
