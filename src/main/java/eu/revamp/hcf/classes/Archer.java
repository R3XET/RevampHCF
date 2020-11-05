package eu.revamp.hcf.classes;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.classes.utils.ArmorClass;
import eu.revamp.hcf.managers.CooldownManager;
import eu.revamp.hcf.utils.Utils;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.spigot.utils.date.DateUtils;
import eu.revamp.spigot.utils.time.TimeUtils;
import javafx.scene.control.Label;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
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
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Archer extends ArmorClass implements Listener
{
    public static PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, 160, RevampHCF.getInstance().getConfig().getInt("ARCHER.SPEED-LEVEL"));
    public static PotionEffect jumpEffect = new PotionEffect(PotionEffectType.JUMP, 160, RevampHCF.getInstance().getConfig().getInt("ARCHER.JUMP-LEVEL"));
    public static HashMap<UUID, UUID> TAGGED = new HashMap<>();
    public static Random random = new Random();
    public static long speedDelay = DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("ARCHER.SPEED-COOLDOWN"));
    public static long jumpDelay = DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("ARCHER.JUMP-COOLDOWN"));
    public RevampHCF plugin;
    
    public Archer(RevampHCF plugin) {
        super(RevampHCF.getInstance().getConfig().getString("ARCHER.NAME"), RevampHCF.getInstance().getConfig().getBoolean("ARCHER.WARMUP-ENABLED") ? DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("ARCHER.WARMUP-TIME")) : TimeUnit.MILLISECONDS.toMillis(1L));
        this.plugin = plugin;
        this.passiveEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
        this.passiveEffects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if (entity instanceof Player && damager instanceof Arrow) {
            Arrow arrow = (Arrow)damager;
            ProjectileSource source = arrow.getShooter();
            if (source instanceof Player) {
                Player damaged = (Player)event.getEntity();
                Player shooter = (Player)source;
                ArmorClass equipped = this.plugin.getHandlerManager().getArmorClassManager().getEquippedClass(shooter);
                if (equipped == null || !equipped.equals(this)) {
                    return;
                }
                if (this.plugin.getHandlerManager().getTimerManager().getArcherHandler().getRemaining((Player)entity) == 0L || this.plugin.getHandlerManager().getTimerManager().getArcherHandler().getRemaining((Player)entity) < TimeUnit.SECONDS.toMillis(5L)) {
                    if (this.plugin.getHandlerManager().getArmorClassManager().getEquippedClass(damaged) != null && this.plugin.getHandlerManager().getArmorClassManager().getEquippedClass(damaged).equals(this)) return;
                    this.plugin.getHandlerManager().getTimerManager().getArcherHandler().setCooldown((Player)entity, entity.getUniqueId());
                    Archer.TAGGED.put(damaged.getUniqueId(), shooter.getUniqueId());
                    double distance = shooter.getLocation().distance(damaged.getLocation());
                    shooter.sendMessage(CC.translate("&e[&9Arrow Range &e(&c" + String.format("%.1f", distance) + "&e)] " + "&6Marked " + damaged.getName() + " &6for 6 seconds. &9&l(1 heart)"));
                    damaged.sendMessage(ChatColor.GOLD + "You were " + RevampHCF.getInstance().getConfig().getString("ARCHER.NAME") + "tagged by " + ChatColor.RED + shooter.getName() + ChatColor.GOLD + " from " + ChatColor.RED + String.format("%.1f", distance) + ChatColor.GOLD + " blocks away.");
                    LeatherArmorMeta helmMeta = (LeatherArmorMeta)shooter.getInventory().getHelmet().getItemMeta();
                    LeatherArmorMeta chestMeta = (LeatherArmorMeta)shooter.getInventory().getChestplate().getItemMeta();
                    LeatherArmorMeta leggingsMeta = (LeatherArmorMeta)shooter.getInventory().getLeggings().getItemMeta();
                    LeatherArmorMeta bootsMeta = (LeatherArmorMeta)shooter.getInventory().getBoots().getItemMeta();
                    org.bukkit.Color green = org.bukkit.Color.fromRGB(6717235);
                    double r = Archer.random.nextDouble();
                    if (r <= 0.5 && helmMeta.getColor().equals(green) && chestMeta.getColor().equals(green) && leggingsMeta.getColor().equals(green) && bootsMeta.getColor().equals(green)) {
                        damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 120, 0));
                        shooter.sendMessage(Language.ARCHER_MARK_ARMOR_GREEN_SHOOTER.toString().replace("%damaged%", damaged.getName()));
                        damaged.sendMessage(Language.ARCHER_MARK_ARMOR_GREEN_DAMAGED.toString().replace("%shooter%", shooter.getName()));
                    }
                    org.bukkit.Color blue = org.bukkit.Color.fromRGB(3361970);
                    if (r <= 0.5 && helmMeta.getColor().equals(blue) && chestMeta.getColor().equals(blue) && leggingsMeta.getColor().equals(blue) && bootsMeta.getColor().equals(blue)) {
                        damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 0));
                        shooter.sendMessage(Language.ARCHER_MARK_ARMOR_BLUE_SHOOTER.toString().replace("%damaged%", damaged.getName()));
                        damaged.sendMessage(Language.ARCHER_MARK_ARMOR_BLUE_DAMAGED.toString().replace("%shooter%", shooter.getName()));
                    }
                    org.bukkit.Color gray = org.bukkit.Color.fromRGB(5000268);
                    if (r <= 0.5 && helmMeta.getColor().equals(gray) && chestMeta.getColor().equals(gray) && leggingsMeta.getColor().equals(gray) && bootsMeta.getColor().equals(gray)) {
                        damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 0));
                        shooter.sendMessage(Language.ARCHER_MARK_ARMOR_GRAY_SHOOTER.toString().replace("%damaged%", damaged.getName()));
                        damaged.sendMessage(Language.ARCHER_MARK_ARMOR_GRAY_DAMAGED.toString().replace("%shooter%", shooter.getName()));
                    }
                    org.bukkit.Color black = org.bukkit.Color.fromRGB(1644825);
                    if (r <= 0.2 && helmMeta.getColor().equals(black) && chestMeta.getColor().equals(black) && leggingsMeta.getColor().equals(black) && bootsMeta.getColor().equals(black)) {
                        damaged.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 120, 0));
                        shooter.sendMessage(Language.ARCHER_MARK_ARMOR_BLACK_SHOOTER.toString().replace("%damaged%", damaged.getName()));
                        damaged.sendMessage(Language.ARCHER_MARK_ARMOR_BLACK_DAMAGED.toString().replace("%shooter%", shooter.getName()));
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
            if (this.plugin.getHandlerManager().getArmorClassManager().getEquippedClass(event.getPlayer()) != this) {
                return;
            }
            if (CooldownManager.isOnCooldown("ARCHER_SPEED_DELAY", player)) {
                player.sendMessage(Language.COOLDOWN_ARCHER_SUGAR.toString().replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("ARCHER_SPEED_DELAY", player), true)));
            }
            else {
                ItemStack stack = player.getItemInHand();
                if (stack.getAmount() == 1) {
                    player.setItemInHand(new ItemStack(Material.AIR, 1));
                }
                else {
                    stack.setAmount(stack.getAmount() - 1);
                }
                if (!CooldownManager.getCooldowns().containsKey("ARCHER_SPEED_DELAY")) {
                    CooldownManager.createCooldown("ARCHER_SPEED_DELAY");
                }
                CooldownManager.addCooldown("ARCHER_SPEED_DELAY", player, speedDelay);
                RevampHCF.getInstance().getEffectRestorer().setRestoreEffect(player, Archer.speedEffect);
            }
        }
        else if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && event.hasItem() && event.getItem().getType() == Material.FEATHER) {
            if (this.plugin.getHandlerManager().getArmorClassManager().getEquippedClass(event.getPlayer()) != this) {
                return;
            }
            if (CooldownManager.isOnCooldown("ARCHER_JUMP_DELAY", player)) {
                player.sendMessage(Language.COOLDOWN_ARCHER_FEATHER.toString().replace("%time%", TimeUtils.getRemaining(CooldownManager.getCooldownMillis("ARCHER_JUMP_DELAY", player), true)));
            }
            else {
                ItemStack stack = player.getItemInHand();
                if (stack.getAmount() == 1) {
                    player.setItemInHand(new ItemStack(Material.AIR, 1));
                }
                else {
                    stack.setAmount(stack.getAmount() - 1);
                }
                if (!CooldownManager.getCooldowns().containsKey("ARCHER_JUMP_DELAY")) {
                    CooldownManager.createCooldown("ARCHER_JUMP_DELAY");
                }
                CooldownManager.addCooldown("ARCHER_JUMP_DELAY", player, jumpDelay);
                RevampHCF.getInstance().getEffectRestorer().setRestoreEffect(player, Archer.jumpEffect);
            }
        }
    }
    
    @Override
    public boolean isApplicableFor(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack helmet = playerInventory.getHelmet();
        if (helmet == null || helmet.getType() != Material.LEATHER_HELMET) {
            return false;
        }
        ItemStack chestplate = playerInventory.getChestplate();
        if (chestplate == null || chestplate.getType() != Material.LEATHER_CHESTPLATE) {
            return false;
        }
        ItemStack leggings = playerInventory.getLeggings();
        if (leggings == null || leggings.getType() != Material.LEATHER_LEGGINGS) {
            return false;
        }
        ItemStack boots = playerInventory.getBoots();
        return boots != null && boots.getType() == Material.LEATHER_BOOTS;
    }
}
