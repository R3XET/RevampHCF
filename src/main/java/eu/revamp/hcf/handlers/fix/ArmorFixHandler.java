package eu.revamp.hcf.handlers.fix;

import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.event.player.PlayerItemDamageEvent;
import eu.revamp.hcf.RevampHCF;
import java.util.Arrays;
import org.bukkit.Material;
import java.util.List;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class ArmorFixHandler extends Handler implements Listener
{
    private static List<Material> ALLOWED = Arrays.asList(Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS);
    
    public ArmorFixHandler(RevampHCF plugin) {
        super(plugin);
    }
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        ItemStack stack = event.getItem();
        if (stack != null && ArmorFixHandler.ALLOWED.contains(stack.getType()) && ThreadLocalRandom.current().nextInt(3) != 0) {
            event.setCancelled(true);
        }
    }
}
