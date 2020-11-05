package eu.revamp.hcf.classes;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.classes.utils.ArmorClass;
import eu.revamp.hcf.classes.utils.events.ArmorClassEquipEvent;
import eu.revamp.hcf.utils.inventory.BukkitUtils;
import eu.revamp.spigot.utils.date.DateUtils;
import eu.revamp.spigot.utils.player.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Miner extends ArmorClass implements Listener
{
    private static PotionEffect STAGE_R = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0);
    private static PotionEffect STAGE_2 = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1);
    private static PotionEffect STAGE_1 = new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 2);
    private static PotionEffect STAGE_3 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1);
    private static int INVISIBILITY_HEIGHT_LEVEL = RevampHCF.getInstance().getConfig().getInt("MINER.INVIS-Y-LEVEL");
    private static PotionEffect HEIGHT_INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0);
    private RevampHCF plugin;
    private Map<PotionEffect, Integer> removeableEffects;

    public Miner(RevampHCF plugin) {
        super(RevampHCF.getInstance().getConfig().getString("MINER.NAME"), RevampHCF.getInstance().getConfig().getBoolean("MINER.WARMUP-ENABLED") ? DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("MINER.WARMUP-TIME")) : TimeUnit.MILLISECONDS.toMillis(1L));
        this.plugin = plugin;
        this.removeableEffects = new HashMap<>();
        this.removeableEffects.put(Miner.STAGE_R, 15);
        this.removeableEffects.put(Miner.STAGE_1, 50);
        this.removeableEffects.put(Miner.STAGE_2, 75);
        this.removeableEffects.put(Miner.STAGE_3, 125);
        this.passiveEffects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
        this.passiveEffects.add(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
    }
    
    private void removeInvisibilitySafely(Player player) {
        for (PotionEffect active : player.getActivePotionEffects()) {
            if (active.getType().equals(PotionEffectType.INVISIBILITY) && active.getDuration() > Miner.DEFAULT_MAX_DURATION) {
                player.sendMessage(Language.MINER_INVISIBILITY_REMOVED.toString().replace("%miner%", this.getName()));
                player.removePotionEffect(active.getType());
                break;
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && PlayerUtils.getFinalAttacker(event, false) != null) {
            Player player = (Player)entity;
            if (this.plugin.getHandlerManager().getArmorClassManager().hasClassEquipped(player, this)) {
                this.removeInvisibilitySafely(player);
            }
        }
    }
    
    @Override
    public void onUnequip(Player player) {
        super.onUnequip(player);
        this.removeInvisibilitySafely(player);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.SATURATION);
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
        player.removePotionEffect(PotionEffectType.REGENERATION);
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        this.conformMinerInvisibility(event.getPlayer(), event.getFrom(), event.getTo());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        this.conformMinerInvisibility(event.getPlayer(), event.getFrom(), event.getTo());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onClassEquip(ArmorClassEquipEvent event) {
        Player player = event.getPlayer();
        if (event.getPvpClass() == this && player.getLocation().getBlockY() <= Miner.INVISIBILITY_HEIGHT_LEVEL) {
            player.addPotionEffect(Miner.HEIGHT_INVISIBILITY, true);
            player.sendMessage(Language.MINER_INVISIBILITY_ADDED.toString().replace("%miner%", this.getName()));
        }
        if (event.getPvpClass() == this) {
            int diamonds = player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE);
            if (diamonds > 250 && diamonds < 750) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
            }
            if (diamonds > 500) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0));
            }
            if (diamonds > 750) {
                player.removePotionEffect(PotionEffectType.SPEED);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            }
            if (diamonds > 1000) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
            }
            if (diamonds > 1500) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
            }
        }
    }
    
    private void conformMinerInvisibility(Player player, Location from, Location to) {
        int fromY = from.getBlockY();
        int toY = to.getBlockY();
        if (fromY != toY && this.plugin.getHandlerManager().getArmorClassManager().hasClassEquipped(player, this)) {
            boolean isInvisible = player.hasPotionEffect(PotionEffectType.INVISIBILITY);
            if (toY > Miner.INVISIBILITY_HEIGHT_LEVEL) {
                if (fromY <= Miner.INVISIBILITY_HEIGHT_LEVEL && isInvisible) {
                    this.removeInvisibilitySafely(player);
                }
            }
            else if (!isInvisible) {
                player.addPotionEffect(Miner.HEIGHT_INVISIBILITY, true);
                player.sendMessage(Language.MINER_INVISIBILITY_ADDED.toString().replace("%miner%", this.getName()));
            }
        }
    }
    
    @Override
    public boolean isApplicableFor(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack helmet = playerInventory.getHelmet();
        if (helmet == null || helmet.getType() != Material.IRON_HELMET) {
            return false;
        }
        ItemStack chestplate = playerInventory.getChestplate();
        if (chestplate == null || chestplate.getType() != Material.IRON_CHESTPLATE) {
            return false;
        }
        ItemStack leggings = playerInventory.getLeggings();
        if (leggings == null || leggings.getType() != Material.IRON_LEGGINGS) {
            return false;
        }
        ItemStack boots = playerInventory.getBoots();
        return boots != null && boots.getType() == Material.IRON_BOOTS;
    }
}
