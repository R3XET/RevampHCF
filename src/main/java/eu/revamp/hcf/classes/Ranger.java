package eu.revamp.hcf.classes;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.classes.utils.ArmorClass;
import eu.revamp.hcf.classes.utils.events.ArmorClassUnequipEvent;
import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.date.DateUtils;
import eu.revamp.spigot.utils.time.TimeUtils;
import org.apache.commons.codec.language.bm.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Ranger extends ArmorClass implements Listener
{
    private RevampHCF plugin;
    private static HashMap<String, Integer> firstAssassinEffects = new HashMap<>();
    private static PotionEffect strengthEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 220, RevampHCF.getInstance().getConfig().getInt("RANGER.STRENGTH-LEVEL"));
    private static PotionEffect fireEffect = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 640, RevampHCF.getInstance().getConfig().getInt("RANGER.FIRE-LEVEL"));
    private static long strengthDelay = DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("RANGER.STRENGTH-COOLDOWN"));

    private static long fireDelay = DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("RANGER.FIRE-COOLDOWN"));
    private static long demonDelay = DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("RANGER.DEMON-COOLDOWN"));
    
    public Ranger(RevampHCF plugin) {
        super(RevampHCF.getInstance().getConfig().getString("RANGER.NAME"), RevampHCF.getInstance().getConfig().getBoolean("RANGER.WARMUP-ENABLED") ? DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("RANGER.WARMUP-TIME")) : TimeUnit.MILLISECONDS.toMillis(1L));
        this.plugin = plugin;
        this.passiveEffects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        this.passiveEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && event.hasItem() && event.getItem().getType() == Material.DIAMOND_SWORD) {
            if (this.plugin.getHandlerManager().getArmorClassManager().getEquippedClass(player) != this) return;
            if (CooldownManager.isOnCooldown("RANGER_STRENGTH_DELAY", player)) {
                player.sendMessage(Language.COOLDOWN_RANGER_STRENGTH.toString().replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("RANGER_STRENGTH_DELAY", player), true)));
            } else {
                this.plugin.getEffectRestorer().setRestoreEffect(player, Ranger.strengthEffect);
                if (!CooldownManager.getCooldowns().containsKey("RANGER_STRENGTH_DELAY")) {
                    CooldownManager.createCooldown("RANGER_STRENGTH_DELAY");
                }
                CooldownManager.addCooldown("RANGER_STRENGTH_DELAY", player, strengthDelay);
            }
        } else if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && event.hasItem() && event.getItem().getType() == Material.MAGMA_CREAM) {
            if (this.plugin.getHandlerManager().getArmorClassManager().getEquippedClass(player) != this) return;
            if (CooldownManager.isOnCooldown("RANGER_FIRE_DELAY", player)) {
                player.sendMessage(Language.COOLDOWN_RANGER_FIRE.toString().replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("RANGER_FIRE_DELAY", player), true)));
            } else {
                ItemStack stack = player.getItemInHand();
                if (stack.getAmount() == 1) {
                    player.setItemInHand(new ItemStack(Material.AIR, 1));
                } else {
                    stack.setAmount(stack.getAmount() - 1);
                }
                this.plugin.getEffectRestorer().setRestoreEffect(player, Ranger.fireEffect);
                if (!CooldownManager.getCooldowns().containsKey("RANGER_FIRE_DELAY")) {
                    CooldownManager.createCooldown("RANGER_FIRE_DELAY");
                }
                CooldownManager.addCooldown("RANGER_FIRE_DELAY", player, fireDelay);
            }
        }
    }
    
    @Override
    public boolean isApplicableFor(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack helmet = playerInventory.getHelmet();
        if (helmet == null || helmet.getType() != Material.IRON_HELMET) return false;
        ItemStack chestplate = playerInventory.getChestplate();
        if (chestplate == null || chestplate.getType() != Material.DIAMOND_CHESTPLATE) return false;
        ItemStack leggings = playerInventory.getLeggings();
        if (leggings == null || leggings.getType() != Material.IRON_LEGGINGS) return false;
        ItemStack boots = playerInventory.getBoots();
        return boots != null && boots.getType() == Material.DIAMOND_BOOTS;
    }
    
    public Byte direction(Player player) {
        double rotation = (player.getLocation().getYaw() - 90.0f) % 360.0f;
        if (rotation < 0.0) rotation += 360.0;
        if (0.0 <= rotation && rotation < 22.5) return 12;
        if (22.5 <= rotation && rotation < 67.5) return 14;
        if (67.5 <= rotation && rotation < 112.5) return 0;
        if (112.5 <= rotation && rotation < 157.5) return 2;
        if (157.5 <= rotation && rotation < 202.5) return 4;
        if (202.5 <= rotation && rotation < 247.5) return 6;
        if (247.5 <= rotation && rotation < 292.5) return 8;
        if (292.5 <= rotation && rotation < 337.5) return 10;
        if (337.5 <= rotation && rotation < 360.0) return 12;
        return null;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onUnEquip(ArmorClassUnequipEvent event) {
        Player player = event.getPlayer();
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            if (!players.canSee(player)) {
                if (players.hasPermission("hcf.staff")) {
                    continue;
                }
                players.showPlayer(player);
            }
        }
        Ranger.firstAssassinEffects.remove(player.getName());
    }
    @SuppressWarnings("deprecation")
    public void afterFiveSeconds(Player player, boolean force) {
        if (Ranger.firstAssassinEffects.containsKey(player.getName()) && this.isApplicableFor(player)) {
            for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                if (!players.canSee(player) && !players.hasPermission("hcf.staff")) {
                    players.showPlayer(player);
                }
                players.playEffect(player.getLocation().add(0.0, 2.0, 0.0), Effect.ENDER_SIGNAL, 3);
                players.playEffect(player.getLocation().add(0.0, 1.5, 0.0), Effect.ENDER_SIGNAL, 3);
                players.playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.ENDER_SIGNAL, 3);
                players.playEffect(player.getLocation().add(0.0, 2.0, 0.0), Effect.BLAZE_SHOOT, 5);
                players.playEffect(player.getLocation().add(0.0, 1.5, 0.0), Effect.BLAZE_SHOOT, 5);
                players.playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.BLAZE_SHOOT, 5);
            }
            new BukkitRunnable() {
                public void run() {
                    if (Ranger.firstAssassinEffects.containsKey(player.getName()) && Ranger.firstAssassinEffects.get(player.getName()) == 2) {
                        Ranger.firstAssassinEffects.remove(player.getName());
                        player.sendMessage(Language.RANGER_NORMAL.toString());
                        if (Ranger.this.isApplicableFor(player)) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 2));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 2));
                        }
                    }
                }
            }.runTaskLater(RevampHCF.getInstance(), 100L);
            if (force) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120, 1), true);
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                Ranger.firstAssassinEffects.remove(player.getName());
                Ranger.firstAssassinEffects.put(player.getName(), 2);
                player.sendMessage(Language.RANGER_DEMON_FORCED.toString());
                return;
            }
            Ranger.firstAssassinEffects.remove(player.getName());
            Ranger.firstAssassinEffects.put(player.getName(), 2);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120, 1), true);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.sendMessage(Language.RANGER_DEMON_MODE.toString());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            if (RevampHCF.getInstance().getHandlerManager().getArmorClassManager().getEquippedClass(player) == null || !RevampHCF.getInstance().getHandlerManager().getArmorClassManager().getEquippedClass(player).equals(this)) return;
            if (Ranger.firstAssassinEffects.containsKey(player.getName()) && Ranger.firstAssassinEffects.get(player.getName()) == 1) {
                for (Entity entity : player.getNearbyEntities(20.0, 20.0, 20.0)) {
                    if (!(entity instanceof Player)) continue;
                    player.sendMessage(Language.RANGER_DEMON_MODE_OTHER.toString());
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player)event.getDamager();
            if (Ranger.firstAssassinEffects.containsKey(player.getName()) && Ranger.firstAssassinEffects.get(player.getName()) == 1) {
                this.afterFiveSeconds(player, true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST) @SuppressWarnings("deprecation")
    public void onClickItem(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        ArmorClass equipped = RevampHCF.getInstance().getHandlerManager().getArmorClassManager().getEquippedClass(player);
        if (equipped == null || !equipped.equals(this)) return;
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && event.hasItem() && event.getItem().getType() == Material.QUARTZ) {
            if (CooldownManager.isOnCooldown("RANGER_QUARTZ_DELAY", player)) {
                player.sendMessage(Language.COOLDOWN_RANGER_QUARTZ.toString().replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("RANGER_QUARTZ_DELAY", player), true)));
                return;
            }
            ItemStack stack = player.getItemInHand();
            if (stack.getAmount() == 1) {
                player.setItemInHand(new ItemStack(Material.AIR, 1));
            }
            else {
                stack.setAmount(stack.getAmount() - 1);
            }
            player.sendMessage(Language.RANGER_STEALTH_MODE.toString());
            for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                players.playEffect(player.getLocation().add(0.5, 2.0, 0.5), Effect.ENDER_SIGNAL, 5);
                players.playEffect(player.getLocation().add(0.5, 1.5, 0.5), Effect.ENDER_SIGNAL, 5);
                players.playEffect(player.getLocation().add(0.5, 1.0, 0.5), Effect.ENDER_SIGNAL, 5);
                players.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                if (players.hasPermission("hcf.staff")) {
                    continue;
                }
                players.hidePlayer(player);
            }
            if (!CooldownManager.getCooldowns().containsKey("RANGER_QUARTZ_DELAY")) {
                CooldownManager.createCooldown("RANGER_QUARTZ_DELAY");
            }
            CooldownManager.addCooldown("RANGER_QUARTZ_DELAY", player, demonDelay);
            Ranger.firstAssassinEffects.put(player.getName(), 1);
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 4), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 0), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0), true);
            new BukkitRunnable() {
                public void run() {
                    if (Ranger.this.isApplicableFor(player) && Ranger.firstAssassinEffects.containsKey(player.getName()) && Ranger.firstAssassinEffects.get(player.getName()) == 1) {
                        Ranger.this.afterFiveSeconds(player, false);
                    }
                }
            }.runTaskLater(RevampHCF.getInstance(), 100L);
        }
    }
}
