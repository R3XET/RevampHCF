package eu.revamp.hcf.handlers.player;

import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;
import org.bukkit.event.block.Action;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class ExpHandler extends Handler implements Listener
{
    public ExpHandler(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onExpSplash(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (player.getItemInHand().getType() != Material.EXP_BOTTLE) return;
        if (!player.getItemInHand().hasItemMeta()) return;
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && player.getItemInHand().getItemMeta().getDisplayName().equals("§b§lEXP Bottle")) {
            event.setCancelled(true);
            int exp = Integer.parseInt(ChatColor.stripColor(player.getItemInHand().getItemMeta().getLore().get(1)).split(": ")[1]);
            if (exp < 0.0) {
                ItemStack temp = player.getItemInHand().clone();
                temp.setAmount(player.getItemInHand().getAmount() - 1);
                player.getInventory().remove(player.getItemInHand());
                player.getInventory().addItem(temp);
                player.updateInventory();
                return;
            }
            ItemStack temp = player.getItemInHand().clone();
            temp.setAmount(player.getItemInHand().getAmount() - 1);
            player.setItemInHand((temp.getAmount() <= 0) ? new ItemStack(Material.AIR) : temp);
            player.updateInventory();
            player.giveExp(exp);
        }
    }
    
    public static int levelToExp(int level) {
        if (level <= 15) {
            return 17 * level;
        }
        if (level <= 30) {
            return 3 * level * level / 2 - 59 * level / 2 + 360;
        }
        return 7 * level * level / 2 - 303 * level / 2 + 2220;
    }
}
