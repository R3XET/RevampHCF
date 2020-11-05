package eu.revamp.hcf.handlers.fix;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class MobFarmListener extends Handler implements Listener
{
    public MobFarmListener(RevampHCF plugin) {
        super(plugin);
    }
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();
        final Player killer = entity.getKiller();
        if (!(entity instanceof Player) && killer != null && entity.getKiller().hasPermission("mobfarm.access")) {
            if ((entity instanceof Creature || entity instanceof Monster || entity instanceof Slime) && entity.getKiller() != null) {
                killer.giveExp((int)Math.round(event.getDroppedExp() * RevampHCF.getInstance().getConfig().getDouble("EXP.GLOBAL")));
                if (!killer.getInventory().addItem(event.getDrops().toArray(new ItemStack[0])).isEmpty()) {
                    killer.sendMessage(ChatColor.RED + "Your inventory is full.");
                }
                event.getDrops().clear();
                event.setDroppedExp(0);
            }
        }
    }
}
