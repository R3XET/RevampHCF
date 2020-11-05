package eu.revamp.hcf.handlers.hcf;

import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class BookDisenchantHandler extends Handler implements Listener {
    public BookDisenchantHandler(RevampHCF instance) {
        super(instance);
    }
    @Override
    public void enable(){
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        Block block = event.getClickedBlock();
        if (item == null) return;
        if (item.getType().equals(Material.ENCHANTED_BOOK) && action.equals(Action.LEFT_CLICK_BLOCK) && block.getType().equals(Material.ENCHANTMENT_TABLE) && !player.getGameMode().equals(GameMode.CREATIVE)) {
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta instanceof EnchantmentStorageMeta) {
                EnchantmentStorageMeta enchantmentMeta = (EnchantmentStorageMeta)itemMeta;
                for (Enchantment enchantment : enchantmentMeta.getStoredEnchants().keySet()) {
                    enchantmentMeta.removeStoredEnchant(enchantment);
                }
                event.setCancelled(true);
                player.setItemInHand(new ItemStack(Material.BOOK, 1));
                player.sendMessage(CC.translate("&eYou have removed all &fEnchantments &efrom this book."));
            }
        }
    }
}
