package eu.revamp.hcf.customenchants;

import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class LeggingsEnchants extends BukkitRunnable
{

    private void ActivePotionEffectsCheck(Player p) {
        if (p.getActivePotionEffects() != null) {
            for (PotionEffect e : p.getActivePotionEffects()) {
                if (e.getAmplifier() >= 0 && e.getDuration() >= 30400 && e.getType().equals(PotionEffectType.FIRE_RESISTANCE)) {
                    p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                }
            }
        }
    }

    public void run() {
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            Player p = Bukkit.getServer().getPlayerExact(players.getName());
            ItemStack legs = p.getInventory().getLeggings();
            if (legs != null) {
                ItemMeta meta = legs.getItemMeta();
                if (meta.hasLore()) {
                    if (!meta.getLore().contains(CC.translate("&cFire Resistance"))) {
                        continue;
                    }
                    if (p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
                        continue;
                    }
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
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
