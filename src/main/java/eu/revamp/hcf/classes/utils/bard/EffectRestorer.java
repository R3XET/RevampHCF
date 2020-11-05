package eu.revamp.hcf.classes.utils.bard;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import eu.revamp.hcf.classes.Bard;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.classes.utils.events.ArmorClassUnequipEvent;
import eu.revamp.spigot.events.potion.PotionEffectExpireEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.UUID;

public class EffectRestorer implements Listener
{
    private Table<UUID, PotionEffectType, PotionEffect> restores;
    
    public EffectRestorer(RevampHCF plugin) {
        this.restores = HashBasedTable.create();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onArmorClassUnequip(ArmorClassUnequipEvent event) {
        this.restores.rowKeySet().remove(event.getPlayer().getUniqueId());
    }
    
    public void setRestoreEffect(Player player, PotionEffect effect) {
        boolean shouldCancel = true;
        Collection<PotionEffect> activeList = player.getActivePotionEffects();
        for (PotionEffect active : activeList) {
            if (!active.getType().equals(effect.getType())) continue;
            if (effect.getAmplifier() < active.getAmplifier()) return;
            if (effect.getAmplifier() == active.getAmplifier() && effect.getDuration() < active.getDuration()) return;
            this.restores.put(player.getUniqueId(), active.getType(), active);
            shouldCancel = false;
            break;
        }
        player.addPotionEffect(effect, true);
        if (shouldCancel && effect.getDuration() > Bard.HELD_EFFECT_DURATION_TICKS && effect.getDuration() < Bard.DEFAULT_MAX_DURATION) {
            this.restores.remove(player.getUniqueId(), effect.getType());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPotionEffectExpire(PotionEffectExpireEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            PotionEffect previous = this.restores.remove(player.getUniqueId(), event.getEffect().getType());
            if (previous != null) {
                event.setCancelled(true);
                new BukkitRunnable() {
                    public void run() {
                        player.addPotionEffect(previous, true);
                    }
                }.runTask(RevampHCF.getInstance());
            }
        }
    }
}
