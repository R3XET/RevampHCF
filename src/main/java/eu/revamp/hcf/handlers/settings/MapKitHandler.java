package eu.revamp.hcf.handlers.settings;

import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.serialize.BukkitSerilization;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import java.io.IOException;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import eu.revamp.hcf.RevampHCF;
import java.util.UUID;
import java.util.List;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class MapKitHandler extends Handler implements Listener
{
    private Inventory mapKitInv;
    @Getter private List<UUID> editingMapKit;
    
    public MapKitHandler(RevampHCF plugin) {
        super(plugin);
        this.editingMapKit = new ArrayList<>();
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
        this.loadInventory();
    }
    
    @Override
    public void disable() {
        this.saveInventory();
    }
    
    public void loadInventory() {
        this.mapKitInv = Bukkit.createInventory(null, 54, "Map Kit");
        String mapKitItems = this.getInstance().getUtilities().getString("map-kit-items");
        if (mapKitItems != null && !mapKitItems.isEmpty()) {
            ItemStack[] contents = BukkitSerilization.itemStackArrayFromBase64(this.getInstance().getUtilities().getString("map-kit-items"));
            this.mapKitInv.setContents(contents);
        }
    }
    
    public void saveInventory() {
        ItemStack[] contents = this.mapKitInv.getContents();
        String contentsBase64 = BukkitSerilization.itemStackArrayToBase64(contents);
        this.getInstance().getUtilities().set("map-kit-items", contentsBase64);
        try {
            this.getInstance().getUtilities().save(this.getInstance().getUtilities().getFile());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Inventory getMapKitInventory() {
        return this.mapKitInv;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();
        Inventory inv = event.getInventory();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (inv.getName().equals(this.mapKitInv.getName()) && (!player.hasPermission("revamphcf.op") || !this.editingMapKit.contains(player.getUniqueId()))) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity entity = event.getPlayer();
        Inventory inv = event.getInventory();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (inv.getName().equals(this.mapKitInv.getName()) && this.editingMapKit.contains(player.getUniqueId())) {
                this.editingMapKit.remove(player.getUniqueId());
                player.sendMessage(CC.translate("&eYou successfully edited &fMapKit&e."));
            }
        }
    }
}
