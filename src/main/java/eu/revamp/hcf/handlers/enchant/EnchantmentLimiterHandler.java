package eu.revamp.hcf.handlers.enchant;

import eu.revamp.spigot.RevampSpigot;
import eu.revamp.spigot.utils.chat.color.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;
import java.util.Map;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import eu.revamp.hcf.RevampHCF;
import java.util.ArrayList;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;
@Getter
public class EnchantmentLimiterHandler extends Handler implements Listener
{
    private ArrayList<EnchantmentLimit> enchantmentLimits;
    
    public EnchantmentLimiterHandler(RevampHCF plugin) {
        super(plugin);
        this.enchantmentLimits = new ArrayList<>();
    }
    
    @Override
    public void enable() {
        this.loadEnchantmentLimits();
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
        this.enchantmentLimits.clear();
    }
    
    public void loadEnchantmentLimits() {
        ConfigurationSection section = this.getInstance().getLimiters().getConfigurationSection("enchantment-limiter");
        for (String enchantment : section.getKeys(false)) {
            if (section.getInt(enchantment) == -1) {
                continue;
            }
            EnchantmentLimit enchantmentLimit = new EnchantmentLimit();
            enchantmentLimit.setEnchantment(Enchantment.getByName(enchantment));
            enchantmentLimit.setLevel(section.getInt(enchantment));
            this.enchantmentLimits.add(enchantmentLimit);
        }
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        Map<Enchantment, Integer> toAdd = event.getEnchantsToAdd();
        for (EnchantmentLimit enchantmentLimit : this.enchantmentLimits) {
            if (toAdd.containsKey(enchantmentLimit.getEnchantment()) && toAdd.get(enchantmentLimit.getEnchantment()) > enchantmentLimit.getLevel()) {
                toAdd.remove(enchantmentLimit.getEnchantment());
                if (enchantmentLimit.getLevel() <= 0) {
                    continue;
                }
                toAdd.put(enchantmentLimit.getEnchantment(), enchantmentLimit.getLevel());
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        Inventory inventory = event.getInventory();
        InventoryType.SlotType slotType = event.getSlotType();
        if (inventory.getType().equals(InventoryType.ANVIL) && slotType.equals(InventoryType.SlotType.RESULT)) {
            ItemStack item = event.getCurrentItem();
            for (EnchantmentLimit enchantmentLimit : this.enchantmentLimits) {
                if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                    for (String lore : item.getItemMeta().getLore()) {
                        if (lore.equals(RevampHCF.getInstance().getConfig().getString("UNREPAIRABLE_ITEM_LORE"))) {
                            event.setCancelled(true);
                            player.sendMessage(CC.translate("&cThis item is unrepairable."));
                            return;
                        }
                    }
                }
                else {
                    if (item.getType().equals(Material.ENCHANTED_BOOK)) {
                        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta)item.getItemMeta();
                        if (bookMeta.getStoredEnchants().containsKey(enchantmentLimit.getEnchantment()) && bookMeta.getStoredEnchants().get(enchantmentLimit.getEnchantment()) > enchantmentLimit.getLevel()) {
                            event.setCancelled(true);
                            player.sendMessage(CC.translate("&cYou can't merge those items."));
                            return;
                        }
                    }
                }
                if (!item.getEnchantments().containsKey(enchantmentLimit.getEnchantment()) || item.getEnchantments().get(enchantmentLimit.getEnchantment()) <= enchantmentLimit.getLevel()) {
                    continue;
                }
                item.removeEnchantment(enchantmentLimit.getEnchantment());
                if (enchantmentLimit.getLevel() > 0) {
                    item.addEnchantment(enchantmentLimit.getEnchantment(), enchantmentLimit.getLevel());
                }
                player.updateInventory();
            }
        }
    }
    
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Entity entity = event.getCaught();
        if (entity instanceof ItemStack) {
            ItemStack item = (ItemStack)entity;
            if (item.getEnchantments() != null && !item.getEnchantments().isEmpty()) {
                for (EnchantmentLimit enchantmentLimit : this.enchantmentLimits) {
                    if (item.getEnchantments().containsKey(enchantmentLimit.getEnchantment()) && item.getEnchantments().get(enchantmentLimit.getEnchantment()) > enchantmentLimit.getLevel()) {
                        item.removeEnchantment(enchantmentLimit.getEnchantment());
                        if (enchantmentLimit.getLevel() <= 0) {
                            continue;
                        }
                        item.addEnchantment(enchantmentLimit.getEnchantment(), enchantmentLimit.getLevel());
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) return;
        for (ItemStack item : event.getDrops()) {
            if (item != null && !item.getType().equals(Material.AIR) && item.getEnchantments() != null && !item.getEnchantments().isEmpty()) {
                for (EnchantmentLimit enchantmentLimit : this.enchantmentLimits) {
                    if (item.getEnchantments().containsKey(enchantmentLimit.getEnchantment()) && item.getEnchantments().get(enchantmentLimit.getEnchantment()) > enchantmentLimit.getLevel()) {
                        item.removeEnchantment(enchantmentLimit.getEnchantment());
                        if (enchantmentLimit.getLevel() <= 0) {
                            continue;
                        }
                        item.addEnchantment(enchantmentLimit.getEnchantment(), enchantmentLimit.getLevel());
                    }
                }
            }
        }
    }

    @Getter @Setter
    public static class EnchantmentLimit
    {
        private Enchantment enchantment;
        private int level;
    }
}
