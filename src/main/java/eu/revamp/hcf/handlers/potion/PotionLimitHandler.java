package eu.revamp.hcf.handlers.potion;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.utils.Handler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class PotionLimitHandler extends Handler implements Listener
{
    @Getter private ArrayList<PotionLimit> potionLimits;
    
    public PotionLimitHandler(RevampHCF plugin) {
        super(plugin);
        this.potionLimits = new ArrayList<>();
    }
    
    @Override
    public void enable() {
        this.loadPotionLimits();
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }
    
    @Override
    public void disable() {
        this.potionLimits.clear();
    }
    
    public void loadPotionLimits() {
        ConfigurationSection section = this.getInstance().getLimiters().getConfigurationSection("potion-limiter");
        for (String type : section.getKeys(false)) {
            if (section.getInt(type + ".level") == -1) {
                continue;
            }
            PotionLimit potionLimit = new PotionLimit();
            potionLimit.setType(PotionEffectType.getByName(type));
            potionLimit.setLevel(section.getInt(type + ".level"));
            potionLimit.setExtended(section.getBoolean(type + ".extended"));
            this.potionLimits.add(potionLimit);
        }
    }

    
    @EventHandler
    public void onPotionBrew(BrewEvent event) {
        BrewerInventory brewer = event.getContents();
        ItemStack ingredient = brewer.getIngredient().clone();
        ItemStack[] potions = new ItemStack[3];
        for (int i = 0; i < 3; ++i) {
            if (event.getContents().getItem(i) != null) {
                potions[i] = brewer.getItem(i).clone();
            }
        }
        new BukkitRunnable() {
            public void run() {
                for (int i = 0; i < 3; ++i) {
                    if (brewer.getItem(i) != null && brewer.getItem(i).getType() == Material.POTION) {
                        for (PotionEffect potionEffect : Potion.fromItemStack(brewer.getItem(i)).getEffects()) {
                            for (PotionLimit potionLimit : PotionLimitHandler.this.potionLimits) {
                                if (potionLimit.getType().equals(potionEffect.getType())) {
                                    int maxLevel = potionLimit.getLevel();
                                    int level = potionEffect.getAmplifier() + 1;
                                    Potion potion = Potion.fromItemStack(brewer.getItem(i));
                                    if (maxLevel == 0 || level > maxLevel) {
                                        brewer.setIngredient(ingredient);
                                        for (int item = 0; item < 3; ++item) {
                                            brewer.setItem(item, potions[item]);
                                        }
                                        return;
                                    }
                                    if (potion.hasExtendedDuration() && !potionLimit.isExtended()) {
                                        brewer.setIngredient(ingredient);
                                        for (int item = 0; item < 3; ++item) {
                                            brewer.setItem(item, potions[item]);
                                        }
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskLater(RevampHCF.getInstance(), 1L);
    }
    
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (!item.getType().equals(Material.POTION)) return;
        if (item.getType().equals(Material.POTION) && item.getDurability() == 0) return;
        for (PotionEffect potionEffect : Potion.fromItemStack(item).getEffects()) {
            for (PotionLimit potionLimit : this.potionLimits) {
                if (potionLimit.getType().equals(potionEffect.getType())) {
                    int maxLevel = potionLimit.getLevel();
                    int level = potionEffect.getAmplifier() + 1;
                    Potion potion = Potion.fromItemStack(item);
                    if (maxLevel == 0 || level > maxLevel) {
                        event.setCancelled(true);
                        player.setItemInHand(new ItemStack(Material.AIR));
                        player.sendMessage(Language.POTIONEFFECT_DISABLED.toString());
                        return;
                    }
                    if (potion.hasExtendedDuration() && !potionLimit.isExtended()) {
                        event.setCancelled(true);
                        player.setItemInHand(new ItemStack(Material.AIR));
                        player.sendMessage(Language.POTIONEFFECT_DISABLED.toString());
                        return;
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        ThrownPotion thrownPotion = event.getPotion();
        for (PotionEffect potionEffect : thrownPotion.getEffects()) {
            for (PotionLimit potionLimit : this.potionLimits) {
                if (potionLimit.getType().equals(potionEffect.getType())) {
                    if (thrownPotion.getShooter() instanceof Player) {
                        Player shooter = (Player)thrownPotion.getShooter();
                        int maxLevel = potionLimit.getLevel();
                        int level = potionEffect.getAmplifier() + 1;
                        Potion potion = Potion.fromItemStack(thrownPotion.getItem());
                        if (maxLevel == 0 || level > maxLevel) {
                            event.setCancelled(true);
                            shooter.sendMessage(Language.POTIONEFFECT_DISABLED.toString());
                            return;
                        }
                        if (potion.hasExtendedDuration() && !potionLimit.isExtended()) {
                            event.setCancelled(true);
                            shooter.sendMessage(Language.POTIONEFFECT_DISABLED.toString());
                            return;
                        }
                    }
                    else {
                        int maxLevel2 = potionLimit.getLevel();
                        int level2 = potionEffect.getAmplifier();
                        Potion potion2 = Potion.fromItemStack(thrownPotion.getItem());
                        if (maxLevel2 == 0 || level2 > maxLevel2) {
                            event.setCancelled(true);
                            return;
                        }
                        if (potion2.hasExtendedDuration() && !potionLimit.isExtended()) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    @Getter @Setter
    public static class PotionLimit
    {
        private PotionEffectType type;
        private int level;
        private boolean extended;
    }
}
