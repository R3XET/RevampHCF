package eu.revamp.hcf.handlers.fix.glitch;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class DupeGlitchHandler extends Handler implements Listener {
    public DupeGlitchHandler(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    /*
     * Minecart Fix
     */
    @EventHandler
    public void onCraft(CraftItemEvent e){
        ArrayList<Material> items = new ArrayList<>();
        for(ItemStack item : e.getInventory().getContents()){
            if(item.getType() == Material.MINECART || item.getType() == Material.CHEST || item.getType() == Material.TRAPPED_CHEST || item.getType() == Material.HOPPER){
                items.add(item.getType());
            }
        }

        if((items.contains(Material.MINECART) && items.contains(Material.CHEST)) || (items.contains(Material.MINECART) && items.contains(Material.TRAPPED_CHEST) || items.contains(Material.MINECART) && items.contains(Material.HOPPER))){
            e.getWhoClicked().sendMessage(ChatColor.RED + "M3M3D");
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick " + e.getWhoClicked().getName() + " �cM3M3D");
            e.setCancelled(true);
        }
    }
}
