package eu.revamp.hcf.handlers.fix;

import eu.revamp.hcf.RevampHCF;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;
@Getter @Setter
public class NoPotLagHandler extends Handler implements Listener
{
    private double speed;
    
    public NoPotLagHandler(RevampHCF plugin) {
        super(plugin);
        setSpeed(RevampHCF.getInstance().getConfig().getDouble("POTIONS-SPEED"));
    }
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    @EventHandler
    void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntityType() == EntityType.SPLASH_POTION) {
            Projectile projectile = event.getEntity();
            if (projectile.getShooter() instanceof Player && ((Player)projectile.getShooter()).isSprinting()) {
                Vector velocity = projectile.getVelocity();
                velocity.setY(velocity.getY() - getSpeed());
                projectile.setVelocity(velocity);
            }
        }
    }
    
    @EventHandler
    void onPotionSplash(PotionSplashEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player)event.getEntity().getShooter();
            if (shooter.isSprinting() && event.getIntensity(shooter) > 0.5) {
                event.setIntensity(shooter, 1.0);
            }
        }
    }
}
