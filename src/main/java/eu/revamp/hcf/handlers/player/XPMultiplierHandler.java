package eu.revamp.hcf.handlers.player;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class XPMultiplierHandler extends Handler implements Listener
{
    public XPMultiplierHandler(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onEntityDeath(EntityDeathEvent event) {
        double amount = event.getDroppedExp();
        Player killer = event.getEntity().getKiller();
        if (killer != null && amount > 0.0) {
            ItemStack stack = killer.getItemInHand();
            if (stack != null && stack.getType() != Material.AIR) {
                int enchantmentLevel = stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
                if (enchantmentLevel > 0L) {
                    double multiplier = enchantmentLevel * RevampHCF.getInstance().getConfiguration().getLootingXPMultiplier();
                    int result = (int)Math.ceil(amount * multiplier);
                    event.setDroppedExp(result);
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        double amount = event.getExpToDrop();
        Player player = event.getPlayer();
        ItemStack stack = player.getItemInHand();
        if (stack != null && stack.getType() != Material.AIR && amount > 0.0) {
            int enchantmentLevel = stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
            if (enchantmentLevel > 0) {
                double multiplier = enchantmentLevel * RevampHCF.getInstance().getConfiguration().getFortuneXPMultiplier();
                int result = (int)Math.ceil(amount * multiplier);
                event.setExpToDrop(result);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerPickupExp(PlayerExpChangeEvent event) {
        double amount = event.getAmount();
        if (amount > 0.0) {
            int result = (int)Math.ceil(amount * RevampHCF.getInstance().getConfiguration().getGlobalXPMultiplier());
            event.setAmount(result);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerFish(PlayerFishEvent event) {
        double amount = event.getExpToDrop();
        if (amount > 0.0) {
            amount = Math.ceil(amount * RevampHCF.getInstance().getConfiguration().getFishingXPMultiplier());
            ProjectileSource projectileSource = event.getHook().getShooter();
            if (projectileSource instanceof Player) {
                ItemStack stack = ((Player)projectileSource).getItemInHand();
                int enchantmentLevel = stack.getEnchantmentLevel(Enchantment.LUCK);
                if (enchantmentLevel > 0L) {
                    amount = Math.ceil(amount * (enchantmentLevel * RevampHCF.getInstance().getConfiguration().getLuckXPMultiplier()));
                }
            }
            event.setExpToDrop((int)amount);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        double amount = event.getExpToDrop();
        if (amount > 0.0) {
            double multiplier = RevampHCF.getInstance().getConfiguration().getSmeltXPMultiplier();
            int result = (int)Math.ceil(amount * multiplier);
            event.setExpToDrop(result);
        }
    }
}
