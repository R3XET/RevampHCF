package eu.revamp.hcf.handlers.other;

import eu.revamp.hcf.managers.CooldownManager;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.List;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import eu.revamp.hcf.RevampHCF;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class GoldenHeadHandler extends Handler implements Listener
{
    public GoldenHeadHandler(RevampHCF plugin) {
        super(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    public void addEffects(Player p) {
        List<String> effects = RevampHCF.getInstance().getConfig().getStringList("GOLDEN-HEAD.EFFECTS");
        for (String stringEffect : effects) {
            String[] delim = stringEffect.split(":");
            if (delim.length == 3 && !CooldownManager.isOnCooldown("HEADAPPLE_DELAY", p)) {
                PotionEffectType type = PotionEffectType.getByName(delim[0].toUpperCase());
                int duration = Integer.parseInt(delim[1]);
                int amplitude = Integer.parseInt(delim[2]);
                PotionEffect pEffect = new PotionEffect(type, duration * 20, amplitude);
                p.addPotionEffect(pEffect);
            }
        }
    }
    
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        ItemStack goldenHead = e.getItem();
        if (e.getItem().getType().equals(Material.GOLDEN_APPLE) && item.getDurability() == 0 && goldenHead.getItemMeta().hasLore()) {
            if (goldenHead.getItemMeta().hasLore()) {
                CooldownManager.isOnCooldown("HEADAPPLE_DELAY", p);
            }
            this.addEffects(p);
        }
    }
}
