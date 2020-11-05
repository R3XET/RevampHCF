package eu.revamp.hcf.customenchants;

import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ToolsEnchants extends BukkitRunnable implements Listener
{
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();
        ItemStack item = p.getItemInHand();
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore() && meta.getLore().contains(CC.translate("&cAuto Smelt"))) {
                switch (b.getType()) {
                    case STONE:
                        b.setType(Material.AIR);
                        b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.STONE, 1));
                        break;
                    case SAND:
                        b.setType(Material.AIR);
                        b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.GLASS, 1));
                        break;
                    case IRON_ORE:
                        b.setType(Material.AIR);
                        b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.IRON_INGOT, 1));
                        break;
                    case GOLD_ORE:
                        b.setType(Material.AIR);
                        b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.GOLD_INGOT, 1));
                        break;
                    case GRAVEL:
                        b.setType(Material.AIR);
                        b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.FLINT, 1));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void ActivePotionEffectsCheck(Player p) {
        if (p.getActivePotionEffects() != null) {
            for (PotionEffect e : p.getActivePotionEffects()) {
                if (e.getAmplifier() >= 0 && e.getDuration() >= 30400 && e.getType().equals(PotionEffectType.FAST_DIGGING)) {
                    p.removePotionEffect(PotionEffectType.FAST_DIGGING);
                }
            }
        }
    }

    public void run() {
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            Player p = Bukkit.getServer().getPlayerExact(players.getName());
            ItemStack item = p.getInventory().getItemInHand();
            if (p.getItemInHand().getType() == Material.AIR) {
                this.ActivePotionEffectsCheck(p);
            }
            if (p.getItemInHand() != null) {
                if (!p.getItemInHand().hasItemMeta()) {
                    this.ActivePotionEffectsCheck(p);
                }
                else if (!p.getItemInHand().getItemMeta().hasLore()) {
                    this.ActivePotionEffectsCheck(p);
                }
            }
            if (item != null) {
                if (!item.hasItemMeta()) {
                    continue;
                }
                ItemMeta meta = item.getItemMeta();
                if (meta.hasLore()) {
                    if (meta.getLore().contains(CC.translate("&cHaste"))) {
                        if (p.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                            continue;
                        }
                        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
                    }
                    else {
                        this.ActivePotionEffectsCheck(p);
                    }
                }
                else {
                    this.ActivePotionEffectsCheck(p);
                }
            }
            else {
                this.ActivePotionEffectsCheck(p);
            }
        }
    }
}