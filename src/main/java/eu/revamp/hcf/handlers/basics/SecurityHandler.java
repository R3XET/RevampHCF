package eu.revamp.hcf.handlers.basics;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.message.MessageUtils;
import eu.revamp.system.api.player.PlayerData;
import eu.revamp.system.plugin.RevampSystem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

@Getter @Setter
public class SecurityHandler extends Handler implements Listener
{
    public SecurityHandler(RevampHCF plugin) {
        super(plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player)event.getDamager();
            if (damager.isOp()) return;
            ItemStack item = damager.getItemInHand();
            PlayerData targetProfile = RevampSystem.getINSTANCE().getPlayerManagement().getPlayerData(damager.getUniqueId());
            if (item == null) return;
            if (item.getEnchantments() == null) return;
            if (item.getEnchantments().isEmpty()) return;
            if (item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > 10) {
                MessageUtils.sendMessage("§4" + damager.getName() + " §ctried to use sword that isn't supposed to be used §7(§cSharpness " + item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) + "§7)", "revamphcf.staff");
                item.removeEnchantment(Enchantment.DAMAGE_ALL);
                damager.updateInventory();
                targetProfile.setVanished(true);
                event.setCancelled(true);
            }
        }
    }
}
