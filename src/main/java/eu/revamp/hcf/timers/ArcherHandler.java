package eu.revamp.hcf.timers;

import eu.revamp.hcf.classes.Archer;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import java.util.UUID;
import eu.revamp.hcf.utils.timer.events.TimerExpireEvent;
import java.util.concurrent.TimeUnit;

import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.timer.PlayerTimer;

public class ArcherHandler extends PlayerTimer implements Listener
{
    private Double ARCHER_DAMAGE;
    
    public ArcherHandler() {
        super("Archer Mark", TimeUnit.SECONDS.toMillis(RevampHCF.getInstance().getConfig().getInt("COOLDOWNS.ARCHER_MARK")));
        this.defaultCooldown = TimeUnit.SECONDS.toMillis(10L);
        this.ARCHER_DAMAGE = 0.25;
    }
    
    @EventHandler
    public void onExpire(TimerExpireEvent event) {
        if (event.getUserUUID().isPresent() && event.getTimer().equals(this)) {
            UUID userUUID = event.getUserUUID().get();
            Player player = Bukkit.getPlayer(userUUID);
            if (player == null) return;
            Archer.TAGGED.remove(player.getUniqueId());
        }
    }
    
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player entity = (Player)event.getEntity();
            Entity damager = event.getDamager();
            if (this.getRemaining(entity) > 0L) {
                double damage = event.getDamage() * this.ARCHER_DAMAGE;
                event.setDamage(event.getDamage() + damage);
            }
        }
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            if (event.getEntity() instanceof Player) return;
            Player entity = (Player)event.getEntity();
            Entity damager = (Entity)((Arrow)event.getDamager()).getShooter();
            if (damager instanceof Player && this.getRemaining(entity) > 0L) {
                if (Archer.TAGGED.get(entity.getUniqueId()).equals(damager.getUniqueId())) {
                    this.setCooldown(entity, entity.getUniqueId());
                }
                double damage = event.getDamage() * this.ARCHER_DAMAGE;
                event.setDamage(event.getDamage() + damage);
            }
        }
    }
}
