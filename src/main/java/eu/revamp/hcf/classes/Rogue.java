package eu.revamp.hcf.classes;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.classes.utils.ArmorClass;
import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.date.DateUtils;
import eu.revamp.spigot.utils.time.TimeUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public class Rogue extends ArmorClass implements Listener
{
    private RevampHCF plugin;
    private PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, 160, RevampHCF.getInstance().getConfig().getInt("ROGUE.SPEED-LEVEL"));
    private PotionEffect jumpEffect = new PotionEffect(PotionEffectType.JUMP, 160, RevampHCF.getInstance().getConfig().getInt("ROGUE.JUMP-LEVEL"));
    private long speedDelay = DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("ROGUE.SPEED-COOLDOWN"));
    private long jumpDelay = DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("ROGUE.JUMP-COOLDOWN"));

    public Rogue(RevampHCF plugin) {
        super(RevampHCF.getInstance().getConfig().getString("ROGUE.NAME"), RevampHCF.getInstance().getConfig().getBoolean("ROGUE.WARMUP-ENABLED") ? DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("ROGUE.WARMUP-TIME")) : TimeUnit.MILLISECONDS.toMillis(1L));
        this.plugin = plugin;
        this.passiveEffects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        this.passiveEffects.add(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1));
        this.passiveEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Entity entity = event.getEntity();
            Entity damager = event.getDamager();
            Player attacker = (Player)damager;
            if (RevampHCF.getInstance().getHandlerManager().getArmorClassManager().getEquippedClass(attacker) == this) {
                ItemStack stack = attacker.getItemInHand();
                if (stack != null && stack.getType() == Material.GOLD_SWORD && stack.getEnchantments().isEmpty()) {
                    Player player = (Player)entity;
                    if (this.direction(attacker).equals(this.direction(player))) {
                        if (player.getHealth() <= 0.0) return;
                        if (player.getHealth() <= 4.0) {
                            player.damage(20.0);
                        }
                        else {
                            player.setHealth(player.getHealth() - 4.0);
                        }
                        player.sendMessage(Language.ROGUE_BACKSTABBED_DAMAGED.toString().replace("%player%", attacker.getName()));
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                        attacker.sendMessage(Language.ROGUE_BACKSTABBED_ATTACKER.toString().replace("%player%", player.getName()));
                        attacker.setItemInHand(new ItemStack(Material.AIR, 1));
                        attacker.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                        attacker.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && event.hasItem() && event.getItem().getType() == Material.SUGAR) {
            if (this.plugin.getHandlerManager().getArmorClassManager().getEquippedClass(event.getPlayer()) != this) return;
            if (CooldownManager.isOnCooldown("ROGUE_SPEED_DELAY", player)) {
                player.sendMessage(Language.COOLDOWN_ROGUE_SUGAR.toString().replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("ROGUE_SPEED_DELAY", player), true)));
            }
            else {
                ItemStack stack = player.getItemInHand();
                if (stack.getAmount() == 1) {
                    player.setItemInHand(new ItemStack(Material.AIR, 1));
                } else {
                    stack.setAmount(stack.getAmount() - 1);
                }
                if (!CooldownManager.getCooldowns().containsKey("ROGUE_SPEED_DELAY")) {
                    CooldownManager.createCooldown("ROGUE_SPEED_DELAY");
                }
                CooldownManager.addCooldown("ROGUE_SPEED_DELAY", player, speedDelay);
                RevampHCF.getInstance().getEffectRestorer().setRestoreEffect(player, speedEffect);
            }
        }
        else if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && event.hasItem() && event.getItem().getType() == Material.FEATHER) {
            if (this.plugin.getHandlerManager().getArmorClassManager().getEquippedClass(event.getPlayer()) != this) return;
            if (CooldownManager.isOnCooldown("ROGUE_JUMP_DELAY", player)) {
                player.sendMessage(Language.COOLDOWN_ROGUE_FEATHER.toString().replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("ROGUE_JUMP_DELAY", player), true)));
            }
            else {
                ItemStack stack = player.getItemInHand();
                if (stack.getAmount() == 1) {
                    player.setItemInHand(new ItemStack(Material.AIR, 1));
                }
                else {
                    stack.setAmount(stack.getAmount() - 1);
                }
                if (!CooldownManager.getCooldowns().containsKey("ROGUE_JUMP_DELAY")) {
                    CooldownManager.createCooldown("ROGUE_JUMP_DELAY");
                }
                CooldownManager.addCooldown("ROGUE_JUMP_DELAY", player, jumpDelay);
                RevampHCF.getInstance().getEffectRestorer().setRestoreEffect(player, jumpEffect);
            }
        }
    }
    
    @Override
    public boolean isApplicableFor(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack helmet = playerInventory.getHelmet();
        if (helmet == null || helmet.getType() != Material.CHAINMAIL_HELMET) {
            return false;
        }
        ItemStack chestplate = playerInventory.getChestplate();
        if (chestplate == null || chestplate.getType() != Material.CHAINMAIL_CHESTPLATE) {
            return false;
        }
        ItemStack leggings = playerInventory.getLeggings();
        if (leggings == null || leggings.getType() != Material.CHAINMAIL_LEGGINGS) {
            return false;
        }
        ItemStack boots = playerInventory.getBoots();
        return boots != null && boots.getType() == Material.CHAINMAIL_BOOTS;
    }
    
    public Byte direction(Player player) {
        double rotation = (player.getLocation().getYaw() - 90.0f) % 360.0f;
        if (rotation < 0.0) {
            rotation += 360.0;
        }
        if (0.0 <= rotation && rotation < 22.5) {
            return 12;
        }
        if (22.5 <= rotation && rotation < 67.5) {
            return 14;
        }
        if (67.5 <= rotation && rotation < 112.5) {
            return 0;
        }
        if (112.5 <= rotation && rotation < 157.5) {
            return 2;
        }
        if (157.5 <= rotation && rotation < 202.5) {
            return 4;
        }
        if (202.5 <= rotation && rotation < 247.5) {
            return 6;
        }
        if (247.5 <= rotation && rotation < 292.5) {
            return 8;
        }
        if (292.5 <= rotation && rotation < 337.5) {
            return 10;
        }
        if (337.5 <= rotation && rotation < 360.0) {
            return 12;
        }
        return null;
    }
}