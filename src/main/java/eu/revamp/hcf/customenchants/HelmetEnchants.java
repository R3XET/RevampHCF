package eu.revamp.hcf.customenchants;

import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class HelmetEnchants extends BukkitRunnable
{
    private void ActivePotionEffectsCheck(Player p) {
        if (p.getActivePotionEffects() != null) {
            for (PotionEffect e : p.getActivePotionEffects()) {
                if (e.getAmplifier() >= 0 && e.getDuration() >= 30400) {
                    if (e.getType().equals(PotionEffectType.NIGHT_VISION)) {
                        p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    }
                    if (e.getType().equals(PotionEffectType.WATER_BREATHING)) {
                        p.removePotionEffect(PotionEffectType.WATER_BREATHING);
                    }
                    if (!e.getType().equals(PotionEffectType.SATURATION)) {
                        continue;
                    }
                    p.removePotionEffect(PotionEffectType.SATURATION);
                }
            }
        }
    }

    public void run() {
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            Player p = Bukkit.getServer().getPlayerExact(players.getName());
            ItemStack helmet = p.getInventory().getHelmet();
            if (helmet != null) {
                ItemMeta meta = helmet.getItemMeta();
                if (meta.hasLore()) {
                    if (meta.getLore().contains(CC.translate("&cNight Vision")) && !p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
                    }
                }
                else {
                    this.ActivePotionEffectsCheck(p);
                }
            }
            else {
                this.ActivePotionEffectsCheck(p);
            }
            if (helmet != null) {
                ItemMeta meta = helmet.getItemMeta();
                if (meta.hasLore()) {
                    if (meta.getLore().contains(CC.translate("&cWater Breathing")) && !p.hasPotionEffect(PotionEffectType.WATER_BREATHING)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0));
                    }
                }
                else {
                    this.ActivePotionEffectsCheck(p);
                }
            }
            else {
                this.ActivePotionEffectsCheck(p);
            }
            if (helmet != null) {
                ItemMeta meta = helmet.getItemMeta();
                if (meta.hasLore()) {
                    if (!meta.getLore().contains(CC.translate("&cImplants"))) {
                        continue;
                    }
                    if (p.hasPotionEffect(PotionEffectType.SATURATION)) {
                        continue;
                    }
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0));
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
