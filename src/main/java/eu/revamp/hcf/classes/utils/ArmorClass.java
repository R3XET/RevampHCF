package eu.revamp.hcf.classes.utils;

import eu.revamp.spigot.utils.chat.color.CC;
import lombok.Getter;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import org.bukkit.potion.PotionEffect;
import java.util.Set;

public abstract class ArmorClass
{
    public static long DEFAULT_MAX_DURATION = TimeUnit.MINUTES.toMillis(8L);
    protected Set<PotionEffect> passiveEffects;
    @Getter protected String name;
    @Getter protected long warmupDelay;

    public ArmorClass(String name, long warmupDelay) {
        this.passiveEffects = new HashSet<>();
        this.name = name;
        this.warmupDelay = warmupDelay;
    }

    public boolean onEquip(Player player) {
        for (PotionEffect effect : this.passiveEffects) {
            player.addPotionEffect(effect, true);
        }
        player.sendMessage(CC.translate(RevampHCF.getInstance().getConfig().getString("EQUIP-MESSAGE").replace("%class%", this.name)));
        return true;
    }

    public void onUnequip(Player player) {
        for (PotionEffect effect : this.passiveEffects) {
            for (PotionEffect active : player.getActivePotionEffects()) {
                if (active.getDuration() > ArmorClass.DEFAULT_MAX_DURATION && active.getType().equals(effect.getType()) && active.getAmplifier() == effect.getAmplifier()) {
                    player.removePotionEffect(effect.getType());
                    break;
                }
            }
        }
        player.sendMessage(CC.translate(RevampHCF.getInstance().getConfig().getString("UNEQUIP-MESSAGE").replace("%class%", this.name)));
    }

    public abstract boolean isApplicableFor(Player p0);
}
