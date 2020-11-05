package eu.revamp.hcf.handlers.combat;

import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.spigot.utils.chat.color.CC;
import lombok.Getter;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;
import eu.revamp.hcf.factions.utils.struction.Role;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import org.bukkit.entity.LivingEntity;
import eu.revamp.hcf.utils.chat.JavaUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.World;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.HashMap;
import org.bukkit.event.Listener;
import eu.revamp.hcf.utils.Handler;

public class CombatLoggerHandler extends Handler implements Listener
{
    @Getter private HashMap<String, List<Player>> combatLoggers;
    public static long HCF_REGEN_DELAY = TimeUnit.MINUTES.toMillis(RevampHCF.getInstance().getConfig().getLong("FACTIONS-SETTINGS.DTR-REGEN-TIME"));
    public static long KITMAP_REGEN_DELAY = TimeUnit.MINUTES.toMillis(RevampHCF.getInstance().getConfig().getLong("FACTIONS-SETTINGS.DTR-REGEN-TIME"));
    
    public CombatLoggerHandler(RevampHCF plugin) {
        super(plugin);
        this.combatLoggers = new HashMap<>();
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
        new BukkitRunnable() {
            public void run() {
                CombatLoggerHandler.this.removeVillagers();
                CombatLoggerHandler.this.removeVillagers2();
            }
        }.runTaskLater(this.getInstance(), 200L);
    }
    
    @Override
    public void disable() {
        this.combatLoggers.clear();
    }
    
    public void removeVillagers() {
        for (World world : Bukkit.getWorlds()) {
            for (Villager villager : world.getEntitiesByClass(Villager.class)) {
                villager.remove();
            }
        }
    }
    
    public void removeVillagers2() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Villager) {
                    entity.remove();
                }
            }
        }
    }
    
    public void onJoin(Player player) {
        for (Villager villager : player.getWorld().getEntitiesByClass(Villager.class)) {
            if (!villager.isDead() && villager.hasMetadata("CombatLogger") && villager.getCustomName().equals(player.getName())) {
                villager.removeMetadata("CombatLogger", RevampHCF.getInstance());
                villager.removeMetadata("Player", RevampHCF.getInstance());
                villager.removeMetadata("Contents", RevampHCF.getInstance());
                villager.removeMetadata("Armor", RevampHCF.getInstance());
                villager.remove();
            }
        }
    }
    
    public void onQuit(Player player) {
        if (player.getHealth() == 0.0) return;
        if (player.hasPermission("hcf.staff")) return;
        if (player.hasMetadata("LogoutCommand")) return;
        if (!this.getInstance().getConfiguration().isKitMap()) {
            if (RevampHCF.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().getRemaining(player.getUniqueId()) > 0L) return;
            if (RevampHCF.getInstance().getHandlerManager().getSotwHandler().isRunning()) return;
            if (RevampHCF.getInstance().getHandlerManager().getSotwHandler().getSotwRunnable() != null) return;
        }
        else if (this.getInstance().getFactionManager().getFactionAt(player.getLocation()).getName().equalsIgnoreCase("Tournament")) return;
        if (RevampHCF.getInstance().getHandlerManager().getTimerManager().getHomeHandler().getNearbyEnemies(player, 64) <= 0) return;
        Location location = player.getLocation();
        if (RevampHCF.getInstance().getFactionManager().getFactionAt(location).isSafezone()) return;
        this.spawnVillager(player);
    }
    
    public void spawnVillager(Player player) {
        Villager villager = (Villager)player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        villager.setCustomName(player.getName());
        villager.setCustomNameVisible(true);
        villager.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 100));
        villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100));
        villager.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 100));
        villager.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 100));
        villager.setMetadata("CombatLogger", new FixedMetadataValue(this.getInstance(), player.getUniqueId()));
        villager.setMetadata("Player", new FixedMetadataValue(this.getInstance(), player));
        villager.setMetadata("Contents", new FixedMetadataValue(this.getInstance(), player.getInventory().getContents()));
        villager.setMetadata("Armor", new FixedMetadataValue(this.getInstance(), player.getInventory().getArmorContents()));
        villager.setMaxHealth(40.0);
        villager.setHealth(villager.getMaxHealth());
        new BukkitRunnable() {
            public void run() {
                if (villager != null && !villager.isDead()) {
                    villager.removeMetadata("CombatLogger", RevampHCF.getInstance());
                    villager.removeMetadata("Player", RevampHCF.getInstance());
                    villager.removeMetadata("Contents", RevampHCF.getInstance());
                    villager.removeMetadata("Armor", RevampHCF.getInstance());
                    villager.remove();
                }
            }
        }.runTaskLater(this.getInstance(), 400L);
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Villager) {
            Villager villager = (Villager)entity;
            Entity killer = entity.getKiller();
            if (villager.hasMetadata("CombatLogger")) {
                Player player = (Player)villager.getMetadata("Player").get(0).value();
                HCFPlayerData hcfPlayerData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayerData(player.getUniqueId());
                hcfPlayerData.addDeath();
                if (killer instanceof Player) {
                    HCFPlayerData killerData = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer((Player)killer);
                    killerData.addKill();
                    Bukkit.broadcastMessage(CC.translate("&c" + villager.getCustomName() + "&4[" + hcfPlayerData.getKills() + "&4] &7(Combat Logger) &ewas slain by &c" + killer.getName() + "&4[" + killerData.getKills() + "&4]"));
                }
                else {
                    Bukkit.broadcastMessage(CC.translate("&c" + villager.getCustomName() + "&4[" + hcfPlayerData.getKills() + "&4] &7(Combat Logger) &edied"));
                }
                ItemStack[] contentsArray = (ItemStack[])villager.getMetadata("Contents").get(0).value();
                ItemStack[] armorArray = (ItemStack[])villager.getMetadata("Armor").get(0).value();
                ItemStack[] array;
                for (int length = (array = contentsArray).length, i = 0; i < length; ++i) {
                    ItemStack content = array[i];
                    if (content != null && !content.getType().equals(Material.AIR)) {
                        villager.getWorld().dropItemNaturally(villager.getLocation(), content);
                    }
                }
                ItemStack[] array2;
                for (int length2 = (array2 = armorArray).length, j = 0; j < length2; ++j) {
                    ItemStack armor = array2[j];
                    if (armor != null && !armor.getType().equals(Material.AIR)) {
                        villager.getWorld().dropItemNaturally(villager.getLocation(), armor);
                    }
                }
                hcfPlayerData.setCombatLogger(true);

                PlayerFaction playerFaction = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
                if (playerFaction != null) {
                    Faction factionAt = RevampHCF.getInstance().getFactionManager().getFactionAt(player.getLocation());
                    double dtrLoss = 1.0 * factionAt.getDtrLossMultiplier();
                    double newDtr = playerFaction.setDeathsUntilRaidable(playerFaction.getDeathsUntilRaidable() - dtrLoss);
                    Role role = playerFaction.getMember(player.getUniqueId()).getRole();
                    if (this.getInstance().getConfiguration().isKitMap()) {
                        playerFaction.setRemainingRegenerationTime(CombatLoggerHandler.KITMAP_REGEN_DELAY);
                    }
                    else {
                        playerFaction.setRemainingRegenerationTime(CombatLoggerHandler.HCF_REGEN_DELAY + playerFaction.getOnlinePlayers().size() * TimeUnit.MINUTES.toMillis(2L));
                    }
                    playerFaction.broadcast(CC.translate("&cMember Death: &f" + role.getAstrix() + player.getName()));
                    playerFaction.broadcast(CC.translate("&cDTR: &f" + JavaUtils.format(newDtr, 2) + '/' + JavaUtils.format(playerFaction.getMaximumDeathsUntilRaidable(), 2)));
                }
                this.getInstance().getHandlerManager().getDeathBanHandler().banVillager(player, villager, event);
            }
        }
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Villager) {
            Villager villager = (Villager)event.getEntity();
            if (event.getDamager() instanceof Player) {
                Player player = (Player)event.getDamager();
                if (!villager.hasMetadata("CombatLogger")) return;
                if (!(event.getDamager() instanceof Player)) return;
                Player pvillager = (Player)villager.getMetadata("Player").get(0).value();
                PlayerFaction faction1 = RevampHCF.getInstance().getFactionManager().getPlayerFaction(player);
                PlayerFaction faction2 = RevampHCF.getInstance().getFactionManager().getPlayerFaction(pvillager);
                HCFPlayerData data = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                if (faction1 != null && faction2 != null && faction1 == faction2) {
                    event.getDamager().sendMessage(CC.translate("&eYou can't hurt &a" + pvillager.getName() + " &ebecause he is in your faction."));
                    event.setCancelled(true);
                }
                else {
                    this.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().setCooldown((Player)event.getDamager(), event.getDamager().getUniqueId(), this.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().getTime(), false);
                    data.setSpawnTagCooldown(this.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().getTime());
                }
                if (!this.combatLoggers.containsKey(villager.getCustomName())) return;
                if (this.combatLoggers.get(villager.getCustomName()).contains(player)) {
                    event.setCancelled(true);
                }
                else {
                    new BukkitRunnable() {
                        public void run() {
                            villager.setVelocity(new Vector(0, 0, 0));
                        }
                    }.runTaskLater(this.getInstance(), 1L);
                }
            }
            else if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile)event.getDamager();
                if (projectile.getShooter() instanceof Player) {
                    Player shooter = (Player)projectile.getShooter();
                    if (shooter != event.getEntity()) {
                        if (!villager.hasMetadata("CombatLogger")) return;
                        Player pvillager2 = (Player)villager.getMetadata("Player").get(0).value();
                        PlayerFaction faction3 = RevampHCF.getInstance().getFactionManager().getPlayerFaction(shooter);
                        PlayerFaction faction4 = RevampHCF.getInstance().getFactionManager().getPlayerFaction(pvillager2);
                        HCFPlayerData data2 = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(shooter);
                        if (faction3 != null && faction4 != null && faction3 == faction4) {
                            shooter.sendMessage(CC.translate("&eYou can't hurt &a" + pvillager2.getName() + " &ebecause he is in your faction."));
                            event.setCancelled(true);
                        }
                        else {
                            this.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().setCooldown(shooter, shooter.getUniqueId(), this.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().getTime(), false);
                            data2.setSpawnTagCooldown(this.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().getTime());
                        }
                        if (!this.combatLoggers.containsKey(villager.getCustomName())) return;
                        if (this.combatLoggers.get(villager.getCustomName()).contains(shooter)) {
                            event.setCancelled(true);
                        }
                        else {
                            new BukkitRunnable() {
                                public void run() {
                                    villager.setVelocity(new Vector(0, 0, 0));
                                }
                            }.runTaskLater(this.getInstance(), 1L);
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().hasMetadata("CombatLogger")) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        Entity[] entities;
        for (int length = (entities = event.getChunk().getEntities()).length, i = 0; i < length; ++i) {
            Entity entity = entities[i];
            if (entity.hasMetadata("CombatLogger") && !entity.isDead()) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.onJoin(player);
        if (player.hasMetadata("LogoutCommand")) {
            player.removeMetadata("LogoutCommand", this.getInstance());
        }
        HCFPlayerData HCFPlayerData = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
        if (HCFPlayerData.isCombatLogger()) {
            HCFPlayerData.setCombatLogger(false);
            player.teleport(new Location(Bukkit.getWorld("world"), 0.0, 100.0, 0.0));
            player.sendMessage(CC.translate("&eYou were killed because your &7Combat Logger &edied."));
            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[4]);
            if (!this.getInstance().getConfiguration().isKitMap() && this.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().setCooldown(player, player.getUniqueId(), 1800000L, true)) {
                this.getInstance().getHandlerManager().getTimerManager().getPvpTimerHandler().setPaused(player.getUniqueId(), true);
                player.sendMessage(CC.translate("&cYou now have your &a&lPvP Protection&c timer."));
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.onQuit(player);
    }
}
