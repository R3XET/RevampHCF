package eu.revamp.hcf.handlers.fix;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class MonsterSpawnHandler extends Handler implements Listener
{
    public MonsterSpawnHandler(RevampHCF plugin) {
        super(plugin);
    }
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if ((e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CHUNK_GEN) && e.getEntity() instanceof Monster) {
            e.setCancelled(true);
        }
    }
}
