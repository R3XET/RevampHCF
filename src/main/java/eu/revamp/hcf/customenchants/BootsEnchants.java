package eu.revamp.hcf.customenchants;

import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class BootsEnchants extends BukkitRunnable
{
    private void ActivePotionEffectsCheck(Player p) {
        if (p.getActivePotionEffects() != null) {
            for (PotionEffect e : p.getActivePotionEffects()) {
                if (e.getAmplifier() >= 0 && e.getDuration() >= 30400) {
                    if (e.getType().equals(PotionEffectType.JUMP)) {
                        p.removePotionEffect(PotionEffectType.JUMP);
                    }
                    if (!e.getType().equals(PotionEffectType.SPEED)) {
                        continue;
                    }
                    p.removePotionEffect(PotionEffectType.SPEED);
                }
            }
        }
    }

    public void run() {
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            Player p = Bukkit.getServer().getPlayerExact(players.getName());
            ItemStack boots = p.getInventory().getBoots();
            if (boots != null) {
                ItemMeta meta = boots.getItemMeta();
                if (meta.hasLore()) {
                    if (meta.getLore().contains(CC.translate("&cSpeed")) && !p.hasPotionEffect(PotionEffectType.SPEED)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                    }
                }
                else {
                    this.ActivePotionEffectsCheck(p);
                }
            }
            else {
                this.ActivePotionEffectsCheck(p);
            }
            if (boots != null) {
                ItemMeta meta = boots.getItemMeta();
                if (meta.hasLore()) {
                    if (!meta.getLore().contains(CC.translate("&cJump"))) {
                        continue;
                    }
                    if (p.hasPotionEffect(PotionEffectType.JUMP)) {
                        continue;
                    }
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
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
