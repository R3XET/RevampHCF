package eu.revamp.hcf.handlers.fix;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.player.PlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class IllegalEnchantHandler extends Handler implements Listener
{
    public IllegalEnchantHandler(RevampHCF plugin) {
        super(plugin);
    }
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        if (attacker instanceof Player && PlayerUtils.getFinalAttacker(event, false) != null) {
            Player damager = (Player)attacker;
            ItemStack itemInHand = damager.getItemInHand();
            if (event.getEntityType().equals(EntityType.PLAYER)) {
                if (itemInHand.getType() == null || itemInHand.getType().equals(Material.AIR)) return;
                if ((itemInHand.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > 5 || itemInHand.getEnchantmentLevel(Enchantment.KNOCKBACK) > 5) && !damager.isOp()) {
                    event.setCancelled(true);
                    damager.getInventory().removeItem(itemInHand);
                    damager.sendMessage(ChatColor.YELLOW + "Your item has been removed from your hand.");
                }
            }
        }
    }
}
