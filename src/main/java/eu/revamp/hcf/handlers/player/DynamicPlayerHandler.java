package eu.revamp.hcf.handlers.player;

import eu.revamp.hcf.factions.type.GlowstoneFaction;
import eu.revamp.hcf.playerdata.HCFPlayerData;
import eu.revamp.spigot.utils.chat.color.CC;
import eu.revamp.hcf.RevampHCF;
import eu.revamp.hcf.commands.games.EOTWFFACommand;
import eu.revamp.hcf.commands.staff.SpawnCommand;
import eu.revamp.hcf.factions.Faction;
import eu.revamp.hcf.factions.type.ClaimableFaction;
import eu.revamp.hcf.factions.type.PlayerFaction;
import eu.revamp.hcf.factions.type.WarzoneFaction;
import eu.revamp.hcf.factions.utils.struction.Role;
import eu.revamp.hcf.utils.Handler;
import eu.revamp.hcf.utils.chat.JavaUtils;
import eu.revamp.spigot.utils.world.WorldUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class DynamicPlayerHandler extends Handler implements Listener
{
    private List<Material> allowed;
    public static long HCF_REGEN_DELAY = TimeUnit.MINUTES.toMillis(RevampHCF.getInstance().getConfig().getLong("FACTIONS-SETTINGS.DTR-REGEN-TIME"));
    public static long KITMAP_REGEN_DELAY = TimeUnit.MINUTES.toMillis(RevampHCF.getInstance().getConfig().getLong("FACTIONS-SETTINGS.DTR-REGEN-TIME"));
    
    public DynamicPlayerHandler(RevampHCF plugin) {
        super(plugin);
        this.allowed = Arrays.asList(Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS);
    }
    
    @Override
    public void enable() {
        this.getInstance().getServer().getPluginManager().registerEvents(this, this.getInstance());
    }

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if ((event.getMessage().toLowerCase().startsWith("/f ") || event.getMessage().toLowerCase().startsWith("/faction ") || event.getMessage().toLowerCase().startsWith("/team ") || event.getMessage().toLowerCase().startsWith("/t ")) && EOTWFFACommand.isEOTWFFA() && !event.getPlayer().hasPermission("revamphcf.op")) {
            event.getPlayer().sendMessage(ChatColor.RED + "You may not use this command in FFA");
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockPlacee(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            Player player = event.getPlayer();
            if (player.getGameMode() == GameMode.CREATIVE) return;
            if (player.getAllowFlight()) return;
            Block block = event.getBlockPlaced();
            if (block.getType().isSolid() && !(block.getState() instanceof Sign) && player.getLocation().getBlockY() > block.getLocation().getBlockY()) {
                Vector vector = player.getVelocity();
                vector.setX(-0.1);
                vector.setZ(-0.1);
                player.setVelocity(vector.setY(vector.getY() - 0.41999998688697815));
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onCreatureSpawe(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) return;
        if (this.getInstance().getFactionManager().getFactionAt(event.getEntity().getLocation()) instanceof WarzoneFaction && event.getEntity().getWorld().getEnvironment() != World.Environment.THE_END && event.getEntity() instanceof Monster) {
            event.setCancelled(true);
        }
        switch (event.getSpawnReason()) {
            case NATURAL:
            case CHUNK_GEN: {
                if (event.getLocation().getChunk().getEntities().length > 25) {
                    event.setCancelled(true);
                    break;
                }
                break;
            }
        }
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void onEntityDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        Player k = p.getKiller();
        Player killer = event.getEntity().getKiller();
        Player victim = event.getEntity();
        if (this.getInstance().getConfig().getBoolean("NO-CLEAN") && k != null) {
            k.setHealth(20.0);
        }
        if (this.getInstance().getConfiguration().isKitMap() && k != null) {
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta)skull.getItemMeta();
            meta.setOwner(p.getName());
            skull.setItemMeta(meta);
            k.getInventory().addItem(skull);
        }
        if (!this.getInstance().getConfiguration().isKitMap() && k != null && k.hasPermission("revamphcf.donor")) {
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta)skull.getItemMeta();
            meta.setOwner(p.getName());
            skull.setItemMeta(meta);
            k.getInventory().addItem(skull);
        }
        HCFPlayerData victimData = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(victim);
        victimData.setSpawnTagCooldown(0L);
        this.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().clearCooldown(victim.getUniqueId());
        new BukkitRunnable() {
            public void run() {
                if (victimData.getSpawnTagCooldown() > 0L) {
                    victimData.setSpawnTagCooldown(0L);
                }
                if (RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().getRemaining(victim) > 0L) {
                    RevampHCF.getInstance().getHandlerManager().getTimerManager().getSpawnTagHandler().clearCooldown(victim);
                }
            }
        }.runTaskLater(RevampHCF.getInstance(), 100L);
        new BukkitRunnable() {
            public void run() {
                if (!RevampHCF.getInstance().getConfiguration().isKitMap() && !victim.hasPermission("hcf.staff")) {
                    RevampHCF.getInstance().sendToServer(victim, "hub");
                }
            }
        }.runTaskLater(this.getInstance(), 500L);
        HCFPlayerData data = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(victim);
        victimData.setDeaths(victimData.getDeaths() + 1);
        FastDateFormat date = FastDateFormat.getInstance(RevampHCF.getInstance().getConfig().getString("DATE_FORMAT"), TimeZone.getTimeZone("EST"), Locale.ENGLISH);
        if (data.getLastDeaths().size() == 10) {
            data.getLastDeaths().remove(4);
        }
        data.getLastDeaths().add(0, date.format(System.currentTimeMillis()) + " - " + event.getDeathMessage());
        if (killer != null && this.getInstance().getFactionManager().getPlayerFaction(killer) != null) {
            HCFPlayerData killerData = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(killer);
            PlayerFaction killerFaction = this.getInstance().getFactionManager().getPlayerFaction(killer);
            killerData.setKills(killerData.getKills() + 1);
            killerFaction.setPoints(killerFaction.getPoints() + this.getInstance().getConfig().getInt("POINTS.WIN-PER-KILL"));
            killerFaction.broadcast("&a" + killerFaction.getName() + " &eHas gotten &a1 &epoint &efor your faction");
            if (this.getInstance().getConfiguration().isKitMap()) {
                killerData.setBalance(killerData.getBalance() + 100);
                for (String command: this.getInstance().getConfig().getStringList("KITMAP-KILLREWARD")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", killer.getName()));
                }
                killer.sendMessage(CC.translate("&aYou earned &f$100 &afor killing &f" + victim.getDisplayName() + "&a."));
            }
            else {
                victim.sendMessage(CC.translate("&aYou lost &f$" + victimData.getBalance() + " &abecause &f" + killer.getDisplayName() + " &akilled you."));
                killerData.setBalance(killerData.getBalance() + victimData.getBalance());
                killer.sendMessage(CC.translate("&aYou earned &f$" + victimData.getBalance() + " &afor killing &f" + victim.getDisplayName() + "&a."));
                victimData.setBalance(0);
            }
        }
        PlayerFaction playerFaction = this.getInstance().getFactionManager().getPlayerFaction(victim);
        HCFPlayerData user = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(victim);
        if (user.isShowLightning()) {
            victim.getLocation().getWorld().strikeLightningEffect(victim.getLocation());
        }
        if (playerFaction != null) {
            Faction factionAt = this.getInstance().getFactionManager().getFactionAt(victim.getLocation());
            double dtrLoss = 1.0 * factionAt.getDtrLossMultiplier();
            double newDtr = playerFaction.setDeathsUntilRaidable(playerFaction.getDeathsUntilRaidable() - dtrLoss);
            Role role = playerFaction.getMember(victim.getUniqueId()).getRole();
            if (this.getInstance().getConfiguration().isKitMap()) {
                playerFaction.setRemainingRegenerationTime(DynamicPlayerHandler.KITMAP_REGEN_DELAY);
            }
            else {
                playerFaction.setRemainingRegenerationTime(DynamicPlayerHandler.HCF_REGEN_DELAY);
            }
            playerFaction.setPoints(playerFaction.getPoints() - this.getInstance().getConfig().getInt("POINTS.LOSS-PER-DEATH"));
            playerFaction.broadcast(CC.translate("&cMember Death: &f" + role.getAstrix() + victim.getName()));
            playerFaction.broadcast(CC.translate("&cDTR: &f" + JavaUtils.format(newDtr, 2)));
            playerFaction.broadcast("&eYour faction has lost &a1 point&e because &a" + victim.getName() + " &edied!");
            Bukkit.getConsoleSender().sendMessage(CC.translate("&c" + victim.getName() + " &4[" + playerFaction.getName() + "] &cwas killed! DTR -> &4" + JavaUtils.format(newDtr, 2)));
        }
        if (!this.getInstance().getConfiguration().isKitMap()) {
            victim.sendTitle("§c§lYou died", "§eYou lost the money in your balance");
        }
        else if (this.getInstance().getConfiguration().isKitMap()) {
            victim.sendTitle("§c§lYou died", "§You lost the money in your balance");
        }
        if (killer != null) {
            this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(killer.getUniqueId()).addKill();
        }
        victim.setFoodLevel(20);
        victim.setFallDistance(0.0f);
        victim.setFireTicks(0);
        victim.playSound(victim.getLocation(), Sound.COW_HURT, 3.0f, 1.0f);
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) {
            Player killer = entity.getKiller();
            if (killer != null && killer.getItemInHand() != null && killer.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) {
                int amplifier = killer.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) + 45;
                event.setDroppedExp(event.getDroppedExp() * amplifier);
            }
        }
    }
    
    @EventHandler
    public void onBlockBreae(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (EOTWFFACommand.isEOTWFFA()) {
            if (player.hasPermission("revamphcf.op")) return;
            event.setCancelled(true);
        }
        if (player.getItemInHand() != null && player.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            int amplifier = player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 7;
            event.setExpToDrop(event.getExpToDrop() * amplifier);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Arrow) {
            Arrow arrow = (Arrow)entity;
            if (!(arrow.getShooter() instanceof Player) || ((CraftArrow)arrow).getHandle().fromPlayer == 2) {
                arrow.remove();
            }
        }
    }
    
    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (vehicle instanceof Boat) {
            Boat boat = (Boat)vehicle;
            Block belowBlock = boat.getLocation().add(0.0, -1.0, 0.0).getBlock();
            if (belowBlock.getType() != Material.WATER && belowBlock.getType() != Material.STATIONARY_WATER) {
                boat.remove();
            }
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) return;
        if (SpawnCommand.teleporting.containsKey(player)) {
            int runnable = SpawnCommand.teleporting.get(player);
            Bukkit.getScheduler().cancelTask(runnable);
            SpawnCommand.teleporting.remove(player);
            player.sendMessage(CC.translate("&e&lSPAWN TELEPORT &c&lCANCELLED!"));
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (SpawnCommand.teleporting.containsKey(player)) {
                int runnable = SpawnCommand.teleporting.get(player);
                Bukkit.getScheduler().cancelTask(runnable);
                SpawnCommand.teleporting.remove(player);
                player.sendMessage(CC.translate("&e&lSPAWN TELEPORT &c&lCANCELLED!"));
            }
        }
    }
    
    @EventHandler
    public void onSignCreate(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (player != null && player.hasPermission("revamphcf.op")) {
            String[] lines = event.getLines();
            for (int i = 0; i < lines.length; ++i) {
                if (!player.hasPermission("revamphcf.op") && (event.getLine(i).contains(CC.translate("Sell")) || event.getLine(i).contains("Buy"))) {
                    event.setCancelled(true);
                }
                event.setLine(i, CC.translate(lines[i]));
            }
        }
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void onBlockFix(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Faction factionAt = this.getInstance().getFactionManager().getFactionAt(player.getLocation());
        if (factionAt instanceof ClaimableFaction) {
            if (this.getInstance().getFactionManager().getPlayerFaction(player) == factionAt) return;
            Location loc = player.getLocation();
            if (loc.getBlock().getType() == Material.AIR) return;
            if (!loc.getBlock().getType().isSolid()) return;
            loc.setY(loc.getY() + 2.0);
            if (loc.getBlock().getType() != Material.AIR) return;
            loc.setY(loc.getY() - 1.0);
            player.teleport(loc);
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(ExplosionPrimeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        ItemStack stack = event.getItem();
        if (stack != null && this.allowed.contains(stack.getType()) && ThreadLocalRandom.current().nextInt(3) != 0) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()){
            //PrefixMenu.removePrefix(player);
        }
        /*
        if (player.hasPermission("hcf.staff") || player.isOp()) {
            player.setPlayerListName(CC.translate("&9\u2748 &9&o" + player.getName()));
        }
        else if ((player.hasPermission("rank.famous") && !player.isOp()) || (player.hasPermission("rank.youtuber") && !player.isOp())) {
            player.setPlayerListName(CC.translate("&d&l" + player.getName()));
        }
        else if (player.hasPermission("rank.partner") && !player.isOp()) {
            player.setPlayerListName(CC.translate("&d\u2726 &l" + player.getName()));
        }
        else if (player.hasPermission("rank.cobalt") && !player.isOp()) {
            player.setPlayerListName(CC.translate("&6&l" + player.getName()));
        }
        else if (player.hasPermission("rank.raver") && !player.isOp()) {
            player.setPlayerListName(CC.translate("&e&l" + player.getName()));
        }
        else if (player.hasPermission("rank.might") && !player.isOp()) {
            player.setPlayerListName(CC.translate("&c&l" + player.getName()));
        }
        else if (player.hasPermission("rank.fiery") && !player.isOp()) {
            player.setPlayerListName(CC.translate("&5&l" + player.getName()));
        }
        else if (player.hasPermission("rank.vaunt") && !player.isOp()) {
            player.setPlayerListName(CC.translate("&b&l" + player.getName()));
        }
        else {
            player.setPlayerListName(ChatColor.RED + player.getName());
        }
        player.getPlayerListName();
        */

        event.setJoinMessage("");
        new BukkitRunnable() {
            public void run() {
                HCFPlayerData data = RevampHCF.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(player);
                data.setLastSeen(System.currentTimeMillis());
            }
        }.runTaskLater(this.getInstance(), 1L);
        new BukkitRunnable() {
            public void run() {
                player.playSound(player.getLocation(), Sound.CAT_MEOW, 3.0f, 1.0f);
            }
        }.runTaskLater(this.getInstance(), 2L);
        if (!player.hasPlayedBefore()) {
            new BukkitRunnable() {
                public void run() {
                    Location spawn = WorldUtils.destringifyLocation(RevampHCF.getInstance().getLocation().getString("World-Spawn.world-spawn"));
                    if (spawn == null) {
                        System.out.print("SPAWN IS NULL!");
                        player.sendMessage(CC.translate("&c&lSpawn is not set please contact a Staff Member!"));
                        return;
                    }
                    player.setHealth(player.getMaxHealth());
                    player.setFoodLevel(20);
                    player.setFallDistance(0.0f);
                    player.setFireTicks(0);
                    player.teleport(spawn);
                }
            }.runTaskLater(this.getInstance(), 5L);
            player.getInventory().setContents(this.getInstance().getHandlerManager().getConfigHandler().getFirstJoinItems());
            if (!player.isOp() && player.getGameMode().equals(GameMode.CREATIVE)) {
                player.setGameMode(GameMode.SURVIVAL);
            }
            if (player.hasPotionEffect(PotionEffectType.SATURATION)) {
                player.removePotionEffect(PotionEffectType.SATURATION);
            }
            for (String join : this.getInstance().getLanguage().getStringList("JOIN_MESSAGE")) {
                player.sendMessage(join);
            }
            player.sendMessage(CC.translate("&7&m"));
        }
    }
    
    @EventHandler
    public void onPlayerPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (event.getMessage().toLowerCase().startsWith("/stuck")) {
            event.setCancelled(true);
            player.performCommand("f stuck");
        }
        else if (event.getMessage().toLowerCase().startsWith("/sethome")) {
            event.setCancelled(true);
            player.performCommand("f sethome");
        }
        else if (event.getMessage().toLowerCase().startsWith("/home")) {
            event.setCancelled(true);
            player.performCommand("f home");
        }
    }
    
    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            player.setSaturation(1000.0f);
            player.setSaturation(10.0f);
        }
        assert entity instanceof Player;
        if (this.getInstance().getHandlerManager().getSotwHandler().getSotwRunnable() != null && !this.getInstance().getHandlerManager().getSotwHandler().isSotwEnabled((Player)entity) && event.getEntity() instanceof Player) {
            event.setCancelled(true);
            event.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
        HCFPlayerData user = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(event.getPlayer());
        this.getInstance().getHandlerManager().getVisualiseHandler().clearVisualBlocks(event.getPlayer(), null, null, false);
        user.setShowClaimMap(false);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage("");
        HCFPlayerData user = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(event.getPlayer());
        this.getInstance().getHandlerManager().getVisualiseHandler().clearVisualBlocks(event.getPlayer(), null, null, false);
        user.setShowClaimMap(false);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        HCFPlayerData user = this.getInstance().getHandlerManager().getHCFPlayerDataHandler().getPlayer(event.getPlayer());
        this.getInstance().getHandlerManager().getVisualiseHandler().clearVisualBlocks(event.getPlayer(), null, null, false);
        user.setShowClaimMap(false);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPortalEnters(PlayerPortalEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL && event.getTo().getWorld().getEnvironment() == World.Environment.THE_END) {
            event.setCancelled(true);
            Location endspawn = WorldUtils.destringifyLocation(this.getInstance().getLocation().getString("World-Spawn.end-spawn"));
            if (endspawn == null) {
                System.out.print("END SPAWN IS NULL!");
                event.getPlayer().sendMessage(CC.translate("&c&lEnd Spawn is not set please contact a Staff Member!"));
                return;
            }
            event.getPlayer().teleport(endspawn);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onWaterPortalEnter(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        if (loc.getWorld().getEnvironment() == World.Environment.THE_END && (loc.getBlock().getType() == Material.WATER || loc.getBlock().getType() == Material.STATIONARY_WATER)) {
            Location endexit = WorldUtils.destringifyLocation(this.getInstance().getLocation().getString("World-Spawn.end-exit"));
            if (endexit != null) {
                player.teleport(endexit);
            }
            else {
                event.getPlayer().sendMessage(CC.translate("&cEnd exit is not set."));
            }
        }
        //TODO CHECK IF IT WORKS
        if (loc.getWorld().getEnvironment() == World.Environment.NETHER && (loc.getBlock().getType() == Material.WATER || loc.getBlock().getType() == Material.STATIONARY_WATER)) {
            //Location netherexit = WorldUtils.destringifyLocation(this.getInstance().getLocation().getString("World-Spawn.nether-spawn"));
            Location netherexit = WorldUtils.destringifyLocation(this.getInstance().getLocation().getString("World-Spawn.world-spawn"));
            if (netherexit != null) {
                player.teleport(netherexit);
            }
            else {
                event.getPlayer().sendMessage(CC.translate("&cSpawn is not set."));
            }
        }
    }
    
    @EventHandler
    public void onDamagePlayer(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player)e.getEntity();
            if (!e.getCause().equals(EntityDamageEvent.DamageCause.VOID) && this.getInstance().getHandlerManager().getSotwHandler().getSotwRunnable() != null && !this.getInstance().getHandlerManager().getSotwHandler().isSotwEnabled(player)) {
                e.setCancelled(true);
            }
            if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID) && this.getInstance().getHandlerManager().getSotwHandler().getSotwRunnable() != null && !this.getInstance().getHandlerManager().getSotwHandler().isSotwEnabled(player)) {
                e.setCancelled(true);
                Location endspawn = WorldUtils.destringifyLocation(this.getInstance().getLocation().getString("World-Spawn.end-spawn"));
                player.teleport(endspawn);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            event.setSpawnLocation(WorldUtils.destringifyLocation(this.getInstance().getLocation().getString("World-Spawn.world-spawn")));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(Objects.requireNonNull(WorldUtils.destringifyLocation(this.getInstance().getLocation().getString("World-Spawn.world-spawn"))));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Squid) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMagmaNetherSpawn(EntitySpawnEvent event){
        if (event.getLocation().getWorld().getEnvironment() == World.Environment.NETHER){
            Faction factionAt = this.getInstance().getFactionManager().getFactionAt(event.getLocation());
            if (event.getEntity() instanceof MagmaCube && !(factionAt instanceof GlowstoneFaction)){
                event.setCancelled(true);
            }
        }
    }
//TODO STRENGTH CONFIG
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player)event.getDamager();
            if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                        int level = effect.getAmplifier() + 1;
                        if (level == 1) {
                            event.setDamage(10.0 * event.getDamage() / (10.0 + 13.0 * level) + 13.0 * event.getDamage() * level * 10.0 / 100.0 / (10.0 + 13.0 * level));
                        }
                        else {
                            event.setDamage(10.0 * event.getDamage() / (10.0 + 13.0 * level) + 13.0 * event.getDamage() * level * 20.0 / 100.0 / (10.0 + 13.0 * level));
                        }
                    }
                }
            }
        }
    }
}
