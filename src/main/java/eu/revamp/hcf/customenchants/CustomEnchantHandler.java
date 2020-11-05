package eu.revamp.hcf.customenchants;
/*
import eu.revamp.hcf.extra.events.armor.ArmorEquipEvent;
import eu.revamp.spigot.utils.chat.color.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CustomEnchantHandler implements Listener {

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event){
        if (event.getType().equals(ArmorEquipEvent.ArmorType.HELMET)){
            ItemStack helmet = event.getNewArmorPiece();
            Player player = event.getPlayer();
            if (helmet != null) {
                ItemMeta meta = helmet.getItemMeta();
                if (meta.hasLore()) {
                    if (meta.getLore().contains(CC.translate("&cNight Vision")) && !player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
                    }
                }
                else {
                    this.HelmetEffectsCheck(player);
                }
            }
            else {
                this.HelmetEffectsCheck(player);
            }
            if (helmet != null) {
                ItemMeta meta = helmet.getItemMeta();
                if (meta.hasLore()) {
                    if (meta.getLore().contains(CC.translate("&cWater Breathing")) && !player.hasPotionEffect(PotionEffectType.WATER_BREATHING)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0));
                    }
                }
                else {
                    this.HelmetEffectsCheck(player);
                }
            }
            else {
                this.HelmetEffectsCheck(player);
            }
            if (helmet != null) {
                ItemMeta meta = helmet.getItemMeta();
                if (meta.hasLore()) {
                    if (!meta.getLore().contains(CC.translate("&cImplants"))) return;

                    if (player.hasPotionEffect(PotionEffectType.SATURATION)) return;

                    player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0));
                }
                else {
                    this.HelmetEffectsCheck(player);
                }
            }
            else {
                this.HelmetEffectsCheck(player);
            }
        }
    }

    private void HelmetEffectsCheck(Player player) {
        if (player.getActivePotionEffects() != null) {
            for (PotionEffect e : player.getActivePotionEffects()) {
                if (e.getAmplifier() >= 0 && e.getDuration() >= 30400) {
                    if (e.getType().equals(PotionEffectType.NIGHT_VISION)) {
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    }
                    if (e.getType().equals(PotionEffectType.WATER_BREATHING)) {
                        player.removePotionEffect(PotionEffectType.WATER_BREATHING);
                    }
                    if (!e.getType().equals(PotionEffectType.SATURATION)) {
                        continue;
                    }
                    player.removePotionEffect(PotionEffectType.SATURATION);
                }
            }
        }
    }

}
 */