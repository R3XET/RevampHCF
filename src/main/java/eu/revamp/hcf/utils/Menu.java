package eu.revamp.hcf.utils;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.Listener;

public abstract class Menu implements Listener
{
    @Getter private Inventory inventory;
    
    public Menu(String name, int slots) {
        this.inventory = Bukkit.createInventory(null, 9 * slots, CC.translate(name));
        RevampHCF.getInstance().getServer().getPluginManager().registerEvents(this, RevampHCF.getInstance());
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(this.getInventory()) && event.getCurrentItem() != null && this.getInventory().contains(event.getCurrentItem()) && event.getWhoClicked() instanceof Player) {
            this.onClick((Player)event.getWhoClicked(), event.getCurrentItem());
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(this.getInventory()) && event.getPlayer() instanceof Player) {
            this.onClose((Player)event.getPlayer());
        }
    }
    
    public void addItem(ItemStack itemStack) {
        this.inventory.addItem(itemStack);
    }
    
    public void set(int n, ItemStack mat) {
        this.inventory.setItem(n, mat);
    }

    public String getName() {
        return this.inventory.getName();
    }
    
    public void openInventory(Player player) {
        player.openInventory(this.inventory);
    }
    
    public void onClose(Player player) {
    }
    
    public abstract void onClick(Player p0, ItemStack p1);
}
