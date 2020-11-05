package eu.revamp.hcf.handlers.blocks;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.item.ItemBuilder;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.GameMode;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class AutoSmeltOreHandler extends Handler implements Listener
{
    public AutoSmeltOreHandler(RevampHCF plugin) {
        super(plugin);
    }
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();
        if (player.getGameMode() != GameMode.CREATIVE && player.hasPermission("donors.autosmelt") && itemStack != null && itemStack.getType() != Material.AIR && !itemStack.containsEnchantment(Enchantment.SILK_TOUCH)) {
            Block block = event.getBlock();
            Material dropType;
            switch (block.getType()) {
                case IRON_ORE: {
                    dropType = Material.IRON_INGOT;
                    break;
                }
                case GOLD_ORE: {
                    dropType = Material.GOLD_INGOT;
                    break;
                }
                default: {
                    return;
                }
            }
            Location location = block.getLocation();
            World world = location.getWorld();
            ItemStack dropItem = new ItemBuilder(dropType).toItemStack();
            world.dropItemNaturally(location, dropItem);
            block.setType(Material.AIR);
            block.getState().update();
        }
    }
}
