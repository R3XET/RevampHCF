package eu.revamp.hcf.handlers.fix;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.Bukkit;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class InfinityArrowFixListener extends Handler implements Listener
{
    public InfinityArrowFixListener(RevampHCF plugin) {
        super(plugin);
    }
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Arrow) {
            Arrow arrow = (Arrow)entity;
            if (!(arrow.getShooter() instanceof Player) || ((CraftArrow)arrow).getHandle().fromPlayer == 2) {
                arrow.remove();
            }
        }
    }
}
