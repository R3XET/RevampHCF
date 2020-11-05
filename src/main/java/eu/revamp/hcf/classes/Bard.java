package eu.revamp.hcf.classes;

import eu.revamp.hcf.Language;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.classes.utils.ArmorClass;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.date.DateUtils;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.classes.utils.bard.BardData;
import eu.revamp.hcf.classes.utils.bard.EffectData;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Bard extends ArmorClass implements Listener {
    public static int HELD_EFFECT_DURATION_TICKS = 100;
    private static long BUFF_COOLDOWN_MILLIS = DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("BARD.BUFF-COOLDOWN"));
    private static int TEAMMATE_NEARBY_RADIUS = RevampHCF.getInstance().getConfig().getInt("BARD.TEAMMATE_NEARBY_RADIUS");
    private static long HELD_REAPPLY_TICKS = 20L;
    private final Map<UUID, BardData> bardDataMap;
    private Map<Material, EffectData> bardEffects;
    private RevampHCF plugin;
    private TObjectLongMap<UUID> msgCooldowns;

    public Bard(RevampHCF plugin) {
        super(RevampHCF.getInstance().getConfig().getString("BARD.NAME"), RevampHCF.getInstance().getConfig().getBoolean("BARD.WARMUP-ENABLED") ? DateUtils.parseTime(RevampHCF.getInstance().getConfig().getString("BARD.WARMUP-TIME")) : TimeUnit.MILLISECONDS.toMillis(1L));
        this.bardDataMap = new HashMap<>();
        this.bardEffects = new EnumMap<>(Material.class);
        this.msgCooldowns = new TObjectLongHashMap<>();
        this.plugin = plugin;
        this.passiveEffects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
        this.passiveEffects.add(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
        this.passiveEffects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        this.bardEffects.put(Material.WHEAT, new EffectData(15, new PotionEffect(PotionEffectType.SATURATION, 120, 2), new PotionEffect(PotionEffectType.SATURATION, 100, 0)));
        this.bardEffects.put(Material.SUGAR, new EffectData(25, new PotionEffect(PotionEffectType.SPEED, 120, 2), new PotionEffect(PotionEffectType.SPEED, 100, 1)));
        this.bardEffects.put(Material.BLAZE_POWDER, new EffectData(45, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0)));
        this.bardEffects.put(Material.IRON_INGOT, new EffectData(30, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, 2), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0)));
        this.bardEffects.put(Material.GHAST_TEAR, new EffectData(45, new PotionEffect(PotionEffectType.REGENERATION, 60, 2), new PotionEffect(PotionEffectType.REGENERATION, 100, 0)));
        this.bardEffects.put(Material.FEATHER, new EffectData(30, new PotionEffect(PotionEffectType.JUMP, 240, 9), new PotionEffect(PotionEffectType.JUMP, 100, 1)));
        this.bardEffects.put(Material.SPIDER_EYE, new EffectData(50, new PotionEffect(PotionEffectType.WITHER, 100, 1), null));
        this.bardEffects.put(Material.MAGMA_CREAM, new EffectData(20, new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 900, 0), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 120, 0)));
    }

    @Override
    public boolean onEquip(Player player) {
        if (!RevampHCF.getInstance().getConfiguration().isKitMap() && RevampHCF.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().getRemaining(player) > 0L) {
            player.sendMessage(Language.BARD_PVPTIMER_EQUIP.toString().replace("%bard%", RevampHCF.getInstance().getConfig().getString("BARD.NAME")));
            return false;
        }
        if (!super.onEquip(player)) {
            return false;
        }
        BardData bardData = new BardData();
        this.bardDataMap.put(player.getUniqueId(), bardData);
        bardData.startEnergyTracking();
        bardData.heldTask = new BukkitRunnable() {
            int lastEnergy;

            @SuppressWarnings("deprecation")
            public void run() {
                ItemStack held = player.getItemInHand();
                if (held != null) {
                    EffectData bardEffect = Bard.this.bardEffects.get(held.getType());
                    if (bardEffect == null) {
                        return;
                    }
                    if (!RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation()).isSafezone()) {
                        PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
                        if (playerFaction != null) {
                            Collection<Entity> nearbyEntities = player.getNearbyEntities(Bard.TEAMMATE_NEARBY_RADIUS, Bard.TEAMMATE_NEARBY_RADIUS, Bard.TEAMMATE_NEARBY_RADIUS);
                            for (Entity nearby : nearbyEntities) {
                                if (nearby instanceof Player && !player.equals(nearby)) {
                                    Player target = (Player) nearby;
                                    if (!playerFaction.getMembers().containsKey(target.getUniqueId())) {
                                        continue;
                                    }
                                    Bard.this.plugin.getEffectRestorer().setRestoreEffect(target, bardEffect.heldable);
                                }
                            }
                        }
                    }
                }
                int energy = (int) Bard.this.getEnergy(player);
                if (energy != 0 && energy != this.lastEnergy && (energy % 10 == 0 || this.lastEnergy - energy - 1 > 0 || energy == BardData.MAX_ENERGY)) {
                    this.lastEnergy = energy;
                    player.sendMessage(Language.BARD_ENERGY.toString().replace("%bard%", RevampHCF.getInstance().getConfig().getString("BARD.NAME")).replace("%energy%", String.valueOf(energy)));
                }
            }
        }.runTaskTimer(this.plugin, 10L, Bard.HELD_REAPPLY_TICKS);
        return true;
    }

    @Override
    public void onUnequip(Player player) {
        super.onUnequip(player);
        this.clearBardData(player.getUniqueId());
    }

    private void clearBardData(UUID uuid) {
        BardData bardData = this.bardDataMap.remove(uuid);
        if (bardData != null && bardData.getHeldTask() != null) {
            bardData.getHeldTask().cancel();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.clearBardData(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        this.clearBardData(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ArmorClass equipped = this.plugin.getHandlerManager().getArmorClassManager().getEquippedClass(player);
        if (equipped == null || !equipped.equals(this)) {
            return;
        }
        UUID uuid = player.getUniqueId();
        long lastMessage = this.msgCooldowns.get(uuid);
        long millis = System.currentTimeMillis();
        if (lastMessage != this.msgCooldowns.getNoEntryValue() && lastMessage - millis > 0L) {
            return;
        }
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasItem()) {
            return;
        }
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || (!event.isCancelled() && action == Action.RIGHT_CLICK_BLOCK)) {
            Player player = event.getPlayer();
            ItemStack stack = event.getItem();
            EffectData bardEffect = this.bardEffects.get(stack.getType());
            if (bardEffect == null || bardEffect.clickable == null) {
                return;
            }
            event.setUseItemInHand(Event.Result.DENY);
            BardData bardData = this.bardDataMap.get(player.getUniqueId());
            if (bardData != null) {
                if (!this.canUseBardEffect(player, bardData, bardEffect, true)) {
                    return;
                }
                if (stack.getAmount() > 1) {
                    stack.setAmount(stack.getAmount() - 1);
                    HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                    data.setSpawnTagCooldown(RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().getTime());
                    RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().setCooldown(player, player.getUniqueId());
                }
                else {
                    player.setItemInHand(new ItemStack(Material.AIR, 1));
                }
                if (bardEffect != null && !RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation()).isSafezone()) {
                    PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
                    if (playerFaction != null && !bardEffect.clickable.getType().equals(PotionEffectType.WITHER)) {
                        Collection<Entity> nearbyEntities = player.getNearbyEntities(25.0, 25.0, 25.0);
                        for (Entity nearby : nearbyEntities) {
                            if (nearby instanceof Player && !player.equals(nearby)) {
                                Player target = (Player)nearby;
                                if (!playerFaction.getMembers().containsKey(target.getUniqueId())) {
                                    continue;
                                }
                                this.plugin.getEffectRestorer().setRestoreEffect(target, bardEffect.clickable);
                            }
                        }
                    }
                    else if (playerFaction != null && bardEffect.clickable.getType().equals(PotionEffectType.WITHER)) {
                        Collection<Entity> nearbyEntities = player.getNearbyEntities(25.0, 25.0, 25.0);
                        for (Entity nearby : nearbyEntities) {
                            if (nearby instanceof Player && !player.equals(nearby)) {
                                Player target = (Player)nearby;
                                if (playerFaction.getMembers().containsKey(target.getUniqueId())) {
                                    continue;
                                }
                                HCFPlayerData data2 = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(target);
                                data2.setSpawnTagCooldown(RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().getTime());
                                RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().setCooldown(target, target.getUniqueId());
                                this.plugin.getEffectRestorer().setRestoreEffect(target, bardEffect.clickable);
                            }
                        }
                    }
                    else if (bardEffect.clickable.getType().equals(PotionEffectType.WITHER)) {
                        Collection<Entity> nearbyEntities = player.getNearbyEntities(25.0, 25.0, 25.0);
                        for (Entity nearby : nearbyEntities) {
                            if (nearby instanceof Player && !player.equals(nearby)) {
                                Player target = (Player)nearby;
                                this.plugin.getEffectRestorer().setRestoreEffect(target, bardEffect.clickable);
                            }
                        }
                    }
                }
                this.plugin.getEffectRestorer().setRestoreEffect(player, bardEffect.clickable);
                bardData.setBuffCooldown(Bard.BUFF_COOLDOWN_MILLIS);
                double newEnergy = this.setEnergy(player, bardData.getEnergy() - bardEffect.energyCost);
                player.sendMessage(Language.BARD_ENERGY_USED.toString().replace("%bard%", RevampHCF.getInstance().getConfig().getString("BARD.NAME")).replace("%energycost%", String.valueOf(bardEffect.energyCost)));
            }
        }
    }
    
    private boolean canUseBardEffect(Player player, BardData bardData, EffectData bardEffect, boolean sendFeedback) {
        String errorFeedback = null;
        double currentEnergy = bardData.getEnergy();
        if (bardEffect.energyCost > currentEnergy) {
            errorFeedback = Language.BARD_NOT_ENOUGH_ENERGY.toString().replace("%energy%", String.valueOf(currentEnergy)).replace("%energycost%", String.valueOf(bardEffect.energyCost));
        }
        long remaining = bardData.getRemainingBuffDelay() / 1000L;
        if (remaining > 0L) {
            errorFeedback = Language.COOLDOWN_BARD_BUFF.toString().replace("%time%", String.valueOf(remaining));
        }
        Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation());
        if (factionAt.isSafezone()) {
            errorFeedback = Language.BARD_CANNOT_USE_SPAWN.toString().replace("%bard%", RevampHCF.getInstance().getConfig().getString("BARD.NAME"));
        }
        if (RevampHCF.getInstance().getConfiguration().isKitMap() && factionAt.getName().equalsIgnoreCase("Tournament")) {
            errorFeedback = Language.BARD_CANNOT_USE_TOURNAMENT.toString().replace("%bard%", RevampHCF.getInstance().getConfig().getString("BARD.NAME"));
        }
        if (sendFeedback && errorFeedback != null) {
            player.sendMessage(errorFeedback);
        }
        return errorFeedback == null;
    }
    
    @Override
    public boolean isApplicableFor(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet == null || helmet.getType() != Material.GOLD_HELMET) {
            return false;
        }
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate == null || chestplate.getType() != Material.GOLD_CHESTPLATE) {
            return false;
        }
        ItemStack leggings = player.getInventory().getLeggings();
        if (leggings == null || leggings.getType() != Material.GOLD_LEGGINGS) {
            return false;
        }
        ItemStack boots = player.getInventory().getBoots();
        return boots != null && boots.getType() == Material.GOLD_BOOTS;
    }
    
    public long getRemainingBuffDelay(Player player) {
        synchronized (this.bardDataMap) {
            BardData bardData = this.bardDataMap.get(player.getUniqueId());
            // monitorexit(this.bardDataMap)
            return (bardData == null) ? 0L : bardData.getRemainingBuffDelay();
        }
    }
    
    public double getEnergy(Player player) {
        synchronized (this.bardDataMap) {
            BardData bardData = this.bardDataMap.get(player.getUniqueId());
            // monitorexit(this.bardDataMap)
            return (bardData == null) ? 0.0 : bardData.getEnergy();
        }
    }
    
    public long getEnergyMillis(Player player) {
        synchronized (this.bardDataMap) {
            BardData bardData = this.bardDataMap.get(player.getUniqueId());
            // monitorexit(this.bardDataMap)
            return (bardData == null) ? 0L : bardData.getEnergyMillis();
        }
    }
    
    public double setEnergy(Player player, double energy) {
        BardData bardData = this.bardDataMap.get(player.getUniqueId());
        if (bardData == null) {
            return 0.0;
        }
        bardData.setEnergy(energy);
        return bardData.getEnergy();
    }
}
