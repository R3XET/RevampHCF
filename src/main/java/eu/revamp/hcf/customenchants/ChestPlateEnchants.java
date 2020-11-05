package eu.revamp.hcf.customenchants;

import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ChestPlateEnchants extends BukkitRunnable
{
    private void ActivePotionEffectsCheck(Player p) {
        if (p.getActivePotionEffects() != null) {
            for (PotionEffect e : p.getActivePotionEffects()) {
                if (e.getAmplifier() >= 0 && e.getDuration() >= 30400 && e.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                    p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                }
            }
        }
    }

    public void run() {
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            Player p = Bukkit.getServer().getPlayerExact(players.getName());
            ItemStack chestplate = p.getInventory().getChestplate();
            if (chestplate != null) {
                ItemMeta meta = chestplate.getItemMeta();
                if (meta.hasLore()) {
                    if (!meta.getLore().contains(CC.translate("&cStrength"))) {
                        continue;
                    }
                    if (p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                        continue;
                    }
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
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
